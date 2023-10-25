package nl.matsv.viabackwards.api.entities.storage;

public abstract class EntityPositionStorage implements EntityStorage {
  private double x;
  
  private double y;
  
  private double z;
  
  public double getX() {
    return this.x;
  }
  
  public double getY() {
    return this.y;
  }
  
  public double getZ() {
    return this.z;
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


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\api\entities\storage\EntityPositionStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */