package tech.atani.client.listener.event.game;

import tech.atani.client.listener.event.Event;

public class GameEndEvent extends Event {

    private final boolean isWin;

    public GameEndEvent(boolean isWin) {
        this.isWin = isWin;
    }

    public boolean isWin() {
        return isWin;
    }
}
