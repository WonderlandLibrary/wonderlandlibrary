package ru.smertnix.celestial.utils.math;

import ru.smertnix.celestial.utils.Helper;

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

