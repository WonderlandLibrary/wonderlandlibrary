package de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage;

import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;

public class PlayerPosition extends StoredObject {
  private double posX;
  
  private double posY;
  
  private double posZ;
  
  private float yaw;
  
  private float pitch;
  
  private boolean onGround;
  
  public void setPosX(double posX) {
    this.posX = posX;
  }
  
  public void setPosY(double posY) {
    this.posY = posY;
  }
  
  public void setPosZ(double posZ) {
    this.posZ = posZ;
  }
  
  public void setYaw(float yaw) {
    this.yaw = yaw;
  }
  
  public void setPitch(float pitch) {
    this.pitch = pitch;
  }
  
  public void setOnGround(boolean onGround) {
    this.onGround = onGround;
  }
  
  public void setConfirmId(int confirmId) {
    this.confirmId = confirmId;
  }
  
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof PlayerPosition))
      return false; 
    PlayerPosition other = (PlayerPosition)o;
    return !other.canEqual(this) ? false : ((Double.compare(getPosX(), other.getPosX()) != 0) ? false : ((Double.compare(getPosY(), other.getPosY()) != 0) ? false : ((Double.compare(getPosZ(), other.getPosZ()) != 0) ? false : ((Float.compare(getYaw(), other.getYaw()) != 0) ? false : ((Float.compare(getPitch(), other.getPitch()) != 0) ? false : ((isOnGround() != other.isOnGround()) ? false : (!(getConfirmId() != other.getConfirmId()))))))));
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof PlayerPosition;
  }
  
  public int hashCode() {
    int PRIME = 59;
    result = 1;
    long $posX = Double.doubleToLongBits(getPosX());
    result = result * 59 + (int)($posX >>> 32L ^ $posX);
    long $posY = Double.doubleToLongBits(getPosY());
    result = result * 59 + (int)($posY >>> 32L ^ $posY);
    long $posZ = Double.doubleToLongBits(getPosZ());
    result = result * 59 + (int)($posZ >>> 32L ^ $posZ);
    result = result * 59 + Float.floatToIntBits(getYaw());
    result = result * 59 + Float.floatToIntBits(getPitch());
    result = result * 59 + (isOnGround() ? 79 : 97);
    return result * 59 + getConfirmId();
  }
  
  public String toString() {
    return "PlayerPosition(posX=" + getPosX() + ", posY=" + getPosY() + ", posZ=" + getPosZ() + ", yaw=" + getYaw() + ", pitch=" + getPitch() + ", onGround=" + isOnGround() + ", confirmId=" + getConfirmId() + ")";
  }
  
  public double getPosX() {
    return this.posX;
  }
  
  public double getPosY() {
    return this.posY;
  }
  
  public double getPosZ() {
    return this.posZ;
  }
  
  public float getYaw() {
    return this.yaw;
  }
  
  public float getPitch() {
    return this.pitch;
  }
  
  public boolean isOnGround() {
    return this.onGround;
  }
  
  private int confirmId = -1;
  
  public int getConfirmId() {
    return this.confirmId;
  }
  
  public PlayerPosition(UserConnection user) {
    super(user);
  }
  
  public void setPos(double x, double y, double z) {
    this.posX = x;
    this.posY = y;
    this.posZ = z;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_8to1_9\storage\PlayerPosition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */