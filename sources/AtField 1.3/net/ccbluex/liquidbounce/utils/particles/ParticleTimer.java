/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.particles;

public class ParticleTimer {
    private long prevTime;
    private long time;
    public long lastMS;

    public final long getElapsedTime() {
        return this.getCurrentMS() - this.lastMS;
    }

    public boolean hasReached(double d) {
        return (double)(this.getCurrentMS() - this.lastMS) >= d;
    }

    public void setTime(long l) {
        this.lastMS = l;
    }

    public boolean sleep(long l) {
        if (this.time() >= l) {
            this.reset();
            return true;
        }
        return false;
    }

    public boolean delay(float f) {
        return (float)(this.getTime() - this.lastMS) >= f;
    }

    public long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public void reset() {
        this.lastMS = this.getCurrentMS();
    }

    public boolean hasPassed(double d) {
        return (double)(System.currentTimeMillis() - this.prevTime) >= d;
    }

    public boolean isDelayComplete(long l) {
        return System.currentTimeMillis() - this.lastMS > l;
    }

    public long time() {
        return System.nanoTime() / 1000000L - this.time;
    }

    public boolean hasTimeElapsed(long l) {
        return System.currentTimeMillis() - this.lastMS > l;
    }

    private long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }
}

