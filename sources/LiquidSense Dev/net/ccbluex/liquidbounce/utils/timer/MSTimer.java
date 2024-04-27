/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.utils.timer;

public final class MSTimer {

    private long time = -1L;

    private long lastMs;

    public boolean isDelayComplete(long delay) {
        if (System.currentTimeMillis() - this.lastMs > delay) {
            return true;
        }
        return false;
    }

    public void Reset() {
        lastMs = System.currentTimeMillis();
    }
    public boolean hasTimePassed(final long MS) {
        return System.currentTimeMillis() >= time + MS;
    }

    public long hasTimeLeft(final long MS) {
        return (MS + time) - System.currentTimeMillis();
    }

    public void reset() {
        time = System.currentTimeMillis();
    }

    public long getLastMs() {
        return this.lastMs;
    }

    public void setLastMs(int i) {
        this.lastMs = System.currentTimeMillis() + (long)i;
    }

    public void setLastMS() {
        this.lastMs = System.currentTimeMillis();
    }

    public long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public double getPassed() {
        return (double)(this.getTime() - this.lastMs);
    }

    public void reSet() {
        this.lastMs = this.getTime();
    }

    public boolean hasPassed(double milli, boolean reSet) {
        boolean result = (double)(this.getTime() - this.lastMs) >= milli;
        if (reSet) {
            this.reSet();
        }

        return result;
    }

    public boolean hasPassed(double milli) {
        return (double)(this.getTime() - this.lastMs) >= milli;
    }

}

