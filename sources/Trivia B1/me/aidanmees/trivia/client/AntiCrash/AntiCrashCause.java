package me.aidanmees.trivia.client.AntiCrash;

public class AntiCrashCause
{
  public String name;
  public long time = System.currentTimeMillis();
  public String debugValue;
  
  public AntiCrashCause(String name)
  {
    this.name = name;
  }
  
  public AntiCrashCause(String name, String debugValue)
  {
    this.name = name;
    this.debugValue = debugValue;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public long getTime()
  {
    return this.time;
  }
  
  public long timeRemaining()
  {
    return System.currentTimeMillis() - this.time;
  }
  
  public boolean equals(Object obj)
  {
    if ((obj instanceof AntiCrashCause))
    {
      AntiCrashCause o = (AntiCrashCause)obj;
      return o.name.equals(this.name);
    }
    return false;
  }
}