/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Utils.normal;

public class TimerUtil {
    public long lastMS = System.currentTimeMillis();

    public void reset() {
        this.lastMS = System.currentTimeMillis();
    }

    public boolean hasTimeElapsed(long time, boolean reset) {
        if (System.currentTimeMillis() - this.lastMS > time) {
            if (reset) {
                this.reset();
            }
            return true;
        }
        return false;
    }

    public boolean hasTimeElapsed(long time) {
        return System.currentTimeMillis() - this.lastMS > time;
    }

    public long getTime() {
        return System.currentTimeMillis() - this.lastMS;
    }

    public void setTime(long time) {
        this.lastMS = time;
    }
}

