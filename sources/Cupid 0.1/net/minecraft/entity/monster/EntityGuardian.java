package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityGuardian extends EntityMob {
  private float field_175482_b;
  
  private float field_175484_c;
  
  private float field_175483_bk;
  
  private float field_175485_bl;
  
  private float field_175486_bm;
  
  private EntityLivingBase targetedEntity;
  
  private int field_175479_bo;
  
  private boolean field_175480_bp;
  
  private EntityAIWander wander;
  
  public EntityGuardian(World worldIn) {
    super(worldIn);
    this.experienceValue = 10;
    setSize(0.85F, 0.85F);
    this.tasks.addTask(4, new AIGuardianAttack(this));
    EntityAIMoveTowardsRestriction entityaimovetowardsrestriction;
    this.tasks.addTask(5, (EntityAIBase)(entityaimovetowardsrestriction = new EntityAIMoveTowardsRestriction(this, 1.0D)));
    this.tasks.addTask(7, (EntityAIBase)(this.wander = new EntityAIWander(this, 1.0D, 80)));
    this.tasks.addTask(8, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
    this.tasks.addTask(8, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityGuardian.class, 12.0F, 0.01F));
    this.tasks.addTask(9, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
    this.wander.setMutexBits(3);
    entityaimovetowardsrestriction.setMutexBits(3);
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 10, true, false, new GuardianTargetSelector(this)));
    this.moveHelper = new GuardianMoveHelper(this);
    this.field_175484_c = this.field_175482_b = this.rand.nextFloat();
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0D);
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
    getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
    getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0D);
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund) {
    super.readEntityFromNBT(tagCompund);
    setElder(tagCompund.getBoolean("Elder"));
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound) {
    super.writeEntityToNBT(tagCompound);
    tagCompound.setBoolean("Elder", isElder());
  }
  
  protected PathNavigate getNewNavigator(World worldIn) {
    return (PathNavigate)new PathNavigateSwimmer((EntityLiving)this, worldIn);
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataWatcher.addObject(16, Integer.valueOf(0));
    this.dataWatcher.addObject(17, Integer.valueOf(0));
  }
  
  private boolean isSyncedFlagSet(int flagId) {
    return ((this.dataWatcher.getWatchableObjectInt(16) & flagId) != 0);
  }
  
  private void setSyncedFlag(int flagId, boolean state) {
    int i = this.dataWatcher.getWatchableObjectInt(16);
    if (state) {
      this.dataWatcher.updateObject(16, Integer.valueOf(i | flagId));
    } else {
      this.dataWatcher.updateObject(16, Integer.valueOf(i & (flagId ^ 0xFFFFFFFF)));
    } 
  }
  
  public boolean func_175472_n() {
    return isSyncedFlagSet(2);
  }
  
  private void func_175476_l(boolean p_175476_1_) {
    setSyncedFlag(2, p_175476_1_);
  }
  
  public int func_175464_ck() {
    return isElder() ? 60 : 80;
  }
  
  public boolean isElder() {
    return isSyncedFlagSet(4);
  }
  
  public void setElder(boolean elder) {
    setSyncedFlag(4, elder);
    if (elder) {
      setSize(1.9975F, 1.9975F);
      getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
      getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(8.0D);
      getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(80.0D);
      enablePersistence();
      this.wander.setExecutionChance(400);
    } 
  }
  
  public void setElder() {
    setElder(true);
    this.field_175486_bm = this.field_175485_bl = 1.0F;
  }
  
  private void setTargetedEntity(int entityId) {
    this.dataWatcher.updateObject(17, Integer.valueOf(entityId));
  }
  
  public boolean hasTargetedEntity() {
    return (this.dataWatcher.getWatchableObjectInt(17) != 0);
  }
  
  public EntityLivingBase getTargetedEntity() {
    if (!hasTargetedEntity())
      return null; 
    if (this.worldObj.isRemote) {
      if (this.targetedEntity != null)
        return this.targetedEntity; 
      Entity entity = this.worldObj.getEntityByID(this.dataWatcher.getWatchableObjectInt(17));
      if (entity instanceof EntityLivingBase) {
        this.targetedEntity = (EntityLivingBase)entity;
        return this.targetedEntity;
      } 
      return null;
    } 
    return getAttackTarget();
  }
  
  public void onDataWatcherUpdate(int dataID) {
    super.onDataWatcherUpdate(dataID);
    if (dataID == 16) {
      if (isElder() && this.width < 1.0F)
        setSize(1.9975F, 1.9975F); 
    } else if (dataID == 17) {
      this.field_175479_bo = 0;
      this.targetedEntity = null;
    } 
  }
  
  public int getTalkInterval() {
    return 160;
  }
  
  protected String getLivingSound() {
    return !isInWater() ? "mob.guardian.land.idle" : (isElder() ? "mob.guardian.elder.idle" : "mob.guardian.idle");
  }
  
  protected String getHurtSound() {
    return !isInWater() ? "mob.guardian.land.hit" : (isElder() ? "mob.guardian.elder.hit" : "mob.guardian.hit");
  }
  
  protected String getDeathSound() {
    return !isInWater() ? "mob.guardian.land.death" : (isElder() ? "mob.guardian.elder.death" : "mob.guardian.death");
  }
  
  protected boolean canTriggerWalking() {
    return false;
  }
  
  public float getEyeHeight() {
    return this.height * 0.5F;
  }
  
  public float getBlockPathWeight(BlockPos pos) {
    return (this.worldObj.getBlockState(pos).getBlock().getMaterial() == Material.water) ? (10.0F + this.worldObj.getLightBrightness(pos) - 0.5F) : super.getBlockPathWeight(pos);
  }
  
  public void onLivingUpdate() {
    if (this.worldObj.isRemote) {
      this.field_175484_c = this.field_175482_b;
      if (!isInWater()) {
        this.field_175483_bk = 2.0F;
        if (this.motionY > 0.0D && this.field_175480_bp && !isSilent())
          this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.guardian.flop", 1.0F, 1.0F, false); 
        this.field_175480_bp = (this.motionY < 0.0D && this.worldObj.isBlockNormalCube((new BlockPos((Entity)this)).down(), false));
      } else if (func_175472_n()) {
        if (this.field_175483_bk < 0.5F) {
          this.field_175483_bk = 4.0F;
        } else {
          this.field_175483_bk += (0.5F - this.field_175483_bk) * 0.1F;
        } 
      } else {
        this.field_175483_bk += (0.125F - this.field_175483_bk) * 0.2F;
      } 
      this.field_175482_b += this.field_175483_bk;
      this.field_175486_bm = this.field_175485_bl;
      if (!isInWater()) {
        this.field_175485_bl = this.rand.nextFloat();
      } else if (func_175472_n()) {
        this.field_175485_bl += (0.0F - this.field_175485_bl) * 0.25F;
      } else {
        this.field_175485_bl += (1.0F - this.field_175485_bl) * 0.06F;
      } 
      if (func_175472_n() && isInWater()) {
        Vec3 vec3 = getLook(0.0F);
        for (int i = 0; i < 2; i++)
          this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + (this.rand.nextDouble() - 0.5D) * this.width - vec3.xCoord * 1.5D, this.posY + this.rand.nextDouble() * this.height - vec3.yCoord * 1.5D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width - vec3.zCoord * 1.5D, 0.0D, 0.0D, 0.0D, new int[0]); 
      } 
      if (hasTargetedEntity()) {
        if (this.field_175479_bo < func_175464_ck())
          this.field_175479_bo++; 
        EntityLivingBase entitylivingbase = getTargetedEntity();
        if (entitylivingbase != null) {
          getLookHelper().setLookPositionWithEntity((Entity)entitylivingbase, 90.0F, 90.0F);
          getLookHelper().onUpdateLook();
          double d5 = func_175477_p(0.0F);
          double d0 = entitylivingbase.posX - this.posX;
          double d1 = entitylivingbase.posY + (entitylivingbase.height * 0.5F) - this.posY + getEyeHeight();
          double d2 = entitylivingbase.posZ - this.posZ;
          double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
          d0 /= d3;
          d1 /= d3;
          d2 /= d3;
          double d4 = this.rand.nextDouble();
          while (d4 < d3) {
            d4 += 1.8D - d5 + this.rand.nextDouble() * (1.7D - d5);
            this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + d0 * d4, this.posY + d1 * d4 + getEyeHeight(), this.posZ + d2 * d4, 0.0D, 0.0D, 0.0D, new int[0]);
          } 
        } 
      } 
    } 
    if (this.inWater) {
      setAir(300);
    } else if (this.onGround) {
      this.motionY += 0.5D;
      this.motionX += ((this.rand.nextFloat() * 2.0F - 1.0F) * 0.4F);
      this.motionZ += ((this.rand.nextFloat() * 2.0F - 1.0F) * 0.4F);
      this.rotationYaw = this.rand.nextFloat() * 360.0F;
      this.onGround = false;
      this.isAirBorne = true;
    } 
    if (hasTargetedEntity())
      this.rotationYaw = this.rotationYawHead; 
    super.onLivingUpdate();
  }
  
  public float func_175471_a(float p_175471_1_) {
    return this.field_175484_c + (this.field_175482_b - this.field_175484_c) * p_175471_1_;
  }
  
  public float func_175469_o(float p_175469_1_) {
    return this.field_175486_bm + (this.field_175485_bl - this.field_175486_bm) * p_175469_1_;
  }
  
  public float func_175477_p(float p_175477_1_) {
    return (this.field_175479_bo + p_175477_1_) / func_175464_ck();
  }
  
  protected void updateAITasks() {
    super.updateAITasks();
    if (isElder()) {
      int i = 1200;
      int j = 1200;
      int k = 6000;
      int l = 2;
      if ((this.ticksExisted + getEntityId()) % 1200 == 0) {
        Potion potion = Potion.digSlowdown;
        for (EntityPlayerMP entityplayermp : this.worldObj.getPlayers(EntityPlayerMP.class, new Predicate<EntityPlayerMP>() {
              public boolean apply(EntityPlayerMP p_apply_1_) {
                return (EntityGuardian.this.getDistanceSqToEntity((Entity)p_apply_1_) < 2500.0D && p_apply_1_.theItemInWorldManager.survivalOrAdventure());
              }
            })) {
          if (!entityplayermp.isPotionActive(potion) || entityplayermp.getActivePotionEffect(potion).getAmplifier() < 2 || entityplayermp.getActivePotionEffect(potion).getDuration() < 1200) {
            entityplayermp.playerNetServerHandler.sendPacket((Packet)new S2BPacketChangeGameState(10, 0.0F));
            entityplayermp.addPotionEffect(new PotionEffect(potion.id, 6000, 2));
          } 
        } 
      } 
      if (!hasHome())
        setHomePosAndDistance(new BlockPos((Entity)this), 16); 
    } 
  }
  
  protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
    int i = this.rand.nextInt(3) + this.rand.nextInt(p_70628_2_ + 1);
    if (i > 0)
      entityDropItem(new ItemStack(Items.prismarine_shard, i, 0), 1.0F); 
    if (this.rand.nextInt(3 + p_70628_2_) > 1) {
      entityDropItem(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.getMetadata()), 1.0F);
    } else if (this.rand.nextInt(3 + p_70628_2_) > 1) {
      entityDropItem(new ItemStack(Items.prismarine_crystals, 1, 0), 1.0F);
    } 
    if (p_70628_1_ && isElder())
      entityDropItem(new ItemStack(Blocks.sponge, 1, 1), 1.0F); 
  }
  
  protected void addRandomDrop() {
    ItemStack itemstack = ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, EntityFishHook.func_174855_j())).getItemStack(this.rand);
    entityDropItem(itemstack, 1.0F);
  }
  
  protected boolean isValidLightLevel() {
    return true;
  }
  
  public boolean isNotColliding() {
    return (this.worldObj.checkNoEntityCollision(getEntityBoundingBox(), (Entity)this) && this.worldObj.getCollidingBoundingBoxes((Entity)this, getEntityBoundingBox()).isEmpty());
  }
  
  public boolean getCanSpawnHere() {
    return ((this.rand.nextInt(20) == 0 || !this.worldObj.canBlockSeeSky(new BlockPos((Entity)this))) && super.getCanSpawnHere());
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (!func_175472_n() && !source.isMagicDamage() && source.getSourceOfDamage() instanceof EntityLivingBase) {
      EntityLivingBase entitylivingbase = (EntityLivingBase)source.getSourceOfDamage();
      if (!source.isExplosion()) {
        entitylivingbase.attackEntityFrom(DamageSource.causeThornsDamage((Entity)this), 2.0F);
        entitylivingbase.playSound("damage.thorns", 0.5F, 1.0F);
      } 
    } 
    this.wander.makeUpdate();
    return super.attackEntityFrom(source, amount);
  }
  
  public int getVerticalFaceSpeed() {
    return 180;
  }
  
  public void moveEntityWithHeading(float strafe, float forward) {
    if (isServerWorld()) {
      if (isInWater()) {
        moveFlying(strafe, forward, 0.1F);
        moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.8999999761581421D;
        this.motionY *= 0.8999999761581421D;
        this.motionZ *= 0.8999999761581421D;
        if (!func_175472_n() && getAttackTarget() == null)
          this.motionY -= 0.005D; 
      } else {
        super.moveEntityWithHeading(strafe, forward);
      } 
    } else {
      super.moveEntityWithHeading(strafe, forward);
    } 
  }
  
  static class AIGuardianAttack extends EntityAIBase {
    private EntityGuardian theEntity;
    
    private int tickCounter;
    
    public AIGuardianAttack(EntityGuardian p_i45833_1_) {
      this.theEntity = p_i45833_1_;
      setMutexBits(3);
    }
    
    public boolean shouldExecute() {
      EntityLivingBase entitylivingbase = this.theEntity.getAttackTarget();
      return (entitylivingbase != null && entitylivingbase.isEntityAlive());
    }
    
    public boolean continueExecuting() {
      return (super.continueExecuting() && (this.theEntity.isElder() || this.theEntity.getDistanceSqToEntity((Entity)this.theEntity.getAttackTarget()) > 9.0D));
    }
    
    public void startExecuting() {
      this.tickCounter = -10;
      this.theEntity.getNavigator().clearPathEntity();
      this.theEntity.getLookHelper().setLookPositionWithEntity((Entity)this.theEntity.getAttackTarget(), 90.0F, 90.0F);
      this.theEntity.isAirBorne = true;
    }
    
    public void resetTask() {
      this.theEntity.setTargetedEntity(0);
      this.theEntity.setAttackTarget((EntityLivingBase)null);
      this.theEntity.wander.makeUpdate();
    }
    
    public void updateTask() {
      EntityLivingBase entitylivingbase = this.theEntity.getAttackTarget();
      this.theEntity.getNavigator().clearPathEntity();
      this.theEntity.getLookHelper().setLookPositionWithEntity((Entity)entitylivingbase, 90.0F, 90.0F);
      if (!this.theEntity.canEntityBeSeen((Entity)entitylivingbase)) {
        this.theEntity.setAttackTarget((EntityLivingBase)null);
      } else {
        this.tickCounter++;
        if (this.tickCounter == 0) {
          this.theEntity.setTargetedEntity(this.theEntity.getAttackTarget().getEntityId());
          this.theEntity.worldObj.setEntityState((Entity)this.theEntity, (byte)21);
        } else if (this.tickCounter >= this.theEntity.func_175464_ck()) {
          float f = 1.0F;
          if (this.theEntity.worldObj.getDifficulty() == EnumDifficulty.HARD)
            f += 2.0F; 
          if (this.theEntity.isElder())
            f += 2.0F; 
          entitylivingbase.attackEntityFrom(DamageSource.causeIndirectMagicDamage((Entity)this.theEntity, (Entity)this.theEntity), f);
          entitylivingbase.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this.theEntity), (float)this.theEntity.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue());
          this.theEntity.setAttackTarget((EntityLivingBase)null);
        } else if (this.tickCounter < 60 || this.tickCounter % 20 == 0) {
        
        } 
        super.updateTask();
      } 
    }
  }
  
  static class GuardianMoveHelper extends EntityMoveHelper {
    private EntityGuardian entityGuardian;
    
    public GuardianMoveHelper(EntityGuardian p_i45831_1_) {
      super((EntityLiving)p_i45831_1_);
      this.entityGuardian = p_i45831_1_;
    }
    
    public void onUpdateMoveHelper() {
      if (this.update && !this.entityGuardian.getNavigator().noPath()) {
        double d0 = this.posX - this.entityGuardian.posX;
        double d1 = this.posY - this.entityGuardian.posY;
        double d2 = this.posZ - this.entityGuardian.posZ;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;
        d3 = MathHelper.sqrt_double(d3);
        d1 /= d3;
        float f = (float)(MathHelper.func_181159_b(d2, d0) * 180.0D / Math.PI) - 90.0F;
        this.entityGuardian.rotationYaw = limitAngle(this.entityGuardian.rotationYaw, f, 30.0F);
        this.entityGuardian.renderYawOffset = this.entityGuardian.rotationYaw;
        float f1 = (float)(this.speed * this.entityGuardian.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
        this.entityGuardian.setAIMoveSpeed(this.entityGuardian.getAIMoveSpeed() + (f1 - this.entityGuardian.getAIMoveSpeed()) * 0.125F);
        double d4 = Math.sin((this.entityGuardian.ticksExisted + this.entityGuardian.getEntityId()) * 0.5D) * 0.05D;
        double d5 = Math.cos((this.entityGuardian.rotationYaw * 3.1415927F / 180.0F));
        double d6 = Math.sin((this.entityGuardian.rotationYaw * 3.1415927F / 180.0F));
        this.entityGuardian.motionX += d4 * d5;
        this.entityGuardian.motionZ += d4 * d6;
        d4 = Math.sin((this.entityGuardian.ticksExisted + this.entityGuardian.getEntityId()) * 0.75D) * 0.05D;
        this.entityGuardian.motionY += d4 * (d6 + d5) * 0.25D;
        this.entityGuardian.motionY += this.entityGuardian.getAIMoveSpeed() * d1 * 0.1D;
        EntityLookHelper entitylookhelper = this.entityGuardian.getLookHelper();
        double d7 = this.entityGuardian.posX + d0 / d3 * 2.0D;
        double d8 = this.entityGuardian.getEyeHeight() + this.entityGuardian.posY + d1 / d3 * 1.0D;
        double d9 = this.entityGuardian.posZ + d2 / d3 * 2.0D;
        double d10 = entitylookhelper.getLookPosX();
        double d11 = entitylookhelper.getLookPosY();
        double d12 = entitylookhelper.getLookPosZ();
        if (!entitylookhelper.getIsLooking()) {
          d10 = d7;
          d11 = d8;
          d12 = d9;
        } 
        this.entityGuardian.getLookHelper().setLookPosition(d10 + (d7 - d10) * 0.125D, d11 + (d8 - d11) * 0.125D, d12 + (d9 - d12) * 0.125D, 10.0F, 40.0F);
        this.entityGuardian.func_175476_l(true);
      } else {
        this.entityGuardian.setAIMoveSpeed(0.0F);
        this.entityGuardian.func_175476_l(false);
      } 
    }
  }
  
  static class GuardianTargetSelector implements Predicate<EntityLivingBase> {
    private EntityGuardian parentEntity;
    
    public GuardianTargetSelector(EntityGuardian p_i45832_1_) {
      this.parentEntity = p_i45832_1_;
    }
    
    public boolean apply(EntityLivingBase p_apply_1_) {
      return ((p_apply_1_ instanceof EntityPlayer || p_apply_1_ instanceof net.minecraft.entity.passive.EntitySquid) && p_apply_1_.getDistanceSqToEntity((Entity)this.parentEntity) > 9.0D);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\monster\EntityGuardian.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */