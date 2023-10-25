package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIBeg;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityWolf extends EntityTameable {
  private float headRotationCourse;
  
  private float headRotationCourseOld;
  
  private boolean isWet;
  
  private boolean isShaking;
  
  private float timeWolfIsShaking;
  
  private float prevTimeWolfIsShaking;
  
  public EntityWolf(World worldIn) {
    super(worldIn);
    setSize(0.6F, 0.8F);
    ((PathNavigateGround)getNavigator()).setAvoidsWater(true);
    this.tasks.addTask(1, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(2, (EntityAIBase)this.aiSit);
    this.tasks.addTask(3, (EntityAIBase)new EntityAILeapAtTarget((EntityLiving)this, 0.4F));
    this.tasks.addTask(4, (EntityAIBase)new EntityAIAttackOnCollide((EntityCreature)this, 1.0D, true));
    this.tasks.addTask(5, (EntityAIBase)new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
    this.tasks.addTask(6, (EntityAIBase)new EntityAIMate(this, 1.0D));
    this.tasks.addTask(7, (EntityAIBase)new EntityAIWander((EntityCreature)this, 1.0D));
    this.tasks.addTask(8, (EntityAIBase)new EntityAIBeg(this, 8.0F));
    this.tasks.addTask(9, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
    this.tasks.addTask(9, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAIOwnerHurtByTarget(this));
    this.targetTasks.addTask(2, (EntityAIBase)new EntityAIOwnerHurtTarget(this));
    this.targetTasks.addTask(3, (EntityAIBase)new EntityAIHurtByTarget((EntityCreature)this, true, new Class[0]));
    this.targetTasks.addTask(4, (EntityAIBase)new EntityAITargetNonTamed(this, EntityAnimal.class, false, new Predicate<Entity>() {
            public boolean apply(Entity p_apply_1_) {
              return (p_apply_1_ instanceof EntitySheep || p_apply_1_ instanceof EntityRabbit);
            }
          }));
    this.targetTasks.addTask(5, (EntityAIBase)new EntityAINearestAttackableTarget((EntityCreature)this, EntitySkeleton.class, false));
    setTamed(false);
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
    if (isTamed()) {
      getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
    } else {
      getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
    } 
    getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
    getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2.0D);
  }
  
  public void setAttackTarget(EntityLivingBase entitylivingbaseIn) {
    super.setAttackTarget(entitylivingbaseIn);
    if (entitylivingbaseIn == null) {
      setAngry(false);
    } else if (!isTamed()) {
      setAngry(true);
    } 
  }
  
  protected void updateAITasks() {
    this.dataWatcher.updateObject(18, Float.valueOf(getHealth()));
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataWatcher.addObject(18, new Float(getHealth()));
    this.dataWatcher.addObject(19, new Byte((byte)0));
    this.dataWatcher.addObject(20, new Byte((byte)EnumDyeColor.RED.getMetadata()));
  }
  
  protected void playStepSound(BlockPos pos, Block blockIn) {
    playSound("mob.wolf.step", 0.15F, 1.0F);
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound) {
    super.writeEntityToNBT(tagCompound);
    tagCompound.setBoolean("Angry", isAngry());
    tagCompound.setByte("CollarColor", (byte)getCollarColor().getDyeDamage());
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund) {
    super.readEntityFromNBT(tagCompund);
    setAngry(tagCompund.getBoolean("Angry"));
    if (tagCompund.hasKey("CollarColor", 99))
      setCollarColor(EnumDyeColor.byDyeDamage(tagCompund.getByte("CollarColor"))); 
  }
  
  protected String getLivingSound() {
    return isAngry() ? "mob.wolf.growl" : ((this.rand.nextInt(3) == 0) ? ((isTamed() && this.dataWatcher.getWatchableObjectFloat(18) < 10.0F) ? "mob.wolf.whine" : "mob.wolf.panting") : "mob.wolf.bark");
  }
  
  protected String getHurtSound() {
    return "mob.wolf.hurt";
  }
  
  protected String getDeathSound() {
    return "mob.wolf.death";
  }
  
  protected float getSoundVolume() {
    return 0.4F;
  }
  
  protected Item getDropItem() {
    return Item.getItemById(-1);
  }
  
  public void onLivingUpdate() {
    super.onLivingUpdate();
    if (!this.worldObj.isRemote && this.isWet && !this.isShaking && !hasPath() && this.onGround) {
      this.isShaking = true;
      this.timeWolfIsShaking = 0.0F;
      this.prevTimeWolfIsShaking = 0.0F;
      this.worldObj.setEntityState((Entity)this, (byte)8);
    } 
    if (!this.worldObj.isRemote && getAttackTarget() == null && isAngry())
      setAngry(false); 
  }
  
  public void onUpdate() {
    super.onUpdate();
    this.headRotationCourseOld = this.headRotationCourse;
    if (isBegging()) {
      this.headRotationCourse += (1.0F - this.headRotationCourse) * 0.4F;
    } else {
      this.headRotationCourse += (0.0F - this.headRotationCourse) * 0.4F;
    } 
    if (isWet()) {
      this.isWet = true;
      this.isShaking = false;
      this.timeWolfIsShaking = 0.0F;
      this.prevTimeWolfIsShaking = 0.0F;
    } else if ((this.isWet || this.isShaking) && this.isShaking) {
      if (this.timeWolfIsShaking == 0.0F)
        playSound("mob.wolf.shake", getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F); 
      this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
      this.timeWolfIsShaking += 0.05F;
      if (this.prevTimeWolfIsShaking >= 2.0F) {
        this.isWet = false;
        this.isShaking = false;
        this.prevTimeWolfIsShaking = 0.0F;
        this.timeWolfIsShaking = 0.0F;
      } 
      if (this.timeWolfIsShaking > 0.4F) {
        float f = (float)(getEntityBoundingBox()).minY;
        int i = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4F) * 3.1415927F) * 7.0F);
        for (int j = 0; j < i; j++) {
          float f1 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
          float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
          this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + f1, (f + 0.8F), this.posZ + f2, this.motionX, this.motionY, this.motionZ, new int[0]);
        } 
      } 
    } 
  }
  
  public boolean isWolfWet() {
    return this.isWet;
  }
  
  public float getShadingWhileWet(float p_70915_1_) {
    return 0.75F + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70915_1_) / 2.0F * 0.25F;
  }
  
  public float getShakeAngle(float p_70923_1_, float p_70923_2_) {
    float f = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70923_1_ + p_70923_2_) / 1.8F;
    if (f < 0.0F) {
      f = 0.0F;
    } else if (f > 1.0F) {
      f = 1.0F;
    } 
    return MathHelper.sin(f * 3.1415927F) * MathHelper.sin(f * 3.1415927F * 11.0F) * 0.15F * 3.1415927F;
  }
  
  public float getInterestedAngle(float p_70917_1_) {
    return (this.headRotationCourseOld + (this.headRotationCourse - this.headRotationCourseOld) * p_70917_1_) * 0.15F * 3.1415927F;
  }
  
  public float getEyeHeight() {
    return this.height * 0.8F;
  }
  
  public int getVerticalFaceSpeed() {
    return isSitting() ? 20 : super.getVerticalFaceSpeed();
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    Entity entity = source.getEntity();
    this.aiSit.setSitting(false);
    if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof net.minecraft.entity.projectile.EntityArrow))
      amount = (amount + 1.0F) / 2.0F; 
    return super.attackEntityFrom(source, amount);
  }
  
  public boolean attackEntityAsMob(Entity entityIn) {
    boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), (int)getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue());
    if (flag)
      applyEnchantments((EntityLivingBase)this, entityIn); 
    return flag;
  }
  
  public void setTamed(boolean tamed) {
    super.setTamed(tamed);
    if (tamed) {
      getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
    } else {
      getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
    } 
    getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
  }
  
  public boolean interact(EntityPlayer player) {
    ItemStack itemstack = player.inventory.getCurrentItem();
    if (isTamed()) {
      if (itemstack != null)
        if (itemstack.getItem() instanceof ItemFood) {
          ItemFood itemfood = (ItemFood)itemstack.getItem();
          if (itemfood.isWolfsFavoriteMeat() && this.dataWatcher.getWatchableObjectFloat(18) < 20.0F) {
            if (!player.capabilities.isCreativeMode)
              itemstack.stackSize--; 
            heal(itemfood.getHealAmount(itemstack));
            if (itemstack.stackSize <= 0)
              player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null); 
            return true;
          } 
        } else if (itemstack.getItem() == Items.dye) {
          EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(itemstack.getMetadata());
          if (enumdyecolor != getCollarColor()) {
            setCollarColor(enumdyecolor);
            if (!player.capabilities.isCreativeMode && --itemstack.stackSize <= 0)
              player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null); 
            return true;
          } 
        }  
      if (isOwner((EntityLivingBase)player) && !this.worldObj.isRemote && !isBreedingItem(itemstack)) {
        this.aiSit.setSitting(!isSitting());
        this.isJumping = false;
        this.navigator.clearPathEntity();
        setAttackTarget((EntityLivingBase)null);
      } 
    } else if (itemstack != null && itemstack.getItem() == Items.bone && !isAngry()) {
      if (!player.capabilities.isCreativeMode)
        itemstack.stackSize--; 
      if (itemstack.stackSize <= 0)
        player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null); 
      if (!this.worldObj.isRemote)
        if (this.rand.nextInt(3) == 0) {
          setTamed(true);
          this.navigator.clearPathEntity();
          setAttackTarget((EntityLivingBase)null);
          this.aiSit.setSitting(true);
          setHealth(20.0F);
          setOwnerId(player.getUniqueID().toString());
          playTameEffect(true);
          this.worldObj.setEntityState((Entity)this, (byte)7);
        } else {
          playTameEffect(false);
          this.worldObj.setEntityState((Entity)this, (byte)6);
        }  
      return true;
    } 
    return super.interact(player);
  }
  
  public void handleStatusUpdate(byte id) {
    if (id == 8) {
      this.isShaking = true;
      this.timeWolfIsShaking = 0.0F;
      this.prevTimeWolfIsShaking = 0.0F;
    } else {
      super.handleStatusUpdate(id);
    } 
  }
  
  public float getTailRotation() {
    return isAngry() ? 1.5393804F : (isTamed() ? ((0.55F - (20.0F - this.dataWatcher.getWatchableObjectFloat(18)) * 0.02F) * 3.1415927F) : 0.62831855F);
  }
  
  public boolean isBreedingItem(ItemStack stack) {
    return (stack == null) ? false : (!(stack.getItem() instanceof ItemFood) ? false : ((ItemFood)stack.getItem()).isWolfsFavoriteMeat());
  }
  
  public int getMaxSpawnedInChunk() {
    return 8;
  }
  
  public boolean isAngry() {
    return ((this.dataWatcher.getWatchableObjectByte(16) & 0x2) != 0);
  }
  
  public void setAngry(boolean angry) {
    byte b0 = this.dataWatcher.getWatchableObjectByte(16);
    if (angry) {
      this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 0x2)));
    } else {
      this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & 0xFFFFFFFD)));
    } 
  }
  
  public EnumDyeColor getCollarColor() {
    return EnumDyeColor.byDyeDamage(this.dataWatcher.getWatchableObjectByte(20) & 0xF);
  }
  
  public void setCollarColor(EnumDyeColor collarcolor) {
    this.dataWatcher.updateObject(20, Byte.valueOf((byte)(collarcolor.getDyeDamage() & 0xF)));
  }
  
  public EntityWolf createChild(EntityAgeable ageable) {
    EntityWolf entitywolf = new EntityWolf(this.worldObj);
    String s = getOwnerId();
    if (s != null && s.trim().length() > 0) {
      entitywolf.setOwnerId(s);
      entitywolf.setTamed(true);
    } 
    return entitywolf;
  }
  
  public void setBegging(boolean beg) {
    if (beg) {
      this.dataWatcher.updateObject(19, Byte.valueOf((byte)1));
    } else {
      this.dataWatcher.updateObject(19, Byte.valueOf((byte)0));
    } 
  }
  
  public boolean canMateWith(EntityAnimal otherAnimal) {
    if (otherAnimal == this)
      return false; 
    if (!isTamed())
      return false; 
    if (!(otherAnimal instanceof EntityWolf))
      return false; 
    EntityWolf entitywolf = (EntityWolf)otherAnimal;
    return !entitywolf.isTamed() ? false : (entitywolf.isSitting() ? false : ((isInLove() && entitywolf.isInLove())));
  }
  
  public boolean isBegging() {
    return (this.dataWatcher.getWatchableObjectByte(19) == 1);
  }
  
  protected boolean canDespawn() {
    return (!isTamed() && this.ticksExisted > 2400);
  }
  
  public boolean shouldAttackEntity(EntityLivingBase p_142018_1_, EntityLivingBase p_142018_2_) {
    if (!(p_142018_1_ instanceof net.minecraft.entity.monster.EntityCreeper) && !(p_142018_1_ instanceof net.minecraft.entity.monster.EntityGhast)) {
      if (p_142018_1_ instanceof EntityWolf) {
        EntityWolf entitywolf = (EntityWolf)p_142018_1_;
        if (entitywolf.isTamed() && entitywolf.getOwner() == p_142018_2_)
          return false; 
      } 
      return (p_142018_1_ instanceof EntityPlayer && p_142018_2_ instanceof EntityPlayer && !((EntityPlayer)p_142018_2_).canAttackPlayer((EntityPlayer)p_142018_1_)) ? false : ((!(p_142018_1_ instanceof EntityHorse) || !((EntityHorse)p_142018_1_).isTame()));
    } 
    return false;
  }
  
  public boolean allowLeashing() {
    return (!isAngry() && super.allowLeashing());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\passive\EntityWolf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */