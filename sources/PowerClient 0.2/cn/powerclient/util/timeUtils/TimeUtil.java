/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.util.timeUtils;

public class TimeUtil {
    static long prevMS;
    private long time;
    protected static long lastMS;
    private long lastMS1;

    public TimeUtil() {
        prevMS = 0L;
        lastMS = -1L;
        this.time = System.nanoTime() / 1000000L;
    }

    public static boolean delay(float milliSec) {
        if ((float)(TimeUtil.getTime() - prevMS) >= milliSec) {
            return true;
        }
        return false;
    }

    public static boolean delay(double milliSec) {
        if ((double)(TimeUtil.getTime() - prevMS) >= milliSec) {
            return true;
        }
        return false;
    }

    public boolean delay1(float milliSec) {
        if ((float)(TimeUtil.getTime() - prevMS) >= milliSec) {
            return true;
        }
        return false;
    }

    public void reset1() {
        this.lastMS1 = TimeUtil.getCurrentMS();
    }

    public static void reset() {
        prevMS = TimeUtil.getTime();
    }

    public static long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public long time() {
        return System.nanoTime() / 1000000L - this.time;
    }

    public long getDifference() {
        return TimeUtil.getTime() - prevMS;
    }

    public void setDifference(long difference) {
        prevMS = TimeUtil.getTime() - difference;
    }

    public boolean hasTimeElapsed(long time, boolean reset) {
        if (this.time() >= time) {
            if (reset) {
                TimeUtil.reset();
            }
            return true;
        }
        return false;
    }

    public void setLastMS() {
        lastMS = System.currentTimeMillis();
    }

    public boolean isDelayComplete(long delay) {
        if (System.currentTimeMillis() - lastMS >= delay) {
            return true;
        }
        return false;
    }

    public long getLastMs() {
        return System.currentTimeMillis() - lastMS;
    }

    public static long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public long getLastMS() {
        return lastMS;
    }

    public static boolean hasReached(long milliseconds) {
        if (TimeUtil.getCurrentMS() - lastMS >= milliseconds) {
            return true;
        }
        return false;
    }

    public void setLastMS(long currentMS) {
        lastMS = currentMS;
    }

    public boolean hasReached(double milliseconds) {
        if ((double)(TimeUtil.getCurrentMS() - lastMS) >= milliseconds) {
            return true;
        }
        return false;
    }
}

