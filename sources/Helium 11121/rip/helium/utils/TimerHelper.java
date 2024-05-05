package rip.helium.utils;

public class TimerHelper {
    private long prevTime;

    public TimerHelper() {
        reset();
    }

    public boolean hasPassed(double milli) {
        return getTime() - this.prevTime >= milli;
    }

    public long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public void reset() {
        this.prevTime = getTime();
    }
}
