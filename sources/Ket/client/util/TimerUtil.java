package client.util;

public class TimerUtil {
    private long lastTime;
    private long time;

    public TimerUtil() {
        this.reset();
    }

    public long getCurrentTime() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasHit(float cPS) {
        return (float)this.getDifference() >= cPS;
    }

    public boolean hasHit(double cPS) {
        return (double)this.getDifference() >= cPS;
    }

    public long getLastTime() {
        return this.lastTime;
    }

    public long getDifference() {
        return this.getCurrentTime() - this.lastTime;
    }

    public void reset() {
        this.lastTime = this.getCurrentTime();
    }

    public boolean hasReached(long milliseconds) {
        return this.getDifference() >= milliseconds;
    }

    public boolean hasCompleted(long l) {
        return false;
    }

    public boolean reach(final long time) {
        return time() >= time;
    }
    public long time() {
        return System.nanoTime() / 1000000L - time;
    }
    public long lastMS = System.currentTimeMillis();

    public void reset1() {
        lastMS = System.currentTimeMillis();
    }
    public boolean hasTimeElapsed(long time, boolean reset) {
        if(System.currentTimeMillis()-lastMS > time) {
            if(reset)
                reset1();

            return true;
        }

        return false;
    }

    public boolean delay(float milliSec) {
        return ((float)(getTime() - this.lastMS) >= milliSec);
    }

    public long getTime() {
        return System.nanoTime() / 1000000L;
    }
}