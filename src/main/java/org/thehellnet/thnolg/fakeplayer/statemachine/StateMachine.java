package org.thehellnet.thnolg.fakeplayer.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thehellnet.thnolg.fakeplayer.player.Player;
import org.thehellnet.thnolg.fakeplayer.protocol.Packet;
import org.thehellnet.thnolg.fakeplayer.protocol.PacketParser;
import org.thehellnet.thnolg.fakeplayer.protocol.exception.GenericProtocolException;
import org.thehellnet.thnolg.fakeplayer.protocol.exception.ProtocolException;
import org.thehellnet.thnolg.fakeplayer.socket.UdpSocket;
import org.thehellnet.thnolg.fakeplayer.utility.ByteArrayUtility;
import org.thehellnet.thnolg.fakeplayer.utility.CmdArrayUtils;
import org.thehellnet.thnolg.fakeplayer.utility.KeyValueMap;
import org.thehellnet.thnolg.fakeplayer.utility.SocketUtility;
import org.thehellnet.utility.gaming.pb.Hash;
import org.thehellnet.utility.gaming.pb.HashSeed;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sardylan on 07/06/16.
 */
public class StateMachine {

    private static final Logger logger = LoggerFactory.getLogger(StateMachine.class);

    private UdpSocket socket;
    private Player player;
    private String challenge;
    private String guid;

    private State state = State.INIT;

    public StateMachine(UdpSocket socket, Player player) {
        this.socket = socket;
        this.player = player;
    }

    public void run() {
        while (state != State.END) {
            logger.info(String.format("State: %s", state));
            parseState();
        }
    }

    private void setState(State state) {
        this.state = state;
    }

    private void parseState() {
        switch (state) {
            case INIT:
                stateInit();
                break;
            case DEINIT:
                stateDeinit();
                break;
            case GET_KEY_AUTHORIZE:
                stateGetKeyAuthorize();
                break;
            case GET_CHALLENGE:
                stateGetChallenge();
                break;
            case CONNECT:
                stateConnect();
                break;
            case IN_GAME:
                stateInGame();
                break;
        }
    }

    private void stateInit() {
        player.setName("Name");
        player.setKey("abcd1234abcd1234e5f6");
        guid = Hash.guid(player.getKey(), HashSeed.COD2);

        logger.info(String.format("Player name: %s", player.getName()));
        logger.info(String.format("Player key:  %s", player.getKey()));
        logger.info(String.format("GUID:        %s", guid));

        setState(State.GET_KEY_AUTHORIZE);
    }

    private void stateDeinit() {
        setState(State.END);
    }

    private void stateGetKeyAuthorize() {
        setState(State.GET_CHALLENGE);
    }

    private void stateGetChallenge() {
        byte[] cmd = CmdArrayUtils.stringToCmd("getchallenge");

        try {
            socket.send(cmd);
        } catch (IOException e) {
            e.printStackTrace();
            state = State.DEINIT;
            return;
        }

        byte[] response = socket.read();
        if (!CmdArrayUtils.isOOB(response)) {
            logger.error("Wrong response. Must be OOB");
            state = State.DEINIT;
            return;
        }

        String[] items = CmdArrayUtils.cmdToString(response).split("\\s+");
        if (items.length != 2 || !items[0].toLowerCase().equals("challengeresponse")) {
            logger.error("Wrong response to \"getchallenge\" command");
            state = State.DEINIT;
            return;
        }
        challenge = items[1];
        socket.poll();

        setState(State.CONNECT);
    }

    private void stateConnect() {
        Map<String, String> connectionParams = new HashMap<>();
        connectionParams.put("cg_predictItems", "1");
        connectionParams.put("cl_anonymous", "0");
        connectionParams.put("cl_punkbuster", "1");
        connectionParams.put("cl_voice", "1");
        connectionParams.put("cl_wwwDownload", "1");
        connectionParams.put("rate", "25000");
        connectionParams.put("snaps", "10");
        connectionParams.put("protocol", "118");
        connectionParams.put("qport", String.valueOf(SocketUtility.randomPort()));
        connectionParams.put("name", player.getName());
        connectionParams.put("challenge", challenge);
        String connectionString = String.format("connect \"%s\"", KeyValueMap.mapToStrong(connectionParams));
        byte[] cmd = CmdArrayUtils.stringToCmd(connectionString);
        logger.debug(String.format("Connection string: %s", new String(cmd)));

        try {
            socket.send(cmd);
        } catch (IOException e) {
            e.printStackTrace();
            state = State.DEINIT;
            return;
        }

        byte[] response = socket.read();
        if (!CmdArrayUtils.isOOB(response)) {
            logger.error("Wrong response. Must be OOB");
            state = State.DEINIT;
            return;
        }

        String[] items = CmdArrayUtils.cmdToString(response).split("\\s+");
        if (!items[0].toLowerCase().equals("connectresponse")) {
            logger.error("Wrong response to \"connect\" command");
            state = State.DEINIT;
            return;
        }

        socket.poll();

        setState(State.IN_GAME);
    }

    private void stateInGame() {
        try {
            while (true) {
                Packet packet = PacketParser.parse(socket.poll());
                if (packet == null) {
                    throw new GenericProtocolException();
                }
                logger.debug(String.format("Packet %d: %s",
                        packet.getSequenceNumber(),
                        ByteArrayUtility.toHex(packet.getRawData())));
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
    }
}
