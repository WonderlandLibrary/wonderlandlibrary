/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.timer;

public final class MSTimer {
    private long time = -1L;

    public boolean hasTimePassed(long MS) {
        return System.currentTimeMillis() >= this.time + MS;
    }

    public long hasTimeLeft(long MS) {
        return MS + this.time - System.currentTimeMillis();
    }

    public void reset() {
        this.time = System.currentTimeMillis();
    }
}

