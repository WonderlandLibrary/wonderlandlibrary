package rip.helium.utils;

/**
 * @author antja03
 */
public class Stopwatch {

    private long prevMS;

    public boolean hasPassed(double milliSec) {

        return (float) (this.getTime() - this.prevMS) >= milliSec;
    }

    public void reset() {
        this.prevMS = this.getTime();
    }

    public long getTime() {
        return System.nanoTime() / 1000000;
    }

    public boolean sleep(long time) {
        if (getTime() >= time) {
            reset();
            return true;
        }
        return false;
    }


}