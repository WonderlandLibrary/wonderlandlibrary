package cn.hanabi.utils;

public class AnimationUtil {
   public static float moveUD(float current, float end, float smoothSpeed, float minSpeed) {
      float movement = (end - current) * smoothSpeed;
      if (movement > 0.0F) {
         movement = Math.max(minSpeed, movement);
         movement = Math.min(end - current, movement);
      } else if (movement < 0.0F) {
         movement = Math.min(-minSpeed, movement);
         movement = Math.max(end - current, movement);
      }

      return current + movement;
   }

   public static float lstransition(float now, float desired, double speed) {
      double dif = (double)Math.abs(desired - now);
      float a = (float)Math.abs((double)(desired - (desired - Math.abs(desired - now))) / (100.0 - speed * 10.0));
      float x = now;
      if (dif != 0.0 && dif < (double)a) {
         a = (float)dif;
      }

      if (dif > 0.0) {
         if (now < desired) {
            x = now + a * RenderUtil.delta;
         } else if (now > desired) {
            x = now - a * RenderUtil.delta;
         }
      } else {
         x = desired;
      }

      if ((double)Math.abs(desired - x) < 0.05 && x != desired) {
         x = desired;
      }

      return x;
   }

   public static float calculateCompensation(float target, float current, long delta, int speed) {
      float diff = current - target;
      if (delta < 1L) {
         delta = 1L;
      }

      double xD;
      if (diff > (float)speed) {
         xD = (double)((long)speed * delta / 16L) < 0.25 ? 0.5 : (double)((long)speed * delta / 16L);
         current = (float)((double)current - xD);
         if (current < target) {
            current = target;
         }
      } else if (diff < (float)(-speed)) {
         xD = (double)((long)speed * delta / 16L) < 0.25 ? 0.5 : (double)((long)speed * delta / 16L);
         current = (float)((double)current + xD);
         if (current > target) {
            current = target;
         }
      } else {
         current = target;
      }

      return current;
   }

   public static float calculateCompensation(float target, float current, long delta, double speed) {
      float diff = current - target;
      if (delta < 1L) {
         delta = 1L;
      }

      if (delta > 1000L) {
         delta = 16L;
      }

      double xD;
      if ((double)diff > speed) {
         xD = Math.max(speed * (double)delta / 16.0, 0.5);
         current = (float)((double)current - xD);
         if (current < target) {
            current = target;
         }
      } else if ((double)diff < -speed) {
         xD = Math.max(speed * (double)delta / 16.0, 0.5);
         current = (float)((double)current + xD);
         if (current > target) {
            current = target;
         }
      } else {
         current = target;
      }

      return current;
   }
}
