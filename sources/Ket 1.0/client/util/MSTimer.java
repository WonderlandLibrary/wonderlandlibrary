package client.util;

public final class MSTimer {
    private long time = System.currentTimeMillis();

    public long getPassedTime() {
        return System.currentTimeMillis() - time;
    }

    public boolean hasTimePassed(final long millis) {
        return getPassedTime() >= millis;
    }

    public long getRemainingTime(final long millis) {
        return Math.max(millis - getPassedTime(), 0L);
    }

    public void reset() {
        time = System.currentTimeMillis();
    }
}