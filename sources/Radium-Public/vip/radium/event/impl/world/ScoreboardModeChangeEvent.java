package vip.radium.event.impl.world;

import vip.radium.event.Event;

public final class ScoreboardModeChangeEvent implements Event {

    private final String mode;

    public ScoreboardModeChangeEvent(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

}
