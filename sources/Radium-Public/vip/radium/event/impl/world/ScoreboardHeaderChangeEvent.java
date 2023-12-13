package vip.radium.event.impl.world;

import vip.radium.event.Event;

public final class ScoreboardHeaderChangeEvent implements Event {
    private final String header;

    public ScoreboardHeaderChangeEvent(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }
}
