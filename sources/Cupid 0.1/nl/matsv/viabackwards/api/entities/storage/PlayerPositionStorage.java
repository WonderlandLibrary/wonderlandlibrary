package nl.matsv.viabackwards.api.entities.storage;

import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.type.Type;

public abstract class PlayerPositionStorage extends StoredObject {
  private double x;
  
  private double y;
  
  private double z;
  
  protected PlayerPositionStorage(UserConnection user) {
    super(user);
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
  
  public void setX(double x) {
    this.x = x;
  }
  
  public void setY(double y) {
    this.y = y;
  }
  
  public void setZ(double z) {
    this.z = z;
  }
  
  public void setCoordinates(PacketWrapper wrapper, boolean relative) throws Exception {
    setCoordinates(((Double)wrapper.get(Type.DOUBLE, 0)).doubleValue(), ((Double)wrapper.get(Type.DOUBLE, 1)).doubleValue(), ((Double)wrapper.get(Type.DOUBLE, 2)).doubleValue(), relative);
  }
  
  public void setCoordinates(double x, double y, double z, boolean relative) {
    if (relative) {
      this.x += x;
      this.y += y;
      this.z += z;
    } else {
      this.x = x;
      this.y = y;
      this.z = z;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\api\entities\storage\PlayerPositionStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */