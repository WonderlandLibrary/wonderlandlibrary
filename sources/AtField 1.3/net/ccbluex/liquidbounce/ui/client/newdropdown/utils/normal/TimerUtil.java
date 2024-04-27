/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.newdropdown.utils.normal;

public class TimerUtil {
    public long lastMS = System.currentTimeMillis();

    public long getTime() {
        return System.currentTimeMillis() - this.lastMS;
    }

    public void setTime(long l) {
        this.lastMS = l;
    }

    public boolean hasTimeElapsed(long l) {
        return System.currentTimeMillis() - this.lastMS > l;
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

