package de.tired.util.math;

import lombok.Getter;

public class TimerUtil {

    @Getter
    private long curTime;

    public TimerUtil() {
        doReset();
    }

    public boolean reachedTime(long time) {
        return System.nanoTime() / 1000000L - curTime >= time;
    }

    public boolean reachedTime(long delay, boolean reset) {
        if(reachedTime(delay)) {
            if(reset)
                doReset();
            return true;
        }
        return false;
    }

    public void doReset() {
        this.curTime = System.nanoTime() / 1000000L;
    }

}
