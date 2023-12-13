package cn.hanabi.events;

import com.darkmagician6.eventapi.events.Event;

public class SommtheEvent implements Event {
   public static float YAW;
   public static float PITCH;
   public static float PREVYAW;
   public static float PREVPITCH;
   public static boolean SNEAKING;
   private final float yaw;
   private final float pitch;
   private final double x;
   private final double y;
   private final double z;
   private final boolean onground;
   private boolean sneaking;

   public SommtheEvent(double x, double y, double z, float yaw, float pitch, boolean ground) {
      this.yaw = yaw;
      this.pitch = pitch;
      this.y = y;
      this.x = x;
      this.z = z;
      this.onground = ground;
      PREVYAW = YAW;
      PREVPITCH = PITCH;
      YAW = this.yaw;
      PITCH = this.pitch;
   }

   public float getYaw() {
      return this.yaw;
   }

   public float getPitch() {
      return this.pitch;
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   public double getZ() {
      return this.z;
   }

   public boolean isSneaking() {
      return this.sneaking;
   }

   public boolean isOnground() {
      return this.onground;
   }
}
