package org.dreamcore.client.helpers.misc;

import org.dreamcore.client.helpers.Helper;

public class TimerHelper implements Helper {

    private long ms = getCurrentMS();

    private long getCurrentMS() {
        return System.currentTimeMillis();
    }

    public boolean hasReached(float milliseconds) {
        return getCurrentMS() - ms > milliseconds;
    }

    public void reset() {
        ms = getCurrentMS();
    }

    public long getTime() {
        return getCurrentMS() - ms;
    }
}
