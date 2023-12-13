package koks.api.utils;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

public class TimeHelper {
    private long ms;

    public boolean hasReached(long delay) {
        return System.currentTimeMillis() - ms >= delay;
    }

    public boolean hasReached(long delay, boolean reset) {
        if(hasReached(delay)) {
            if(reset)
                reset();
            return true;
        }
        return false;
    }


    public void reset() {
        ms = System.currentTimeMillis();
    }

    public long getMs() {
        return System.currentTimeMillis() - ms;
    }
}