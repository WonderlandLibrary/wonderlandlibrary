package net.minecraft.entity.item;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityItem extends Entity {
  private static final Logger logger = LogManager.getLogger();
  
  private int age;
  
  private int delayBeforeCanPickup;
  
  private int health;
  
  private String thrower;
  
  private String owner;
  
  public float hoverStart;
  
  public EntityItem(World worldIn, double x, double y, double z) {
    super(worldIn);
    this.health = 5;
    this.hoverStart = (float)(Math.random() * Math.PI * 2.0D);
    setSize(0.25F, 0.25F);
    setPosition(x, y, z);
    this.rotationYaw = (float)(Math.random() * 360.0D);
    this.motionX = (float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D);
    this.motionY = 0.20000000298023224D;
    this.motionZ = (float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D);
  }
  
  public EntityItem(World worldIn, double x, double y, double z, ItemStack stack) {
    this(worldIn, x, y, z);
    setEntityItemStack(stack);
  }
  
  protected boolean canTriggerWalking() {
    return false;
  }
  
  public EntityItem(World worldIn) {
    super(worldIn);
    this.health = 5;
    this.hoverStart = (float)(Math.random() * Math.PI * 2.0D);
    setSize(0.25F, 0.25F);
    setEntityItemStack(new ItemStack(Blocks.air, 0));
  }
  
  protected void entityInit() {
    getDataWatcher().addObjectByDataType(10, 5);
  }
  
  public void onUpdate() {
    if (getEntityItem() == null) {
      setDead();
    } else {
      super.onUpdate();
      if (this.delayBeforeCanPickup > 0 && this.delayBeforeCanPickup != 32767)
        this.delayBeforeCanPickup--; 
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.motionY -= 0.03999999910593033D;
      this.noClip = pushOutOfBlocks(this.posX, ((getEntityBoundingBox()).minY + (getEntityBoundingBox()).maxY) / 2.0D, this.posZ);
      moveEntity(this.motionX, this.motionY, this.motionZ);
      boolean flag = ((int)this.prevPosX != (int)this.posX || (int)this.prevPosY != (int)this.posY || (int)this.prevPosZ != (int)this.posZ);
      if (flag || this.ticksExisted % 25 == 0) {
        if (this.worldObj.getBlockState(new BlockPos(this)).getBlock().getMaterial() == Material.lava) {
          this.motionY = 0.20000000298023224D;
          this.motionX = ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
          this.motionZ = ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
          playSound("random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
        } 
        if (!this.worldObj.isRemote)
          searchForOtherItemsNearby(); 
      } 
      float f = 0.98F;
      if (this.onGround)
        f = (this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double((getEntityBoundingBox()).minY) - 1, MathHelper.floor_double(this.posZ))).getBlock()).slipperiness * 0.98F; 
      this.motionX *= f;
      this.motionY *= 0.9800000190734863D;
      this.motionZ *= f;
      if (this.onGround)
        this.motionY *= -0.5D; 
      if (this.age != -32768)
        this.age++; 
      handleWaterMovement();
      if (!this.worldObj.isRemote && this.age >= 6000)
        setDead(); 
    } 
  }
  
  private void searchForOtherItemsNearby() {
    for (EntityItem entityitem : this.worldObj.getEntitiesWithinAABB(EntityItem.class, getEntityBoundingBox().expand(0.5D, 0.0D, 0.5D)))
      combineItems(entityitem); 
  }
  
  private boolean combineItems(EntityItem other) {
    if (other == this)
      return false; 
    if (other.isEntityAlive() && isEntityAlive()) {
      ItemStack itemstack = getEntityItem();
      ItemStack itemstack1 = other.getEntityItem();
      if (this.delayBeforeCanPickup != 32767 && other.delayBeforeCanPickup != 32767) {
        if (this.age != -32768 && other.age != -32768) {
          if (itemstack1.getItem() != itemstack.getItem())
            return false; 
          if ((itemstack1.hasTagCompound() ^ itemstack.hasTagCompound()) != 0)
            return false; 
          if (itemstack1.hasTagCompound() && !itemstack1.getTagCompound().equals(itemstack.getTagCompound()))
            return false; 
          if (itemstack1.getItem() == null)
            return false; 
          if (itemstack1.getItem().getHasSubtypes() && itemstack1.getMetadata() != itemstack.getMetadata())
            return false; 
          if (itemstack1.stackSize < itemstack.stackSize)
            return other.combineItems(this); 
          if (itemstack1.stackSize + itemstack.stackSize > itemstack1.getMaxStackSize())
            return false; 
          itemstack1.stackSize += itemstack.stackSize;
          other.delayBeforeCanPickup = Math.max(other.delayBeforeCanPickup, this.delayBeforeCanPickup);
          other.age = Math.min(other.age, this.age);
          other.setEntityItemStack(itemstack1);
          setDead();
          return true;
        } 
        return false;
      } 
      return false;
    } 
    return false;
  }
  
  public void setAgeToCreativeDespawnTime() {
    this.age = 4800;
  }
  
  public boolean handleWaterMovement() {
    if (this.worldObj.handleMaterialAcceleration(getEntityBoundingBox(), Material.water, this)) {
      if (!this.inWater && !this.firstUpdate)
        resetHeight(); 
      this.inWater = true;
    } else {
      this.inWater = false;
    } 
    return this.inWater;
  }
  
  protected void dealFireDamage(int amount) {
    attackEntityFrom(DamageSource.inFire, amount);
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    if (getEntityItem() != null && getEntityItem().getItem() == Items.nether_star && source.isExplosion())
      return false; 
    setBeenAttacked();
    this.health = (int)(this.health - amount);
    if (this.health <= 0)
      setDead(); 
    return false;
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound) {
    tagCompound.setShort("Health", (short)(byte)this.health);
    tagCompound.setShort("Age", (short)this.age);
    tagCompound.setShort("PickupDelay", (short)this.delayBeforeCanPickup);
    if (getThrower() != null)
      tagCompound.setString("Thrower", this.thrower); 
    if (getOwner() != null)
      tagCompound.setString("Owner", this.owner); 
    if (getEntityItem() != null)
      tagCompound.setTag("Item", (NBTBase)getEntityItem().writeToNBT(new NBTTagCompound())); 
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund) {
    this.health = tagCompund.getShort("Health") & 0xFF;
    this.age = tagCompund.getShort("Age");
    if (tagCompund.hasKey("PickupDelay"))
      this.delayBeforeCanPickup = tagCompund.getShort("PickupDelay"); 
    if (tagCompund.hasKey("Owner"))
      this.owner = tagCompund.getString("Owner"); 
    if (tagCompund.hasKey("Thrower"))
      this.thrower = tagCompund.getString("Thrower"); 
    NBTTagCompound nbttagcompound = tagCompund.getCompoundTag("Item");
    setEntityItemStack(ItemStack.loadItemStackFromNBT(nbttagcompound));
    if (getEntityItem() == null)
      setDead(); 
  }
  
  public void onCollideWithPlayer(EntityPlayer entityIn) {
    if (!this.worldObj.isRemote) {
      ItemStack itemstack = getEntityItem();
      int i = itemstack.stackSize;
      if (this.delayBeforeCanPickup == 0 && (this.owner == null || 6000 - this.age <= 200 || this.owner.equals(entityIn.getName())) && entityIn.inventory.addItemStackToInventory(itemstack)) {
        if (itemstack.getItem() == Item.getItemFromBlock(Blocks.log))
          entityIn.triggerAchievement((StatBase)AchievementList.mineWood); 
        if (itemstack.getItem() == Item.getItemFromBlock(Blocks.log2))
          entityIn.triggerAchievement((StatBase)AchievementList.mineWood); 
        if (itemstack.getItem() == Items.leather)
          entityIn.triggerAchievement((StatBase)AchievementList.killCow); 
        if (itemstack.getItem() == Items.diamond)
          entityIn.triggerAchievement((StatBase)AchievementList.diamonds); 
        if (itemstack.getItem() == Items.blaze_rod)
          entityIn.triggerAchievement((StatBase)AchievementList.blazeRod); 
        if (itemstack.getItem() == Items.diamond && getThrower() != null) {
          EntityPlayer entityplayer = this.worldObj.getPlayerEntityByName(getThrower());
          if (entityplayer != null && entityplayer != entityIn)
            entityplayer.triggerAchievement((StatBase)AchievementList.diamondsToYou); 
        } 
        if (!isSilent())
          this.worldObj.playSoundAtEntity((Entity)entityIn, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F); 
        entityIn.onItemPickup(this, i);
        if (itemstack.stackSize <= 0)
          setDead(); 
      } 
    } 
  }
  
  public String getName() {
    return hasCustomName() ? getCustomNameTag() : StatCollector.translateToLocal("item." + getEntityItem().getUnlocalizedName());
  }
  
  public boolean canAttackWithItem() {
    return false;
  }
  
  public void travelToDimension(int dimensionId) {
    super.travelToDimension(dimensionId);
    if (!this.worldObj.isRemote)
      searchForOtherItemsNearby(); 
  }
  
  public ItemStack getEntityItem() {
    ItemStack itemstack = getDataWatcher().getWatchableObjectItemStack(10);
    if (itemstack == null) {
      if (this.worldObj != null)
        logger.error("Item entity " + getEntityId() + " has no item?!"); 
      return new ItemStack(Blocks.stone);
    } 
    return itemstack;
  }
  
  public void setEntityItemStack(ItemStack stack) {
    getDataWatcher().updateObject(10, stack);
    getDataWatcher().setObjectWatched(10);
  }
  
  public String getOwner() {
    return this.owner;
  }
  
  public void setOwner(String owner) {
    this.owner = owner;
  }
  
  public String getThrower() {
    return this.thrower;
  }
  
  public void setThrower(String thrower) {
    this.thrower = thrower;
  }
  
  public int getAge() {
    return this.age;
  }
  
  public void setDefaultPickupDelay() {
    this.delayBeforeCanPickup = 10;
  }
  
  public void setNoPickupDelay() {
    this.delayBeforeCanPickup = 0;
  }
  
  public void setInfinitePickupDelay() {
    this.delayBeforeCanPickup = 32767;
  }
  
  public void setPickupDelay(int ticks) {
    this.delayBeforeCanPickup = ticks;
  }
  
  public boolean cannotPickup() {
    return (this.delayBeforeCanPickup > 0);
  }
  
  public void setNoDespawn() {
    this.age = -6000;
  }
  
  public void func_174870_v() {
    setInfinitePickupDelay();
    this.age = 5999;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\item\EntityItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */