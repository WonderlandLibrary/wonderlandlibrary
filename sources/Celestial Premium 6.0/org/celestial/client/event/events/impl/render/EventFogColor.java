/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.event.events.impl.render;

import org.celestial.client.event.events.Event;

public class EventFogColor
implements Event {
    public float red;
    public float green;
    public float blue;
    public int alpha;

    public EventFogColor(float red, float green, float blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public float getRed() {
        return this.red;
    }

    public void setRed(float red) {
        this.red = red;
    }

    public float getGreen() {
        return this.green;
    }

    public void setGreen(float green) {
        this.green = green;
    }

    public float getBlue() {
        return this.blue;
    }

    public void setBlue(float blue) {
        this.blue = blue;
    }

    public int getAlpha() {
        return this.alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }
}

