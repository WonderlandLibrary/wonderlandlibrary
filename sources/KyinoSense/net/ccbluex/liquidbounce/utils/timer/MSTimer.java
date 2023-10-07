/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.timer;

public final class MSTimer {
    public long time = -1L;
    private long prevMS = this.getTime();

    public boolean hasTimePassed(long MS) {
        return System.currentTimeMillis() >= this.time + MS;
    }

    public long hasTimeLeft(long MS) {
        return MS + this.time - System.currentTimeMillis();
    }

    public void reset() {
        this.prevMS = this.getTime();
        this.time = System.currentTimeMillis();
    }

    public boolean delay(float milliSec) {
        return (float)(this.getTime() - this.prevMS) >= milliSec;
    }

    public long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public long getDifference() {
        return this.getTime() - this.prevMS;
    }

    public void setDifference(long difference) {
        this.prevMS = this.getTime() - difference;
    }

    public void reset2() {
        this.time = System.currentTimeMillis();
    }
}

