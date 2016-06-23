package org.thehellnet.thnolg.fakeplayer.utility;

import java.util.Random;

/**
 * Created by sardylan on 23/06/16.
 */
public final class SocketUtility {

    public static int randomPort() {
        Random random = new Random();
        return random.nextInt(65535 - 1024) + 1024;
    }
}
