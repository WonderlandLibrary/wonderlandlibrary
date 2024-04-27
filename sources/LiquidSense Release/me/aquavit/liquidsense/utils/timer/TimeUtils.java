package me.aquavit.liquidsense.utils.timer;

import me.aquavit.liquidsense.utils.misc.RandomUtils;

public final class TimeUtils {
    private long lastMS;

    public static long randomDelay(final int minDelay, final int maxDelay) {
        return RandomUtils.nextInt(minDelay, maxDelay);
    }

    public static long randomClickDelay(final int minCPS, final int maxCPS) {
        return (long) ((Math.random() * (1000 / minCPS - 1000 / maxCPS + 1)) + 1000 / maxCPS);
    }

    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasReached(long milliseconds) {
        return (double) (getCurrentMS() - lastMS) >= milliseconds;
    }

    public void reset() {
        lastMS = getCurrentMS();
    }
}