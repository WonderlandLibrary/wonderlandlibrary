/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.events;

import com.darkmagician6.eventapi.events.Event;

public final class RenderPostBobbingEvent
implements Event {
    private float partialTicks;
    private int pass;

    public RenderPostBobbingEvent(float partialTicks, int pass) {
        this.partialTicks = partialTicks;
        this.pass = pass;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public int getPass() {
        return this.pass;
    }
}

