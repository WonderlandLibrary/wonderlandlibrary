package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIOcelotAttack;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityOcelot extends EntityTameable {
  private EntityAIAvoidEntity<EntityPlayer> avoidEntity;
  
  private EntityAITempt aiTempt;
  
  public EntityOcelot(World worldIn) {
    super(worldIn);
    setSize(0.6F, 0.7F);
    ((PathNavigateGround)getNavigator()).setAvoidsWater(true);
    this.tasks.addTask(1, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(2, (EntityAIBase)this.aiSit);
    this.tasks.addTask(3, (EntityAIBase)(this.aiTempt = new EntityAITempt((EntityCreature)this, 0.6D, Items.fish, true)));
    this.tasks.addTask(5, (EntityAIBase)new EntityAIFollowOwner(this, 1.0D, 10.0F, 5.0F));
    this.tasks.addTask(6, (EntityAIBase)new EntityAIOcelotSit(this, 0.8D));
    this.tasks.addTask(7, (EntityAIBase)new EntityAILeapAtTarget((EntityLiving)this, 0.3F));
    this.tasks.addTask(8, (EntityAIBase)new EntityAIOcelotAttack((EntityLiving)this));
    this.tasks.addTask(9, (EntityAIBase)new EntityAIMate(this, 0.8D));
    this.tasks.addTask(10, (EntityAIBase)new EntityAIWander((EntityCreature)this, 0.8D));
    this.tasks.addTask(11, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 10.0F));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAITargetNonTamed(this, EntityChicken.class, false, (Predicate)null));
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataWatcher.addObject(18, Byte.valueOf((byte)0));
  }
  
  public void updateAITasks() {
    if (getMoveHelper().isUpdating()) {
      double d0 = getMoveHelper().getSpeed();
      if (d0 == 0.6D) {
        setSneaking(true);
        setSprinting(false);
      } else if (d0 == 1.33D) {
        setSneaking(false);
        setSprinting(true);
      } else {
        setSneaking(false);
        setSprinting(false);
      } 
    } else {
      setSneaking(false);
      setSprinting(false);
    } 
  }
  
  protected boolean canDespawn() {
    return (!isTamed() && this.ticksExisted > 2400);
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
  }
  
  public void fall(float distance, float damageMultiplier) {}
  
  public void writeEntityToNBT(NBTTagCompound tagCompound) {
    super.writeEntityToNBT(tagCompound);
    tagCompound.setInteger("CatType", getTameSkin());
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund) {
    super.readEntityFromNBT(tagCompund);
    setTameSkin(tagCompund.getInteger("CatType"));
  }
  
  protected String getLivingSound() {
    return isTamed() ? (isInLove() ? "mob.cat.purr" : ((this.rand.nextInt(4) == 0) ? "mob.cat.purreow" : "mob.cat.meow")) : "";
  }
  
  protected String getHurtSound() {
    return "mob.cat.hitt";
  }
  
  protected String getDeathSound() {
    return "mob.cat.hitt";
  }
  
  protected float getSoundVolume() {
    return 0.4F;
  }
  
  protected Item getDropItem() {
    return Items.leather;
  }
  
  public boolean attackEntityAsMob(Entity entityIn) {
    return entityIn.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), 3.0F);
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    this.aiSit.setSitting(false);
    return super.attackEntityFrom(source, amount);
  }
  
  protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {}
  
  public boolean interact(EntityPlayer player) {
    ItemStack itemstack = player.inventory.getCurrentItem();
    if (isTamed()) {
      if (isOwner((EntityLivingBase)player) && !this.worldObj.isRemote && !isBreedingItem(itemstack))
        this.aiSit.setSitting(!isSitting()); 
    } else if (this.aiTempt.isRunning() && itemstack != null && itemstack.getItem() == Items.fish && player.getDistanceSqToEntity((Entity)this) < 9.0D) {
      if (!player.capabilities.isCreativeMode)
        itemstack.stackSize--; 
      if (itemstack.stackSize <= 0)
        player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null); 
      if (!this.worldObj.isRemote)
        if (this.rand.nextInt(3) == 0) {
          setTamed(true);
          setTameSkin(1 + this.worldObj.rand.nextInt(3));
          setOwnerId(player.getUniqueID().toString());
          playTameEffect(true);
          this.aiSit.setSitting(true);
          this.worldObj.setEntityState((Entity)this, (byte)7);
        } else {
          playTameEffect(false);
          this.worldObj.setEntityState((Entity)this, (byte)6);
        }  
      return true;
    } 
    return super.interact(player);
  }
  
  public EntityOcelot createChild(EntityAgeable ageable) {
    EntityOcelot entityocelot = new EntityOcelot(this.worldObj);
    if (isTamed()) {
      entityocelot.setOwnerId(getOwnerId());
      entityocelot.setTamed(true);
      entityocelot.setTameSkin(getTameSkin());
    } 
    return entityocelot;
  }
  
  public boolean isBreedingItem(ItemStack stack) {
    return (stack != null && stack.getItem() == Items.fish);
  }
  
  public boolean canMateWith(EntityAnimal otherAnimal) {
    if (otherAnimal == this)
      return false; 
    if (!isTamed())
      return false; 
    if (!(otherAnimal instanceof EntityOcelot))
      return false; 
    EntityOcelot entityocelot = (EntityOcelot)otherAnimal;
    return !entityocelot.isTamed() ? false : ((isInLove() && entityocelot.isInLove()));
  }
  
  public int getTameSkin() {
    return this.dataWatcher.getWatchableObjectByte(18);
  }
  
  public void setTameSkin(int skinId) {
    this.dataWatcher.updateObject(18, Byte.valueOf((byte)skinId));
  }
  
  public boolean getCanSpawnHere() {
    return (this.worldObj.rand.nextInt(3) != 0);
  }
  
  public boolean isNotColliding() {
    if (this.worldObj.checkNoEntityCollision(getEntityBoundingBox(), (Entity)this) && this.worldObj.getCollidingBoundingBoxes((Entity)this, getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(getEntityBoundingBox())) {
      BlockPos blockpos = new BlockPos(this.posX, (getEntityBoundingBox()).minY, this.posZ);
      if (blockpos.getY() < this.worldObj.func_181545_F())
        return false; 
      Block block = this.worldObj.getBlockState(blockpos.down()).getBlock();
      if (block == Blocks.grass || block.getMaterial() == Material.leaves)
        return true; 
    } 
    return false;
  }
  
  public String getName() {
    return hasCustomName() ? getCustomNameTag() : (isTamed() ? StatCollector.translateToLocal("entity.Cat.name") : super.getName());
  }
  
  public void setTamed(boolean tamed) {
    super.setTamed(tamed);
  }
  
  protected void setupTamedAI() {
    if (this.avoidEntity == null)
      this.avoidEntity = new EntityAIAvoidEntity((EntityCreature)this, EntityPlayer.class, 16.0F, 0.8D, 1.33D); 
    this.tasks.removeTask((EntityAIBase)this.avoidEntity);
    if (!isTamed())
      this.tasks.addTask(4, (EntityAIBase)this.avoidEntity); 
  }
  
  public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
    livingdata = super.onInitialSpawn(difficulty, livingdata);
    if (this.worldObj.rand.nextInt(7) == 0)
      for (int i = 0; i < 2; i++) {
        EntityOcelot entityocelot = new EntityOcelot(this.worldObj);
        entityocelot.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
        entityocelot.setGrowingAge(-24000);
        this.worldObj.spawnEntityInWorld((Entity)entityocelot);
      }  
    return livingdata;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\passive\EntityOcelot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */