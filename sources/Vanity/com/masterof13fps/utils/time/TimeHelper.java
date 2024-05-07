package com.masterof13fps.utils.time;

public class TimeHelper
{
  
  public boolean isDelayComplete(long delay)
  {
    if (System.currentTimeMillis() - this.lastMS >= delay) {
      return true;
    }
    return false;
  }
  
  public void setLastMS()
  {
    this.lastMS = System.currentTimeMillis();
  }
  
  public int convertToMS(int perSecond)
  {
    return 1000 / perSecond;
  }
  
  private long lastMS;

  public long getCurrentMS()
  {
      return System.nanoTime() / 1000000L;
  }

  public long getLastMS()
  {
      return this.lastMS;
  }

  public boolean hasReached(long milliseconds)
  {
      return getCurrentMS() - this.lastMS >= milliseconds;
  }

  public void reset()
  {
      this.lastMS = getCurrentMS();
  }

  public void setLastMS(long currentMS)
  {
      this.lastMS = currentMS;
  }

  public long getTimeSinceReset()
  {
      return getCurrentMS() - this.lastMS;
  }


  
}
