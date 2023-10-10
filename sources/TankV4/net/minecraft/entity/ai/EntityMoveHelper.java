package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.MathHelper;

public class EntityMoveHelper {
   protected double speed;
   protected boolean update;
   protected double posY;
   protected EntityLiving entity;
   protected double posX;
   protected double posZ;

   public void setMoveTo(double var1, double var3, double var5, double var7) {
      this.posX = var1;
      this.posY = var3;
      this.posZ = var5;
      this.speed = var7;
      this.update = true;
   }

   public EntityMoveHelper(EntityLiving var1) {
      this.entity = var1;
      this.posX = var1.posX;
      this.posY = var1.posY;
      this.posZ = var1.posZ;
   }

   public boolean isUpdating() {
      return this.update;
   }

   public double getY() {
      return this.posY;
   }

   public double getSpeed() {
      return this.speed;
   }

   protected float limitAngle(float var1, float var2, float var3) {
      float var4 = MathHelper.wrapAngleTo180_float(var2 - var1);
      if (var4 > var3) {
         var4 = var3;
      }

      if (var4 < -var3) {
         var4 = -var3;
      }

      float var5 = var1 + var4;
      if (var5 < 0.0F) {
         var5 += 360.0F;
      } else if (var5 > 360.0F) {
         var5 -= 360.0F;
      }

      return var5;
   }

   public double getX() {
      return this.posX;
   }

   public void onUpdateMoveHelper() {
      this.entity.setMoveForward(0.0F);
      if (this.update) {
         this.update = false;
         int var1 = MathHelper.floor_double(this.entity.getEntityBoundingBox().minY + 0.5D);
         double var2 = this.posX - this.entity.posX;
         double var4 = this.posZ - this.entity.posZ;
         double var6 = this.posY - (double)var1;
         double var8 = var2 * var2 + var6 * var6 + var4 * var4;
         if (var8 >= 2.500000277905201E-7D) {
            float var10 = (float)(MathHelper.func_181159_b(var4, var2) * 180.0D / 3.141592653589793D) - 90.0F;
            this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, var10, 30.0F);
            this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
            if (var6 > 0.0D && var2 * var2 + var4 * var4 < 1.0D) {
               this.entity.getJumpHelper().setJumping();
            }
         }
      }

   }

   public double getZ() {
      return this.posZ;
   }
}
