package net.minecraft.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityBlaze extends EntityMob {
  private float heightOffset = 0.5F;
  
  private int heightOffsetUpdateTime;
  
  public EntityBlaze(World worldIn) {
    super(worldIn);
    this.isImmuneToFire = true;
    this.experienceValue = 10;
    this.tasks.addTask(4, new AIFireballAttack(this));
    this.tasks.addTask(5, (EntityAIBase)new EntityAIMoveTowardsRestriction(this, 1.0D));
    this.tasks.addTask(7, (EntityAIBase)new EntityAIWander(this, 1.0D));
    this.tasks.addTask(8, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
    this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget(this, true, new Class[0]));
    this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0D);
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
    getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(48.0D);
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataWatcher.addObject(16, new Byte((byte)0));
  }
  
  protected String getLivingSound() {
    return "mob.blaze.breathe";
  }
  
  protected String getHurtSound() {
    return "mob.blaze.hit";
  }
  
  protected String getDeathSound() {
    return "mob.blaze.death";
  }
  
  public int getBrightnessForRender(float partialTicks) {
    return 15728880;
  }
  
  public float getBrightness(float partialTicks) {
    return 1.0F;
  }
  
  public void onLivingUpdate() {
    if (!this.onGround && this.motionY < 0.0D)
      this.motionY *= 0.6D; 
    if (this.worldObj.isRemote) {
      if (this.rand.nextInt(24) == 0 && !isSilent())
        this.worldObj.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "fire.fire", 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false); 
      for (int i = 0; i < 2; i++)
        this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D, new int[0]); 
    } 
    super.onLivingUpdate();
  }
  
  protected void updateAITasks() {
    if (isWet())
      attackEntityFrom(DamageSource.drown, 1.0F); 
    this.heightOffsetUpdateTime--;
    if (this.heightOffsetUpdateTime <= 0) {
      this.heightOffsetUpdateTime = 100;
      this.heightOffset = 0.5F + (float)this.rand.nextGaussian() * 3.0F;
    } 
    EntityLivingBase entitylivingbase = getAttackTarget();
    if (entitylivingbase != null && entitylivingbase.posY + entitylivingbase.getEyeHeight() > this.posY + getEyeHeight() + this.heightOffset) {
      this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
      this.isAirBorne = true;
    } 
    super.updateAITasks();
  }
  
  public void fall(float distance, float damageMultiplier) {}
  
  protected Item getDropItem() {
    return Items.blaze_rod;
  }
  
  public boolean isBurning() {
    return func_70845_n();
  }
  
  protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
    if (p_70628_1_) {
      int i = this.rand.nextInt(2 + p_70628_2_);
      for (int j = 0; j < i; j++)
        dropItem(Items.blaze_rod, 1); 
    } 
  }
  
  public boolean func_70845_n() {
    return ((this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0);
  }
  
  public void setOnFire(boolean onFire) {
    byte b0 = this.dataWatcher.getWatchableObjectByte(16);
    if (onFire) {
      b0 = (byte)(b0 | 0x1);
    } else {
      b0 = (byte)(b0 & 0xFFFFFFFE);
    } 
    this.dataWatcher.updateObject(16, Byte.valueOf(b0));
  }
  
  protected boolean isValidLightLevel() {
    return true;
  }
  
  static class AIFireballAttack extends EntityAIBase {
    private EntityBlaze blaze;
    
    private int field_179467_b;
    
    private int field_179468_c;
    
    public AIFireballAttack(EntityBlaze p_i45846_1_) {
      this.blaze = p_i45846_1_;
      setMutexBits(3);
    }
    
    public boolean shouldExecute() {
      EntityLivingBase entitylivingbase = this.blaze.getAttackTarget();
      return (entitylivingbase != null && entitylivingbase.isEntityAlive());
    }
    
    public void startExecuting() {
      this.field_179467_b = 0;
    }
    
    public void resetTask() {
      this.blaze.setOnFire(false);
    }
    
    public void updateTask() {
      this.field_179468_c--;
      EntityLivingBase entitylivingbase = this.blaze.getAttackTarget();
      double d0 = this.blaze.getDistanceSqToEntity((Entity)entitylivingbase);
      if (d0 < 4.0D) {
        if (this.field_179468_c <= 0) {
          this.field_179468_c = 20;
          this.blaze.attackEntityAsMob((Entity)entitylivingbase);
        } 
        this.blaze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
      } else if (d0 < 256.0D) {
        double d1 = entitylivingbase.posX - this.blaze.posX;
        double d2 = (entitylivingbase.getEntityBoundingBox()).minY + (entitylivingbase.height / 2.0F) - this.blaze.posY + (this.blaze.height / 2.0F);
        double d3 = entitylivingbase.posZ - this.blaze.posZ;
        if (this.field_179468_c <= 0) {
          this.field_179467_b++;
          if (this.field_179467_b == 1) {
            this.field_179468_c = 60;
            this.blaze.setOnFire(true);
          } else if (this.field_179467_b <= 4) {
            this.field_179468_c = 6;
          } else {
            this.field_179468_c = 100;
            this.field_179467_b = 0;
            this.blaze.setOnFire(false);
          } 
          if (this.field_179467_b > 1) {
            float f = MathHelper.sqrt_float(MathHelper.sqrt_double(d0)) * 0.5F;
            this.blaze.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1009, new BlockPos((int)this.blaze.posX, (int)this.blaze.posY, (int)this.blaze.posZ), 0);
            for (int i = 0; i < 1; i++) {
              EntitySmallFireball entitysmallfireball = new EntitySmallFireball(this.blaze.worldObj, (EntityLivingBase)this.blaze, d1 + this.blaze.getRNG().nextGaussian() * f, d2, d3 + this.blaze.getRNG().nextGaussian() * f);
              entitysmallfireball.posY = this.blaze.posY + (this.blaze.height / 2.0F) + 0.5D;
              this.blaze.worldObj.spawnEntityInWorld((Entity)entitysmallfireball);
            } 
          } 
        } 
        this.blaze.getLookHelper().setLookPositionWithEntity((Entity)entitylivingbase, 10.0F, 10.0F);
      } else {
        this.blaze.getNavigator().clearPathEntity();
        this.blaze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
      } 
      super.updateTask();
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\monster\EntityBlaze.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */