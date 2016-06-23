package org.thehellnet.thnolg.fakeplayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thehellnet.thnolg.fakeplayer.player.Player;
import org.thehellnet.thnolg.fakeplayer.socket.UdpSocket;
import org.thehellnet.thnolg.fakeplayer.statemachine.StateMachine;

/**
 * Created by sardylan on 07/06/16.
 */
public class FakePlayer {

    private static final Logger logger = LoggerFactory.getLogger(FakePlayer.class);

    private UdpSocket socket;
    private Player player;

    private StateMachine stateMachine;

    public static void main(String[] args) {
        FakePlayer fakePlayer = new FakePlayer();
        logger.info("FakePlayer START");
        try {
            fakePlayer.run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("FakePlayer END");
    }

    private void run(String[] args) throws Exception {
        logger.info("Creating player");
        player = new Player();

        logger.info("Creating socket");
        socket = new UdpSocket(args[0], 28960);
        socket.connect();

        logger.info("Creating state machine");
        stateMachine = new StateMachine(socket, player);
        stateMachine.run();
    }
}
