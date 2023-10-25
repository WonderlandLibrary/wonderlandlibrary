package net.minecraft.entity.monster;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public abstract class EntityMob extends EntityCreature implements IMob {
  public EntityMob(World worldIn) {
    super(worldIn);
    this.experienceValue = 5;
  }
  
  public void onLivingUpdate() {
    updateArmSwingProgress();
    float f = getBrightness(1.0F);
    if (f > 0.5F)
      this.entityAge += 2; 
    super.onLivingUpdate();
  }
  
  public void onUpdate() {
    super.onUpdate();
    if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL)
      setDead(); 
  }
  
  protected String getSwimSound() {
    return "game.hostile.swim";
  }
  
  protected String getSplashSound() {
    return "game.hostile.swim.splash";
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    if (super.attackEntityFrom(source, amount)) {
      Entity entity = source.getEntity();
      return (this.riddenByEntity != entity && this.ridingEntity != entity) ? true : true;
    } 
    return false;
  }
  
  protected String getHurtSound() {
    return "game.hostile.hurt";
  }
  
  protected String getDeathSound() {
    return "game.hostile.die";
  }
  
  protected String getFallSoundString(int damageValue) {
    return (damageValue > 4) ? "game.hostile.hurt.fall.big" : "game.hostile.hurt.fall.small";
  }
  
  public boolean attackEntityAsMob(Entity entityIn) {
    float f = (float)getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
    int i = 0;
    if (entityIn instanceof EntityLivingBase) {
      f += EnchantmentHelper.func_152377_a(getHeldItem(), ((EntityLivingBase)entityIn).getCreatureAttribute());
      i += EnchantmentHelper.getKnockbackModifier((EntityLivingBase)this);
    } 
    boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), f);
    if (flag) {
      if (i > 0) {
        entityIn.addVelocity((-MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F) * i * 0.5F), 0.1D, (MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F) * i * 0.5F));
        this.motionX *= 0.6D;
        this.motionZ *= 0.6D;
      } 
      int j = EnchantmentHelper.getFireAspectModifier((EntityLivingBase)this);
      if (j > 0)
        entityIn.setFire(j * 4); 
      applyEnchantments((EntityLivingBase)this, entityIn);
    } 
    return flag;
  }
  
  public float getBlockPathWeight(BlockPos pos) {
    return 0.5F - this.worldObj.getLightBrightness(pos);
  }
  
  protected boolean isValidLightLevel() {
    BlockPos blockpos = new BlockPos(this.posX, (getEntityBoundingBox()).minY, this.posZ);
    if (this.worldObj.getLightFor(EnumSkyBlock.SKY, blockpos) > this.rand.nextInt(32))
      return false; 
    int i = this.worldObj.getLightFromNeighbors(blockpos);
    if (this.worldObj.isThundering()) {
      int j = this.worldObj.getSkylightSubtracted();
      this.worldObj.setSkylightSubtracted(10);
      i = this.worldObj.getLightFromNeighbors(blockpos);
      this.worldObj.setSkylightSubtracted(j);
    } 
    return (i <= this.rand.nextInt(8));
  }
  
  public boolean getCanSpawnHere() {
    return (this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL && isValidLightLevel() && super.getCanSpawnHere());
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
  }
  
  protected boolean canDropLoot() {
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\monster\EntityMob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */