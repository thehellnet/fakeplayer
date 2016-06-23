package org.thehellnet.thnolg.fakeplayer.utility;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by sardylan on 23/06/16.
 */
public class ByteArrayUtilityTest {

    @Test
    public void testToLong() {
        byte[] input;
        long expected;
        long actual;

        input = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        expected = 0L;
        actual = ByteArrayUtility.toLong(input);
        assertEquals(expected, actual);

        input = new byte[]{0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        expected = 1L;
        actual = ByteArrayUtility.toLong(input);
        assertEquals(expected, actual);

        input = new byte[]{(byte) 0xff, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        expected = 255L;
        actual = ByteArrayUtility.toLong(input);
        assertEquals(expected, actual);

        input = new byte[]{0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        expected = 256L;
        actual = ByteArrayUtility.toLong(input);
        assertEquals(expected, actual);

        input = new byte[]{0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00};
        expected = 256L * 256L;
        actual = ByteArrayUtility.toLong(input);
        assertEquals(expected, actual);

        input = new byte[]{0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00};
        expected = 256L * 256L * 256L;
        actual = ByteArrayUtility.toLong(input);
        assertEquals(expected, actual);

        input = new byte[]{0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00};
        expected = 256L * 256L * 256L * 256L;
        actual = ByteArrayUtility.toLong(input);
        assertEquals(expected, actual);

        input = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00};
        expected = 256L * 256L * 256L * 256L * 256L;
        actual = ByteArrayUtility.toLong(input);
        assertEquals(expected, actual);

        input = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00};
        expected = 256L * 256L * 256L * 256L * 256L * 256L;
        actual = ByteArrayUtility.toLong(input);
        assertEquals(expected, actual);
    }
}