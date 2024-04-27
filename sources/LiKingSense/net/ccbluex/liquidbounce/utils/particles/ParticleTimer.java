/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.particles;

public class ParticleTimer {
    public long lastMS;
    private long time;
    private long prevTime;

    private long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasReached(double milliseconds) {
        return (double)(this.getCurrentMS() - this.lastMS) >= milliseconds;
    }

    public void setTime(long time) {
        this.lastMS = time;
    }

    public boolean hasTimeElapsed(long time) {
        return System.currentTimeMillis() - this.lastMS > time;
    }

    public boolean hasPassed(double milli) {
        return (double)(System.currentTimeMillis() - this.prevTime) >= milli;
    }

    public boolean sleep(long time) {
        if (this.time() >= time) {
            this.reset();
            return true;
        }
        return false;
    }

    public long time() {
        return System.nanoTime() / 1000000L - this.time;
    }

    public final long getElapsedTime() {
        return this.getCurrentMS() - this.lastMS;
    }

    public void reset() {
        this.lastMS = this.getCurrentMS();
    }

    public boolean delay(float milliSec) {
        return (float)(this.getTime() - this.lastMS) >= milliSec;
    }

    public long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public boolean isDelayComplete(long delay) {
        return System.currentTimeMillis() - this.lastMS > delay;
    }
}

