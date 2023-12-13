package net.minecraft.entity;

import net.minecraft.util.MathHelper;

public class EntityBodyHelper {
   private int rotationTickCounter;
   private float prevRenderYawHead;
   private EntityLivingBase theLiving;

   public void updateRenderAngles() {
      double var1 = this.theLiving.posX - this.theLiving.prevPosX;
      double var3 = this.theLiving.posZ - this.theLiving.prevPosZ;
      if (var1 * var1 + var3 * var3 > 2.500000277905201E-7D) {
         this.theLiving.renderYawOffset = this.theLiving.rotationYaw;
         this.theLiving.rotationYawHead = this.computeAngleWithBound(this.theLiving.renderYawOffset, this.theLiving.rotationYawHead, 75.0F);
         this.prevRenderYawHead = this.theLiving.rotationYawHead;
         this.rotationTickCounter = 0;
      } else {
         float var5 = 75.0F;
         if (Math.abs(this.theLiving.rotationYawHead - this.prevRenderYawHead) > 15.0F) {
            this.rotationTickCounter = 0;
            this.prevRenderYawHead = this.theLiving.rotationYawHead;
         } else {
            ++this.rotationTickCounter;
            boolean var6 = true;
            if (this.rotationTickCounter > 10) {
               var5 = Math.max(1.0F - (float)(this.rotationTickCounter - 10) / 10.0F, 0.0F) * 75.0F;
            }
         }

         this.theLiving.renderYawOffset = this.computeAngleWithBound(this.theLiving.rotationYawHead, this.theLiving.renderYawOffset, var5);
      }

   }

   private float computeAngleWithBound(float var1, float var2, float var3) {
      float var4 = MathHelper.wrapAngleTo180_float(var1 - var2);
      if (var4 < -var3) {
         var4 = -var3;
      }

      if (var4 >= var3) {
         var4 = var3;
      }

      return var1 - var4;
   }

   public EntityBodyHelper(EntityLivingBase var1) {
      this.theLiving = var1;
   }
}
