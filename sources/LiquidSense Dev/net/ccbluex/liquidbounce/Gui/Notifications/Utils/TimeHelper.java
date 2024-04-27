package net.ccbluex.liquidbounce.Gui.Notifications.Utils;

public class TimeHelper {
    public long lastMs;

    public TimeHelper() {
        this.lastMs = 0L;
    }

    public boolean isDelayComplete(long delay) {
        if (System.currentTimeMillis() - this.lastMs > delay) {
            return true;
        }
        return false;
    }

    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }
    public boolean hasPassed(double milli, boolean reset) {
        boolean result = (double)(this.getCurrentMS() - this.lastMs) >= milli;
        if (reset) {
            this.reset();
        }

        return result;
    }
    public void reset() {
        this.lastMs = System.currentTimeMillis();
    }

    public long getLastMs() {
        return this.lastMs;
    }

    public void setLastMs(int i) {
        this.lastMs = System.currentTimeMillis() + (long)i;
    }

    public boolean hasReached(long milliseconds) {
        return this.getCurrentMS() - this.lastMs >= milliseconds;
    }

    public boolean hasReached(float timeLeft) {
        return (float)(this.getCurrentMS() - this.lastMs) >= timeLeft;
    }

    public boolean delay(double nextDelay) {
        return System.currentTimeMillis() - lastMs >= nextDelay;
    }

    public void setLastMS() {
        this.lastMs = System.currentTimeMillis();
    }
}
