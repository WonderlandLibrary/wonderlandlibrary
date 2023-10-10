/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.utils;

public class Timer {
    public long lastMS = System.currentTimeMillis();

    public void reset() {
        this.lastMS = System.currentTimeMillis();
    }

    public boolean hasTimeElapsed(long time) {
        return this.hasTimeElapsed(time, false);
    }

    public boolean hasTimeElapsed(long time, boolean reset) {
        if (this.lastMS > System.currentTimeMillis()) {
            this.lastMS = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - this.lastMS > time) {
            if (reset) {
                this.reset();
            }
            return true;
        }
        return false;
    }

    public long getEllapsedTime() {
        return System.currentTimeMillis() - this.lastMS;
    }
}

