package org.thehellnet.thnolg.fakeplayer.statemachine;

/**
 * Created by sardylan on 07/06/16.
 */
public enum State {
    INIT,
    DEINIT,
    GET_KEY_AUTHORIZE,
    GET_CHALLENGE,
    CONNECT,
    IN_GAME,
    END
}
