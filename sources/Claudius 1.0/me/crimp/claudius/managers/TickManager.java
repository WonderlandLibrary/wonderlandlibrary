package me.crimp.claudius.managers;

import me.crimp.claudius.utils.Globals;

public class TickManager implements Globals {
    private float tickLength = 50.0f;

    public void onUpdate() {
        mc.timer.tickLength = this.tickLength;
    }

    public void setTicks(float ticks) {
        this.setTicks(50.0f, ticks);
    }

    public void setTicks(float factor, float ticks) {
        this.setTickLength(factor / ticks);
    }

    public void reset() {
        this.tickLength = 50.0f;
    }

    public float getTickLength() {
        return tickLength;
    }

    public void setTickLength(float tickLength) {
        this.tickLength = tickLength;
    }
}