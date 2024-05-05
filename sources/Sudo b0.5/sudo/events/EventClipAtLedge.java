package sudo.events;

import sudo.core.event.Event;

public class EventClipAtLedge extends Event {
    private static final EventClipAtLedge INSTANCE = new EventClipAtLedge();

    private boolean set, clip;

    public void reset() {
        set = false;
    }

    public void setClip(boolean clip) {
        set = true;
        this.clip = clip;
    }

    public boolean isSet() {
        return set;
    }
    public boolean isClip() {
        return clip;
    }

    public static EventClipAtLedge get() {
        INSTANCE.reset();
        return INSTANCE;
    }
}