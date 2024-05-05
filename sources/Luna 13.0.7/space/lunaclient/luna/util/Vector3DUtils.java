package space.lunaclient.luna.util;

import java.util.ArrayList;
import net.minecraft.util.Vec3;

public class Vector3DUtils
{
  private Vec3 vec1;
  private Vec3 vec2;
  private Vec3 vec3;
  private double offsetX;
  private double offsetY;
  private double offsetZ;
  
  public Vector3DUtils(Vec3 start, Vec3 end)
  {
    this.vec1 = start;
    this.vec3 = end;
    init(start, end);
  }
  
  public Vector3DUtils(Vec3 vector, float yaw, float pitch, double length)
  {
    this.vec1 = vector;
    double calculatedX = Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
    double calculatedY = Math.sin(Math.toRadians(pitch));
    double calculatedZ = Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
    double x = calculatedX * length + vector.xCoord;
    double y = calculatedY * length + vector.yCoord;
    double z = calculatedZ * length + vector.zCoord;
    this.vec3 = new Vec3(x, y, z);
    init(this.vec1, this.vec3);
  }
  
  private void init(Vec3 start, Vec3 end)
  {
    this.vec2 = calculate(start, end);
    this.offsetX = (end.xCoord - start.xCoord);
    this.offsetY = (end.yCoord - start.yCoord);
    this.offsetZ = (end.zCoord - start.zCoord);
  }
  
  private Vec3 calculate(Vec3 v1, Vec3 v2)
  {
    double x = (v1.xCoord + v1.yCoord) / 2.0D;
    double y = (v1.yCoord + v2.yCoord) / 2.0D;
    double z = (v1.zCoord + v2.zCoord) / 2.0D;
    
    return new Vec3(x, y, z);
  }
  
  public double getOffsetX()
  {
    return this.offsetX;
  }
  
  public double getOffsetY()
  {
    return this.offsetY;
  }
  
  public double getOffsetZ()
  {
    return this.offsetZ;
  }
  
  public Vec3 getStartVector()
  {
    return this.vec1;
  }
  
  public double getLength(Vec3 vector1, Vec3 vector2)
  {
    return vector1.distanceTo(vector2);
  }
  
  public ArrayList<Vec3> getIntervalTimer(double interval)
  {
    int intervaL = (int)(getStartVector().distanceTo(getEndVector()) / interval) + 1;
    return calculateInterval(intervaL);
  }
  
  private ArrayList<Vec3> calculateInterval(int interval)
  {
    interval--;
    ArrayList<Vec3> points = new ArrayList();
    double xOff = getOffsetX() / interval;
    double yOff = getOffsetY() / interval;
    double zOff = getOffsetZ() / interval;
    for (int i = 0; i <= interval; i++)
    {
      double xOffset = xOff * i;
      double yOffset = yOff * i;
      double zOffset = zOff * i;
      points.add(new Vec3(getStartVector().xCoord + xOffset, getStartVector().yCoord + yOffset, getStartVector().zCoord + zOffset));
    }
    return points;
  }
  
  public Vec3 getEndVector()
  {
    return this.vec3;
  }
}
