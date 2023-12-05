package rip.helium.utils;


public class TimerUtil {

    private long time = System.nanoTime() / 1000000L;

    public boolean reach(long time) {
        return (time() >= time);
    }

    public void reset() {
        this.time = System.nanoTime() / 1000000L;
    }

    public boolean sleep(long time) {
        if (time() >= time) {
            reset();
            return true;
        }
        return false;
    }

    public long time() {
        return System.nanoTime() / 1000000L - this.time;
    }
}


