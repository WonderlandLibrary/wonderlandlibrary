package vip.radium.utils;

public final class TimerUtil {

    private long currentMs;

    public TimerUtil() {
        reset();
    }

    public long lastReset() {
        return currentMs;
    }

    public boolean hasElapsed(long milliseconds) {
        return elapsed() > milliseconds;
    }

    public long elapsed() {
        return System.currentTimeMillis() - currentMs;
    }

    public void reset() {
        currentMs = System.currentTimeMillis();
    }
    }
