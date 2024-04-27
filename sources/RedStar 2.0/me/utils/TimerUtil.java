package me.utils;

public class TimerUtil {
    public long lastMS = System.currentTimeMillis();
    private long currentMs;

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

    public boolean hasElapsed(long milliseconds) {
        return this.elapsed() > milliseconds;
    }

    public long elapsed() {
        return System.currentTimeMillis() - this.currentMs;
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
