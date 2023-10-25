/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.event.events.impl.motion;

import org.celestial.client.event.events.Event;

public class EventStep
implements Event {
    private final boolean pre;
    private double stepHeight;

    public EventStep(boolean pre, double stepHeight) {
        this.pre = pre;
        this.stepHeight = stepHeight;
    }

    public boolean isPre() {
        return this.pre;
    }

    public double getStepHeight() {
        return this.stepHeight;
    }

    public void setStepHeight(double stepHeight) {
        this.stepHeight = stepHeight;
    }
}

