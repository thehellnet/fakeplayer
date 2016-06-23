package org.thehellnet.thnolg.fakeplayer.utility;

/**
 * Created by sardylan on 23/06/16.
 */
public final class ByteArrayUtility {

    public static String toCleanString(byte[] input) {
        return new String(input).replaceAll("\\x00", "");
    }

    public static String toHex(byte[] input) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : input) {
            stringBuilder.append(String.format("%02X ", b));
        }
        return stringBuilder.toString();
    }

    public static long toLong(byte[] input) {
        long value = 0;
        for (int i = 0; i < input.length; i++) {
            value += ((long) input[i] & 0xffL) << (8 * i);
        }
        return value;
    }
}
