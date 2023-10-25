package me.rhys.client.module.player.breaker;

public class TimeHelper {
  public long lastMs = 0L;
  
  public void reset() {
    this.lastMs = System.currentTimeMillis();
  }
  
  public boolean delay(long nextDelay) {
    return (System.currentTimeMillis() - this.lastMs >= nextDelay);
  }
  
  public boolean delay(float nextDelay, boolean reset) {
    if ((float)(System.currentTimeMillis() - this.lastMs) >= nextDelay) {
      if (reset)
        reset(); 
      return true;
    } 
    return false;
  }
  
  public boolean isDelayComplete(double valueState) {
    return ((System.currentTimeMillis() - this.lastMs) >= valueState);
  }
  
  public long getElapsedTime() {
    return System.currentTimeMillis() - this.lastMs;
  }
  
  public long getLastMs() {
    return this.lastMs;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\breaker\TimeHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */