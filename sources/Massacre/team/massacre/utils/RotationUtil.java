package team.massacre.utils;

import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import team.massacre.impl.event.EventUpdate;

public class RotationUtil {
   public static Minecraft mc = Minecraft.getMinecraft();

   public static float getDistanceBetweenAngles(float angle1, float angle2) {
      float angle = Math.abs(angle1 - angle2) % 360.0F;
      if (angle > 180.0F) {
         angle = 360.0F - angle;
      }

      return angle;
   }

   public static float getYawBetween(float yaw, double srcX, double srcZ, double destX, double destZ) {
      double xDist = destX - srcX;
      double zDist = destZ - srcZ;
      float var1 = (float)(StrictMath.atan2(zDist, xDist) * 180.0D / 3.141592653589793D) - 90.0F;
      return yaw + MathHelper.wrapAngleTo180_float(var1 - yaw);
   }

   public static float getYawToEntity(Entity entity) {
      EntityPlayerSP player = PlayerUtils.getLocalPlayer();
      return getYawBetween(player.rotationYaw, player.posX, player.posZ, entity.posX, entity.posZ);
   }

   public static Vec3 getVectorForRotation(float pitch, float yaw) {
      float f = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
      float f2 = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
      float f3 = -MathHelper.cos(-pitch * 0.017453292F);
      float f4 = MathHelper.sin(-pitch * 0.017453292F);
      return new Vec3((double)(f2 * f3), (double)f4, (double)(f * f3));
   }

   public static float[] getRotationsToEntity(Entity entity) {
      EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
      double xDif = entity.posX - player.posX;
      double zDif = entity.posZ - player.posZ;
      AxisAlignedBB entityBB = entity.getEntityBoundingBox().expand(0.10000000149011612D, 0.10000000149011612D, 0.10000000149011612D);
      double playerEyePos = player.posY + (double)player.getEyeHeight();
      double yDif = playerEyePos > entityBB.maxY ? entityBB.maxY - playerEyePos : (playerEyePos < entityBB.minY ? entityBB.minY - playerEyePos : 0.0D);
      double fDist = (double)MathHelper.sqrt_double(xDif * xDif + zDif * zDif);
      return new float[]{(float)(StrictMath.atan2(zDif, xDif) * 180.0D / 3.141592653589793D) - 90.0F, (float)(-(StrictMath.atan2(yDif, fDist) * 180.0D / 3.141592653589793D))};
   }

   public static float[] faceEntity(Entity entityIn, boolean instant) {
      double var4 = entityIn.posX - mc.thePlayer.posX;
      double var5 = entityIn.posZ - mc.thePlayer.posZ;
      EntityLivingBase var6 = (EntityLivingBase)entityIn;
      double var7 = var6.posY - mc.thePlayer.posY - 1.2D;
      double var8 = (double)MathHelper.sqrt_double(var4 * var4 + var5 * var5);
      float var9 = (float)(Math.atan2(var5, var4) * 180.0D / 3.141592653589793D) - 90.0F;
      float var10 = (float)(-(Math.atan2(var7, var8) * 180.0D / 3.141592653589793D));
      float agr = 1.5F;
      float add = agr > 0.0F ? agr : 0.2F;
      add *= add;
      float[] rot = new float[]{updateRotation(mc.thePlayer.rotationYaw, var9, add), updateRotation(mc.thePlayer.rotationPitch, var10 + 1.2F, 0.5F)};
      return rot;
   }

   public static void rotate(EventUpdate event, float[] rotations, float aimSpeed, boolean lockView) {
      float[] prevRotations = new float[]{event.getPrevYaw(), event.getPrevPitch()};
      float[] cappedRotations = new float[]{maxAngleChange(prevRotations[0], rotations[0], aimSpeed), maxAngleChange(prevRotations[1], rotations[1], aimSpeed)};
      event.setYaw(cappedRotations[0]);
      event.setPitch(cappedRotations[1]);
      if (lockView) {
         mc.thePlayer.rotationYaw = cappedRotations[0];
         mc.thePlayer.rotationPitch = cappedRotations[1];
      }

   }

   public static float maxAngleChange(float prev, float now, float maxTurn) {
      float dif = MathHelper.wrapAngleTo180_float(now - prev);
      if (dif > maxTurn) {
         dif = maxTurn;
      }

      if (dif < -maxTurn) {
         dif = -maxTurn;
      }

      return prev + dif;
   }

   private static float updateRotation(float jyaw, float aquaPitch, float mcFl) {
      float var4 = MathHelper.wrapAngleTo180_float(aquaPitch - 0.0F - jyaw);
      float tog = var4 + mcFl;
      if (tog < 0.0F) {
         mcFl -= tog / 6.0F;
      } else if (tog > 0.0F) {
         mcFl += tog / 6.0F;
      }

      if (var4 > mcFl) {
         var4 = mcFl;
      }

      if (var4 < -mcFl) {
         var4 = -mcFl;
      }

      return jyaw + var4;
   }

   public static float[] getFacingRotations(int paramInt1, double d, int paramInt3) {
      EntityPig localEntityPig = new EntityPig(Minecraft.getMinecraft().theWorld);
      localEntityPig.posX = (double)paramInt1 + 0.5D - (double)MathUtil.randomFloatValue();
      localEntityPig.posY = d + 0.5D - (double)MathUtil.randomFloatValue();
      localEntityPig.posZ = (double)paramInt3 + 0.5D - (double)MathUtil.randomFloatValue();
      return getRotationsNeeded(localEntityPig);
   }

   public static float[] getRotationsNeeded(Entity entity) {
      if (entity == null) {
         return null;
      } else {
         Minecraft mc = Minecraft.getMinecraft();
         double xSize = entity.posX - mc.thePlayer.posX;
         double ySize = entity.posY + (double)(entity.getEyeHeight() / 2.0F) - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
         double zSize = entity.posZ - mc.thePlayer.posZ;
         double theta = (double)MathHelper.sqrt_double(xSize * xSize + zSize * zSize);
         float yaw = (float)(Math.atan2(zSize, xSize) * 180.0D / 3.141592653589793D) - 90.0F;
         float pitch = (float)(-(Math.atan2(ySize, theta) * 180.0D / 3.141592653589793D));
         return new float[]{(mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw)) % 360.0F, (mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch)) % 360.0F};
      }
   }

   public static Rotation getRotationsRandom(EntityLivingBase entity) {
      ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
      double randomXZ = threadLocalRandom.nextDouble(-0.08D, 0.08D);
      double randomY = threadLocalRandom.nextDouble(-0.125D, 0.125D);
      double x = entity.posX + randomXZ;
      double y = entity.posY + (double)entity.getEyeHeight() / 2.05D + randomY;
      double z = entity.posZ + randomXZ;
      return attemptFacePosition(x, y, z);
   }

   public static Rotation attemptFacePosition(double x, double y, double z) {
      double xDiff = x - mc.thePlayer.posX;
      double yDiff = y - mc.thePlayer.posY - 1.2D;
      double zDiff = z - mc.thePlayer.posZ;
      double dist = Math.hypot(xDiff, zDiff);
      float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
      float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D));
      return new Rotation(yaw, pitch);
   }

   public static float[] getRotations(EntityLivingBase entityIn, float speed) {
      float yaw = updateRotation(mc.thePlayer.rotationYaw, getNeededRotations(entityIn)[0], speed);
      float pitch = updateRotation(mc.thePlayer.rotationPitch, getNeededRotations(entityIn)[1], speed);
      return new float[]{yaw, pitch};
   }

   public static float[] getRotations(float[] lastRotations, float changeMax, float[] dstRotation) {
      dstRotation[0] = maxAngleChange(lastRotations[0], dstRotation[0], changeMax);
      dstRotation[1] = maxAngleChange(lastRotations[1], dstRotation[1], changeMax);
      return applyGCD(dstRotation, lastRotations);
   }

   public static double getMouseGCD() {
      float sens = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
      float pow = sens * sens * sens * 8.0F;
      return (double)pow * 0.15D;
   }

   public static float[] applyGCD(float[] rotations, float[] prevRots) {
      float yawDif = rotations[0] - prevRots[0];
      float pitchDif = rotations[1] - prevRots[1];
      double gcd = getMouseGCD();
      rotations[0] = (float)((double)rotations[0] - (double)yawDif % gcd);
      rotations[1] = (float)((double)rotations[1] - (double)pitchDif % gcd);
      return rotations;
   }

   public static float[] getNeededRotations(EntityLivingBase entityIn) {
      double d0 = entityIn.posX - mc.thePlayer.posX;
      double d1 = entityIn.posZ - mc.thePlayer.posZ;
      double d2 = entityIn.posY + (double)entityIn.getEyeHeight() - (mc.thePlayer.getEntityBoundingBox().minY + (double)mc.thePlayer.getEyeHeight());
      double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d1 * d1);
      float f = (float)(MathHelper.func_181159_b(d1, d0) * 180.0D / 3.141592653589793D) - 90.0F;
      float f1 = (float)(-(MathHelper.func_181159_b(d2, d3) * 180.0D / 3.141592653589793D));
      return new float[]{f, f1};
   }
}
