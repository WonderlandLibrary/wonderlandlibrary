package cn.hanabi.utils;

public class Translate {
   private float x;
   private float y;
   private long lastMS;

   public Translate(float x, float y) {
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
      this.x = AnimationUtil.calculateCompensation(targetX, this.x, delta, deltaX);
      this.y = AnimationUtil.calculateCompensation(targetY, this.y, delta, deltaY);
   }

   public void translate(float targetX, float targetY) {
      this.x = AnimationUtil.lstransition(this.x, targetX, 1.0);
      this.y = AnimationUtil.lstransition(this.y, targetY, 1.0);
   }

   public void translate(float targetX, float targetY, double speed) {
      this.x = AnimationUtil.lstransition(this.x, targetX, speed);
      this.y = AnimationUtil.lstransition(this.y, targetY, speed);
   }

   public float getX() {
      return this.x;
   }

   public void setX(float x) {
      this.x = x;
   }

   public float getY() {
      return this.y;
   }

   public void setY(float y) {
      this.y = y;
   }
}
