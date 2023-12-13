/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.events;

import com.darkmagician6.eventapi.events.Event;

public class EventTracers
implements Event {
    private final float entityT;

    public EventTracers(float entityT) {
        this.entityT = entityT;
    }

    public float getPartialTicks() {
        return this.entityT;
    }
}

