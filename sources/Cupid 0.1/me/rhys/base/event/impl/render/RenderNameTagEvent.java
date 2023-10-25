package me.rhys.base.event.impl.render;

import me.rhys.base.event.Event;
import net.minecraft.entity.EntityLivingBase;

public class RenderNameTagEvent extends Event {
  private EntityLivingBase entity;
  
  private String name;
  
  private double x;
  
  private double y;
  
  private double z;
  
  private int maxDistance;
  
  public RenderNameTagEvent(EntityLivingBase entity, String name, double x, double y, double z, int maxDistance) {
    this.entity = entity;
    this.name = name;
    this.x = x;
    this.y = y;
    this.z = z;
    this.maxDistance = maxDistance;
  }
  
  public EntityLivingBase getEntity() {
    return this.entity;
  }
  
  public String getName() {
    return this.name;
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
  
  public int getMaxDistance() {
    return this.maxDistance;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\event\impl\render\RenderNameTagEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */