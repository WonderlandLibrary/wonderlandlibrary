package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
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
   public int timeUntilNextEgg;
   public boolean chickenJockey;
   public float wingRotation;
   public float field_70888_h;
   public float field_70884_g;
   public float wingRotDelta = 1.0F;
   public float destPos;

   public EntityAgeable createChild(EntityAgeable var1) {
      return this.createChild(var1);
   }

   public EntityChicken createChild(EntityAgeable var1) {
      return new EntityChicken(this.worldObj);
   }

   protected Item getDropItem() {
      return Items.feather;
   }

   public float getEyeHeight() {
      return this.height;
   }

   protected String getHurtSound() {
      return "mob.chicken.hurt";
   }

   protected String getDeathSound() {
      return "mob.chicken.hurt";
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      this.field_70888_h = this.wingRotation;
      this.field_70884_g = this.destPos;
      this.destPos = (float)((double)this.destPos + (double)(this.onGround ? -1 : 4) * 0.3D);
      this.destPos = MathHelper.clamp_float(this.destPos, 0.0F, 1.0F);
      if (!this.onGround && this.wingRotDelta < 1.0F) {
         this.wingRotDelta = 1.0F;
      }

      this.wingRotDelta = (float)((double)this.wingRotDelta * 0.9D);
      if (!this.onGround && this.motionY < 0.0D) {
         this.motionY *= 0.6D;
      }

      this.wingRotation += this.wingRotDelta * 2.0F;
      if (!this.worldObj.isRemote && !this.isChild() && !this.isChickenJockey() && --this.timeUntilNextEgg <= 0) {
         this.playSound("mob.chicken.plop", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
         this.dropItem(Items.egg, 1);
         this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
      }

   }

   protected String getLivingSound() {
      return "mob.chicken.say";
   }

   public boolean isChickenJockey() {
      return this.chickenJockey;
   }

   protected void dropFewItems(boolean var1, int var2) {
      int var3 = this.rand.nextInt(3) + this.rand.nextInt(1 + var2);

      for(int var4 = 0; var4 < var3; ++var4) {
         this.dropItem(Items.feather, 1);
      }

      if (this.isBurning()) {
         this.dropItem(Items.cooked_chicken, 1);
      } else {
         this.dropItem(Items.chicken, 1);
      }

   }

   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
      var1.setBoolean("IsChickenJockey", this.chickenJockey);
      var1.setInteger("EggLayTime", this.timeUntilNextEgg);
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
   }

   public void setChickenJockey(boolean var1) {
      this.chickenJockey = var1;
   }

   public void updateRiderPosition() {
      super.updateRiderPosition();
      float var1 = MathHelper.sin(this.renderYawOffset * 3.1415927F / 180.0F);
      float var2 = MathHelper.cos(this.renderYawOffset * 3.1415927F / 180.0F);
      float var3 = 0.1F;
      float var4 = 0.0F;
      this.riddenByEntity.setPosition(this.posX + (double)(var3 * var1), this.posY + (double)(this.height * 0.5F) + this.riddenByEntity.getYOffset() + (double)var4, this.posZ - (double)(var3 * var2));
      if (this.riddenByEntity instanceof EntityLivingBase) {
         ((EntityLivingBase)this.riddenByEntity).renderYawOffset = this.renderYawOffset;
      }

   }

   public boolean isBreedingItem(ItemStack var1) {
      return var1 != null && var1.getItem() == Items.wheat_seeds;
   }

   public EntityChicken(World var1) {
      super(var1);
      this.setSize(0.4F, 0.7F);
      this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new EntityAIPanic(this, 1.4D));
      this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
      this.tasks.addTask(3, new EntityAITempt(this, 1.0D, Items.wheat_seeds, false));
      this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
      this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
      this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      this.tasks.addTask(7, new EntityAILookIdle(this));
   }

   public void fall(float var1, float var2) {
   }

   protected void playStepSound(BlockPos var1, Block var2) {
      this.playSound("mob.chicken.step", 0.15F, 1.0F);
   }

   protected boolean canDespawn() {
      return this.isChickenJockey() && this.riddenByEntity == null;
   }

   protected int getExperiencePoints(EntityPlayer var1) {
      return this.isChickenJockey() ? 10 : super.getExperiencePoints(var1);
   }

   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
      this.chickenJockey = var1.getBoolean("IsChickenJockey");
      if (var1.hasKey("EggLayTime")) {
         this.timeUntilNextEgg = var1.getInteger("EggLayTime");
      }

   }
}
