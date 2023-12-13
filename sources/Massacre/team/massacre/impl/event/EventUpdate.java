package team.massacre.impl.event;

import team.massacre.api.event.Cancellable;
import team.massacre.api.event.Event;

public class EventUpdate extends Cancellable implements Event {
   private double posX;
   private double lastPosX;
   private double posY;
   private double lastPosY;
   private double posZ;
   private double lastPosZ;
   private float yaw;
   private float pitch;
   private boolean onGround;
   private float prevYaw;
   private float prevPitch;
   private boolean rotating;
   private EventUpdate.Type type;

   public EventUpdate(double posX, double posY, double posZ, float yaw, float pitch, float prevYaw, float prevPitch, boolean onGround, EventUpdate.Type type) {
      this.posX = posX;
      this.posY = posY;
      this.posZ = posZ;
      this.yaw = yaw;
      this.pitch = pitch;
      this.onGround = onGround;
      this.type = type;
      this.prevPitch = prevPitch;
      this.prevYaw = prevYaw;
   }

   public boolean isPre() {
      return this.type == EventUpdate.Type.PRE;
   }

   public boolean isPost() {
      return this.type == EventUpdate.Type.POST;
   }

   public boolean isRotating() {
      return this.rotating;
   }

   public double getPosX() {
      return this.posX;
   }

   public void setPosX(double posX) {
      this.posX = posX;
   }

   public double getLastPosX() {
      return this.lastPosX;
   }

   public void setLastPosX(double lastPosX) {
      this.lastPosX = lastPosX;
   }

   public double getPosY() {
      return this.posY;
   }

   public void setPosY(double posY) {
      this.posY = posY;
   }

   public double getLastPosY() {
      return this.lastPosY;
   }

   public void setLastPosY(double lastPosY) {
      this.lastPosY = lastPosY;
   }

   public double getPosZ() {
      return this.posZ;
   }

   public void setPosZ(double posZ) {
      this.posZ = posZ;
   }

   public double getLastPosZ() {
      return this.lastPosZ;
   }

   public void setLastPosZ(double lastPosZ) {
      this.lastPosZ = lastPosZ;
   }

   public float getYaw() {
      return this.yaw;
   }

   public void setYaw(float yaw) {
      this.yaw = yaw;
      this.rotating = true;
   }

   public float getPitch() {
      return this.pitch;
   }

   public void setPitch(float pitch) {
      this.pitch = pitch;
      this.rotating = true;
   }

   public boolean isOnGround() {
      return this.onGround;
   }

   public float getPrevPitch() {
      return this.prevPitch;
   }

   public float getPrevYaw() {
      return this.prevYaw;
   }

   public void setOnGround(boolean onGround) {
      this.onGround = onGround;
   }

   public EventUpdate.Type getType() {
      return this.type;
   }

   public void setType(EventUpdate.Type type) {
      this.type = type;
   }

   public static enum Type {
      PRE,
      POST;
   }
}
