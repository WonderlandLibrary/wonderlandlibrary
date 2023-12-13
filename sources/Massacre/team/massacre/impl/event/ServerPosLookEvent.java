package team.massacre.impl.event;

import team.massacre.api.event.Event;

public final class ServerPosLookEvent implements Event {
   private double posX;
   private double posY;
   private double posZ;
   private float yaw;
   private float pitch;
   private float sendYaw;
   private float sendPitch;

   public ServerPosLookEvent(double posX, double posY, double posZ, float yaw, float pitch, float sendYaw, float sendPitch) {
      this.posX = posX;
      this.posY = posY;
      this.posZ = posZ;
      this.yaw = yaw;
      this.pitch = pitch;
      this.sendYaw = sendYaw;
      this.sendPitch = sendPitch;
   }

   public float getSendYaw() {
      return this.sendYaw;
   }

   public void setSendYaw(float sendYaw) {
      this.sendYaw = sendYaw;
   }

   public float getSendPitch() {
      return this.sendPitch;
   }

   public void setSendPitch(float sendPitch) {
      this.sendPitch = sendPitch;
   }

   public double getPosX() {
      return this.posX;
   }

   public void setPosX(double posX) {
      this.posX = posX;
   }

   public double getPosY() {
      return this.posY;
   }

   public void setPosY(double posY) {
      this.posY = posY;
   }

   public double getPosZ() {
      return this.posZ;
   }

   public void setPosZ(double posZ) {
      this.posZ = posZ;
   }

   public float getYaw() {
      return this.yaw;
   }

   public void setYaw(float yaw) {
      this.yaw = yaw;
   }

   public float getPitch() {
      return this.pitch;
   }

   public void setPitch(float pitch) {
      this.pitch = pitch;
   }
}
