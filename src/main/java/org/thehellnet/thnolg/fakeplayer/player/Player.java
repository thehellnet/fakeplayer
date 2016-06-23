package org.thehellnet.thnolg.fakeplayer.player;

/**
 * Created by sardylan on 07/06/16.
 */
public class Player {

    private String name;
    private String key;

    public String getName() {
        return name;
    }

    public Player setName(String name) {
        if (name != null && name.length() > 0) {
            this.name = name;
            return this;
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public Player setKey(String key) {
        if (key != null && key.length() == 20) {
            this.key = key.toUpperCase();
            return this;
        }
        return null;
    }
}
