package me.rhys.base.event.impl.render;

import me.rhys.base.event.Event;
import net.minecraft.entity.EntityLivingBase;

public class RenderEntityEvent extends Event {
  private EntityLivingBase entity;
  
  private boolean isLayers;
  
  private double x;
  
  private double y;
  
  private double z;
  
  public boolean esp = false;
  
  public RenderEntityEvent(double x, double y, double z, EntityLivingBase entity, boolean isLayers) {
    this.entity = entity;
    this.isLayers = isLayers;
    this.x = x;
    this.y = y;
    this.z = z;
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
  
  public boolean isLayers() {
    return this.isLayers;
  }
  
  public EntityLivingBase getEntity() {
    return this.entity;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\event\impl\render\RenderEntityEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */