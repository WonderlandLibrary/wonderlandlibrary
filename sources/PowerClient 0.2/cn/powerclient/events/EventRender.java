/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.events;

import com.darkmagician6.eventapi.events.Event;

public class EventRender
implements Event {
    private float partialTicks;

    public EventRender(float a2) {
        this.partialTicks = a2;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}

