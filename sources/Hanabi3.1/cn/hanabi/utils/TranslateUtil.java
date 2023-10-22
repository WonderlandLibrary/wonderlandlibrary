package cn.hanabi.utils;

public class TranslateUtil {
   private float x;
   private float y;
   private long lastMS;

   public TranslateUtil(float x, float y) {
      this.x = x;
      this.y = y;
      this.lastMS = System.currentTimeMillis();
   }

   public void interpolate(float targetX, float targetY, float smoothing) {
      long currentMS = System.currentTimeMillis();
      long delta = currentMS - this.lastMS;
      this.lastMS = currentMS;
      int deltaX = (int)(Math.abs(targetX - this.x) * smoothing);
      int deltaY = (int)(Math.abs(targetY - this.y) * smoothing);
      this.x = this.calculateCompensation(targetX, this.x, delta, deltaX);
      this.y = this.calculateCompensation(targetY, this.y, delta, deltaY);
   }

   public float getX() {
      return this.x;
   }

   public void setX(float x) {
      this.x = x;
   }

   public void setXY(float x, float y) {
      this.x = x;
      this.y = y;
   }

   public float getY() {
      return this.y;
   }

   public void setY(float y) {
      this.y = y;
   }

   public float calculateCompensation(float target, float current, long delta, int speed) {
      float diff = current - target;
      if (delta < 1L) {
         delta = 1L;
      }

      double dell;
      if (diff > (float)speed) {
         dell = (double)((long)speed * delta / 16L) < 0.25 ? 0.5 : (double)((long)speed * delta / 16L);
         current = (float)((double)current - dell);
         if (current < target) {
            current = target;
         }
      } else if (diff < (float)(-speed)) {
         dell = (double)((long)speed * delta / 16L) < 0.25 ? 0.5 : (double)((long)speed * delta / 16L);
         current = (float)((double)current + dell);
         if (current > target) {
            current = target;
         }
      } else {
         current = target;
      }

      return current;
   }
}
