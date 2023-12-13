package me.aidanmees.trivia.client.tools;

import java.util.concurrent.TimeUnit;

public class TimerM
{
  private long previousTime;
  private long currentMS = 0L;
  protected long lastMS = -1L;
  private long prevMS;
  
  public long getPreviousTime()
  {
    return this.previousTime;
  }
  
  public TimerM()
  {
    this.prevMS = 0L;
  }
  
  public boolean delay(float milliSec)
  {
    return (float)(getTime() - this.prevMS) >= milliSec;
  }
  
  public final void update()
  {
    this.currentMS = System.currentTimeMillis();
  }
  
  public boolean isDelayComplete(long delay)
  {
    return System.currentTimeMillis() - this.lastMS >= delay;
  }
  
  public boolean isDelayCompleteParadox(float delay)
  {
    return (float)(System.currentTimeMillis() - this.lastMS) >= delay;
  }
  
  public long systemTime()
  {
    return System.currentTimeMillis();
  }
  
  public float currentTime()
  {
    return (float)(systemTime() - this.previousTime);
  }
  
  public boolean isTime(float time)
  {
    return currentTime() >= time * 1000.0F;
  }
  
  public void setLastMS(long lastMS)
  {
    this.lastMS = lastMS;
  }
  
  public void setLastMS()
  {
    this.lastMS = System.currentTimeMillis();
  }
  
  public void reset()
  {
    this.prevMS = getTime();
  }
  
  public long getTime()
  {
    return System.nanoTime() / 1000000L;
  }
  
  public final boolean hasPassed(long MS)
  {
    return this.currentMS >= this.lastMS + MS;
  }
  
  public long getDifference()
  {
    return getTime() - this.prevMS;
  }
  
  public boolean hasReach(long a, TimeUnit milliseconds)
  {
    return hasReach(a, TimeUnit.MILLISECONDS);
  }
  
  public void setDifference(long difference)
  {
    this.prevMS = (getTime() - difference);
  }
  
  public long getLastMs()
  {
    return System.currentTimeMillis() - this.lastMS;
  }
}
