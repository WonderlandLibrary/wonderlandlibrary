package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityBat extends EntityAmbientCreature {
   private BlockPos spawnPosition;

   public void onUpdate() {
      super.onUpdate();
      if (this != false) {
         this.motionX = this.motionY = this.motionZ = 0.0D;
         this.posY = (double)MathHelper.floor_double(this.posY) + 1.0D - (double)this.height;
      } else {
         this.motionY *= 0.6000000238418579D;
      }

   }

   public boolean doesEntityNotTriggerPressurePlate() {
      return true;
   }

   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(var1)) {
         return false;
      } else {
         if (!this.worldObj.isRemote && this != false) {
            this.setIsBatHanging(false);
         }

         return super.attackEntityFrom(var1, var2);
      }
   }

   public void fall(float var1, float var2) {
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(6.0D);
   }

   public boolean canBePushed() {
      return false;
   }

   public EntityBat(World var1) {
      super(var1);
      this.setSize(0.5F, 0.9F);
      this.setIsBatHanging(true);
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
      this.dataWatcher.updateObject(16, var1.getByte("BatFlags"));
   }

   protected void updateAITasks() {
      super.updateAITasks();
      BlockPos var1 = new BlockPos(this);
      BlockPos var2 = var1.up();
      if (this != false) {
         if (!this.worldObj.getBlockState(var2).getBlock().isNormalCube()) {
            this.setIsBatHanging(false);
            this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1015, var1, 0);
         } else {
            if (this.rand.nextInt(200) == 0) {
               this.rotationYawHead = (float)this.rand.nextInt(360);
            }

            if (this.worldObj.getClosestPlayerToEntity(this, 4.0D) != null) {
               this.setIsBatHanging(false);
               this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1015, var1, 0);
            }
         }
      } else {
         if (this.spawnPosition != null && (!this.worldObj.isAirBlock(this.spawnPosition) || this.spawnPosition.getY() < 1)) {
            this.spawnPosition = null;
         }

         if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.distanceSq((double)((int)this.posX), (double)((int)this.posY), (double)((int)this.posZ)) < 4.0D) {
            this.spawnPosition = new BlockPos((int)this.posX + this.rand.nextInt(7) - this.rand.nextInt(7), (int)this.posY + this.rand.nextInt(6) - 2, (int)this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7));
         }

         double var3 = (double)this.spawnPosition.getX() + 0.5D - this.posX;
         double var5 = (double)this.spawnPosition.getY() + 0.1D - this.posY;
         double var7 = (double)this.spawnPosition.getZ() + 0.5D - this.posZ;
         this.motionX += (Math.signum(var3) * 0.5D - this.motionX) * 0.10000000149011612D;
         this.motionY += (Math.signum(var5) * 0.699999988079071D - this.motionY) * 0.10000000149011612D;
         this.motionZ += (Math.signum(var7) * 0.5D - this.motionZ) * 0.10000000149011612D;
         float var9 = (float)(MathHelper.func_181159_b(this.motionZ, this.motionX) * 180.0D / 3.141592653589793D) - 90.0F;
         float var10 = MathHelper.wrapAngleTo180_float(var9 - this.rotationYaw);
         this.moveForward = 0.5F;
         this.rotationYaw += var10;
         if (this.rand.nextInt(100) == 0 && this.worldObj.getBlockState(var2).getBlock().isNormalCube()) {
            this.setIsBatHanging(true);
         }
      }

   }

   public float getEyeHeight() {
      return this.height / 2.0F;
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, new Byte((byte)0));
   }

   protected float getSoundVolume() {
      return 0.1F;
   }

   protected void updateFallState(double var1, boolean var3, Block var4, BlockPos var5) {
   }

   protected float getSoundPitch() {
      return super.getSoundPitch() * 0.95F;
   }

   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
      var1.setByte("BatFlags", this.dataWatcher.getWatchableObjectByte(16));
   }

   protected String getDeathSound() {
      return "mob.bat.death";
   }

   protected String getLivingSound() {
      return this != false && this.rand.nextInt(4) != 0 ? null : "mob.bat.idle";
   }

   protected void collideWithEntity(Entity var1) {
   }

   public void setIsBatHanging(boolean var1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      if (var1) {
         this.dataWatcher.updateObject(16, (byte)(var2 | 1));
      } else {
         this.dataWatcher.updateObject(16, (byte)(var2 & -2));
      }

   }

   protected String getHurtSound() {
      return "mob.bat.hurt";
   }

   public boolean getCanSpawnHere() {
      BlockPos var1 = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
      if (var1.getY() >= this.worldObj.func_181545_F()) {
         return false;
      } else {
         int var2 = this.worldObj.getLightFromNeighbors(var1);
         byte var3 = 4;
         if (this == this.worldObj.getCurrentDate()) {
            var3 = 7;
         } else if (this.rand.nextBoolean()) {
            return false;
         }

         return var2 > this.rand.nextInt(var3) ? false : super.getCanSpawnHere();
      }
   }

   protected void collideWithNearbyEntities() {
   }
}
