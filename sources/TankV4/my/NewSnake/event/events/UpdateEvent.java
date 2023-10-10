package my.NewSnake.event.events;

import my.NewSnake.event.Event;

public class UpdateEvent extends Event {
   private boolean onground;
   public float pitch;
   private double z;
   private boolean alwaysSend;
   private Event.State state;
   private double y;
   private double x;
   public float yaw;

   public Event.State getState() {
      return this.state;
   }

   public double getX() {
      return this.x;
   }

   public float getYaw() {
      return this.yaw;
   }

   public void setZ(double var1) {
      this.z = var1;
   }

   public void setAlwaysSend(boolean var1) {
      this.alwaysSend = var1;
   }

   public void setGround(boolean var1) {
      this.onground = var1;
   }

   public UpdateEvent(double var1, double var3, double var5, float var7, float var8, boolean var9) {
      this.state = Event.State.PRE;
      this.x = var1;
      this.y = var3;
      this.z = var5;
      this.yaw = var7;
      this.pitch = var8;
      this.onground = var9;
   }

   public float getPitch() {
      return this.pitch;
   }

   public UpdateEvent() {
      this.state = Event.State.POST;
   }

   public boolean isOnground() {
      return this.onground;
   }

   public double getY() {
      return this.y;
   }

   public double getZ() {
      return this.z;
   }

   public boolean shouldAlwaysSend() {
      return this.alwaysSend;
   }

   public void setX(double var1) {
      this.x = var1;
   }

   public void setY(double var1) {
      this.y = var1;
   }

   public void setYaw(float var1) {
      this.yaw = var1;
   }

   public void setPitch(float var1) {
      this.pitch = var1;
   }
}
