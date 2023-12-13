package my.NewSnake.utils;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class RotationUtils {
   public static float getDistanceBetweenAngles(float var0, float var1) {
      float var2 = Math.abs(var0 - var1) % 360.0F;
      if (var2 > 180.0F) {
         var2 = 360.0F - var2;
      }

      return var2;
   }

   public static float[] getRotationsA(EntityLivingBase var0) {
      double var1 = var0.posX + (double)(var0.getEyeHeight() / 2.0F) - 0.5D;
      double var3 = var0.posZ + (double)(var0.getEyeHeight() / 2.0F) - 0.5D;
      double var5 = var0.posY + (double)(var0.getEyeHeight() / 2.0F) - 0.5D;
      return getRotationFromPositionA(var1, var3, var5);
   }

   public static float getTrajAngleSolutionLow(float var0, float var1, float var2) {
      float var3 = 0.006F;
      float var4 = var2 * var2 * var2 * var2 - 0.006F * (0.006F * var0 * var0 + 2.0F * var1 * var2 * var2);
      return (float)Math.toDegrees(Math.atan(((double)(var2 * var2) - Math.sqrt((double)var4)) / (double)(0.006F * var0)));
   }

   public static float changeRotation(float var0, float var1) {
      float var2 = MathHelper.wrapAngleTo180_float(var1 - var0);
      if (var2 > 1000.0F) {
         var2 = 1000.0F;
      }

      if (var2 < -1000.0F) {
         var2 = -1000.0F;
      }

      return var0 + var2;
   }

   public static float[] getRotations2(Entity var0) {
      double var10000 = var0.posX;
      ClientUtils.mc();
      double var3 = var10000 - Minecraft.thePlayer.posX;
      var10000 = var0.posZ;
      ClientUtils.mc();
      double var5 = var10000 - Minecraft.thePlayer.posZ;
      double var1;
      if (var0 instanceof EntityLivingBase) {
         EntityLivingBase var7 = (EntityLivingBase)var0;
         var10000 = var7.posY + (double)var7.getEyeHeight();
         ClientUtils.mc();
         var10000 -= Minecraft.thePlayer.posY;
         ClientUtils.mc();
         var1 = var10000 + (double)Minecraft.thePlayer.getEyeHeight();
      } else {
         var10000 = (var0.getEntityBoundingBox().minY + var0.getEntityBoundingBox().maxY) / 2.0D;
         ClientUtils.mc();
         var10000 -= Minecraft.thePlayer.posY;
         ClientUtils.mc();
         var1 = var10000 + (double)Minecraft.thePlayer.getEyeHeight();
      }

      double var13 = (double)MathHelper.sqrt_double(var3 * var3 + var5 * var5);
      float var9 = (float)(Math.atan2(var5, var3) * 180.0D / 3.141592653589793D) - 90.0F;
      float var10 = (float)(-(Math.atan2(var1 - (var0 instanceof EntityPlayer ? 0.25D : 0.0D), var13) * 180.0D / 3.141592653589793D));
      ClientUtils.mc();
      float var11 = changeRotation(Minecraft.thePlayer.rotationPitch, var10);
      ClientUtils.mc();
      float var12 = changeRotation(Minecraft.thePlayer.rotationYaw, var9);
      return new float[]{var12, var11};
   }

   public static float[] getRotationFromPositionA(double var0, double var2, double var4) {
      Minecraft.getMinecraft();
      double var6 = var0 - Minecraft.thePlayer.posX - 0.6D;
      Minecraft.getMinecraft();
      double var8 = var2 - Minecraft.thePlayer.posZ - 0.6D;
      Minecraft.getMinecraft();
      double var10 = var4 - Minecraft.thePlayer.posY - 0.6D;
      double var12 = (double)MathHelper.sqrt_double(var6 * var6 + var8 * var8);
      float var14 = (float)(Math.atan2(var8, var6) * 180.0D / 3.141592653589793D) - 90.0F;
      float var15 = (float)(-(Math.atan2(var10, var12) * 180.0D / 3.141592653589793D));
      return new float[]{var14, var15};
   }

   public static float[] getRotationFromPosition(double var0, double var2, double var4) {
      Minecraft.getMinecraft();
      double var6 = var0 - Minecraft.thePlayer.posX;
      Minecraft.getMinecraft();
      double var8 = var2 - Minecraft.thePlayer.posZ;
      Minecraft.getMinecraft();
      double var10 = var4 - Minecraft.thePlayer.posY - 0.6D;
      double var12 = (double)MathHelper.sqrt_double(var6 * var6 + var8 * var8);
      float var14 = (float)(Math.atan2(var8, var6) * 180.0D / 3.141592653589793D) - 90.0F;
      float var15 = (float)(-(Math.atan2(var10, var12) * 180.0D / 3.141592653589793D));
      return new float[]{var14, var15};
   }

   public static float getNewAngle(float var0) {
      var0 %= 360.0F;
      if (var0 >= 180.0F) {
         var0 -= 360.0F;
      }

      if (var0 < -180.0F) {
         var0 += 360.0F;
      }

      return var0;
   }

   public static float[] getAverageRotations(List var0) {
      double var1 = 0.0D;
      double var3 = 0.0D;
      double var5 = 0.0D;

      Entity var7;
      for(Iterator var8 = var0.iterator(); var8.hasNext(); var5 += var7.posZ) {
         var7 = (Entity)var8.next();
         var1 += var7.posX;
         var3 += var7.boundingBox.maxY - 2.0D;
      }

      var1 /= (double)var0.size();
      var3 /= (double)var0.size();
      var5 /= (double)var0.size();
      return new float[]{getRotationFromPosition(var1, var5, var3)[0], getRotationFromPosition(var1, var5, var3)[1]};
   }

   public static float[] getRotations(EntityLivingBase var0) {
      double var1 = var0.posX;
      double var3 = var0.posZ;
      double var5 = var0.posY + (double)(var0.getEyeHeight() / 2.0F) - 0.5D;
      return getRotationFromPosition(var1, var3, var5);
   }
}
