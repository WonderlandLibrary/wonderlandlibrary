package net.minecraft.client.renderer.block.model;

import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;
import net.minecraftforge.client.model.ITransformation;
import optifine.Config;
import optifine.Reflector;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import shadersmod.client.Shaders;

public class FaceBakery {
   private static final float field_178418_a = 1.0F / (float)Math.cos(0.39269909262657166D) - 1.0F;
   private static final float field_178417_b = 1.0F / (float)Math.cos(0.7853981633974483D) - 1.0F;
   private static final String __OBFID = "CL_00002490";

   private void func_178401_a(int var1, int[] var2, EnumFacing var3, BlockFaceUV var4, TextureAtlasSprite var5) {
      int var6 = var2.length / 4;
      int var7 = var6 * var1;
      float var8 = Float.intBitsToFloat(var2[var7]);
      float var9 = Float.intBitsToFloat(var2[var7 + 1]);
      float var10 = Float.intBitsToFloat(var2[var7 + 2]);
      if (var8 < -0.1F || var8 >= 1.1F) {
         var8 -= (float)MathHelper.floor_float(var8);
      }

      if (var9 < -0.1F || var9 >= 1.1F) {
         var9 -= (float)MathHelper.floor_float(var9);
      }

      if (var10 < -0.1F || var10 >= 1.1F) {
         var10 -= (float)MathHelper.floor_float(var10);
      }

      float var11 = 0.0F;
      float var12 = 0.0F;
      switch(var3) {
      case DOWN:
         var11 = var8 * 16.0F;
         var12 = (1.0F - var10) * 16.0F;
         break;
      case UP:
         var11 = var8 * 16.0F;
         var12 = var10 * 16.0F;
         break;
      case NORTH:
         var11 = (1.0F - var8) * 16.0F;
         var12 = (1.0F - var9) * 16.0F;
         break;
      case SOUTH:
         var11 = var8 * 16.0F;
         var12 = (1.0F - var9) * 16.0F;
         break;
      case WEST:
         var11 = var10 * 16.0F;
         var12 = (1.0F - var9) * 16.0F;
         break;
      case EAST:
         var11 = (1.0F - var10) * 16.0F;
         var12 = (1.0F - var9) * 16.0F;
      }

      int var13 = var4.func_178345_c(var1) * var6;
      var2[var13 + 4] = Float.floatToRawIntBits(var5.getInterpolatedU((double)var11));
      var2[var13 + 4 + 1] = Float.floatToRawIntBits(var5.getInterpolatedV((double)var12));
   }

   public BakedQuad makeBakedQuad(Vector3f var1, Vector3f var2, BlockPartFace var3, TextureAtlasSprite var4, EnumFacing var5, ITransformation var6, BlockPartRotation var7, boolean var8, boolean var9) {
      int[] var10 = this.makeQuadVertexData(var3, var4, var5, this.getPositionsDiv16(var1, var2), var6, var7, var8, var9);
      EnumFacing var11 = getFacingFromVertexData(var10);
      if (var8) {
         this.func_178409_a(var10, var11, var3.blockFaceUV, var4);
      }

      if (var7 == null) {
         this.applyFacing(var10, var11);
      }

      if (Reflector.ForgeHooksClient_fillNormal.exists()) {
         Reflector.callVoid(Reflector.ForgeHooksClient_fillNormal, var10, var11);
      }

      return new BakedQuad(var10, var3.tintIndex, var11, var4);
   }

   public int rotateVertex(Vector3f var1, EnumFacing var2, int var3, ModelRotation var4, boolean var5) {
      return this.rotateVertex(var1, var2, var3, var4, var5);
   }

   private void fillVertexData(int[] var1, int var2, EnumFacing var3, BlockPartFace var4, float[] var5, TextureAtlasSprite var6, ITransformation var7, BlockPartRotation var8, boolean var9, boolean var10) {
      EnumFacing var11 = var7.rotate(var3);
      int var12 = var10 ? this.getFaceShadeColor(var11) : -1;
      EnumFaceDirection.VertexInformation var13 = EnumFaceDirection.getFacing(var3).func_179025_a(var2);
      Vector3f var14 = new Vector3f(var5[var13.field_179184_a], var5[var13.field_179182_b], var5[var13.field_179183_c]);
      this.func_178407_a(var14, var8);
      int var15 = this.rotateVertex(var14, var3, var2, var7, var9);
      this.storeVertexData(var1, var15, var2, var14, var12, var6, var4.blockFaceUV);
   }

   private int[] makeQuadVertexData(BlockPartFace var1, TextureAtlasSprite var2, EnumFacing var3, float[] var4, ITransformation var5, BlockPartRotation var6, boolean var7, boolean var8) {
      byte var9 = 28;
      if (Config.isShaders()) {
         var9 = 56;
      }

      int[] var10 = new int[var9];

      for(int var11 = 0; var11 < 4; ++var11) {
         this.fillVertexData(var10, var11, var3, var1, var4, var2, var5, var6, var7, var8);
      }

      return var10;
   }

   private int getFaceShadeColor(EnumFacing var1) {
      float var2 = this.getFaceBrightness(var1);
      int var3 = MathHelper.clamp_int((int)(var2 * 255.0F), 0, 255);
      return -16777216 | var3 << 16 | var3 << 8 | var3;
   }

   public int rotateVertex(Vector3f var1, EnumFacing var2, int var3, ITransformation var4, boolean var5) {
      if (var4 == ModelRotation.X0_Y0) {
         return var3;
      } else {
         if (Reflector.ForgeHooksClient_transform.exists()) {
            Reflector.call(Reflector.ForgeHooksClient_transform, var1, var4.getMatrix());
         } else {
            this.rotateScale(var1, new Vector3f(0.5F, 0.5F, 0.5F), ((ModelRotation)var4).getMatrix4d(), new Vector3f(1.0F, 1.0F, 1.0F));
         }

         return var4.rotate(var2, var3);
      }
   }

   private void applyFacing(int[] var1, EnumFacing var2) {
      int[] var3 = new int[var1.length];
      System.arraycopy(var1, 0, var3, 0, var1.length);
      float[] var4 = new float[EnumFacing.values().length];
      var4[EnumFaceDirection.Constants.WEST_INDEX] = 999.0F;
      var4[EnumFaceDirection.Constants.DOWN_INDEX] = 999.0F;
      var4[EnumFaceDirection.Constants.NORTH_INDEX] = 999.0F;
      var4[EnumFaceDirection.Constants.EAST_INDEX] = -999.0F;
      var4[EnumFaceDirection.Constants.UP_INDEX] = -999.0F;
      var4[EnumFaceDirection.Constants.SOUTH_INDEX] = -999.0F;
      int var5 = var1.length / 4;

      int var7;
      float var10;
      for(int var6 = 0; var6 < 4; ++var6) {
         var7 = var5 * var6;
         float var8 = Float.intBitsToFloat(var3[var7]);
         float var9 = Float.intBitsToFloat(var3[var7 + 1]);
         var10 = Float.intBitsToFloat(var3[var7 + 2]);
         if (var8 < var4[EnumFaceDirection.Constants.WEST_INDEX]) {
            var4[EnumFaceDirection.Constants.WEST_INDEX] = var8;
         }

         if (var9 < var4[EnumFaceDirection.Constants.DOWN_INDEX]) {
            var4[EnumFaceDirection.Constants.DOWN_INDEX] = var9;
         }

         if (var10 < var4[EnumFaceDirection.Constants.NORTH_INDEX]) {
            var4[EnumFaceDirection.Constants.NORTH_INDEX] = var10;
         }

         if (var8 > var4[EnumFaceDirection.Constants.EAST_INDEX]) {
            var4[EnumFaceDirection.Constants.EAST_INDEX] = var8;
         }

         if (var9 > var4[EnumFaceDirection.Constants.UP_INDEX]) {
            var4[EnumFaceDirection.Constants.UP_INDEX] = var9;
         }

         if (var10 > var4[EnumFaceDirection.Constants.SOUTH_INDEX]) {
            var4[EnumFaceDirection.Constants.SOUTH_INDEX] = var10;
         }
      }

      EnumFaceDirection var18 = EnumFaceDirection.getFacing(var2);

      for(var7 = 0; var7 < 4; ++var7) {
         int var19 = var5 * var7;
         EnumFaceDirection.VertexInformation var20 = var18.func_179025_a(var7);
         var10 = var4[var20.field_179184_a];
         float var11 = var4[var20.field_179182_b];
         float var12 = var4[var20.field_179183_c];
         var1[var19] = Float.floatToRawIntBits(var10);
         var1[var19 + 1] = Float.floatToRawIntBits(var11);
         var1[var19 + 2] = Float.floatToRawIntBits(var12);

         for(int var13 = 0; var13 < 4; ++var13) {
            int var14 = var5 * var13;
            float var15 = Float.intBitsToFloat(var3[var14]);
            float var16 = Float.intBitsToFloat(var3[var14 + 1]);
            float var17 = Float.intBitsToFloat(var3[var14 + 2]);
            if (MathHelper.epsilonEquals(var10, var15) && MathHelper.epsilonEquals(var11, var16) && MathHelper.epsilonEquals(var12, var17)) {
               var1[var19 + 4] = var3[var14 + 4];
               var1[var19 + 4 + 1] = var3[var14 + 4 + 1];
            }
         }
      }

   }

