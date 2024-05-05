package space.lunaclient.luna.util;

public final class Box
{
  public final double minX;
  public final double minY;
  public final double minZ;
  public final double maxX;
  public final double maxY;
  public final double maxZ;
  
  public Box(double x, double y, double z, double x1, double y1, double z1)
  {
    this.minX = x;
    this.minY = y;
    this.minZ = z;
    this.maxX = x1;
    this.maxY = y1;
    this.maxZ = z1;
  }
}
