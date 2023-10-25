package club.async.util;

public class TimeUtil {

    private long lastTime = getCurrentTime();

    public TimeUtil() {
        reset();
    }

    public void reset() {
        lastTime = getCurrentTime();
    }

    public long getCurrentTime() {
        return System.nanoTime() / 1000000;
    }

    public long getLastTime() {
        return lastTime;
    }

    public long getDifference() {
        return getCurrentTime() - lastTime;
    }

    public boolean hasTimePassed(long milliseconds) {
        return getDifference() >= milliseconds;
    }

}
