package client.util;

public final class TimeUtil {
    private long time = System.currentTimeMillis();
    public boolean hasTimePassed(final long time) {
        return System.currentTimeMillis() >= this.time + time;
    }
    public long hasTimeLeft(final long time) {
        return (time + this.time) - System.currentTimeMillis();
    }
    public void reset() {
        time = System.currentTimeMillis();
    }
}