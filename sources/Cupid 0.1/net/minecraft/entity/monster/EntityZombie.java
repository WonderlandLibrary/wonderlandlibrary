package net.minecraft.entity.monster;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class EntityZombie extends EntityMob {
  protected static final IAttribute reinforcementChance = (IAttribute)(new RangedAttribute((IAttribute)null, "zombie.spawnReinforcements", 0.0D, 0.0D, 1.0D)).setDescription("Spawn Reinforcements Chance");
  
  private static final UUID babySpeedBoostUUID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
  
  private static final AttributeModifier babySpeedBoostModifier = new AttributeModifier(babySpeedBoostUUID, "Baby speed boost", 0.5D, 1);
  
  private final EntityAIBreakDoor breakDoor = new EntityAIBreakDoor((EntityLiving)this);
  
  private int conversionTime;
  
  private boolean isBreakDoorsTaskSet = false;
  
  private float zombieWidth = -1.0F;
  
  private float zombieHeight;
  
  public EntityZombie(World worldIn) {
    super(worldIn);
    ((PathNavigateGround)getNavigator()).setBreakDoors(true);
    this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(2, (EntityAIBase)new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
    this.tasks.addTask(5, (EntityAIBase)new EntityAIMoveTowardsRestriction(this, 1.0D));
    this.tasks.addTask(7, (EntityAIBase)new EntityAIWander(this, 1.0D));
    this.tasks.addTask(8, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
    this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
    applyEntityAI();
    setSize(0.6F, 1.95F);
  }
  
  protected void applyEntityAI() {
    this.tasks.addTask(4, (EntityAIBase)new EntityAIAttackOnCollide(this, EntityVillager.class, 1.0D, true));
    this.tasks.addTask(4, (EntityAIBase)new EntityAIAttackOnCollide(this, EntityIronGolem.class, 1.0D, true));
    this.tasks.addTask(6, (EntityAIBase)new EntityAIMoveThroughVillage(this, 1.0D, false));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget(this, true, new Class[] { EntityPigZombie.class }));
    this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityVillager.class, false));
    this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(35.0D);
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
    getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0D);
    getAttributeMap().registerAttribute(reinforcementChance).setBaseValue(this.rand.nextDouble() * 0.10000000149011612D);
  }
  
  protected void entityInit() {
    super.entityInit();
    getDataWatcher().addObject(12, Byte.valueOf((byte)0));
    getDataWatcher().addObject(13, Byte.valueOf((byte)0));
    getDataWatcher().addObject(14, Byte.valueOf((byte)0));
  }
  
  public int getTotalArmorValue() {
    int i = super.getTotalArmorValue() + 2;
    if (i > 20)
      i = 20; 
    return i;
  }
  
  public boolean isBreakDoorsTaskSet() {
    return this.isBreakDoorsTaskSet;
  }
  
  public void setBreakDoorsAItask(boolean par1) {
    if (this.isBreakDoorsTaskSet != par1) {
      this.isBreakDoorsTaskSet = par1;
      if (par1) {
        this.tasks.addTask(1, (EntityAIBase)this.breakDoor);
      } else {
        this.tasks.removeTask((EntityAIBase)this.breakDoor);
      } 
    } 
  }
  
  public boolean isChild() {
    return (getDataWatcher().getWatchableObjectByte(12) == 1);
  }
  
  protected int getExperiencePoints(EntityPlayer player) {
    if (isChild())
      this.experienceValue = (int)(this.experienceValue * 2.5F); 
    return super.getExperiencePoints(player);
  }
  
  public void setChild(boolean childZombie) {
    getDataWatcher().updateObject(12, Byte.valueOf((byte)(childZombie ? 1 : 0)));
    if (this.worldObj != null && !this.worldObj.isRemote) {
      IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
      iattributeinstance.removeModifier(babySpeedBoostModifier);
      if (childZombie)
        iattributeinstance.applyModifier(babySpeedBoostModifier); 
    } 
    setChildSize(childZombie);
  }
  
  public boolean isVillager() {
    return (getDataWatcher().getWatchableObjectByte(13) == 1);
  }
  
  public void setVillager(boolean villager) {
    getDataWatcher().updateObject(13, Byte.valueOf((byte)(villager ? 1 : 0)));
  }
  
  public void onLivingUpdate() {
    if (this.worldObj.isDaytime() && !this.worldObj.isRemote && !isChild()) {
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
    if (isRiding() && getAttackTarget() != null && this.ridingEntity instanceof EntityChicken)
      ((EntityLiving)this.ridingEntity).getNavigator().setPath(getNavigator().getPath(), 1.5D); 
    super.onLivingUpdate();
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (super.attackEntityFrom(source, amount)) {
      EntityLivingBase entitylivingbase = getAttackTarget();
      if (entitylivingbase == null && source.getEntity() instanceof EntityLivingBase)
        entitylivingbase = (EntityLivingBase)source.getEntity(); 
      if (entitylivingbase != null && this.worldObj.getDifficulty() == EnumDifficulty.HARD && this.rand.nextFloat() < getEntityAttribute(reinforcementChance).getAttributeValue()) {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.posY);
        int k = MathHelper.floor_double(this.posZ);
        EntityZombie entityzombie = new EntityZombie(this.worldObj);
        for (int l = 0; l < 50; l++) {
          int i1 = i + MathHelper.getRandomIntegerInRange(this.rand, 7, 40) * MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
          int j1 = j + MathHelper.getRandomIntegerInRange(this.rand, 7, 40) * MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
          int k1 = k + MathHelper.getRandomIntegerInRange(this.rand, 7, 40) * MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
          if (World.doesBlockHaveSolidTopSurface((IBlockAccess)this.worldObj, new BlockPos(i1, j1 - 1, k1)) && this.worldObj.getLightFromNeighbors(new BlockPos(i1, j1, k1)) < 10) {
            entityzombie.setPosition(i1, j1, k1);
            if (!this.worldObj.isAnyPlayerWithinRangeAt(i1, j1, k1, 7.0D) && this.worldObj.checkNoEntityCollision(entityzombie.getEntityBoundingBox(), (Entity)entityzombie) && this.worldObj.getCollidingBoundingBoxes((Entity)entityzombie, entityzombie.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(entityzombie.getEntityBoundingBox())) {
              this.worldObj.spawnEntityInWorld((Entity)entityzombie);
              entityzombie.setAttackTarget(entitylivingbase);
              entityzombie.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos((Entity)entityzombie)), (IEntityLivingData)null);
              getEntityAttribute(reinforcementChance).applyModifier(new AttributeModifier("Zombie reinforcement caller charge", -0.05000000074505806D, 0));
              entityzombie.getEntityAttribute(reinforcementChance).applyModifier(new AttributeModifier("Zombie reinforcement callee charge", -0.05000000074505806D, 0));
              break;
            } 
          } 
        } 
      } 
      return true;
    } 
    return false;
  }
  
  public void onUpdate() {
    if (!this.worldObj.isRemote && isConverting()) {
      int i = getConversionTimeBoost();
      this.conversionTime -= i;
      if (this.conversionTime <= 0)
        convertToVillager(); 
    } 
    super.onUpdate();
  }
  
  public boolean attackEntityAsMob(Entity entityIn) {
    boolean flag = super.attackEntityAsMob(entityIn);
    if (flag) {
      int i = this.worldObj.getDifficulty().getDifficultyId();
      if (getHeldItem() == null && isBurning() && this.rand.nextFloat() < i * 0.3F)
        entityIn.setFire(2 * i); 
    } 
    return flag;
  }
  
  protected String getLivingSound() {
    return "mob.zombie.say";
  }
  
  protected String getHurtSound() {
    return "mob.zombie.hurt";
  }
  
  protected String getDeathSound() {
    return "mob.zombie.death";
  }
  
  protected void playStepSound(BlockPos pos, Block blockIn) {
    playSound("mob.zombie.step", 0.15F, 1.0F);
  }
  
  protected Item getDropItem() {
    return Items.rotten_flesh;
  }
  
  public EnumCreatureAttribute getCreatureAttribute() {
    return EnumCreatureAttribute.UNDEAD;
  }
  
  protected void addRandomDrop() {
    switch (this.rand.nextInt(3)) {
      case 0:
        dropItem(Items.iron_ingot, 1);
        break;
      case 1:
        dropItem(Items.carrot, 1);
        break;
      case 2:
        dropItem(Items.potato, 1);
        break;
    } 
  }
  
  protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
    super.setEquipmentBasedOnDifficulty(difficulty);
    if (this.rand.nextFloat() < ((this.worldObj.getDifficulty() == EnumDifficulty.HARD) ? 0.05F : 0.01F)) {
      int i = this.rand.nextInt(3);
      if (i == 0) {
        setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
      } else {
        setCurrentItemOrArmor(0, new ItemStack(Items.iron_shovel));
      } 
    } 
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound) {
    super.writeEntityToNBT(tagCompound);
    if (isChild())
      tagCompound.setBoolean("IsBaby", true); 
    if (isVillager())
      tagCompound.setBoolean("IsVillager", true); 
    tagCompound.setInteger("ConversionTime", isConverting() ? this.conversionTime : -1);
    tagCompound.setBoolean("CanBreakDoors", isBreakDoorsTaskSet());
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund) {
    super.readEntityFromNBT(tagCompund);
    if (tagCompund.getBoolean("IsBaby"))
      setChild(true); 
    if (tagCompund.getBoolean("IsVillager"))
      setVillager(true); 
    if (tagCompund.hasKey("ConversionTime", 99) && tagCompund.getInteger("ConversionTime") > -1)
      startConversion(tagCompund.getInteger("ConversionTime")); 
    setBreakDoorsAItask(tagCompund.getBoolean("CanBreakDoors"));
  }
  
  public void onKillEntity(EntityLivingBase entityLivingIn) {
    super.onKillEntity(entityLivingIn);
    if ((this.worldObj.getDifficulty() == EnumDifficulty.NORMAL || this.worldObj.getDifficulty() == EnumDifficulty.HARD) && entityLivingIn instanceof EntityVillager) {
      if (this.worldObj.getDifficulty() != EnumDifficulty.HARD && this.rand.nextBoolean())
        return; 
      EntityLiving entityliving = (EntityLiving)entityLivingIn;
      EntityZombie entityzombie = new EntityZombie(this.worldObj);
      entityzombie.copyLocationAndAnglesFrom((Entity)entityLivingIn);
      this.worldObj.removeEntity((Entity)entityLivingIn);
      entityzombie.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos((Entity)entityzombie)), (IEntityLivingData)null);
      entityzombie.setVillager(true);
      if (entityLivingIn.isChild())
        entityzombie.setChild(true); 
      entityzombie.setNoAI(entityliving.isAIDisabled());
      if (entityliving.hasCustomName()) {
        entityzombie.setCustomNameTag(entityliving.getCustomNameTag());
        entityzombie.setAlwaysRenderNameTag(entityliving.getAlwaysRenderNameTag());
      } 
      this.worldObj.spawnEntityInWorld((Entity)entityzombie);
      this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1016, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
    } 
  }
  
  public float getEyeHeight() {
    float f = 1.74F;
    if (isChild())
      f = (float)(f - 0.81D); 
    return f;
  }
  
  protected boolean func_175448_a(ItemStack stack) {
    return (stack.getItem() == Items.egg && isChild() && isRiding()) ? false : super.func_175448_a(stack);
  }
  
  public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
    livingdata = super.onInitialSpawn(difficulty, livingdata);
    float f = difficulty.getClampedAdditionalDifficulty();
    setCanPickUpLoot((this.rand.nextFloat() < 0.55F * f));
    if (livingdata == null)
      livingdata = new GroupData((this.worldObj.rand.nextFloat() < 0.05F), (this.worldObj.rand.nextFloat() < 0.05F)); 
    if (livingdata instanceof GroupData) {
      GroupData entityzombie$groupdata = (GroupData)livingdata;
      if (entityzombie$groupdata.isVillager)
        setVillager(true); 
      if (entityzombie$groupdata.isChild) {
        setChild(true);
        if (this.worldObj.rand.nextFloat() < 0.05D) {
          List<EntityChicken> list = this.worldObj.getEntitiesWithinAABB(EntityChicken.class, getEntityBoundingBox().expand(5.0D, 3.0D, 5.0D), EntitySelectors.IS_STANDALONE);
          if (!list.isEmpty()) {
            EntityChicken entitychicken = list.get(0);
            entitychicken.setChickenJockey(true);
            mountEntity((Entity)entitychicken);
          } 
        } else if (this.worldObj.rand.nextFloat() < 0.05D) {
          EntityChicken entitychicken1 = new EntityChicken(this.worldObj);
          entitychicken1.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
          entitychicken1.onInitialSpawn(difficulty, (IEntityLivingData)null);
          entitychicken1.setChickenJockey(true);
          this.worldObj.spawnEntityInWorld((Entity)entitychicken1);
          mountEntity((Entity)entitychicken1);
        } 
      } 
    } 
    setBreakDoorsAItask((this.rand.nextFloat() < f * 0.1F));
    setEquipmentBasedOnDifficulty(difficulty);
    setEnchantmentBasedOnDifficulty(difficulty);
    if (getEquipmentInSlot(4) == null) {
      Calendar calendar = this.worldObj.getCurrentDate();
      if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F) {
        setCurrentItemOrArmor(4, new ItemStack((this.rand.nextFloat() < 0.1F) ? Blocks.lit_pumpkin : Blocks.pumpkin));
        this.equipmentDropChances[4] = 0.0F;
      } 
    } 
    getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806D, 0));
    double d0 = this.rand.nextDouble() * 1.5D * f;
    if (d0 > 1.0D)
      getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2)); 
    if (this.rand.nextFloat() < f * 0.05F) {
      getEntityAttribute(reinforcementChance).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 0.25D + 0.5D, 0));
      getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 3.0D + 1.0D, 2));
      setBreakDoorsAItask(true);
    } 
    return livingdata;
  }
  
  public boolean interact(EntityPlayer player) {
    ItemStack itemstack = player.getCurrentEquippedItem();
    if (itemstack != null && itemstack.getItem() == Items.golden_apple && itemstack.getMetadata() == 0 && isVillager() && isPotionActive(Potion.weakness)) {
      if (!player.capabilities.isCreativeMode)
        itemstack.stackSize--; 
      if (itemstack.stackSize <= 0)
        player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null); 
      if (!this.worldObj.isRemote)
        startConversion(this.rand.nextInt(2401) + 3600); 
      return true;
    } 
    return false;
  }
  
  protected void startConversion(int ticks) {
    this.conversionTime = ticks;
    getDataWatcher().updateObject(14, Byte.valueOf((byte)1));
    removePotionEffect(Potion.weakness.id);
    addPotionEffect(new PotionEffect(Potion.damageBoost.id, ticks, Math.min(this.worldObj.getDifficulty().getDifficultyId() - 1, 0)));
    this.worldObj.setEntityState((Entity)this, (byte)16);
  }
  
  public void handleStatusUpdate(byte id) {
    if (id == 16) {
      if (!isSilent())
        this.worldObj.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "mob.zombie.remedy", 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false); 
    } else {
      super.handleStatusUpdate(id);
    } 
  }
  
  protected boolean canDespawn() {
    return !isConverting();
  }
  
  public boolean isConverting() {
    return (getDataWatcher().getWatchableObjectByte(14) == 1);
  }
  
  protected void convertToVillager() {
    EntityVillager entityvillager = new EntityVillager(this.worldObj);
    entityvillager.copyLocationAndAnglesFrom((Entity)this);
    entityvillager.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos((Entity)entityvillager)), (IEntityLivingData)null);
    entityvillager.setLookingForHome();
    if (isChild())
      entityvillager.setGrowingAge(-24000); 
    this.worldObj.removeEntity((Entity)this);
    entityvillager.setNoAI(isAIDisabled());
    if (hasCustomName()) {
      entityvillager.setCustomNameTag(getCustomNameTag());
      entityvillager.setAlwaysRenderNameTag(getAlwaysRenderNameTag());
    } 
    this.worldObj.spawnEntityInWorld((Entity)entityvillager);
    entityvillager.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0));
    this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1017, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
  }
  
  protected int getConversionTimeBoost() {
    int i = 1;
    if (this.rand.nextFloat() < 0.01F) {
      int j = 0;
      BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
      for (int k = (int)this.posX - 4; k < (int)this.posX + 4 && j < 14; k++) {
        for (int l = (int)this.posY - 4; l < (int)this.posY + 4 && j < 14; l++) {
          for (int i1 = (int)this.posZ - 4; i1 < (int)this.posZ + 4 && j < 14; i1++) {
            Block block = this.worldObj.getBlockState((BlockPos)blockpos$mutableblockpos.func_181079_c(k, l, i1)).getBlock();
            if (block == Blocks.iron_bars || block == Blocks.bed) {
              if (this.rand.nextFloat() < 0.3F)
                i++; 
              j++;
            } 
          } 
        } 
      } 
    } 
    return i;
  }
  
  public void setChildSize(boolean isChild) {
    multiplySize(isChild ? 0.5F : 1.0F);
  }
  
  protected final void setSize(float width, float height) {
    boolean flag = (this.zombieWidth > 0.0F && this.zombieHeight > 0.0F);
    this.zombieWidth = width;
    this.zombieHeight = height;
    if (!flag)
      multiplySize(1.0F); 
  }
  
  protected final void multiplySize(float size) {
    super.setSize(this.zombieWidth * size, this.zombieHeight * size);
  }
  
  public double getYOffset() {
    return isChild() ? 0.0D : -0.35D;
  }
  
  public void onDeath(DamageSource cause) {
    super.onDeath(cause);
    if (cause.getEntity() instanceof EntityCreeper && !(this instanceof EntityPigZombie) && ((EntityCreeper)cause.getEntity()).getPowered() && ((EntityCreeper)cause.getEntity()).isAIEnabled()) {
      ((EntityCreeper)cause.getEntity()).func_175493_co();
      entityDropItem(new ItemStack(Items.skull, 1, 2), 0.0F);
    } 
  }
  
  class GroupData implements IEntityLivingData {
    public boolean isChild;
    
    public boolean isVillager;
    
    private GroupData(boolean isBaby, boolean isVillagerZombie) {
      this.isChild = false;
      this.isVillager = false;
      this.isChild = isBaby;
      this.isVillager = isVillagerZombie;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\monster\EntityZombie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */