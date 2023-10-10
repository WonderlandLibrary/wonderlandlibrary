package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class EntityLookHelper {
   private boolean isLooking;
   private double posY;
   private double posZ;
   private float deltaLookYaw;
   private double posX;
   private float deltaLookPitch;
   private EntityLiving entity;

   public boolean getIsLooking() {
      return this.isLooking;
   }

   public void onUpdateLook() {
      this.entity.rotationPitch = 0.0F;
      if (this.isLooking) {
         this.isLooking = false;
         double var1 = this.posX - this.entity.posX;
         double var3 = this.posY - (this.entity.posY + (double)this.entity.getEyeHeight());
         double var5 = this.posZ - this.entity.posZ;
         double var7 = (double)MathHelper.sqrt_double(var1 * var1 + var5 * var5);
         float var9 = (float)(MathHelper.func_181159_b(var5, var1) * 180.0D / 3.141592653589793D) - 90.0F;
         float var10 = (float)(-(MathHelper.func_181159_b(var3, var7) * 180.0D / 3.141592653589793D));
         this.entity.rotationPitch = this.updateRotation(this.entity.rotationPitch, var10, this.deltaLookPitch);
         this.entity.rotationYawHead = this.updateRotation(this.entity.rotationYawHead, var9, this.deltaLookYaw);
      } else {
         this.entity.rotationYawHead = this.updateRotation(this.entity.rotationYawHead, this.entity.renderYawOffset, 10.0F);
      }

      float var11 = MathHelper.wrapAngleTo180_float(this.entity.rotationYawHead - this.entity.renderYawOffset);
      if (!this.entity.getNavigator().noPath()) {
         if (var11 < -75.0F) {
            this.entity.rotationYawHead = this.entity.renderYawOffset - 75.0F;
         }

         if (var11 > 75.0F) {
            this.entity.rotationYawHead = this.entity.renderYawOffset + 75.0F;
         }
      }

   }

   public EntityLookHelper(EntityLiving var1) {
      this.entity = var1;
   }

   public void setLookPositionWithEntity(Entity var1, float var2, float var3) {
      this.posX = var1.posX;
      if (var1 instanceof EntityLivingBase) {
         this.posY = var1.posY + (double)var1.getEyeHeight();
      } else {
         this.posY = (var1.getEntityBoundingBox().minY + var1.getEntityBoundingBox().maxY) / 2.0D;
      }

      this.posZ = var1.posZ;
      this.deltaLookYaw = var2;
      this.deltaLookPitch = var3;
      this.isLooking = true;
   }

   public double getLookPosY() {
      return this.posY;
   }

   private float updateRotation(float var1, float var2, float var3) {
      float var4 = MathHelper.wrapAngleTo180_float(var2 - var1);
      if (var4 > var3) {
         var4 = var3;
      }

      if (var4 < -var3) {
         var4 = -var3;
      }

      return var1 + var4;
   }

   public double getLookPosX() {
      return this.posX;
   }

   public void setLookPosition(double var1, double var3, double var5, float var7, float var8) {
      this.posX = var1;
      this.posY = var3;
      this.posZ = var5;
      this.deltaLookYaw = var7;
      this.deltaLookPitch = var8;
      this.isLooking = true;
   }

   public double getLookPosZ() {
      return this.posZ;
   }
}
