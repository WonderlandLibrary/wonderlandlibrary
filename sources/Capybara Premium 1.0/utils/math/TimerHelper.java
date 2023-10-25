package fun.rich.client.utils.math;

import fun.rich.client.utils.Helper;

public class TimerHelper implements Helper {
    private long ms = getCurrentMS();

    private long getCurrentMS() {
        return System.currentTimeMillis();
    }

    public boolean hasReached(double milliseconds) {
        return ((getCurrentMS() - this.ms) > milliseconds);
    }

    public void reset() {
        this.ms = getCurrentMS();
    }

    public long getTime() {
        return getCurrentMS() - this.ms;
    }
}

