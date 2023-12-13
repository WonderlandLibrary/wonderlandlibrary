package my.NewSnake.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class RotationUtil {
   private static Minecraft mc = Minecraft.getMinecraft();

   public static boolean isFacingEntity(EntityLivingBase var0) {
      float var1 = getNeededRotations(var0)[0];
      float var2 = getNeededRotations(var0)[1];
      float var3 = Minecraft.thePlayer.rotationYaw;
      float var4 = Minecraft.thePlayer.rotationPitch;
      float var5 = 21.0F + var0.getCollisionBorderSize();
      if (var3 < 0.0F) {
         var3 += 360.0F;
      }

      if (var4 < 0.0F) {
         var4 += 360.0F;
      }

      if (var1 < 0.0F) {
         var1 += 360.0F;
      }

      if (var2 < 0.0F) {
         var2 += 360.0F;
      }

      if (var3 >= var1 - var5 && var3 <= var1 + var5) {
         return var4 >= var2 - var5 && var4 <= var2 + var5;
      } else {
         return false;
      }
   }

   private static float updateRotation(float var0, float var1, float var2) {
      float var3 = MathHelper.wrapAngleTo180_float(var1 - var0);
      if (var3 > var2) {
         var3 = var2;
      }

      if (var3 < -var2) {
         var3 = -var2;
      }

      return var0 + var3;
   }

   public static float[] getRotations(EntityLivingBase var0, float var1) {
      float var2 = updateRotation(Minecraft.thePlayer.rotationYaw, getNeededRotations(var0)[0], var1);
      float var3 = updateRotation(Minecraft.thePlayer.rotationPitch, getNeededRotations(var0)[1], var1);
      return new float[]{var2, var3};
   }

   public static float getAngleChange(EntityLivingBase var0) {
      float var1 = getNeededRotations(var0)[0];
      float var2 = getNeededRotations(var0)[1];
      float var3 = Minecraft.thePlayer.rotationYaw;
      float var4 = Minecraft.thePlayer.rotationPitch;
      if (var3 < 0.0F) {
         var3 += 360.0F;
      }

      if (var4 < 0.0F) {
         var4 += 360.0F;
      }

      if (var1 < 0.0F) {
         var1 += 360.0F;
      }

      if (var2 < 0.0F) {
         var2 += 360.0F;
      }

      float var5 = Math.max(var3, var1) - Math.min(var3, var1);
      float var6 = Math.max(var4, var2) - Math.min(var4, var2);
      return var5 + var6;
   }

   public static float[] getNeededRotations(EntityLivingBase var0) {
      double var1 = var0.posX - Minecraft.thePlayer.posX;
      double var3 = var0.posZ - Minecraft.thePlayer.posZ;
      double var5 = var0.posY + (double)var0.getEyeHeight() - (Minecraft.thePlayer.getEntityBoundingBox().minY + (double)Minecraft.thePlayer.getEyeHeight());
      double var7 = (double)MathHelper.sqrt_double(var1 * var1 + var3 * var3);
      float var9 = (float)(MathHelper.func_181159_b(var3, var1) * 180.0D / 3.141592653589793D) - 90.0F;
      float var10 = (float)(-(MathHelper.func_181159_b(var5, var7) * 180.0D / 3.141592653589793D));
      return new float[]{var9, var10};
   }
}
