/*
 * Decompiled with CFR 0.152.
 */
package dev.sakura_starring.util.time;

public class Timer {
    public long lastMS = System.currentTimeMillis();

    public boolean hasTickElapsed(double d, boolean bl) {
        return this.hasTimeElapsed((long)(d * 50.0), bl);
    }

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

