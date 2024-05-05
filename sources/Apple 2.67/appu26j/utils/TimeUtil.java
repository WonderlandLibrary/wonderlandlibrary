package appu26j.utils;

public class TimeUtil
{
    private long previousTime;
    
    public TimeUtil()
    {
        this.previousTime = this.getCurrentTime();
    }
    
    public long getCurrentTime()
    {
        return System.nanoTime() / 1000000;
    }
    
    public long getDifference()
    {
        return this.getCurrentTime() - this.previousTime;
    }
    
    public boolean hasTimePassed(final long milliseconds)
    {
        return this.getDifference() >= milliseconds;
    }
    
    public void reset()
    {
        this.previousTime = this.getCurrentTime();
    }
}
