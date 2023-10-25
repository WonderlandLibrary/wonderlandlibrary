package net.minecraft.entity.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityGiantZombie extends EntityMob {
  public EntityGiantZombie(World worldIn) {
    super(worldIn);
    setSize(this.width * 6.0F, this.height * 6.0F);
  }
  
  public float getEyeHeight() {
    return 10.440001F;
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
    getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(50.0D);
  }
  
  public float getBlockPathWeight(BlockPos pos) {
    return this.worldObj.getLightBrightness(pos) - 0.5F;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\monster\EntityGiantZombie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */