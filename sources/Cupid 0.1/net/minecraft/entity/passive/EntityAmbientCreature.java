package net.minecraft.entity.passive;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class EntityAmbientCreature extends EntityLiving implements IAnimals {
  public EntityAmbientCreature(World worldIn) {
    super(worldIn);
  }
  
  public boolean allowLeashing() {
    return false;
  }
  
  protected boolean interact(EntityPlayer player) {
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\passive\EntityAmbientCreature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */