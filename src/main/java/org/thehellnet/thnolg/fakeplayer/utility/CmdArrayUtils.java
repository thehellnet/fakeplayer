package org.thehellnet.thnolg.fakeplayer.utility;

import java.util.Arrays;

/**
 * Created by sardylan on 07/06/16.
 */
public final class CmdArrayUtils {

    public static boolean isOOB(byte[] cmd) {
        return (cmd != null) &&
                (cmd[0] == (byte) 0xFF
                        && cmd[1] == (byte) 0xFF
                        && cmd[2] == (byte) 0xFF
                        && cmd[3] == (byte) 0xFF);
    }

    public static byte[] cleanOOB(byte[] rawCommand) {
        return isOOB(rawCommand)
                ? Arrays.copyOfRange(rawCommand, 4, rawCommand.length)
                : rawCommand;
    }

    public static String cmdToString(byte[] cmd) {
        return ByteArrayUtility.toCleanString(cleanOOB(cmd));
    }

    public static byte[] stringToCmd(String command) {
        if (command == null || command.length() == 0) {
            return null;
        }

        byte[] cmd = new byte[4 + command.length()];
        cmd[0] = (byte) 0xFF;
        cmd[1] = (byte) 0xFF;
        cmd[2] = (byte) 0xFF;
        cmd[3] = (byte) 0xFF;
        System.arraycopy(command.getBytes(), 0, cmd, 4, command.length());
        return cmd;
    }
}
