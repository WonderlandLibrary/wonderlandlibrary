/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util;

public class Timer {
    public long lastMS = System.currentTimeMillis();

    public void reset() {
        this.lastMS = System.currentTimeMillis();
    }

    public boolean hasTimeElapsed(long l, boolean bl) {
        if (System.currentTimeMillis() - this.lastMS > l) {
            if (bl) {
                this.reset();
            }
            return true;
        }
        return false;
    }
}

