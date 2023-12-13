package summer.base.utilities;

public class TimerUtils {
    private long lastMS;

    private long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasReached(final double milliseconds) {
        return this.getCurrentMS() - this.lastMS >= milliseconds;
    }

    public void reset() {
        this.lastMS = this.getCurrentMS();
    }

    public boolean delay(final float milliSec) {
        return this.getTime() - this.lastMS >= milliSec;
    }

    public long getTime() {
        return getCurrentMS() - this.lastMS;//System.nanoTime() / 1000000L;
    }
}
