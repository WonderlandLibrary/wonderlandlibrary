package net.minecraft.entity.boss;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityWither extends EntityMob implements IBossDisplayData, IRangedAttackMob {
  private float[] field_82220_d = new float[2];
  
  private float[] field_82221_e = new float[2];
  
  private float[] field_82217_f = new float[2];
  
  private float[] field_82218_g = new float[2];
  
  private int[] field_82223_h = new int[2];
  
  private int[] field_82224_i = new int[2];
  
  private int blockBreakCounter;
  
  private static final Predicate<Entity> attackEntitySelector = new Predicate<Entity>() {
      public boolean apply(Entity p_apply_1_) {
        return (p_apply_1_ instanceof EntityLivingBase && ((EntityLivingBase)p_apply_1_).getCreatureAttribute() != EnumCreatureAttribute.UNDEAD);
      }
    };
  
  public EntityWither(World worldIn) {
    super(worldIn);
    setHealth(getMaxHealth());
    setSize(0.9F, 3.5F);
    this.isImmuneToFire = true;
    ((PathNavigateGround)getNavigator()).setCanSwim(true);
    this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(2, (EntityAIBase)new EntityAIArrowAttack(this, 1.0D, 40, 20.0F));
    this.tasks.addTask(5, (EntityAIBase)new EntityAIWander((EntityCreature)this, 1.0D));
    this.tasks.addTask(6, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
    this.tasks.addTask(7, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget((EntityCreature)this, false, new Class[0]));
    this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget((EntityCreature)this, EntityLiving.class, 0, false, false, attackEntitySelector));
    this.experienceValue = 50;
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataWatcher.addObject(17, new Integer(0));
    this.dataWatcher.addObject(18, new Integer(0));
    this.dataWatcher.addObject(19, new Integer(0));
    this.dataWatcher.addObject(20, new Integer(0));
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound) {
    super.writeEntityToNBT(tagCompound);
    tagCompound.setInteger("Invul", getInvulTime());
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund) {
    super.readEntityFromNBT(tagCompund);
    setInvulTime(tagCompund.getInteger("Invul"));
  }
  
  protected String getLivingSound() {
    return "mob.wither.idle";
  }
  
  protected String getHurtSound() {
    return "mob.wither.hurt";
  }
  
  protected String getDeathSound() {
    return "mob.wither.death";
  }
  
  public void onLivingUpdate() {
    this.motionY *= 0.6000000238418579D;
    if (!this.worldObj.isRemote && getWatchedTargetId(0) > 0) {
      Entity entity = this.worldObj.getEntityByID(getWatchedTargetId(0));
      if (entity != null) {
        if (this.posY < entity.posY || (!isArmored() && this.posY < entity.posY + 5.0D)) {
          if (this.motionY < 0.0D)
            this.motionY = 0.0D; 
          this.motionY += (0.5D - this.motionY) * 0.6000000238418579D;
        } 
        double d0 = entity.posX - this.posX;
        double d1 = entity.posZ - this.posZ;
        double d3 = d0 * d0 + d1 * d1;
        if (d3 > 9.0D) {
          double d5 = MathHelper.sqrt_double(d3);
          this.motionX += (d0 / d5 * 0.5D - this.motionX) * 0.6000000238418579D;
          this.motionZ += (d1 / d5 * 0.5D - this.motionZ) * 0.6000000238418579D;
        } 
      } 
    } 
    if (this.motionX * this.motionX + this.motionZ * this.motionZ > 0.05000000074505806D)
      this.rotationYaw = (float)MathHelper.func_181159_b(this.motionZ, this.motionX) * 57.295776F - 90.0F; 
    super.onLivingUpdate();
    for (int i = 0; i < 2; i++) {
      this.field_82218_g[i] = this.field_82221_e[i];
      this.field_82217_f[i] = this.field_82220_d[i];
    } 
    for (int j = 0; j < 2; j++) {
      int k = getWatchedTargetId(j + 1);
      Entity entity1 = null;
      if (k > 0)
        entity1 = this.worldObj.getEntityByID(k); 
      if (entity1 != null) {
        double d11 = func_82214_u(j + 1);
        double d12 = func_82208_v(j + 1);
        double d13 = func_82213_w(j + 1);
        double d6 = entity1.posX - d11;
        double d7 = entity1.posY + entity1.getEyeHeight() - d12;
        double d8 = entity1.posZ - d13;
        double d9 = MathHelper.sqrt_double(d6 * d6 + d8 * d8);
        float f = (float)(MathHelper.func_181159_b(d8, d6) * 180.0D / Math.PI) - 90.0F;
        float f1 = (float)-(MathHelper.func_181159_b(d7, d9) * 180.0D / Math.PI);
        this.field_82220_d[j] = func_82204_b(this.field_82220_d[j], f1, 40.0F);
        this.field_82221_e[j] = func_82204_b(this.field_82221_e[j], f, 10.0F);
      } else {
        this.field_82221_e[j] = func_82204_b(this.field_82221_e[j], this.renderYawOffset, 10.0F);
      } 
    } 
    boolean flag = isArmored();
    for (int l = 0; l < 3; l++) {
      double d10 = func_82214_u(l);
      double d2 = func_82208_v(l);
      double d4 = func_82213_w(l);
      this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d10 + this.rand.nextGaussian() * 0.30000001192092896D, d2 + this.rand.nextGaussian() * 0.30000001192092896D, d4 + this.rand.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D, new int[0]);
      if (flag && this.worldObj.rand.nextInt(4) == 0)
        this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, d10 + this.rand.nextGaussian() * 0.30000001192092896D, d2 + this.rand.nextGaussian() * 0.30000001192092896D, d4 + this.rand.nextGaussian() * 0.30000001192092896D, 0.699999988079071D, 0.699999988079071D, 0.5D, new int[0]); 
    } 
    if (getInvulTime() > 0)
      for (int i1 = 0; i1 < 3; i1++)
        this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + this.rand.nextGaussian() * 1.0D, this.posY + (this.rand.nextFloat() * 3.3F), this.posZ + this.rand.nextGaussian() * 1.0D, 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D, new int[0]);  
  }
  
  protected void updateAITasks() {
    if (getInvulTime() > 0) {
      int j1 = getInvulTime() - 1;
      if (j1 <= 0) {
        this.worldObj.newExplosion((Entity)this, this.posX, this.posY + getEyeHeight(), this.posZ, 7.0F, false, this.worldObj.getGameRules().getBoolean("mobGriefing"));
        this.worldObj.playBroadcastSound(1013, new BlockPos((Entity)this), 0);
      } 
      setInvulTime(j1);
      if (this.ticksExisted % 10 == 0)
        heal(10.0F); 
    } else {
      super.updateAITasks();
      for (int i = 1; i < 3; i++) {
        if (this.ticksExisted >= this.field_82223_h[i - 1]) {
          this.field_82223_h[i - 1] = this.ticksExisted + 10 + this.rand.nextInt(10);
          if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL || this.worldObj.getDifficulty() == EnumDifficulty.HARD) {
            int j3 = i - 1;
            int k3 = this.field_82224_i[i - 1];
            this.field_82224_i[j3] = this.field_82224_i[i - 1] + 1;
            if (k3 > 15) {
              float f = 10.0F;
              float f1 = 5.0F;
              double d0 = MathHelper.getRandomDoubleInRange(this.rand, this.posX - f, this.posX + f);
              double d1 = MathHelper.getRandomDoubleInRange(this.rand, this.posY - f1, this.posY + f1);
              double d2 = MathHelper.getRandomDoubleInRange(this.rand, this.posZ - f, this.posZ + f);
              launchWitherSkullToCoords(i + 1, d0, d1, d2, true);
              this.field_82224_i[i - 1] = 0;
            } 
          } 
          int k1 = getWatchedTargetId(i);
          if (k1 > 0) {
            Entity entity = this.worldObj.getEntityByID(k1);
            if (entity != null && entity.isEntityAlive() && getDistanceSqToEntity(entity) <= 900.0D && canEntityBeSeen(entity)) {
              if (entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.disableDamage) {
                updateWatchedTargetId(i, 0);
              } else {
                launchWitherSkullToEntity(i + 1, (EntityLivingBase)entity);
                this.field_82223_h[i - 1] = this.ticksExisted + 40 + this.rand.nextInt(20);
                this.field_82224_i[i - 1] = 0;
              } 
            } else {
              updateWatchedTargetId(i, 0);
            } 
          } else {
            List<EntityLivingBase> list = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().expand(20.0D, 8.0D, 20.0D), Predicates.and(attackEntitySelector, EntitySelectors.NOT_SPECTATING));
            for (int j2 = 0; j2 < 10 && !list.isEmpty(); j2++) {
              EntityLivingBase entitylivingbase = list.get(this.rand.nextInt(list.size()));
              if (entitylivingbase != this && entitylivingbase.isEntityAlive() && canEntityBeSeen((Entity)entitylivingbase)) {
                if (entitylivingbase instanceof EntityPlayer) {
                  if (!((EntityPlayer)entitylivingbase).capabilities.disableDamage)
                    updateWatchedTargetId(i, entitylivingbase.getEntityId()); 
                  break;
                } 
                updateWatchedTargetId(i, entitylivingbase.getEntityId());
                break;
              } 
              list.remove(entitylivingbase);
            } 
          } 
        } 
      } 
      if (getAttackTarget() != null) {
        updateWatchedTargetId(0, getAttackTarget().getEntityId());
      } else {
        updateWatchedTargetId(0, 0);
      } 
      if (this.blockBreakCounter > 0) {
        this.blockBreakCounter--;
        if (this.blockBreakCounter == 0 && this.worldObj.getGameRules().getBoolean("mobGriefing")) {
          int i1 = MathHelper.floor_double(this.posY);
          int l1 = MathHelper.floor_double(this.posX);
          int i2 = MathHelper.floor_double(this.posZ);
          boolean flag = false;
          for (int k2 = -1; k2 <= 1; k2++) {
            for (int l2 = -1; l2 <= 1; l2++) {
              for (int j = 0; j <= 3; j++) {
                int i3 = l1 + k2;
                int k = i1 + j;
                int l = i2 + l2;
                BlockPos blockpos = new BlockPos(i3, k, l);
                Block block = this.worldObj.getBlockState(blockpos).getBlock();
                if (block.getMaterial() != Material.air && func_181033_a(block))
                  flag = (this.worldObj.destroyBlock(blockpos, true) || flag); 
              } 
            } 
          } 
          if (flag)
            this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1012, new BlockPos((Entity)this), 0); 
        } 
      } 
      if (this.ticksExisted % 20 == 0)
        heal(1.0F); 
    } 
  }
  
  public static boolean func_181033_a(Block p_181033_0_) {
    return (p_181033_0_ != Blocks.bedrock && p_181033_0_ != Blocks.end_portal && p_181033_0_ != Blocks.end_portal_frame && p_181033_0_ != Blocks.command_block && p_181033_0_ != Blocks.barrier);
  }
  
  public void func_82206_m() {
    setInvulTime(220);
    setHealth(getMaxHealth() / 3.0F);
  }
  
  public void setInWeb() {}
  
  public int getTotalArmorValue() {
    return 4;
  }
  
  private double func_82214_u(int p_82214_1_) {
    if (p_82214_1_ <= 0)
      return this.posX; 
    float f = (this.renderYawOffset + (180 * (p_82214_1_ - 1))) / 180.0F * 3.1415927F;
    float f1 = MathHelper.cos(f);
    return this.posX + f1 * 1.3D;
  }
  
  private double func_82208_v(int p_82208_1_) {
    return (p_82208_1_ <= 0) ? (this.posY + 3.0D) : (this.posY + 2.2D);
  }
  
  private double func_82213_w(int p_82213_1_) {
    if (p_82213_1_ <= 0)
      return this.posZ; 
    float f = (this.renderYawOffset + (180 * (p_82213_1_ - 1))) / 180.0F * 3.1415927F;
    float f1 = MathHelper.sin(f);
    return this.posZ + f1 * 1.3D;
  }
  
  private float func_82204_b(float p_82204_1_, float p_82204_2_, float p_82204_3_) {
    float f = MathHelper.wrapAngleTo180_float(p_82204_2_ - p_82204_1_);
    if (f > p_82204_3_)
      f = p_82204_3_; 
    if (f < -p_82204_3_)
      f = -p_82204_3_; 
    return p_82204_1_ + f;
  }
  
  private void launchWitherSkullToEntity(int p_82216_1_, EntityLivingBase p_82216_2_) {
    launchWitherSkullToCoords(p_82216_1_, p_82216_2_.posX, p_82216_2_.posY + p_82216_2_.getEyeHeight() * 0.5D, p_82216_2_.posZ, (p_82216_1_ == 0 && this.rand.nextFloat() < 0.001F));
  }
  
  private void launchWitherSkullToCoords(int p_82209_1_, double x, double y, double z, boolean invulnerable) {
    this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1014, new BlockPos((Entity)this), 0);
    double d0 = func_82214_u(p_82209_1_);
    double d1 = func_82208_v(p_82209_1_);
    double d2 = func_82213_w(p_82209_1_);
    double d3 = x - d0;
    double d4 = y - d1;
    double d5 = z - d2;
    EntityWitherSkull entitywitherskull = new EntityWitherSkull(this.worldObj, (EntityLivingBase)this, d3, d4, d5);
    if (invulnerable)
      entitywitherskull.setInvulnerable(true); 
    entitywitherskull.posY = d1;
    entitywitherskull.posX = d0;
    entitywitherskull.posZ = d2;
    this.worldObj.spawnEntityInWorld((Entity)entitywitherskull);
  }
  
  public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_) {
    launchWitherSkullToEntity(0, p_82196_1_);
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    if (source != DamageSource.drown && !(source.getEntity() instanceof EntityWither)) {
      if (getInvulTime() > 0 && source != DamageSource.outOfWorld)
        return false; 
      if (isArmored()) {
        Entity entity = source.getSourceOfDamage();
        if (entity instanceof net.minecraft.entity.projectile.EntityArrow)
          return false; 
      } 
      Entity entity1 = source.getEntity();
      if (entity1 != null && !(entity1 instanceof EntityPlayer) && entity1 instanceof EntityLivingBase && ((EntityLivingBase)entity1).getCreatureAttribute() == getCreatureAttribute())
        return false; 
      if (this.blockBreakCounter <= 0)
        this.blockBreakCounter = 20; 
      for (int i = 0; i < this.field_82224_i.length; i++)
        this.field_82224_i[i] = this.field_82224_i[i] + 3; 
      return super.attackEntityFrom(source, amount);
    } 
    return false;
  }
  
  protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
    EntityItem entityitem = dropItem(Items.nether_star, 1);
    if (entityitem != null)
      entityitem.setNoDespawn(); 
    if (!this.worldObj.isRemote)
      for (EntityPlayer entityplayer : this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, getEntityBoundingBox().expand(50.0D, 100.0D, 50.0D)))
        entityplayer.triggerAchievement((StatBase)AchievementList.killWither);  
  }
  
  protected void despawnEntity() {
    this.entityAge = 0;
  }
  
  public int getBrightnessForRender(float partialTicks) {
    return 15728880;
  }
  
  public void fall(float distance, float damageMultiplier) {}
  
  public void addPotionEffect(PotionEffect potioneffectIn) {}
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(300.0D);
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.6000000238418579D);
    getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0D);
  }
  
  public float func_82207_a(int p_82207_1_) {
    return this.field_82221_e[p_82207_1_];
  }
  
  public float func_82210_r(int p_82210_1_) {
    return this.field_82220_d[p_82210_1_];
  }
  
  public int getInvulTime() {
    return this.dataWatcher.getWatchableObjectInt(20);
  }
  
  public void setInvulTime(int p_82215_1_) {
    this.dataWatcher.updateObject(20, Integer.valueOf(p_82215_1_));
  }
  
  public int getWatchedTargetId(int p_82203_1_) {
    return this.dataWatcher.getWatchableObjectInt(17 + p_82203_1_);
  }
  
  public void updateWatchedTargetId(int targetOffset, int newId) {
    this.dataWatcher.updateObject(17 + targetOffset, Integer.valueOf(newId));
  }
  
  public boolean isArmored() {
    return (getHealth() <= getMaxHealth() / 2.0F);
  }
  
  public EnumCreatureAttribute getCreatureAttribute() {
    return EnumCreatureAttribute.UNDEAD;
  }
  
  public void mountEntity(Entity entityIn) {
    this.ridingEntity = null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\boss\EntityWither.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */