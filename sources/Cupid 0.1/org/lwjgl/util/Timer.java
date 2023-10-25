package org.lwjgl.util;

import org.lwjgl.Sys;

public class Timer {
  private static long resolution = Sys.getTimerResolution();
  
  private static final int QUERY_INTERVAL = 50;
  
  private static int queryCount;
  
  private static long currentTime;
  
  private long startTime;
  
  private long lastTime;
  
  private boolean paused;
  
  static {
    tick();
  }
  
  public Timer() {
    reset();
    resume();
  }
  
  public float getTime() {
    if (!this.paused)
      this.lastTime = currentTime - this.startTime; 
    return (float)(this.lastTime / resolution);
  }
  
  public boolean isPaused() {
    return this.paused;
  }
  
  public void pause() {
    this.paused = true;
  }
  
  public void reset() {
    set(0.0F);
  }
  
  public void resume() {
    this.paused = false;
    this.startTime = currentTime - this.lastTime;
  }
  
  public void set(float newTime) {
    long newTimeInTicks = (long)(newTime * resolution);
    this.startTime = currentTime - newTimeInTicks;
    this.lastTime = newTimeInTicks;
  }
  
  public static void tick() {
    currentTime = Sys.getTime();
    queryCount++;
    if (queryCount > 50) {
      queryCount = 0;
      resolution = Sys.getTimerResolution();
    } 
  }
  
  public String toString() {
    return "Timer[Time=" + getTime() + ", Paused=" + this.paused + "]";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjg\\util\Timer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */