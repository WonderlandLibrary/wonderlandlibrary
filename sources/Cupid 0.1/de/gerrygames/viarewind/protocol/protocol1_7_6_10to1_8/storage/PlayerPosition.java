package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage;

import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;

public class PlayerPosition extends StoredObject {
  private double posX;
  
  private double posY;
  
  private double posZ;
  
  private float yaw;
  
  private float pitch;
  
  private boolean onGround;
  
  private boolean positionPacketReceived;
  
  private double receivedPosY;
  
  public PlayerPosition(UserConnection user) {
    super(user);
  }
  
  public void setPos(double x, double y, double z) {
    this.posX = x;
    this.posY = y;
    this.posZ = z;
  }
  
  public boolean isPositionPacketReceived() {
    return this.positionPacketReceived;
  }
  
  public void setPositionPacketReceived(boolean positionPacketReceived) {
    this.positionPacketReceived = positionPacketReceived;
  }
  
  public double getReceivedPosY() {
    return this.receivedPosY;
  }
  
  public void setReceivedPosY(double receivedPosY) {
    this.receivedPosY = receivedPosY;
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
  
  public boolean isOnGround() {
    return this.onGround;
  }
  
  public void setOnGround(boolean onGround) {
    this.onGround = onGround;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\storage\PlayerPosition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */