package net.minecraft.entity.monster;

import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityGhast extends EntityFlying implements IMob {
  private int explosionStrength = 1;
  
  public EntityGhast(World worldIn) {
    super(worldIn);
    setSize(4.0F, 4.0F);
    this.isImmuneToFire = true;
    this.experienceValue = 5;
    this.moveHelper = new GhastMoveHelper(this);
    this.tasks.addTask(5, new AIRandomFly(this));
    this.tasks.addTask(7, new AILookAround(this));
    this.tasks.addTask(7, new AIFireballAttack(this));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAIFindEntityNearestPlayer((EntityLiving)this));
  }
  
  public boolean isAttacking() {
    return (this.dataWatcher.getWatchableObjectByte(16) != 0);
  }
  
  public void setAttacking(boolean p_175454_1_) {
    this.dataWatcher.updateObject(16, Byte.valueOf((byte)(p_175454_1_ ? 1 : 0)));
  }
  
  public int getFireballStrength() {
    return this.explosionStrength;
  }
  
  public void onUpdate() {
    super.onUpdate();
    if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL)
      setDead(); 
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    if ("fireball".equals(source.getDamageType()) && source.getEntity() instanceof EntityPlayer) {
      super.attackEntityFrom(source, 1000.0F);
      ((EntityPlayer)source.getEntity()).triggerAchievement((StatBase)AchievementList.ghast);
      return true;
    } 
    return super.attackEntityFrom(source, amount);
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
    getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(100.0D);
  }
  
  protected String getLivingSound() {
    return "mob.ghast.moan";
  }
  
  protected String getHurtSound() {
    return "mob.ghast.scream";
  }
  
  protected String getDeathSound() {
    return "mob.ghast.death";
  }
  
  protected Item getDropItem() {
    return Items.gunpowder;
  }
  
  protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
    int i = this.rand.nextInt(2) + this.rand.nextInt(1 + p_70628_2_);
    for (int j = 0; j < i; j++)
      dropItem(Items.ghast_tear, 1); 
    i = this.rand.nextInt(3) + this.rand.nextInt(1 + p_70628_2_);
    for (int k = 0; k < i; k++)
      dropItem(Items.gunpowder, 1); 
  }
  
  protected float getSoundVolume() {
    return 10.0F;
  }
  
  public boolean getCanSpawnHere() {
    return (this.rand.nextInt(20) == 0 && super.getCanSpawnHere() && this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL);
  }
  
  public int getMaxSpawnedInChunk() {
    return 1;
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound) {
    super.writeEntityToNBT(tagCompound);
    tagCompound.setInteger("ExplosionPower", this.explosionStrength);
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund) {
    super.readEntityFromNBT(tagCompund);
    if (tagCompund.hasKey("ExplosionPower", 99))
      this.explosionStrength = tagCompund.getInteger("ExplosionPower"); 
  }
  
  public float getEyeHeight() {
    return 2.6F;
  }
  
  static class AIFireballAttack extends EntityAIBase {
    private EntityGhast parentEntity;
    
    public int attackTimer;
    
    public AIFireballAttack(EntityGhast p_i45837_1_) {
      this.parentEntity = p_i45837_1_;
    }
    
    public boolean shouldExecute() {
      return (this.parentEntity.getAttackTarget() != null);
    }
    
    public void startExecuting() {
      this.attackTimer = 0;
    }
    
    public void resetTask() {
      this.parentEntity.setAttacking(false);
    }
    
    public void updateTask() {
      EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
      double d0 = 64.0D;
      if (entitylivingbase.getDistanceSqToEntity((Entity)this.parentEntity) < d0 * d0 && this.parentEntity.canEntityBeSeen((Entity)entitylivingbase)) {
        World world = this.parentEntity.worldObj;
        this.attackTimer++;
        if (this.attackTimer == 10)
          world.playAuxSFXAtEntity((EntityPlayer)null, 1007, new BlockPos((Entity)this.parentEntity), 0); 
        if (this.attackTimer == 20) {
          double d1 = 4.0D;
          Vec3 vec3 = this.parentEntity.getLook(1.0F);
          double d2 = entitylivingbase.posX - this.parentEntity.posX + vec3.xCoord * d1;
          double d3 = (entitylivingbase.getEntityBoundingBox()).minY + (entitylivingbase.height / 2.0F) - 0.5D + this.parentEntity.posY + (this.parentEntity.height / 2.0F);
          double d4 = entitylivingbase.posZ - this.parentEntity.posZ + vec3.zCoord * d1;
          world.playAuxSFXAtEntity((EntityPlayer)null, 1008, new BlockPos((Entity)this.parentEntity), 0);
          EntityLargeFireball entitylargefireball = new EntityLargeFireball(world, (EntityLivingBase)this.parentEntity, d2, d3, d4);
          entitylargefireball.explosionPower = this.parentEntity.getFireballStrength();
          entitylargefireball.posX = this.parentEntity.posX + vec3.xCoord * d1;
          entitylargefireball.posY = this.parentEntity.posY + (this.parentEntity.height / 2.0F) + 0.5D;
          entitylargefireball.posZ = this.parentEntity.posZ + vec3.zCoord * d1;
          world.spawnEntityInWorld((Entity)entitylargefireball);
          this.attackTimer = -40;
        } 
      } else if (this.attackTimer > 0) {
        this.attackTimer--;
      } 
      this.parentEntity.setAttacking((this.attackTimer > 10));
    }
  }
  
  static class AILookAround extends EntityAIBase {
    private EntityGhast parentEntity;
    
    public AILookAround(EntityGhast p_i45839_1_) {
      this.parentEntity = p_i45839_1_;
      setMutexBits(2);
    }
    
    public boolean shouldExecute() {
      return true;
    }
    
    public void updateTask() {
      if (this.parentEntity.getAttackTarget() == null) {
        this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw = -((float)MathHelper.func_181159_b(this.parentEntity.motionX, this.parentEntity.motionZ)) * 180.0F / 3.1415927F;
      } else {
        EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
        double d0 = 64.0D;
        if (entitylivingbase.getDistanceSqToEntity((Entity)this.parentEntity) < d0 * d0) {
          double d1 = entitylivingbase.posX - this.parentEntity.posX;
          double d2 = entitylivingbase.posZ - this.parentEntity.posZ;
          this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw = -((float)MathHelper.func_181159_b(d1, d2)) * 180.0F / 3.1415927F;
        } 
      } 
    }
  }
  
  static class AIRandomFly extends EntityAIBase {
    private EntityGhast parentEntity;
    
    public AIRandomFly(EntityGhast p_i45836_1_) {
      this.parentEntity = p_i45836_1_;
      setMutexBits(1);
    }
    
    public boolean shouldExecute() {
      EntityMoveHelper entitymovehelper = this.parentEntity.getMoveHelper();
      if (!entitymovehelper.isUpdating())
        return true; 
      double d0 = entitymovehelper.getX() - this.parentEntity.posX;
      double d1 = entitymovehelper.getY() - this.parentEntity.posY;
      double d2 = entitymovehelper.getZ() - this.parentEntity.posZ;
      double d3 = d0 * d0 + d1 * d1 + d2 * d2;
      return (d3 < 1.0D || d3 > 3600.0D);
    }
    
    public boolean continueExecuting() {
      return false;
    }
    
    public void startExecuting() {
      Random random = this.parentEntity.getRNG();
      double d0 = this.parentEntity.posX + ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
      double d1 = this.parentEntity.posY + ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
      double d2 = this.parentEntity.posZ + ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
      this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
    }
  }
  
  static class GhastMoveHelper extends EntityMoveHelper {
    private EntityGhast parentEntity;
    
    private int courseChangeCooldown;
    
    public GhastMoveHelper(EntityGhast p_i45838_1_) {
      super((EntityLiving)p_i45838_1_);
      this.parentEntity = p_i45838_1_;
    }
    
    public void onUpdateMoveHelper() {
      if (this.update) {
        double d0 = this.posX - this.parentEntity.posX;
        double d1 = this.posY - this.parentEntity.posY;
        double d2 = this.posZ - this.parentEntity.posZ;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;
        if (this.courseChangeCooldown-- <= 0) {
          this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
          d3 = MathHelper.sqrt_double(d3);
          if (isNotColliding(this.posX, this.posY, this.posZ, d3)) {
            this.parentEntity.motionX += d0 / d3 * 0.1D;
            this.parentEntity.motionY += d1 / d3 * 0.1D;
            this.parentEntity.motionZ += d2 / d3 * 0.1D;
          } else {
            this.update = false;
          } 
        } 
      } 
    }
    
    private boolean isNotColliding(double p_179926_1_, double p_179926_3_, double p_179926_5_, double p_179926_7_) {
      double d0 = (p_179926_1_ - this.parentEntity.posX) / p_179926_7_;
      double d1 = (p_179926_3_ - this.parentEntity.posY) / p_179926_7_;
      double d2 = (p_179926_5_ - this.parentEntity.posZ) / p_179926_7_;
      AxisAlignedBB axisalignedbb = this.parentEntity.getEntityBoundingBox();
      for (int i = 1; i < p_179926_7_; i++) {
        axisalignedbb = axisalignedbb.offset(d0, d1, d2);
        if (!this.parentEntity.worldObj.getCollidingBoundingBoxes((Entity)this.parentEntity, axisalignedbb).isEmpty())
          return false; 
      } 
      return true;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\monster\EntityGhast.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */