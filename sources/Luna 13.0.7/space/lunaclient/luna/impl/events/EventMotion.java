package space.lunaclient.luna.impl.events;

import space.lunaclient.luna.api.event.Event;
import space.lunaclient.luna.api.event.Event.Type;

public class EventMotion
  extends Event
{
  private float yaw;
  private float pitch;
  private boolean ground;
  private double y;
  
  public EventMotion(Event.Type type, float yaw, float pitch, boolean ground, double y)
  {
    super(type);
    this.yaw = yaw;
    this.pitch = pitch;
    this.ground = ground;
    this.y = y;
  }
  
  public float getYaw()
  {
    return this.yaw;
  }
  
  public void setYaw(float yaw)
  {
    this.yaw = yaw;
  }
  
  public float getPitch()
  {
    return this.pitch;
  }
  
  public void setPitch(float pitch)
  {
    this.pitch = pitch;
  }
  
  public boolean isGround()
  {
    return this.ground;
  }
  
  public void setGround(boolean ground)
  {
    this.ground = ground;
  }
  
  public double getY()
  {
    return this.y;
  }
  
  public void setY(double y)
  {
    this.y = y;
  }
}
