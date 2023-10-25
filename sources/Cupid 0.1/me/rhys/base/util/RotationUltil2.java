package me.rhys.base.util;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vector3d;
import org.lwjgl.util.vector.Vector2f;

public class RotationUltil2 {
  private static float yaw1 = 0.0F;
  
  private static float pitch1 = 0.0F;
  
  private static Minecraft mc = Minecraft.getMinecraft();
  
  private static List<String> rotating = new ArrayList<>();
  
  public static Vector2f getCirclePosition(double angle) {
    double a = Math.toRadians(angle);
    return new Vector2f((float)-Math.sin(a), (float)Math.cos(a));
  }
  
  public static Vector3d get360Position(double yaw, double pitch, double distance, float circlePosition) {
    double rYaw = Math.toRadians(yaw);
    double rPitch = Math.toRadians(pitch);
    double centerZ = Math.cos(rYaw) * Math.cos(rPitch) * distance;
    double centerY = -Math.sin(rPitch) * distance;
    double centerX = -Math.sin(rYaw) * Math.cos(rPitch) * distance;
    return new Vector3d(centerX, centerY, centerZ);
  }
  
  public static Vector2f tryFaceBlock(BlockPos position, EnumFacing face) {
    double actualFacingX = position.getX() + 0.5D;
    double actualFacingY = position.getY() + 0.5D;
    double actualFacingZ = position.getZ() + 0.5D;
    actualFacingX += face.getDirectionVec().getX() / 2.0D;
    actualFacingY += face.getDirectionVec().getY() / 2.0D;
    actualFacingZ += face.getDirectionVec().getZ() / 2.0D;
    actualFacingX = mc.thePlayer.posX - actualFacingX;
    actualFacingY = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - actualFacingY;
    actualFacingZ = mc.thePlayer.posZ - actualFacingZ;
    float yaw = (float)(Math.toDegrees(Math.atan2(actualFacingZ, actualFacingX)) + 90.0D);
    float pitch = (float)Math.toDegrees(Math.atan(actualFacingY / 
          distanceTo(actualFacingX, 0.0D, actualFacingZ)));
    return new Vector2f(yaw, pitch);
  }
  
  public static Vector2f tryFace(double x, double y, double z) {
    double actualFacingX = x;
    double actualFacingY = y;
    double actualFacingZ = z;
    actualFacingX = mc.thePlayer.posX - actualFacingX;
    actualFacingY = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - actualFacingY;
    actualFacingZ = mc.thePlayer.posZ - actualFacingZ;
    float yaw = (float)(Math.toDegrees(Math.atan2(actualFacingZ, actualFacingX)) + 90.0D);
    float pitch = (float)Math.toDegrees(Math.atan(actualFacingY / 
          distanceTo(actualFacingX, 0.0D, actualFacingZ)));
    return new Vector2f(yaw, pitch);
  }
  
  @Deprecated
  public static boolean setReverseRotating(String id, boolean value) {
    value = !value;
    if (value && !rotating.contains(id))
      rotating.add(id); 
    if (!value)
      rotating.remove(id); 
    return value;
  }
  
  public static boolean setRotating(String id, boolean value) {
    if (value && !rotating.contains(id))
      rotating.add(id); 
    if (!value)
      rotating.remove(id); 
    return value;
  }
  
  public static boolean doReset() {
    return !isRotating();
  }
  
  public static boolean isRotating() {
    return (rotating.size() != 0);
  }
  
  public static boolean isRotating(String id) {
    return rotating.contains(id);
  }
  
  public static float getPitch() {
    return pitch1;
  }
  
  public static float getYaw() {
    return yaw1;
  }
  
  public static void reset() {
    setYaw(mc.thePlayer.rotationYaw);
    setPitch(mc.thePlayer.rotationPitch);
  }
  
  public static void setPitch(float pitch) {
    pitch1 = pitch;
  }
  
  public static void setYaw(float yaw) {
    yaw1 = yaw;
    mc.thePlayer.rotationYawHead = yaw;
  }
  
  public static Vector2f faceBlock(double x, double y, double z) {
    double actualFacingX = x;
    double actualFacingY = y;
    double actualFacingZ = z;
    actualFacingX = mc.thePlayer.posX - actualFacingX;
    actualFacingY = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - actualFacingY;
    actualFacingZ = mc.thePlayer.posZ - actualFacingZ;
    float yaw = (float)(Math.toDegrees(Math.atan2(actualFacingZ, actualFacingX)) + 90.0D);
    float pitch = (float)Math.toDegrees(Math.atan(actualFacingY / 
          distanceTo(actualFacingX, 0.0D, actualFacingZ)));
    return new Vector2f(yaw, pitch);
  }
  
  private static double distanceTo(double x, double y, double z) {
    double distance = 0.0D;
    distance = Math.sqrt(x * x + z * z);
    distance = Math.sqrt(distance * distance + y * y);
    return distance;
  }
  
  public static float[] getRotations(double x, double y, double z) {
    x--;
    y--;
    z--;
    float[] ret = { 0.0F, 0.0F };
    double xSearch;
    for (xSearch = 0.1D; xSearch < 0.9D; xSearch += 0.1D) {
      double ySearch;
      for (ySearch = 0.1D; ySearch < 0.9D; ySearch += 0.1D) {
        double zSearch;
        for (zSearch = 0.1D; zSearch < 0.9D; zSearch += 0.1D) {
          Vec3 eyesPos = new Vec3(mc.thePlayer.posX, (mc.thePlayer.getEntityBoundingBox()).minY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
          Vec3 posVec = (new Vec3(x, y, z)).addVector(xSearch, ySearch, zSearch);
          double diffX = posVec.xCoord - eyesPos.xCoord;
          double diffY = posVec.yCoord - eyesPos.yCoord;
          double diffZ = posVec.zCoord - eyesPos.zCoord;
          double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
          ret[0] = MathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F);
          ret[1] = MathHelper.wrapAngleTo180_float((float)-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        } 
      } 
    } 
    return ret;
  }
  
  public static void oldFaceBlock(double x, double y, double z) {
    x--;
    y--;
    z--;
    double xSearch;
    for (xSearch = 0.1D; xSearch < 0.9D; xSearch += 0.1D) {
      double ySearch;
      for (ySearch = 0.1D; ySearch < 0.9D; ySearch += 0.1D) {
        double zSearch;
        for (zSearch = 0.1D; zSearch < 0.9D; zSearch += 0.1D) {
          Vec3 eyesPos = new Vec3(mc.thePlayer.posX, (mc.thePlayer.getEntityBoundingBox()).minY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
          Vec3 posVec = (new Vec3(x, y, z)).addVector(xSearch, ySearch, zSearch);
          double diffX = posVec.xCoord - eyesPos.xCoord;
          double diffY = posVec.yCoord - eyesPos.yCoord;
          double diffZ = posVec.zCoord - eyesPos.zCoord;
          double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
          setPitch(MathHelper.wrapAngleTo180_float((float)-Math.toDegrees(Math.atan2(diffY, diffXZ))));
          setYaw(MathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F));
        } 
      } 
    } 
  }
  
  public static float[] oldGetRotations(double x, double y, double z) {
    x--;
    y--;
    z--;
    float[] ret = { 0.0F, 0.0F };
    double xSearch;
    for (xSearch = 0.1D; xSearch < 0.9D; xSearch += 0.1D) {
      double ySearch;
      for (ySearch = 0.1D; ySearch < 0.9D; ySearch += 0.1D) {
        double zSearch;
        for (zSearch = 0.1D; zSearch < 0.9D; zSearch += 0.1D) {
          Vec3 eyesPos = new Vec3(mc.thePlayer.posX, (mc.thePlayer.getEntityBoundingBox()).minY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
          Vec3 posVec = (new Vec3(x, y, z)).addVector(xSearch, ySearch, zSearch);
          double diffX = posVec.xCoord - eyesPos.xCoord;
          double diffY = posVec.yCoord - eyesPos.yCoord;
          double diffZ = posVec.zCoord - eyesPos.zCoord;
          double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
          ret[0] = MathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F);
          ret[1] = MathHelper.wrapAngleTo180_float((float)-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        } 
      } 
    } 
    return ret;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\util\RotationUltil2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */