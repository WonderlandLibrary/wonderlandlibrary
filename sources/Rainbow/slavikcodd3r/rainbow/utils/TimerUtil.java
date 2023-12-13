package slavikcodd3r.rainbow.utils;

public final class TimerUtil
{
    private long time;
    
    public TimerUtil() {
        this.time = System.nanoTime() / 1000000L;
    }
    
    public boolean reach(final long time) {
        return this.time() >= time;
    }
    
    public void reset() {
        this.time = System.nanoTime() / 1000000L;
    }
    
    public boolean sleep(final long time) {
        if (this.time() >= time) {
            this.reset();
            return true;
        }
        return false;
    }
    
    public long time() {
        return System.nanoTime() / 1000000L - this.time;
    }
}