   public static EnumFacing getFacingFromVertexData(int[] var0) {
      int var1 = var0.length / 4;
      int var2 = var1 * 2;
      int var3 = var1 * 3;
      Vector3f var4 = new Vector3f(Float.intBitsToFloat(var0[0]), Float.intBitsToFloat(var0[1]), Float.intBitsToFloat(var0[2]));
      Vector3f var5 = new Vector3f(Float.intBitsToFloat(var0[var1]), Float.intBitsToFloat(var0[var1 + 1]), Float.intBitsToFloat(var0[var1 + 2]));
      Vector3f var6 = new Vector3f(Float.intBitsToFloat(var0[var2]), Float.intBitsToFloat(var0[var2 + 1]), Float.intBitsToFloat(var0[var2 + 2]));
      Vector3f var7 = new Vector3f();
      Vector3f var8 = new Vector3f();
      Vector3f var9 = new Vector3f();
      Vector3f.sub(var4, var5, var7);
      Vector3f.sub(var6, var5, var8);
      Vector3f.cross(var8, var7, var9);
      float var10 = (float)Math.sqrt((double)(var9.x * var9.x + var9.y * var9.y + var9.z * var9.z));
      var9.x /= var10;
      var9.y /= var10;
      var9.z /= var10;
      EnumFacing var11 = null;
      float var12 = 0.0F;
      EnumFacing[] var16;
      int var15 = (var16 = EnumFacing.values()).length;

      for(int var14 = 0; var14 < var15; ++var14) {
         EnumFacing var13 = var16[var14];
         Vec3i var17 = var13.getDirectionVec();
         Vector3f var18 = new Vector3f((float)var17.getX(), (float)var17.getY(), (float)var17.getZ());
         float var19 = Vector3f.dot(var9, var18);
         if (var19 >= 0.0F && var19 > var12) {
            var12 = var19;
            var11 = var13;
         }
      }

      if (var12 < 0.719F) {
         if (var11 != EnumFacing.EAST && var11 != EnumFacing.WEST && var11 != EnumFacing.NORTH && var11 != EnumFacing.SOUTH) {
            var11 = EnumFacing.UP;
         } else {
            var11 = EnumFacing.NORTH;
         }
      }

      return var11 == null ? EnumFacing.UP : var11;
   }

   private float getFaceBrightness(EnumFacing var1) {
      switch(var1) {
      case DOWN:
         if (Config.isShaders()) {
            return Shaders.blockLightLevel05;
         }

         return 0.5F;
      case UP:
         return 1.0F;
      case NORTH:
      case SOUTH:
         if (Config.isShaders()) {
            return Shaders.blockLightLevel08;
         }

         return 0.8F;
      case WEST:
      case EAST:
         if (Config.isShaders()) {
            return Shaders.blockLightLevel06;
         }

         return 0.6F;
      default:
         return 1.0F;
      }
   }

   private void func_178407_a(Vector3f var1, BlockPartRotation var2) {
      if (var2 != null) {
         Matrix4f var3 = this.getMatrixIdentity();
         Vector3f var4 = new Vector3f(0.0F, 0.0F, 0.0F);
         switch(var2.axis) {
         case X:
            Matrix4f.rotate(var2.angle * 0.017453292F, new Vector3f(1.0F, 0.0F, 0.0F), var3, var3);
            var4.set(0.0F, 1.0F, 1.0F);
            break;
         case Y:
            Matrix4f.rotate(var2.angle * 0.017453292F, new Vector3f(0.0F, 1.0F, 0.0F), var3, var3);
            var4.set(1.0F, 0.0F, 1.0F);
            break;
         case Z:
            Matrix4f.rotate(var2.angle * 0.017453292F, new Vector3f(0.0F, 0.0F, 1.0F), var3, var3);
            var4.set(1.0F, 1.0F, 0.0F);
         }

         if (var2.rescale) {
            if (Math.abs(var2.angle) == 22.5F) {
               var4.scale(field_178418_a);
            } else {
               var4.scale(field_178417_b);
            }

            Vector3f.add(var4, new Vector3f(1.0F, 1.0F, 1.0F), var4);
         } else {
            var4.set(1.0F, 1.0F, 1.0F);
         }

         this.rotateScale(var1, new Vector3f(var2.origin), var3, var4);
      }

   }

