package team.massacre.utils;

public class TimerUtil {
   private long lastTime;

   public TimerUtil() {
      this.reset();
   }

   public long getCurrentTime() {
      return System.nanoTime() / 1000000L;
   }

   public boolean hasHit(float cPS) {
      return (float)this.getDifference() >= cPS;
   }

   public boolean hasHit(double cPS) {
      return (double)this.getDifference() >= cPS;
   }

   public long getLastTime() {
      return this.lastTime;
   }

   public long getDifference() {
      return this.getCurrentTime() - this.lastTime;
   }

   public void reset() {
      this.lastTime = this.getCurrentTime();
   }

   public boolean hasReached(long milliseconds) {
      return this.getDifference() >= milliseconds;
   }

   public boolean hasCompleted(long l) {
      return false;
   }
}
