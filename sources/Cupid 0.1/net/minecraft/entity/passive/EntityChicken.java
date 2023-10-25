package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityChicken extends EntityAnimal {
  public float wingRotation;
  
  public float destPos;
  
  public float field_70884_g;
  
  public float field_70888_h;
  
  public float wingRotDelta = 1.0F;
  
  public int timeUntilNextEgg;
  
  public boolean chickenJockey;
  
  public EntityChicken(World worldIn) {
    super(worldIn);
    setSize(0.4F, 0.7F);
    this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
    this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(1, (EntityAIBase)new EntityAIPanic((EntityCreature)this, 1.4D));
    this.tasks.addTask(2, (EntityAIBase)new EntityAIMate(this, 1.0D));
    this.tasks.addTask(3, (EntityAIBase)new EntityAITempt((EntityCreature)this, 1.0D, Items.wheat_seeds, false));
    this.tasks.addTask(4, (EntityAIBase)new EntityAIFollowParent(this, 1.1D));
    this.tasks.addTask(5, (EntityAIBase)new EntityAIWander((EntityCreature)this, 1.0D));
    this.tasks.addTask(6, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 6.0F));
    this.tasks.addTask(7, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
  }
  
  public float getEyeHeight() {
    return this.height;
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0D);
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
  }
  
  public void onLivingUpdate() {
    super.onLivingUpdate();
    this.field_70888_h = this.wingRotation;
    this.field_70884_g = this.destPos;
    this.destPos = (float)(this.destPos + (this.onGround ? -1 : 4) * 0.3D);
    this.destPos = MathHelper.clamp_float(this.destPos, 0.0F, 1.0F);
    if (!this.onGround && this.wingRotDelta < 1.0F)
      this.wingRotDelta = 1.0F; 
    this.wingRotDelta = (float)(this.wingRotDelta * 0.9D);
    if (!this.onGround && this.motionY < 0.0D)
      this.motionY *= 0.6D; 
    this.wingRotation += this.wingRotDelta * 2.0F;
    if (!this.worldObj.isRemote && !isChild() && !isChickenJockey() && --this.timeUntilNextEgg <= 0) {
      playSound("mob.chicken.plop", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      dropItem(Items.egg, 1);
      this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
    } 
  }
  
  public void fall(float distance, float damageMultiplier) {}
  
  protected String getLivingSound() {
    return "mob.chicken.say";
  }
  
  protected String getHurtSound() {
    return "mob.chicken.hurt";
  }
  
  protected String getDeathSound() {
    return "mob.chicken.hurt";
  }
  
  protected void playStepSound(BlockPos pos, Block blockIn) {
    playSound("mob.chicken.step", 0.15F, 1.0F);
  }
  
  protected Item getDropItem() {
    return Items.feather;
  }
  
  protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
    int i = this.rand.nextInt(3) + this.rand.nextInt(1 + p_70628_2_);
    for (int j = 0; j < i; j++)
      dropItem(Items.feather, 1); 
    if (isBurning()) {
      dropItem(Items.cooked_chicken, 1);
    } else {
      dropItem(Items.chicken, 1);
    } 
  }
  
  public EntityChicken createChild(EntityAgeable ageable) {
    return new EntityChicken(this.worldObj);
  }
  
  public boolean isBreedingItem(ItemStack stack) {
    return (stack != null && stack.getItem() == Items.wheat_seeds);
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund) {
    super.readEntityFromNBT(tagCompund);
    this.chickenJockey = tagCompund.getBoolean("IsChickenJockey");
    if (tagCompund.hasKey("EggLayTime"))
      this.timeUntilNextEgg = tagCompund.getInteger("EggLayTime"); 
  }
  
  protected int getExperiencePoints(EntityPlayer player) {
    return isChickenJockey() ? 10 : super.getExperiencePoints(player);
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound) {
    super.writeEntityToNBT(tagCompound);
    tagCompound.setBoolean("IsChickenJockey", this.chickenJockey);
    tagCompound.setInteger("EggLayTime", this.timeUntilNextEgg);
  }
  
  protected boolean canDespawn() {
    return (isChickenJockey() && this.riddenByEntity == null);
  }
  
  public void updateRiderPosition() {
    super.updateRiderPosition();
    float f = MathHelper.sin(this.renderYawOffset * 3.1415927F / 180.0F);
    float f1 = MathHelper.cos(this.renderYawOffset * 3.1415927F / 180.0F);
    float f2 = 0.1F;
    float f3 = 0.0F;
    this.riddenByEntity.setPosition(this.posX + (f2 * f), this.posY + (this.height * 0.5F) + this.riddenByEntity.getYOffset() + f3, this.posZ - (f2 * f1));
    if (this.riddenByEntity instanceof EntityLivingBase)
      ((EntityLivingBase)this.riddenByEntity).renderYawOffset = this.renderYawOffset; 
  }
  
  public boolean isChickenJockey() {
    return this.chickenJockey;
  }
  
  public void setChickenJockey(boolean jockey) {
    this.chickenJockey = jockey;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\passive\EntityChicken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */