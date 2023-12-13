package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityAnimal extends EntityAgeable implements IAnimals {
   private int inLove;
   protected Block spawnableBlock;
   private EntityPlayer playerInLove;

   public int getTalkInterval() {
      return 120;
   }

   public void resetInLove() {
      this.inLove = 0;
   }

   protected boolean canDespawn() {
      return false;
   }

   public void setInLove(EntityPlayer var1) {
      this.inLove = 600;
      this.playerInLove = var1;
      this.worldObj.setEntityState(this, (byte)18);
   }

   protected void consumeItemFromStack(EntityPlayer var1, ItemStack var2) {
      if (!var1.capabilities.isCreativeMode) {
         --var2.stackSize;
         if (var2.stackSize <= 0) {
            var1.inventory.setInventorySlotContents(var1.inventory.currentItem, (ItemStack)null);
         }
      }

   }

   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(var1)) {
         return false;
      } else {
         this.inLove = 0;
         return super.attackEntityFrom(var1, var2);
      }
   }

   public boolean canMateWith(EntityAnimal var1) {
      return var1 == this ? false : (var1.getClass() != this.getClass() ? false : this != false && var1 != false);
   }

   public float getBlockPathWeight(BlockPos var1) {
      return this.worldObj.getBlockState(var1.down()).getBlock() == Blocks.grass ? 10.0F : this.worldObj.getLightBrightness(var1) - 0.5F;
   }

   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
      var1.setInteger("InLove", this.inLove);
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      if (this.getGrowingAge() != 0) {
         this.inLove = 0;
      }

      if (this.inLove > 0) {
         --this.inLove;
         if (this.inLove % 10 == 0) {
            double var1 = this.rand.nextGaussian() * 0.02D;
            double var3 = this.rand.nextGaussian() * 0.02D;
            double var5 = this.rand.nextGaussian() * 0.02D;
            this.worldObj.spawnParticle(EnumParticleTypes.HEART, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, var1, var3, var5);
         }
      }

   }

   public boolean interact(EntityPlayer var1) {
      ItemStack var2 = var1.inventory.getCurrentItem();
      if (var2 != null) {
         if (var2 == null && this.getGrowingAge() == 0 && this.inLove <= 0) {
            this.consumeItemFromStack(var1, var2);
            this.setInLove(var1);
            return true;
         }

         if (this.isChild() && var2 == null) {
            this.consumeItemFromStack(var1, var2);
            this.func_175501_a((int)((float)(-this.getGrowingAge() / 20) * 0.1F), true);
            return true;
         }
      }

      return super.interact(var1);
   }

   public boolean getCanSpawnHere() {
      int var1 = MathHelper.floor_double(this.posX);
      int var2 = MathHelper.floor_double(this.getEntityBoundingBox().minY);
      int var3 = MathHelper.floor_double(this.posZ);
      BlockPos var4 = new BlockPos(var1, var2, var3);
      return this.worldObj.getBlockState(var4.down()).getBlock() == this.spawnableBlock && this.worldObj.getLight(var4) > 8 && super.getCanSpawnHere();
   }

   public EntityPlayer getPlayerInLove() {
      return this.playerInLove;
   }

   public EntityAnimal(World var1) {
      super(var1);
      this.spawnableBlock = Blocks.grass;
   }

   protected void updateAITasks() {
      if (this.getGrowingAge() != 0) {
         this.inLove = 0;
      }

      super.updateAITasks();
   }

   public void handleStatusUpdate(byte var1) {
      if (var1 == 18) {
         for(int var2 = 0; var2 < 7; ++var2) {
            double var3 = this.rand.nextGaussian() * 0.02D;
            double var5 = this.rand.nextGaussian() * 0.02D;
            double var7 = this.rand.nextGaussian() * 0.02D;
            this.worldObj.spawnParticle(EnumParticleTypes.HEART, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, var3, var5, var7);
         }
      } else {
         super.handleStatusUpdate(var1);
      }

   }

   protected int getExperiencePoints(EntityPlayer var1) {
      return 1 + this.worldObj.rand.nextInt(3);
   }

   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
      this.inLove = var1.getInteger("InLove");
   }
}
