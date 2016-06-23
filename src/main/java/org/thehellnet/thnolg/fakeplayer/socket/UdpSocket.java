package org.thehellnet.thnolg.fakeplayer.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thehellnet.thnolg.fakeplayer.utility.ByteArrayUtility;

import java.io.IOException;
import java.net.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/**
 * Created by sardylan on 07/06/16.
 */
public class UdpSocket {

    private static final Logger logger = LoggerFactory.getLogger(UdpSocket.class);

    private final Object SYNC = new Object();

    private String address;
    private int port;

    private DatagramSocket socket;
    private Thread thread;
    private InetAddress inetAddress;
    private Queue<byte[]> queue = new ArrayDeque<>();

    public UdpSocket(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void connect() throws SocketException, UnknownHostException {
        logger.debug("Connecting socket");
        socket = new DatagramSocket();
        inetAddress = InetAddress.getByName(address);
        socket.connect(inetAddress, port);

        thread = new Thread(() -> {
            while (!thread.isInterrupted()) {
                byte[] buffer = new byte[4096];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
                byte[] data = Arrays.copyOfRange(packet.getData(), 0, packet.getLength());
//                logger.debug(String.format("Receiving data: %s", ByteArrayUtility.toHex(data)));
                queue.add(data);
            }
        });
        thread.setDaemon(true);
        logger.debug("Starting read thread");
        thread.start();
    }

    public void disconnect() {
        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread = null;
        socket.close();
    }

    public void join() throws InterruptedException {
        thread.join();
    }

    public void send(byte[] data) throws IOException {
        logger.debug("Sending data");
        socket.send(new DatagramPacket(data, 0, data.length, inetAddress, port));
    }

    public byte[] read() {
        synchronized (SYNC) {
            waitForQueue();
//            logger.debug("Requesting data from queue");
            return queue.peek();
        }
    }

    public byte[] poll() {
        synchronized (SYNC) {
            waitForQueue();
//            logger.debug("Removing data from queue");
            return queue.poll();
        }
    }

    private void waitForQueue() {
        logger.debug("Waiting for socket data");
        while (queue.isEmpty()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
