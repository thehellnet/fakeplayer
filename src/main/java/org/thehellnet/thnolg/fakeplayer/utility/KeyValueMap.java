package org.thehellnet.thnolg.fakeplayer.utility;

import java.util.Map;

/**
 * Created by sardylan on 23/06/16.
 */
public final class KeyValueMap {

    public static String mapToStrong(Map<String, String> params) {
        if (params == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        params.forEach((key, value) -> stringBuilder.append(String.format("\\%s\\%s", key, value)));
        return stringBuilder.toString();
    }
}
