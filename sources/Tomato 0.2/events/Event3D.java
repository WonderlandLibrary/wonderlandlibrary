/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.events;

import com.darkmagician6.eventapi.events.Event;

public class Event3D
implements Event {
    private static float entityT;

    public Event3D(float entityT) {
        Event3D.entityT = entityT;
    }

    public static float getPartialTicks() {
        return entityT;
    }
}

