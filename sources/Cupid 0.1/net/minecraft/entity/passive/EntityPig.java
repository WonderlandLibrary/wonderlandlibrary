package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIControlledByPlayer;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityPig extends EntityAnimal {
  private final EntityAIControlledByPlayer aiControlledByPlayer;
  
  public EntityPig(World worldIn) {
    super(worldIn);
    setSize(0.9F, 0.9F);
    ((PathNavigateGround)getNavigator()).setAvoidsWater(true);
    this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(1, (EntityAIBase)new EntityAIPanic((EntityCreature)this, 1.25D));
    this.tasks.addTask(2, (EntityAIBase)(this.aiControlledByPlayer = new EntityAIControlledByPlayer((EntityLiving)this, 0.3F)));
    this.tasks.addTask(3, (EntityAIBase)new EntityAIMate(this, 1.0D));
    this.tasks.addTask(4, (EntityAIBase)new EntityAITempt((EntityCreature)this, 1.2D, Items.carrot_on_a_stick, false));
    this.tasks.addTask(4, (EntityAIBase)new EntityAITempt((EntityCreature)this, 1.2D, Items.carrot, false));
    this.tasks.addTask(5, (EntityAIBase)new EntityAIFollowParent(this, 1.1D));
    this.tasks.addTask(6, (EntityAIBase)new EntityAIWander((EntityCreature)this, 1.0D));
    this.tasks.addTask(7, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 6.0F));
    this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
  }
  
  public boolean canBeSteered() {
    ItemStack itemstack = ((EntityPlayer)this.riddenByEntity).getHeldItem();
    return (itemstack != null && itemstack.getItem() == Items.carrot_on_a_stick);
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound) {
    super.writeEntityToNBT(tagCompound);
    tagCompound.setBoolean("Saddle", getSaddled());
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund) {
    super.readEntityFromNBT(tagCompund);
    setSaddled(tagCompund.getBoolean("Saddle"));
  }
  
  protected String getLivingSound() {
    return "mob.pig.say";
  }
  
  protected String getHurtSound() {
    return "mob.pig.say";
  }
  
  protected String getDeathSound() {
    return "mob.pig.death";
  }
  
  protected void playStepSound(BlockPos pos, Block blockIn) {
    playSound("mob.pig.step", 0.15F, 1.0F);
  }
  
  public boolean interact(EntityPlayer player) {
    if (super.interact(player))
      return true; 
    if (!getSaddled() || this.worldObj.isRemote || (this.riddenByEntity != null && this.riddenByEntity != player))
      return false; 
    player.mountEntity((Entity)this);
    return true;
  }
  
  protected Item getDropItem() {
    return isBurning() ? Items.cooked_porkchop : Items.porkchop;
  }
  
  protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
    int i = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + p_70628_2_);
    for (int j = 0; j < i; j++) {
      if (isBurning()) {
        dropItem(Items.cooked_porkchop, 1);
      } else {
        dropItem(Items.porkchop, 1);
      } 
    } 
    if (getSaddled())
      dropItem(Items.saddle, 1); 
  }
  
  public boolean getSaddled() {
    return ((this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0);
  }
  
  public void setSaddled(boolean saddled) {
    if (saddled) {
      this.dataWatcher.updateObject(16, Byte.valueOf((byte)1));
    } else {
      this.dataWatcher.updateObject(16, Byte.valueOf((byte)0));
    } 
  }
  
  public void onStruckByLightning(EntityLightningBolt lightningBolt) {
    if (!this.worldObj.isRemote && !this.isDead) {
      EntityPigZombie entitypigzombie = new EntityPigZombie(this.worldObj);
      entitypigzombie.setCurrentItemOrArmor(0, new ItemStack(Items.golden_sword));
      entitypigzombie.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
      entitypigzombie.setNoAI(isAIDisabled());
      if (hasCustomName()) {
        entitypigzombie.setCustomNameTag(getCustomNameTag());
        entitypigzombie.setAlwaysRenderNameTag(getAlwaysRenderNameTag());
      } 
      this.worldObj.spawnEntityInWorld((Entity)entitypigzombie);
      setDead();
    } 
  }
  
  public void fall(float distance, float damageMultiplier) {
    super.fall(distance, damageMultiplier);
    if (distance > 5.0F && this.riddenByEntity instanceof EntityPlayer)
      ((EntityPlayer)this.riddenByEntity).triggerAchievement((StatBase)AchievementList.flyPig); 
  }
  
  public EntityPig createChild(EntityAgeable ageable) {
    return new EntityPig(this.worldObj);
  }
  
  public boolean isBreedingItem(ItemStack stack) {
    return (stack != null && stack.getItem() == Items.carrot);
  }
  
  public EntityAIControlledByPlayer getAIControlledByPlayer() {
    return this.aiControlledByPlayer;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\passive\EntityPig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */