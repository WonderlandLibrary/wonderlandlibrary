package net.minecraft.entity.monster;

import java.util.Calendar;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntitySkeleton extends EntityMob implements IRangedAttackMob {
  private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F);
  
  private EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.2D, false);
  
  public EntitySkeleton(World worldIn) {
    super(worldIn);
    this.tasks.addTask(1, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(2, (EntityAIBase)new EntityAIRestrictSun(this));
    this.tasks.addTask(3, (EntityAIBase)new EntityAIFleeSun(this, 1.0D));
    this.tasks.addTask(3, (EntityAIBase)new EntityAIAvoidEntity(this, EntityWolf.class, 6.0F, 1.0D, 1.2D));
    this.tasks.addTask(4, (EntityAIBase)new EntityAIWander(this, 1.0D));
    this.tasks.addTask(6, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
    this.tasks.addTask(6, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget(this, false, new Class[0]));
    this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    this.targetTasks.addTask(3, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
    if (worldIn != null && !worldIn.isRemote)
      setCombatTask(); 
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataWatcher.addObject(13, new Byte((byte)0));
  }
  
  protected String getLivingSound() {
    return "mob.skeleton.say";
  }
  
  protected String getHurtSound() {
    return "mob.skeleton.hurt";
  }
  
  protected String getDeathSound() {
    return "mob.skeleton.death";
  }
  
  protected void playStepSound(BlockPos pos, Block blockIn) {
    playSound("mob.skeleton.step", 0.15F, 1.0F);
  }
  
  public boolean attackEntityAsMob(Entity entityIn) {
    if (super.attackEntityAsMob(entityIn)) {
      if (getSkeletonType() == 1 && entityIn instanceof EntityLivingBase)
        ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(Potion.wither.id, 200)); 
      return true;
    } 
    return false;
  }
  
  public EnumCreatureAttribute getCreatureAttribute() {
    return EnumCreatureAttribute.UNDEAD;
  }
  
  public void onLivingUpdate() {
    if (this.worldObj.isDaytime() && !this.worldObj.isRemote) {
      float f = getBrightness(1.0F);
      BlockPos blockpos = new BlockPos(this.posX, Math.round(this.posY), this.posZ);
      if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.worldObj.canSeeSky(blockpos)) {
        boolean flag = true;
        ItemStack itemstack = getEquipmentInSlot(4);
        if (itemstack != null) {
          if (itemstack.isItemStackDamageable()) {
            itemstack.setItemDamage(itemstack.getItemDamage() + this.rand.nextInt(2));
            if (itemstack.getItemDamage() >= itemstack.getMaxDamage()) {
              renderBrokenItemStack(itemstack);
              setCurrentItemOrArmor(4, (ItemStack)null);
            } 
          } 
          flag = false;
        } 
        if (flag)
          setFire(8); 
      } 
    } 
    if (this.worldObj.isRemote && getSkeletonType() == 1)
      setSize(0.72F, 2.535F); 
    super.onLivingUpdate();
  }
  
  public void updateRidden() {
    super.updateRidden();
    if (this.ridingEntity instanceof EntityCreature) {
      EntityCreature entitycreature = (EntityCreature)this.ridingEntity;
      this.renderYawOffset = entitycreature.renderYawOffset;
    } 
  }
  
  public void onDeath(DamageSource cause) {
    super.onDeath(cause);
    if (cause.getSourceOfDamage() instanceof EntityArrow && cause.getEntity() instanceof EntityPlayer) {
      EntityPlayer entityplayer = (EntityPlayer)cause.getEntity();
      double d0 = entityplayer.posX - this.posX;
      double d1 = entityplayer.posZ - this.posZ;
      if (d0 * d0 + d1 * d1 >= 2500.0D)
        entityplayer.triggerAchievement((StatBase)AchievementList.snipeSkeleton); 
    } else if (cause.getEntity() instanceof EntityCreeper && ((EntityCreeper)cause.getEntity()).getPowered() && ((EntityCreeper)cause.getEntity()).isAIEnabled()) {
      ((EntityCreeper)cause.getEntity()).func_175493_co();
      entityDropItem(new ItemStack(Items.skull, 1, (getSkeletonType() == 1) ? 1 : 0), 0.0F);
    } 
  }
  
  protected Item getDropItem() {
    return Items.arrow;
  }
  
  protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
    if (getSkeletonType() == 1) {
      int i = this.rand.nextInt(3 + p_70628_2_) - 1;
      for (int j = 0; j < i; j++)
        dropItem(Items.coal, 1); 
    } else {
      int k = this.rand.nextInt(3 + p_70628_2_);
      for (int i1 = 0; i1 < k; i1++)
        dropItem(Items.arrow, 1); 
    } 
    int l = this.rand.nextInt(3 + p_70628_2_);
    for (int j1 = 0; j1 < l; j1++)
      dropItem(Items.bone, 1); 
  }
  
  protected void addRandomDrop() {
    if (getSkeletonType() == 1)
      entityDropItem(new ItemStack(Items.skull, 1, 1), 0.0F); 
  }
  
  protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
    super.setEquipmentBasedOnDifficulty(difficulty);
    setCurrentItemOrArmor(0, new ItemStack((Item)Items.bow));
  }
  
  public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
    livingdata = super.onInitialSpawn(difficulty, livingdata);
    if (this.worldObj.provider instanceof net.minecraft.world.WorldProviderHell && getRNG().nextInt(5) > 0) {
      this.tasks.addTask(4, (EntityAIBase)this.aiAttackOnCollide);
      setSkeletonType(1);
      setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
      getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
    } else {
      this.tasks.addTask(4, (EntityAIBase)this.aiArrowAttack);
      setEquipmentBasedOnDifficulty(difficulty);
      setEnchantmentBasedOnDifficulty(difficulty);
    } 
    setCanPickUpLoot((this.rand.nextFloat() < 0.55F * difficulty.getClampedAdditionalDifficulty()));
    if (getEquipmentInSlot(4) == null) {
      Calendar calendar = this.worldObj.getCurrentDate();
      if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F) {
        setCurrentItemOrArmor(4, new ItemStack((this.rand.nextFloat() < 0.1F) ? Blocks.lit_pumpkin : Blocks.pumpkin));
        this.equipmentDropChances[4] = 0.0F;
      } 
    } 
    return livingdata;
  }
  
  public void setCombatTask() {
    this.tasks.removeTask((EntityAIBase)this.aiAttackOnCollide);
    this.tasks.removeTask((EntityAIBase)this.aiArrowAttack);
    ItemStack itemstack = getHeldItem();
    if (itemstack != null && itemstack.getItem() == Items.bow) {
      this.tasks.addTask(4, (EntityAIBase)this.aiArrowAttack);
    } else {
      this.tasks.addTask(4, (EntityAIBase)this.aiAttackOnCollide);
    } 
  }
  
  public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_) {
    EntityArrow entityarrow = new EntityArrow(this.worldObj, (EntityLivingBase)this, p_82196_1_, 1.6F, (14 - this.worldObj.getDifficulty().getDifficultyId() * 4));
    int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, getHeldItem());
    int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, getHeldItem());
    entityarrow.setDamage((p_82196_2_ * 2.0F) + this.rand.nextGaussian() * 0.25D + (this.worldObj.getDifficulty().getDifficultyId() * 0.11F));
    if (i > 0)
      entityarrow.setDamage(entityarrow.getDamage() + i * 0.5D + 0.5D); 
    if (j > 0)
      entityarrow.setKnockbackStrength(j); 
    if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, getHeldItem()) > 0 || getSkeletonType() == 1)
      entityarrow.setFire(100); 
    playSound("random.bow", 1.0F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
    this.worldObj.spawnEntityInWorld((Entity)entityarrow);
  }
  
  public int getSkeletonType() {
    return this.dataWatcher.getWatchableObjectByte(13);
  }
  
  public void setSkeletonType(int p_82201_1_) {
    this.dataWatcher.updateObject(13, Byte.valueOf((byte)p_82201_1_));
    this.isImmuneToFire = (p_82201_1_ == 1);
    if (p_82201_1_ == 1) {
      setSize(0.72F, 2.535F);
    } else {
      setSize(0.6F, 1.95F);
    } 
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund) {
    super.readEntityFromNBT(tagCompund);
    if (tagCompund.hasKey("SkeletonType", 99)) {
      int i = tagCompund.getByte("SkeletonType");
      setSkeletonType(i);
    } 
    setCombatTask();
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound) {
    super.writeEntityToNBT(tagCompound);
    tagCompound.setByte("SkeletonType", (byte)getSkeletonType());
  }
  
  public void setCurrentItemOrArmor(int slotIn, ItemStack stack) {
    super.setCurrentItemOrArmor(slotIn, stack);
    if (!this.worldObj.isRemote && slotIn == 0)
      setCombatTask(); 
  }
  
  public float getEyeHeight() {
    return (getSkeletonType() == 1) ? super.getEyeHeight() : 1.74F;
  }
  
  public double getYOffset() {
    return isChild() ? 0.0D : -0.35D;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\monster\EntitySkeleton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */