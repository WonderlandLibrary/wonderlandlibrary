package net.minecraft.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntitySmallFireball extends EntityFireball {
  public EntitySmallFireball(World worldIn) {
    super(worldIn);
    setSize(0.3125F, 0.3125F);
  }
  
  public EntitySmallFireball(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
    super(worldIn, shooter, accelX, accelY, accelZ);
    setSize(0.3125F, 0.3125F);
  }
  
  public EntitySmallFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
    super(worldIn, x, y, z, accelX, accelY, accelZ);
    setSize(0.3125F, 0.3125F);
  }
  
  protected void onImpact(MovingObjectPosition movingObject) {
    if (!this.worldObj.isRemote) {
      if (movingObject.entityHit != null) {
        boolean flag = movingObject.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, (Entity)this.shootingEntity), 5.0F);
        if (flag) {
          applyEnchantments(this.shootingEntity, movingObject.entityHit);
          if (!movingObject.entityHit.isImmuneToFire())
            movingObject.entityHit.setFire(5); 
        } 
      } else {
        boolean flag1 = true;
        if (this.shootingEntity != null && this.shootingEntity instanceof net.minecraft.entity.EntityLiving)
          flag1 = this.worldObj.getGameRules().getBoolean("mobGriefing"); 
        if (flag1) {
          BlockPos blockpos = movingObject.getBlockPos().offset(movingObject.sideHit);
          if (this.worldObj.isAirBlock(blockpos))
            this.worldObj.setBlockState(blockpos, Blocks.fire.getDefaultState()); 
        } 
      } 
      setDead();
    } 
  }
  
  public boolean canBeCollidedWith() {
    return false;
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\projectile\EntitySmallFireball.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */