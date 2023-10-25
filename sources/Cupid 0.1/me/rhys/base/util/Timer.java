package me.rhys.base.util;

public class Timer {
  private long lastTime;
  
  public Timer() {
    reset();
  }
  
  public void reset() {
    this.lastTime = System.currentTimeMillis();
  }
  
  public boolean hasReached(double miliseconds) {
    return ((System.currentTimeMillis() - this.lastTime) >= miliseconds);
  }
  
  public long getLastTime() {
    return this.lastTime;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\util\Timer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */