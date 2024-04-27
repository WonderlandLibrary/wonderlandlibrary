/*
 * Code by Matilda Staff
 */
package net.ccbluex.liquidbounce.utils.timer;

import java.util.TimerTask;

public class TimerUtil {
    static long prevMS;
    private long time;
    protected static long lastMS;
    private long lastMS1;

    public TimerUtil() {
        prevMS = 0L;
//        lastMS = -1L;
        this.time = System.nanoTime() / 1000000L;
    }

    public static boolean delay(float milliSec) {
        if ((float)(TimerUtil.getTime() - prevMS) >= milliSec) {
            return true;
        }
        return false;
    }

    public static boolean delay(double milliSec) {
        if ((double)(TimerUtil.getTime() - prevMS) >= milliSec) {
            return true;
        }
        return false;
    }

    public boolean delay1(float milliSec) {
        if ((float)(TimerUtil.getTime() - prevMS) >= milliSec) {
            return true;
        }
        return false;
    }

    public void reset1() {
        this.lastMS1 = TimerUtil.getCurrentMS();
    }

    public static void reset() {
        prevMS = TimerUtil.getTime();
    }

    public static long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public long time() {
        return System.nanoTime() / 1000000L - this.time;
    }

    public long getDifference() {
        return TimerUtil.getTime() - prevMS;
    }

    public void setDifference(long difference) {
        prevMS = TimerUtil.getTime() - difference;
    }

    public boolean hasTimeElapsed(long time, boolean reset) {
        if (this.time() >= time) {
            if (reset) {
                TimerUtil.reset();
            }
            return true;
        }
        return false;
    }

    public void setLastMS() {
        lastMS = System.currentTimeMillis();
    }

    public static boolean isDelayComplete(long delay) {
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
        if (TimerUtil.getCurrentMS() - lastMS >= milliseconds) {
            return true;
        }
        return false;
    }

    public void setLastMS(long currentMS) {
        lastMS = currentMS;
    }

    public boolean hasReached(double milliseconds) {
        if ((double)(TimerUtil.getCurrentMS() - lastMS) >= milliseconds) {
            return true;
        }
        return false;
    }

    public boolean hasTimeElapsed(final long time) {
        if (this.time() >= time)
            return true;
        return false;
    }

    public boolean hasTicksElapsed(int ticks) {
        if (this.time() >= (1000 / ticks) - 50)
            return true;
        return false;
    }

    public void schedule(TimerTask timerTask, long l) {


    }

    public boolean check(float milliseconds) {
        return getTime() >= milliseconds;
    }
}

