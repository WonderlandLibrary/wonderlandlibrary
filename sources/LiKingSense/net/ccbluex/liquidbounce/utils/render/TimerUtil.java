/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.render;

public final class TimerUtil {
    private long time = -1L;
    private int tick;
    private long currentMS = System.currentTimeMillis();

    public void update() {
        ++this.tick;
    }

    public void reset3() {
        this.tick = 0;
    }

    public boolean hasTimePassed3(int ticks) {
        return this.tick >= ticks;
    }

    public long lastReset() {
        return this.currentMS;
    }

    public boolean hasElapsed(long milliseconds) {
        return this.elapsed() > milliseconds;
    }

    public boolean hasTimeElapsed(long time, boolean reset) {
        if (this.currentMS > System.currentTimeMillis()) {
            this.currentMS = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - this.currentMS > time) {
            if (reset) {
                this.reset();
            }
            return true;
        }
        return false;
    }

    public boolean hasTimePassed(long MS) {
        return System.currentTimeMillis() >= this.time + MS;
    }

    public long hasTimeLeft(long MS) {
        return MS + this.time - System.currentTimeMillis();
    }

    public long elapsed() {
        return System.currentTimeMillis() - this.currentMS;
    }

    public void reset() {
        this.currentMS = System.currentTimeMillis();
    }

    public void reset2() {
        this.time = System.currentTimeMillis();
    }

    public void setCurrentMS(long currentMS) {
        this.currentMS = currentMS;
    }
}

