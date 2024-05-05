package slavikcodd3r.rainbow.utils;

public class TimerDelay
{
    private long prevMS;
    private static long lastMS;
    
    public TimerDelay() {
        this.prevMS = 0L;
    }
    
    public boolean delay(final double speed) {
        return this.getTime() - this.prevMS >= speed;
    }
    
    public boolean hasPassed(final float milliSec) {
        return this.getTime() - this.prevMS >= milliSec;
    }
    
    public static long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }
    
    public void reset() {
        this.prevMS = this.getTime();
    }
    
    public static boolean hasReached(final long milliseconds) {
        return getCurrentMS() - TimerDelay.lastMS >= milliseconds;
    }
    
    public long getTime() {
        return System.nanoTime() / 1000000L;
    }
    
    public long getDifference() {
        return this.getTime() - this.prevMS;
    }
    
    public void setDifference(final long difference) {
        this.prevMS = this.getTime() - difference;
    }
}
