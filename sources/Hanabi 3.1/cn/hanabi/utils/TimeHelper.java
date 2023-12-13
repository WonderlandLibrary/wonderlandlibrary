package cn.hanabi.utils;

public class TimeHelper {
   public long lastMs = 0L;

   public boolean isDelayComplete(long delay) {
      return System.currentTimeMillis() - this.lastMs >= delay;
   }

   public boolean delay(float nextDelay, boolean reset) {
      if ((float)(System.currentTimeMillis() - this.lastMs) >= nextDelay) {
         if (reset) {
            this.reset();
         }

         return true;
      } else {
         return false;
      }
   }

   public long getCurrentMS() {
      return System.nanoTime() / 1000000L;
   }

   public void reset() {
      this.lastMs = System.currentTimeMillis();
   }

   public long getLastMs() {
      return this.lastMs;
   }

   public void setLastMs(int i) {
      this.lastMs = System.currentTimeMillis() + (long)i;
   }

   public boolean hasReached(long milliseconds) {
      return this.getCurrentMS() - this.lastMs >= milliseconds;
   }

   public boolean isDelayComplete(float delay) {
      return (float)(System.currentTimeMillis() - this.lastMs) > delay;
   }

   public boolean isDelayComplete(Double delay) {
      return (double)(System.currentTimeMillis() - this.lastMs) > delay;
   }
}
