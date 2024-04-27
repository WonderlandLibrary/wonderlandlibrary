/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils;

public class TimeHelper {
    public long lastMs = 0L;

    public boolean delay(float f, boolean bl) {
        if ((float)(System.currentTimeMillis() - this.lastMs) >= f) {
            if (bl) {
                this.reset();
            }
            return true;
        }
        return false;
    }

    public long getLastMs() {
        return this.lastMs;
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - this.lastMs;
    }

    public void reset() {
        this.lastMs = System.currentTimeMillis();
    }

    public boolean isDelayComplete(double d) {
        return (double)(System.currentTimeMillis() - this.lastMs) >= d;
    }

    public boolean delay(long l) {
        return System.currentTimeMillis() - this.lastMs >= l;
    }
}

