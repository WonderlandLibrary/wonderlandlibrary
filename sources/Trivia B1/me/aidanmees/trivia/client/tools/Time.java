package me.aidanmees.trivia.client.tools;

public class Time
{
  long sys_ms;
  
  public Time()
  {
    update();
  }
  
  void update()
  {
    this.sys_ms = System.currentTimeMillis();
  }
  
  public void begin()
  {
    update();
  }
  
  public boolean over(long ms)
  {
    return System.currentTimeMillis() - this.sys_ms > ms;
  }
}
