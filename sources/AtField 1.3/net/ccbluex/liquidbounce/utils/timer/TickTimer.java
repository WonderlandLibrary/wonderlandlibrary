/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.timer;

public final class TickTimer {
    private int tick;

    public boolean hasTimePassed(int n) {
        return this.tick >= n;
    }

    public void update() {
        ++this.tick;
    }

    public void reset() {
        this.tick = 0;
    }
}

