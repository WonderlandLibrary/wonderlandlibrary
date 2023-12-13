package my.NewSnake.utils;

public class Timer {
   private static long lastMS;
   private long prevMS = 0L;
   private long time = -1L;
   private static long lastMs;

   public void setLastMs(int var1) {
      lastMs = System.currentTimeMillis() + (long)var1;
   }

   public boolean delay(float var1) {
      return (float)(this.getTime() - this.prevMS) >= var1;
   }

   public long getTime() {
      return System.nanoTime() / 1000000L;
   }

   public void setDifference(long var1) {
      this.prevMS = this.getTime() - var1;
   }

   public void reset2() {
      lastMs = System.currentTimeMillis();
   }

   public long getDifference() {
      return this.getTime() - this.prevMS;
   }

   public static boolean hasReached(double var0) {
      return (double)(getCurrentMS() - lastMS) >= var0;
   }

   public static long getCurrentMS() {
      return System.nanoTime() / 1000000L;
   }

   public static boolean isDelayComplete(long var0) {
      return System.currentTimeMillis() - lastMs > var0;
   }

   public void reset() {
      this.prevMS = this.getTime();
   }

   public long getLastMs() {
      return lastMs;
   }

   public boolean hasTimedElapsed(long var1, boolean var3) {
      if (System.currentTimeMillis() - lastMS > var1) {
         if (var3) {
            this.reset();
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean hasTimePassed(long var1) {
      return System.currentTimeMillis() >= this.time + var1;
   }
}
