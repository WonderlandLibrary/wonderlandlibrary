package xyz.cucumber.base.utils;

public class Timer
{
    public long time = System.currentTimeMillis();

    public boolean hasTimeElapsed(double delay, boolean reset)
    {
        if (System.currentTimeMillis() - time > delay)
        {
            if (reset)
            {
                reset();
            }

            return true;
        }
        else
        {
            return false;
        }
    }
    public long getTime()
    {
        return System.currentTimeMillis() - time;
    }
    public void reset()
    {
        this.time = System.currentTimeMillis();
    }
    public void setTime(long time) {
    	this.time = time;
    }
}
