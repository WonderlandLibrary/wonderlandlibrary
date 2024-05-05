package space.lunaclient.luna.api.waypoint;

import com.google.gson.annotations.SerializedName;

public class WayPoint
{
  @SerializedName("name")
  private String name;
  @SerializedName("server")
  private String serverIP;
  @SerializedName("x")
  private double x;
  @SerializedName("y")
  private double y;
  @SerializedName("z")
  private double z;
  
  public WayPoint(String name, String serverIP, double x, double y, double z)
  {
    this.name = name;
    this.serverIP = serverIP;
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getServerIP()
  {
    return this.serverIP;
  }
  
  public double getX()
  {
    return this.x;
  }
  
  public double getY()
  {
    return this.y;
  }
  
  public double getZ()
  {
    return this.z;
  }
}
