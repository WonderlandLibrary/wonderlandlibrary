package slavikcodd3r.rainbow.utils;

public class TimeHelper
{
    private long lastMS;
    
    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }
    
    public long getLastMS() {
        return this.lastMS;
    }
    
    public boolean hasReached(final float f) {
        return this.getCurrentMS() - this.lastMS >= f;
    }
    
    public boolean hasReached(final double f) {
        return (float)(this.getCurrentMS() - this.lastMS) >= f;
    }
    
    public boolean hasReached(final long f) {
        return this.getCurrentMS() - this.lastMS >= (float)f;
    }
    
    public void reset() {
        this.lastMS = this.getCurrentMS();
    }
    
    public void setLastMS(final long currentMS) {
        this.lastMS = currentMS;
    }
}
