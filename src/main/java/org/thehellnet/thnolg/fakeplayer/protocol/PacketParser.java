package org.thehellnet.thnolg.fakeplayer.protocol;

import org.thehellnet.thnolg.fakeplayer.utility.ByteArrayUtility;

import java.util.Arrays;

/**
 * Created by sardylan on 23/06/16.
 */
public class PacketParser {

    public static Packet parse(byte[] rawData) {
        if (rawData == null) {
            return null;
        }

        Packet packet = new Packet();
        packet.setSequenceNumber(ByteArrayUtility.toLong(Arrays.copyOfRange(rawData, 0, 8)));
        packet.setRawData(Arrays.copyOfRange(rawData, 8, rawData.length));
        return packet;
    }
}
