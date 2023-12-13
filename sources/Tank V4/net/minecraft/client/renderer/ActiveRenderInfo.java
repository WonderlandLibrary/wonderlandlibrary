package net.minecraft.client.renderer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class ActiveRenderInfo {
   private static float rotationYZ;
   private static Vec3 position = new Vec3(0.0D, 0.0D, 0.0D);
   private static float rotationX;
   private static final FloatBuffer OBJECTCOORDS = GLAllocation.createDirectFloatBuffer(3);
   private static float rotationXZ;
   private static final FloatBuffer MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
   private static final IntBuffer VIEWPORT = GLAllocation.createDirectIntBuffer(16);
   private static float rotationZ;
   private static float rotationXY;
   private static final FloatBuffer PROJECTION = GLAllocation.createDirectFloatBuffer(16);

   public static float getRotationYZ() {
      return rotationYZ;
   }

   public static float getRotationXY() {
      return rotationXY;
   }

   public static float getRotationX() {
      return rotationX;
   }

   public static void updateRenderInfo(EntityPlayer var0, boolean var1) {
      GlStateManager.getFloat(2982, MODELVIEW);
      GlStateManager.getFloat(2983, PROJECTION);
      GL11.glGetInteger(2978, VIEWPORT);
      float var2 = (float)((VIEWPORT.get(0) + VIEWPORT.get(2)) / 2);
      float var3 = (float)((VIEWPORT.get(1) + VIEWPORT.get(3)) / 2);
      GLU.gluUnProject(var2, var3, 0.0F, MODELVIEW, PROJECTION, VIEWPORT, OBJECTCOORDS);
      position = new Vec3((double)OBJECTCOORDS.get(0), (double)OBJECTCOORDS.get(1), (double)OBJECTCOORDS.get(2));
      int var4 = var1 ? 1 : 0;
      float var5 = var0.rotationPitch;
      float var6 = var0.rotationYaw;
      rotationX = MathHelper.cos(var6 * 3.1415927F / 180.0F) * (float)(1 - var4 * 2);
      rotationZ = MathHelper.sin(var6 * 3.1415927F / 180.0F) * (float)(1 - var4 * 2);
      rotationYZ = -rotationZ * MathHelper.sin(var5 * 3.1415927F / 180.0F) * (float)(1 - var4 * 2);
      rotationXY = rotationX * MathHelper.sin(var5 * 3.1415927F / 180.0F) * (float)(1 - var4 * 2);
      rotationXZ = MathHelper.cos(var5 * 3.1415927F / 180.0F);
   }

   public static Vec3 getPosition() {
      return position;
   }

   public static float getRotationZ() {
      return rotationZ;
   }

   public static float getRotationXZ() {
      return rotationXZ;
   }

   public static Block getBlockAtEntityViewpoint(World var0, Entity var1, float var2) {
      Vec3 var3 = projectViewFromEntity(var1, (double)var2);
      BlockPos var4 = new BlockPos(var3);
      IBlockState var5 = var0.getBlockState(var4);
      Block var6 = var5.getBlock();
      if (var6.getMaterial().isLiquid()) {
         float var7 = 0.0F;
         if (var5.getBlock() instanceof BlockLiquid) {
            var7 = BlockLiquid.getLiquidHeightPercent((Integer)var5.getValue(BlockLiquid.LEVEL)) - 0.11111111F;
         }

         float var8 = (float)(var4.getY() + 1) - var7;
         if (var3.yCoord >= (double)var8) {
            var6 = var0.getBlockState(var4.up()).getBlock();
         }
      }

      return var6;
   }

   public static Vec3 projectViewFromEntity(Entity var0, double var1) {
      double var3 = var0.prevPosX + (var0.posX - var0.prevPosX) * var1;
      double var5 = var0.prevPosY + (var0.posY - var0.prevPosY) * var1;
      double var7 = var0.prevPosZ + (var0.posZ - var0.prevPosZ) * var1;
      double var9 = var3 + position.xCoord;
      double var11 = var5 + position.yCoord;
      double var13 = var7 + position.zCoord;
      return new Vec3(var9, var11, var13);
   }
}
