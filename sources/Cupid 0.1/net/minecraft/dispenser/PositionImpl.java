package net.minecraft.dispenser;

public class PositionImpl implements IPosition {
  protected final double x;
  
  protected final double y;
  
  protected final double z;
  
  public PositionImpl(double xCoord, double yCoord, double zCoord) {
    this.x = xCoord;
    this.y = yCoord;
    this.z = zCoord;
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
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\dispenser\PositionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */