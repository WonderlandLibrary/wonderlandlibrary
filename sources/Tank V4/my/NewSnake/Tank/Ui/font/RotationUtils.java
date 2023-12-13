package my.NewSnake.Tank.Ui.font;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class RotationUtils {
   public static RotationUtils.Rotation serverRotation = new RotationUtils.Rotation(0.0F, 0.0F);
   public static Minecraft mc = Minecraft.getMinecraft();

   public static Vec3 getVectorForRotation(float var0, float var1) {
      float var2 = MathHelper.cos(-var0 * 0.017453292F - 3.1415927F);
      float var3 = MathHelper.sin(-var0 * 0.017453292F - 3.1415927F);
      float var4 = -MathHelper.cos(-var1 * 0.017453292F);
      float var5 = MathHelper.sin(-var1 * 0.017453292F);
      return new Vec3((double)(var3 * var4), (double)var5, (double)(var2 * var4));
   }

   public static Vec3 getVectorForRotation(RotationUtils.Rotation var0) {
      float var1 = (float)Math.cos((double)(-var0.getYaw() * 0.017453292F - 3.1415927F));
      float var2 = (float)Math.sin((double)(-var0.getYaw() * 0.017453292F - 3.1415927F));
      float var3 = (float)(-Math.cos((double)(-var0.getPitch() * 0.017453292F)));
      float var4 = (float)Math.sin((double)(-var0.getPitch() * 0.017453292F));
      return new Vec3((double)(var2 * var3), (double)var4, (double)(var1 * var3));
   }

   public static float[] faceTarget(Entity var0, float var1, float var2, boolean var3) {
      double var4 = var0.posX - Minecraft.thePlayer.posX;
      double var6 = var0.posZ - Minecraft.thePlayer.posZ;
      if (var0 instanceof EntityLivingBase) {
         EntityLivingBase var8 = (EntityLivingBase)var0;
         var4 = var8.posY + (double)var8.getEyeHeight() - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
      } else {
         var4 = (var0.getEntityBoundingBox().minY + var0.getEntityBoundingBox().maxY) / 2.0D - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
      }

      Random var17 = new Random();
      float var9 = var3 ? (float)var17.nextInt(15) * 0.25F + 5.0F : 0.0F;
      double var10 = (double)MathHelper.sqrt_double(var4 * var4 + var6 * var6);
      float var12 = (float)(Math.atan2(var6 + (double)var9, var4) * 180.0D / 3.141592653589793D) - 90.0F;
      float var13 = (float)(-(Math.atan2(var4 - (double)(var0 instanceof EntityPlayer ? 0.5F : 0.0F) + (double)var9, var10) * 180.0D / 3.141592653589793D));
      float var14 = changeRotation(Minecraft.thePlayer.rotationPitch, var13, var2);
      float var15 = changeRotation(Minecraft.thePlayer.rotationYaw, var12, var1);
      return new float[]{var15, var14};
   }

   private static double directionCheck(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16, double var18, double var20, double var22) {
      double var24 = Math.sqrt(var6 * var6 + var8 * var8 + var10 * var10);
      if (var24 == 0.0D) {
         var24 = 1.0D;
      }

      double var26 = var12 - var0;
      double var28 = var14 - var2;
      double var30 = var16 - var4;
      double var32 = Math.sqrt(var26 * var26 + var28 * var28 + var30 * var30);
      double var34 = var32 * var6 / var24;
      double var36 = var32 * var8 / var24;
      double var38 = var32 * var10 / var24;
      double var40 = 0.0D;
      var40 += Math.max(Math.abs(var26 - var34) - (var18 / 2.0D + var22), 0.0D);
      var40 += Math.max(Math.abs(var30 - var38) - (var18 / 2.0D + var22), 0.0D);
      var40 += Math.max(Math.abs(var28 - var36) - (var20 / 2.0D + var22), 0.0D);
      if (var40 > 1.0D) {
         var40 = Math.sqrt(var40);
      }

      return var40;
   }

   public static float getYawChangeToEntity(Entity var0) {
      double var1 = var0.posX - Minecraft.thePlayer.posX;
      double var3 = var0.posZ - Minecraft.thePlayer.posZ;
      double var5 = 0.0D;
      if (var3 < 0.0D && var1 < 0.0D) {
         var5 = 90.0D + Math.toDegrees(Math.atan(var3 / var1));
      } else if (var3 < 0.0D && var1 > 0.0D) {
         double var7 = -90.0D + Math.toDegrees(Math.atan(var3 / var1));
      } else {
         Math.toDegrees(-Math.atan(var1 / var3));
      }

      return MathHelper.wrapAngleTo180_float(-(Minecraft.thePlayer.rotationYaw - (float)var5));
   }

   public static float[] getRotation(Entity var0) {
      double var1 = var0.posX - Minecraft.thePlayer.posX;
      double var3 = var0.posY + (double)var0.getEyeHeight() * 0.9D - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
      double var5 = var0.posZ - Minecraft.thePlayer.posZ;
      double var7 = (double)MathHelper.ceiling_float_int((float)(var1 * var1 + var5 * var5));
      float var9 = (float)(Math.atan2(var5, var1) * 180.0D / 3.141592653589793D) - 90.0F;
      float var10 = (float)(-(Math.atan2(var3, var7) * 180.0D / 3.141592653589793D));
      return new float[]{Minecraft.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(var9 - Minecraft.thePlayer.rotationYaw), Minecraft.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(var10 - Minecraft.thePlayer.rotationPitch)};
   }

   private static float getAngleDifference(float var0, float var1) {
      return ((var0 - var1) % 360.0F + 540.0F) % 360.0F - 180.0F;
   }

   public static float[] getRotationsToPos(double var0, double var2, double var4) {
      double var6 = var0 - Minecraft.thePlayer.posX;
      double var8 = var4 - Minecraft.thePlayer.posZ;
      double var10 = var2 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
      double var12 = (double)MathHelper.sqrt_double(var6 * var6 + var8 * var8);
      float var14 = (float)(Math.atan2(var8, var6) * 180.0D / 3.141592653589793D) - 90.0F;
      float var15 = (float)(-(Math.atan2(var10, var12) * 180.0D / 3.141592653589793D));
      return new float[]{var14, var15};
   }

   public static boolean isVisibleFOV(Entity var0, float var1) {
      return (Math.abs(getRotations(var0)[0] - Minecraft.thePlayer.rotationYaw) % 360.0F > 180.0F ? 360.0F - Math.abs(getRotations(var0)[0] - Minecraft.thePlayer.rotationYaw) % 360.0F : Math.abs(getRotations(var0)[0] - Minecraft.thePlayer.rotationYaw) % 360.0F) <= var1;
   }

   public static float[] getRotations(Entity var0) {
      if (var0 == null) {
         return null;
      } else {
         double var1 = var0.posX - Minecraft.thePlayer.posX;
         double var3 = var0.posZ - Minecraft.thePlayer.posZ;
         double var5;
         if (var0 instanceof EntityLivingBase) {
            EntityLivingBase var7 = (EntityLivingBase)var0;
            var5 = var7.posY + (double)var7.getEyeHeight() - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
         } else {
            var5 = (var0.getEntityBoundingBox().minY + var0.getEntityBoundingBox().maxY) / 2.0D - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
         }

         double var11 = (double)MathHelper.sqrt_double(var1 * var1 + var3 * var3);
         float var9 = (float)(Math.atan2(var3, var1) * 180.0D / 3.141592653589793D) - 90.0F;
         float var10 = (float)(-(Math.atan2(var5, var11) * 180.0D / 3.141592653589793D));
         return new float[]{var9, var10};
      }
   }

   public static RotationUtils.VecRotation searchCenter(AxisAlignedBB var0, boolean var1) {
      RotationUtils.VecRotation var2 = null;

      for(double var3 = 0.15D; var3 < 0.85D; var3 += 0.1D) {
         for(double var5 = 0.15D; var5 < 1.0D; var5 += 0.1D) {
            for(double var7 = 0.15D; var7 < 0.85D; var7 += 0.1D) {
               Vec3 var9 = new Vec3(var0.minX + (var0.maxX - var0.minX) * var3, var0.minY + (var0.maxY - var0.minY) * var5, var0.minZ + (var0.maxZ - var0.minZ) * var7);
               RotationUtils.Rotation var10 = toRotation(var9, var1);
               RotationUtils.VecRotation var11 = new RotationUtils.VecRotation(var9, var10);
               if (var2 == null || getRotationDifference(var11.getRotation()) < getRotationDifference(var2.getRotation())) {
                  var2 = var11;
               }
            }
         }
      }

      return var2;
   }

   public static float getTrajAngleSolutionLow(float var0, float var1, float var2) {
      float var3 = 0.006F;
      float var4 = var2 * var2 * var2 * var2 - var3 * (var3 * var0 * var0 + 2.0F * var1 * var2 * var2);
      return (float)Math.toDegrees(Math.atan(((double)(var2 * var2) - Math.sqrt((double)var4)) / (double)(var3 * var0)));
   }

   public static float[] naturalRotation(float[] var0) {
      float var1 = MathHelper.wrapAngleTo180_float(Minecraft.thePlayer.rotationYaw - var0[0]);
      float var2 = MathHelper.wrapAngleTo180_float(Minecraft.thePlayer.rotationPitch - var0[1]);
      return new float[]{var1 + var0[0], var2 + var0[1]};
   }

   public static float[] getRotations(double var0, double var2, double var4) {
      EntityPlayerSP var6 = Minecraft.thePlayer;
      double var7 = var0 - var6.posX;
      double var9 = var2 - (var6.posY + (double)var6.getEyeHeight());
      double var11 = var4 - var6.posZ;
      double var13 = (double)MathHelper.sqrt_double(var7 * var7 + var11 * var11);
      float var15 = (float)(Math.atan2(var11, var7) * 180.0D / 3.141592653589793D) - 90.0F;
      float var16 = (float)(-(Math.atan2(var9, var13) * 180.0D / 3.141592653589793D));
      return new float[]{var15, var16};
   }

   public static float[] getRotationToEntity(EntityLivingBase var0) {
      double var1 = var0.posX - Minecraft.thePlayer.posX;
      double var3 = var0.posZ - Minecraft.thePlayer.posZ;
      double var5 = var0.posY + (double)var0.getEyeHeight() * 0.9D - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
      double var7 = (double)MathHelper.sqrt_double(var1 * var1 + var3 * var3);
      float var9 = (float)(Math.atan2(var3, var1) * 180.0D / 3.141592653589793D) - 90.0F;
      float var10 = (float)(-(Math.atan2(var5, var7) * 180.0D / 3.141592653589793D));
      return new float[]{var9, var10};
   }

   public static float[] getRotations(BlockPos var0, EnumFacing var1) {
      return getRotations((double)var0.getX(), (double)var0.getY(), (double)var0.getZ(), var1);
   }

   public static double[] interpolate(Entity var0) {
      double var1 = (double)mc.timer.renderPartialTicks;
      double[] var3 = new double[]{var0.lastTickPosX + (var0.posX - var0.lastTickPosX) * var1, var0.lastTickPosY + (var0.posY - var0.lastTickPosY) * var1, var0.lastTickPosZ + (var0.posZ - var0.lastTickPosZ) * var1};
      return var3;
   }

   public static float[] getRotations(EntityLivingBase var0, double var1, double var3, double var5) {
      double var7 = var0.posX;
      double var9 = var0.posZ;
      double var11 = var0.posY + (double)(var0.getEyeHeight() / 2.0F);
      double var13 = var7 - var1;
      double var15 = var9 - var5;
      double var17 = var11 - var3 - 0.6D;
      double var19 = (double)MathHelper.sqrt_double(var13 * var13 + var15 * var15);
      float var21 = (float)(Math.atan2(var15, var13) * 180.0D / 3.141592653589793D) - 90.0F;
      float var22 = (float)(-(Math.atan2(var17, var19) * 180.0D / 3.141592653589793D));
      return new float[]{var21, var22};
   }

   public static float getPitchChangeToEntity(Entity var0) {
      double var1 = var0.posX - Minecraft.thePlayer.posX;
      double var3 = var0.posZ - Minecraft.thePlayer.posZ;
      double var5 = var0.posY - 1.6D + (double)var0.getEyeHeight() - Minecraft.thePlayer.posY;
      double var7 = (double)MathHelper.sqrt_double(var1 * var1 + var3 * var3);
      double var9 = -Math.toDegrees(Math.atan(var5 / var7));
      return -MathHelper.wrapAngleTo180_float(Minecraft.thePlayer.rotationPitch - (float)var9);
   }

   public static float getYawToEntity(Entity var0) {
      return Math.abs(getRotations(var0)[0] - Minecraft.thePlayer.rotationYaw) % 360.0F > 180.0F ? 360.0F - Math.abs(getRotations(var0)[0] - Minecraft.thePlayer.rotationYaw) % 360.0F : Math.abs(getRotations(var0)[0] - Minecraft.thePlayer.rotationYaw) % 360.0F;
   }

   public static RotationUtils.Rotation toRotation(Vec3 var0, boolean var1) {
      Vec3 var2 = new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.getEntityBoundingBox().minY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ);
      if (var1) {
         var2.addVector(Minecraft.thePlayer.motionX, Minecraft.thePlayer.motionY, Minecraft.thePlayer.motionZ);
      }

      double var3 = var0.xCoord - var2.xCoord;
      double var5 = var0.yCoord - var2.yCoord;
      double var7 = var0.zCoord - var2.zCoord;
      return new RotationUtils.Rotation(MathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(var7, var3)) - 90.0F), MathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(var5, Math.sqrt(var3 * var3 + var7 * var7))))));
   }

   public static float changeRotation(float var0, float var1, float var2) {
      float var3 = MathHelper.wrapAngleTo180_float(var1 - var0);
      if (var3 > var2) {
         var3 = var2;
      }

      if (var3 < -var2) {
         var3 = -var2;
      }

      return var0 + var3;
   }

   public static float[] getRotations(double var0, double var2, double var4, EnumFacing var6) {
      EntityPig var7 = new EntityPig(Minecraft.theWorld);
      var7.posX = var0 + 0.5D;
      var7.posY = var2 + 0.5D;
      var7.posZ = var4 + 0.5D;
      var7.posX += (double)var6.getDirectionVec().getX() * 0.5D;
      var7.posY += (double)var6.getDirectionVec().getY() * 0.5D;
      var7.posZ += (double)var6.getDirectionVec().getZ() * 0.5D;
      return getRotations(var7);
   }

   public static float getYawChangeGiven(double var0, double var2, float var4) {
      double var5 = var0 - Minecraft.thePlayer.posX;
      double var7 = var2 - Minecraft.thePlayer.posZ;
      double var9 = var7 < 0.0D && var5 < 0.0D ? 90.0D + Math.toDegrees(Math.atan(var7 / var5)) : (var7 < 0.0D && var5 > 0.0D ? -90.0D + Math.toDegrees(Math.atan(var7 / var5)) : Math.toDegrees(-Math.atan(var5 / var7)));
      return MathHelper.wrapAngleTo180_float(-(var4 - (float)var9));
   }

   public static float[] getRotationToLocation(Vec3 var0) {
      double var1 = var0.xCoord - Minecraft.thePlayer.posX;
      double var3 = var0.yCoord - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
      double var5 = var0.zCoord - Minecraft.thePlayer.posZ;
      double var7 = (double)MathHelper.sqrt_double(var1 * var1 + var5 * var5);
      float var9 = (float)(Math.atan2(var5, var1) * 180.0D / 3.141592653589793D) - 90.0F;
      float var10 = (float)(-(Math.atan2(var3, var7) * 180.0D / 3.141592653589793D));
      return new float[]{var9, var10};
   }

   public static double getRotationDifference(RotationUtils.Rotation var0) {
      return getRotationDifference(var0, serverRotation);
   }

   public static double getRotationDifference(RotationUtils.Rotation var0, RotationUtils.Rotation var1) {
      return Math.hypot((double)getAngleDifference(var0.getYaw(), var1.getYaw()), (double)(var0.getPitch() - var1.getPitch()));
   }

   public static RotationUtils.VecRotation faceBlock(BlockPos var0) {
      if (var0 == null) {
         return null;
      } else {
         RotationUtils.VecRotation var1 = null;

         for(double var2 = 0.1D; var2 < 0.9D; var2 += 0.1D) {
            for(double var4 = 0.1D; var4 < 0.9D; var4 += 0.1D) {
               for(double var6 = 0.1D; var6 < 0.9D; var6 += 0.1D) {
                  Vec3 var8 = new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.getEntityBoundingBox().minY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ);
                  Vec3 var9 = (new Vec3(var0)).addVector(var2, var4, var6);
                  double var10 = var8.distanceTo(var9);
                  double var12 = var9.xCoord - var8.xCoord;
                  double var14 = var9.yCoord - var8.yCoord;
                  double var16 = var9.zCoord - var8.zCoord;
                  double var18 = Math.sqrt(var12 * var12 + var16 * var16);
                  RotationUtils.Rotation var20 = new RotationUtils.Rotation(MathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(var16, var12)) - 90.0F), MathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(var14, var18)))));
                  Vec3 var21 = getVectorForRotation(var20);
                  Vec3 var22 = var8.addVector(var21.xCoord * var10, var21.yCoord * var10, var21.zCoord * var10);
                  MovingObjectPosition var23 = Minecraft.theWorld.rayTraceBlocks(var8, var22, false, false, true);
                  if (var23 != null && var23.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                     RotationUtils.VecRotation var24 = new RotationUtils.VecRotation(var9, var20);
                     if (var1 == null || getRotationDifference(var24.getRotation()) < getRotationDifference(var1.getRotation())) {
                        var1 = var24;
                     }
                  }
               }
            }
         }

         return var1;
      }
   }

   public static double angleDifference(double var0, double var2) {
      return ((var0 - var2) % 360.0D + 540.0D) % 360.0D - 180.0D;
   }

   public static class Rotation {
      float yaw;
      float pitch;

      public void setYaw(float var1) {
         this.yaw = var1;
      }

      public void fixedSensitivity(Float var1) {
         float var2 = var1 * 0.6F + 0.2F;
         float var3 = var2 * var2 * var2 * 1.2F;
         this.yaw -= this.yaw % var3;
         this.pitch -= this.pitch % var3;
      }

      public void setPitch(float var1) {
         this.pitch = var1;
      }

      public float getPitch() {
         return this.pitch;
      }

      public float getYaw() {
         return this.yaw;
      }

      public Rotation(float var1, float var2) {
         this.yaw = var1;
         this.pitch = var2;
      }

      public void toPlayer(EntityPlayer var1) {
         if (!Float.isNaN(this.yaw) && !Float.isNaN(this.pitch)) {
            this.fixedSensitivity(RotationUtils.mc.gameSettings.mouseSensitivity);
            var1.rotationYaw = this.yaw;
            var1.rotationPitch = this.pitch;
         }
      }
   }

   public static class VecRotation {
      Vec3 vec;
      RotationUtils.Rotation rotation;

      public VecRotation(Vec3 var1, RotationUtils.Rotation var2) {
         this.vec = var1;
         this.rotation = var2;
      }

      public RotationUtils.Rotation getRotation() {
         return this.rotation;
      }

      public Vec3 getVec() {
         return this.vec;
      }
   }
}
