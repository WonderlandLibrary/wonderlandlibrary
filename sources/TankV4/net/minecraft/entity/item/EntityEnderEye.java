package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityEnderEye extends Entity {
   private double targetY;
   private int despawnTimer;
   private double targetZ;
   private double targetX;
   private boolean shatterOrDrop;

   protected void entityInit() {
   }

   public boolean isInRangeToRenderDist(double var1) {
      double var3 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
      if (Double.isNaN(var3)) {
         var3 = 4.0D;
      }

      var3 *= 64.0D;
      return var1 < var3 * var3;
   }

   public boolean canAttackWithItem() {
      return false;
   }

   public float getBrightness(float var1) {
      return 1.0F;
   }

   public EntityEnderEye(World var1) {
      super(var1);
      this.setSize(0.25F, 0.25F);
   }

   public void moveTowards(BlockPos var1) {
      double var2 = (double)var1.getX();
      int var4 = var1.getY();
      double var5 = (double)var1.getZ();
      double var7 = var2 - this.posX;
      double var9 = var5 - this.posZ;
      float var11 = MathHelper.sqrt_double(var7 * var7 + var9 * var9);
      if (var11 > 12.0F) {
         this.targetX = this.posX + var7 / (double)var11 * 12.0D;
         this.targetZ = this.posZ + var9 / (double)var11 * 12.0D;
         this.targetY = this.posY + 8.0D;
      } else {
         this.targetX = var2;
         this.targetY = (double)var4;
         this.targetZ = var5;
      }

      this.despawnTimer = 0;
      this.shatterOrDrop = this.rand.nextInt(5) > 0;
   }

   public void setVelocity(double var1, double var3, double var5) {
      this.motionX = var1;
      this.motionY = var3;
      this.motionZ = var5;
      if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
         float var7 = MathHelper.sqrt_double(var1 * var1 + var5 * var5);
         this.prevRotationYaw = this.rotationYaw = (float)(MathHelper.func_181159_b(var1, var5) * 180.0D / 3.141592653589793D);
         this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.func_181159_b(var3, (double)var7) * 180.0D / 3.141592653589793D);
      }

   }

   public EntityEnderEye(World var1, double var2, double var4, double var6) {
      super(var1);
      this.despawnTimer = 0;
      this.setSize(0.25F, 0.25F);
      this.setPosition(var2, var4, var6);
   }

   public int getBrightnessForRender(float var1) {
      return 15728880;
   }

   public void writeEntityToNBT(NBTTagCompound var1) {
   }

   public void onUpdate() {
      this.lastTickPosX = this.posX;
      this.lastTickPosY = this.posY;
      this.lastTickPosZ = this.posZ;
      super.onUpdate();
      this.posX += this.motionX;
      this.posY += this.motionY;
      this.posZ += this.motionZ;
      float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
      this.rotationYaw = (float)(MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0D / 3.141592653589793D);

      for(this.rotationPitch = (float)(MathHelper.func_181159_b(this.motionY, (double)var1) * 180.0D / 3.141592653589793D); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
      }

      while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
         this.prevRotationPitch += 360.0F;
      }

      while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
         this.prevRotationYaw -= 360.0F;
      }

      while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
         this.prevRotationYaw += 360.0F;
      }

      this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
      this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
      if (!this.worldObj.isRemote) {
         double var2 = this.targetX - this.posX;
         double var4 = this.targetZ - this.posZ;
         float var6 = (float)Math.sqrt(var2 * var2 + var4 * var4);
         float var7 = (float)MathHelper.func_181159_b(var4, var2);
         double var8 = (double)var1 + (double)(var6 - var1) * 0.0025D;
         if (var6 < 1.0F) {
            var8 *= 0.8D;
            this.motionY *= 0.8D;
         }

         this.motionX = Math.cos((double)var7) * var8;
         this.motionZ = Math.sin((double)var7) * var8;
         if (this.posY < this.targetY) {
            this.motionY += (1.0D - this.motionY) * 0.014999999664723873D;
         } else {
            this.motionY += (-1.0D - this.motionY) * 0.014999999664723873D;
         }
      }

      float var10 = 0.25F;
      if (this.isInWater()) {
         for(int var3 = 0; var3 < 4; ++var3) {
            this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double)var10, this.posY - this.motionY * (double)var10, this.posZ - this.motionZ * (double)var10, this.motionX, this.motionY, this.motionZ);
         }
      } else {
         this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX - this.motionX * (double)var10 + this.rand.nextDouble() * 0.6D - 0.3D, this.posY - this.motionY * (double)var10 - 0.5D, this.posZ - this.motionZ * (double)var10 + this.rand.nextDouble() * 0.6D - 0.3D, this.motionX, this.motionY, this.motionZ);
      }

      if (!this.worldObj.isRemote) {
         this.setPosition(this.posX, this.posY, this.posZ);
         ++this.despawnTimer;
         if (this.despawnTimer > 80 && !this.worldObj.isRemote) {
            this.setDead();
            if (this.shatterOrDrop) {
               this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Items.ender_eye)));
            } else {
               this.worldObj.playAuxSFX(2003, new BlockPos(this), 0);
            }
         }
      }

   }

   public void readEntityFromNBT(NBTTagCompound var1) {
   }
}
