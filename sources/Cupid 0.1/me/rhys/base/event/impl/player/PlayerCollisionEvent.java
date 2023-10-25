package me.rhys.base.event.impl.player;

import me.rhys.base.event.Event;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class PlayerCollisionEvent extends Event {
  private final Entity entity;
  
  private AxisAlignedBB box;
  
  private final BlockPos pos;
  
  public PlayerCollisionEvent(Entity entity, AxisAlignedBB box, BlockPos pos) {
    this.entity = entity;
    this.box = box;
    this.pos = pos;
  }
  
  public Entity getEntity() {
    return this.entity;
  }
  
  public void setBox(AxisAlignedBB box) {
    this.box = box;
  }
  
  public AxisAlignedBB getBox() {
    return this.box;
  }
  
  public BlockPos getPos() {
    return this.pos;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\event\impl\player\PlayerCollisionEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */