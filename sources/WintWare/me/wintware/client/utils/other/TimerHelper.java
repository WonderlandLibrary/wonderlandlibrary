/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.utils.other;

public class TimerHelper {
    private long previousTime = -1L;

    public boolean check(float milliseconds) {
        return (float)(this.getCurrentTime() - this.previousTime) >= milliseconds;
    }

    public void reset() {
        this.previousTime = this.getCurrentTime();
    }

    public short convert(float perSecond) {
        return (short)(1000.0f / perSecond);
    }

    public long get() {
        return this.previousTime;
    }

    public long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public long getTime() {
        return System.currentTimeMillis() - this.previousTime;
    }
}

