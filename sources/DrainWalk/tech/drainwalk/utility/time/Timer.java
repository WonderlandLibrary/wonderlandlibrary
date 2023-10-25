package tech.drainwalk.utility.time;

import tech.drainwalk.utility.Utility;

public class Timer extends Utility {
    public long lastMs = 0L;

    public Timer() {
    }

    public void reset() {
        this.lastMs = System.currentTimeMillis();
    }

    public boolean delay(long nextDelay) {
        return System.currentTimeMillis() - this.lastMs >= nextDelay;
    }

    public boolean delay(float nextDelay, boolean reset) {
        if ((float)(System.currentTimeMillis() - this.lastMs) >= nextDelay) {
            if (reset) {
                this.reset();
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean isDelayComplete(double valueState) {
        return (double)(System.currentTimeMillis() - this.lastMs) >= valueState;
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - this.lastMs;
    }

    public long getLastMs() {
        return this.lastMs;
    }
}
