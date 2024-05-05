package space.lunaclient.luna.util;

public class Timer
{
  private long lastMS;
  
  public Timer()
  {
    this.lastMS = getCurrentMS();
  }
  
  private long getCurrentMS()
  {
    return System.nanoTime() / 1000000L;
  }
  
  public boolean hasReached(long milliseconds)
  {
    return getCurrentMS() - this.lastMS >= milliseconds;
  }
  
  public void reset()
  {
    this.lastMS = getCurrentMS();
  }
}
