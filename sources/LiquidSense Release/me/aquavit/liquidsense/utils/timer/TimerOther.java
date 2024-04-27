package me.aquavit.liquidsense.utils.timer;

public final class TimerOther {

    private long time = -1L;
    private long prevMS;
    public TimerOther(){
        this.prevMS = getTime();
    }
    public boolean hasTimePassed(final long MS) {
        return System.currentTimeMillis() >= time + MS;
    }

    public long hasTimeLeft(final long MS) {
        return (MS + time) - System.currentTimeMillis();
    }

    public void reset() {
        prevMS = getTime();
        time = System.currentTimeMillis();
    }

    public boolean delay(float milliSec) {
        return (float) (getTime() - this.prevMS) >= milliSec;
    }

    public long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public long getDifference() {
        return getTime() - this.prevMS;
    }

    public void setDifference(long difference) {
        this.prevMS = (getTime() - difference);
    }






}