   public BakedQuad makeBakedQuad(Vector3f var1, Vector3f var2, BlockPartFace var3, TextureAtlasSprite var4, EnumFacing var5, ModelRotation var6, BlockPartRotation var7, boolean var8, boolean var9) {
      int[] var10 = this.makeQuadVertexData(var3, var4, var5, this.getPositionsDiv16(var1, var2), var6, var7, var8, var9);
      EnumFacing var11 = getFacingFromVertexData(var10);
      if (var8) {
         this.func_178409_a(var10, var11, var3.blockFaceUV, var4);
      }

      if (var7 == null) {
         this.applyFacing(var10, var11);
      }

      return new BakedQuad(var10, var3.tintIndex, var11, var4);
   }

   private void rotateScale(Vector3f var1, Vector3f var2, Matrix4f var3, Vector3f var4) {
      Vector4f var5 = new Vector4f(var1.x - var2.x, var1.y - var2.y, var1.z - var2.z, 1.0F);
      Matrix4f.transform(var3, var5, var5);
      var5.x *= var4.x;
      var5.y *= var4.y;
      var5.z *= var4.z;
      var1.set(var5.x + var2.x, var5.y + var2.y, var5.z + var2.z);
   }

   private void storeVertexData(int[] var1, int var2, int var3, Vector3f var4, int var5, TextureAtlasSprite var6, BlockFaceUV var7) {
      int var8 = var1.length / 4;
      int var9 = var2 * var8;
      var1[var9] = Float.floatToRawIntBits(var4.x);
      var1[var9 + 1] = Float.floatToRawIntBits(var4.y);
      var1[var9 + 2] = Float.floatToRawIntBits(var4.z);
      var1[var9 + 3] = var5;
      var1[var9 + 4] = Float.floatToRawIntBits(var6.getInterpolatedU((double)var7.func_178348_a(var3)));
      var1[var9 + 4 + 1] = Float.floatToRawIntBits(var6.getInterpolatedV((double)var7.func_178346_b(var3)));
   }

   private Matrix4f getMatrixIdentity() {
      Matrix4f var1 = new Matrix4f();
      var1.setIdentity();
      return var1;
   }

   private float[] getPositionsDiv16(Vector3f var1, Vector3f var2) {
      float[] var3 = new float[EnumFacing.values().length];
      var3[EnumFaceDirection.Constants.WEST_INDEX] = var1.x / 16.0F;
      var3[EnumFaceDirection.Constants.DOWN_INDEX] = var1.y / 16.0F;
      var3[EnumFaceDirection.Constants.NORTH_INDEX] = var1.z / 16.0F;
      var3[EnumFaceDirection.Constants.EAST_INDEX] = var2.x / 16.0F;
      var3[EnumFaceDirection.Constants.UP_INDEX] = var2.y / 16.0F;
      var3[EnumFaceDirection.Constants.SOUTH_INDEX] = var2.z / 16.0F;
      return var3;
   }

   public void func_178409_a(int[] var1, EnumFacing var2, BlockFaceUV var3, TextureAtlasSprite var4) {
      for(int var5 = 0; var5 < 4; ++var5) {
         this.func_178401_a(var5, var1, var2, var3, var4);
      }

   }

   static final class FaceBakery$1 {
      static final int[] field_178400_a;
      private static final String __OBFID = "CL_00002489";
      static final int[] field_178399_b = new int[EnumFacing.Axis.values().length];

      static {
         try {
            field_178399_b[EnumFacing.Axis.X.ordinal()] = 1;
         } catch (NoSuchFieldError var9) {
         }

         try {
            field_178399_b[EnumFacing.Axis.Y.ordinal()] = 2;
         } catch (NoSuchFieldError var8) {
         }

         try {
            field_178399_b[EnumFacing.Axis.Z.ordinal()] = 3;
         } catch (NoSuchFieldError var7) {
         }

         field_178400_a = new int[EnumFacing.values().length];

         try {
            field_178400_a[EnumFacing.DOWN.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
         }

         try {
            field_178400_a[EnumFacing.UP.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
         }

         try {
            field_178400_a[EnumFacing.NORTH.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
         }

         try {
            field_178400_a[EnumFacing.SOUTH.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
         }

         try {
            field_178400_a[EnumFacing.WEST.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
         }

         try {
            field_178400_a[EnumFacing.EAST.ordinal()] = 6;
         } catch (NoSuchFieldError var1) {
         }

      }
   }
}
