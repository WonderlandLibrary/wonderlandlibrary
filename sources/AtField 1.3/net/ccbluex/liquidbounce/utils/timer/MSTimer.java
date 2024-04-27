/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.timer;

public final class MSTimer {
    private long time = -1L;

    public long hasTimeLeft(long l) {
        return l + this.time - System.currentTimeMillis();
    }

    public boolean hasTimePassed(long l) {
        return System.currentTimeMillis() >= this.time + l;
    }

    public void reset() {
        this.time = System.currentTimeMillis();
    }
}

