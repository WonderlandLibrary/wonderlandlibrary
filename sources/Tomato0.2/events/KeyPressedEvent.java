/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.events;

import com.darkmagician6.eventapi.events.Event;

public final class KeyPressedEvent
implements Event {
    private final int eventKey;

    public KeyPressedEvent(int eventKey) {
        this.eventKey = eventKey;
    }

    public int getEventKey() {
        return this.eventKey;
    }
}

