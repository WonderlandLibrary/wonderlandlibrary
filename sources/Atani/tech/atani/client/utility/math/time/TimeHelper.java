package tech.atani.client.utility.math.time;

public class TimeHelper {
    private long ms;

    public boolean hasReached(long delay) {
        return System.currentTimeMillis() - ms >= delay;
    }

    public boolean hasReached(double delay) {
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

    public void setMs(long ms) {
        this.ms = ms;
    }
}