package net.minecraft.entity.projectile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityWitherSkull extends EntityFireball {
  public EntityWitherSkull(World worldIn) {
    super(worldIn);
    setSize(0.3125F, 0.3125F);
  }
  
  public EntityWitherSkull(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
    super(worldIn, shooter, accelX, accelY, accelZ);
    setSize(0.3125F, 0.3125F);
  }
  
  protected float getMotionFactor() {
    return isInvulnerable() ? 0.73F : super.getMotionFactor();
  }
  
  public EntityWitherSkull(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
    super(worldIn, x, y, z, accelX, accelY, accelZ);
    setSize(0.3125F, 0.3125F);
  }
  
  public boolean isBurning() {
    return false;
  }
  
  public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
    float f = super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn);
    Block block = blockStateIn.getBlock();
    if (isInvulnerable() && EntityWither.func_181033_a(block))
      f = Math.min(0.8F, f); 
    return f;
  }
  
  protected void onImpact(MovingObjectPosition movingObject) {
    if (!this.worldObj.isRemote) {
      if (movingObject.entityHit != null) {
        if (this.shootingEntity != null) {
          if (movingObject.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 8.0F))
            if (!movingObject.entityHit.isEntityAlive()) {
              this.shootingEntity.heal(5.0F);
            } else {
              applyEnchantments(this.shootingEntity, movingObject.entityHit);
            }  
        } else {
          movingObject.entityHit.attackEntityFrom(DamageSource.magic, 5.0F);
        } 
        if (movingObject.entityHit instanceof EntityLivingBase) {
          int i = 0;
          if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL) {
            i = 10;
          } else if (this.worldObj.getDifficulty() == EnumDifficulty.HARD) {
            i = 40;
          } 
          if (i > 0)
            ((EntityLivingBase)movingObject.entityHit).addPotionEffect(new PotionEffect(Potion.wither.id, 20 * i, 1)); 
        } 
      } 
      this.worldObj.newExplosion(this, this.posX, this.posY, this.posZ, 1.0F, false, this.worldObj.getGameRules().getBoolean("mobGriefing"));
      setDead();
    } 
  }
  
  public boolean canBeCollidedWith() {
    return false;
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    return false;
  }
  
  protected void entityInit() {
    this.dataWatcher.addObject(10, Byte.valueOf((byte)0));
  }
  
  public boolean isInvulnerable() {
    return (this.dataWatcher.getWatchableObjectByte(10) == 1);
  }
  
  public void setInvulnerable(boolean invulnerable) {
    this.dataWatcher.updateObject(10, Byte.valueOf((byte)(invulnerable ? 1 : 0)));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\projectile\EntityWitherSkull.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */