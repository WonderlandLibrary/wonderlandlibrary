package me.rhys.base.event.impl.network;

import me.rhys.base.event.Event;
import me.rhys.base.util.entity.Location;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;

public class EventBlockBoundingBox extends Event {
  private AxisAlignedBB box;
  
  private Block block;
  
  private Location location;
  
  private Entity collidingEntity;
  
  public EventBlockBoundingBox(AxisAlignedBB box, Block block, Location location, Entity collidingEntity) {
    this.box = box;
    this.block = block;
    this.location = location;
    this.collidingEntity = collidingEntity;
  }
  
  public AxisAlignedBB getBox() {
    return this.box;
  }
  
  public void setBox(AxisAlignedBB box) {
    this.box = box;
  }
  
  public Block getBlock() {
    return this.block;
  }
  
  public void setBlock(Block block) {
    this.block = block;
  }
  
  public Location getLocation() {
    return this.location;
  }
  
  public void setLocation(Location location) {
    this.location = location;
  }
  
  public Entity getCollidingEntity() {
    return this.collidingEntity;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\event\impl\network\EventBlockBoundingBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */