package net.minecraft.client.renderer;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3i;
import net.minecraft.world.IBlockAccess;
import optifine.BetterGrass;
import optifine.BetterSnow;
import optifine.Config;
import optifine.ConnectedTextures;
import optifine.CustomColors;
import optifine.NaturalTextures;
import optifine.Reflector;
import optifine.RenderEnv;
import optifine.SmartLeaves;

public class BlockModelRenderer {
   private static float aoLightValueOpaque = 0.2F;
   private static final String __OBFID = "CL_00002518";

   private void renderModelBrightnessColorQuads(float var1, float var2, float var3, float var4, List var5) {
      Tessellator var6 = Tessellator.getInstance();
      WorldRenderer var7 = var6.getWorldRenderer();
      Iterator var9 = var5.iterator();

      while(var9.hasNext()) {
         Object var8 = var9.next();
         BakedQuad var10 = (BakedQuad)var8;
         var7.begin(7, DefaultVertexFormats.ITEM);
         var7.addVertexData(var10.getVertexData());
         if (var10.hasTintIndex()) {
            var7.putColorRGB_F4(var2 * var1, var3 * var1, var4 * var1);
         } else {
            var7.putColorRGB_F4(var1, var1, var1);
         }

         Vec3i var11 = var10.getFace().getDirectionVec();
         var7.putNormal((float)var11.getX(), (float)var11.getY(), (float)var11.getZ());
         var6.draw();
      }

   }

   private void renderModelAmbientOcclusionQuads(IBlockAccess var1, Block var2, BlockPos var3, WorldRenderer var4, List var5, RenderEnv var6) {
      float[] var7 = var6.getQuadBounds();
      BitSet var8 = var6.getBoundsFlags();
      BlockModelRenderer.AmbientOcclusionFace var9 = var6.getAoFace();
      IBlockState var10 = var6.getBlockState();
      double var11 = (double)var3.getX();
      double var13 = (double)var3.getY();
      double var15 = (double)var3.getZ();
      Block.EnumOffsetType var17 = var2.getOffsetType();
      if (var17 != Block.EnumOffsetType.NONE) {
         long var18 = MathHelper.getPositionRandom(var3);
         var11 += ((double)((float)(var18 >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
         var15 += ((double)((float)(var18 >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;
         if (var17 == Block.EnumOffsetType.XYZ) {
            var13 += ((double)((float)(var18 >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
         }
      }

      for(Iterator var19 = var5.iterator(); var19.hasNext(); var4.putPosition(var11, var13, var15)) {
         Object var26 = var19.next();
         BakedQuad var20 = (BakedQuad)var26;
         if (!var6.isBreakingAnimation(var20)) {
            BakedQuad var21 = var20;
            if (Config.isConnectedTextures()) {
               var20 = ConnectedTextures.getConnectedTexture(var1, var10, var3, var20, var6);
            }

            if (var20 == var21 && Config.isNaturalTextures()) {
               var20 = NaturalTextures.getNaturalTexture(var3, var20);
            }
         }

         this.fillQuadBounds(var2, var20.getVertexData(), var20.getFace(), var7, var8);
         var9.updateVertexBrightness(var1, var2, var3, var20.getFace(), var7, var8);
         if (var4.isMultiTexture()) {
            var4.addVertexData(var20.getVertexDataSingle());
            var4.putSprite(var20.getSprite());
         } else {
            var4.addVertexData(var20.getVertexData());
         }

         var4.putBrightness4(BlockModelRenderer.AmbientOcclusionFace.access$0(var9)[0], BlockModelRenderer.AmbientOcclusionFace.access$0(var9)[1], BlockModelRenderer.AmbientOcclusionFace.access$0(var9)[2], BlockModelRenderer.AmbientOcclusionFace.access$0(var9)[3]);
         int var27 = CustomColors.getColorMultiplier(var20, var2, var1, var3, var6);
         if (!var20.hasTintIndex() && var27 == -1) {
            var4.putColorMultiplier(BlockModelRenderer.AmbientOcclusionFace.access$1(var9)[0], BlockModelRenderer.AmbientOcclusionFace.access$1(var9)[0], BlockModelRenderer.AmbientOcclusionFace.access$1(var9)[0], 4);
            var4.putColorMultiplier(BlockModelRenderer.AmbientOcclusionFace.access$1(var9)[1], BlockModelRenderer.AmbientOcclusionFace.access$1(var9)[1], BlockModelRenderer.AmbientOcclusionFace.access$1(var9)[1], 3);
            var4.putColorMultiplier(BlockModelRenderer.AmbientOcclusionFace.access$1(var9)[2], BlockModelRenderer.AmbientOcclusionFace.access$1(var9)[2], BlockModelRenderer.AmbientOcclusionFace.access$1(var9)[2], 2);
            var4.putColorMultiplier(BlockModelRenderer.AmbientOcclusionFace.access$1(var9)[3], BlockModelRenderer.AmbientOcclusionFace.access$1(var9)[3], BlockModelRenderer.AmbientOcclusionFace.access$1(var9)[3], 1);
         } else {
            int var22;
            if (var27 != -1) {
               var22 = var27;
            } else {
               var22 = var2.colorMultiplier(var1, var3, var20.getTintIndex());
            }

            if (EntityRenderer.anaglyphEnable) {
               var22 = TextureUtil.anaglyphColor(var22);
            }

            float var23 = (float)(var22 >> 16 & 255) / 255.0F;
            float var24 = (float)(var22 >> 8 & 255) / 255.0F;
            float var25 = (float)(var22 & 255) / 255.0F;
            var4.putColorMultiplier(BlockModelRenderer.AmbientOcclusionFace.access$1(var9)[0] * var23, BlockModelRenderer.AmbientOcclusionFace.access$1(var9)[0] * var24, BlockModelRenderer.AmbientOcclusionFace.access$1(var9)[0] * var25, 4);
            var4.putColorMultiplier(BlockModelRenderer.AmbientOcclusionFace.access$1(var9)[1] * var23, BlockModelRenderer.AmbientOcclusionFace.access$1(var9)[1] * var24, BlockModelRenderer.AmbientOcclusionFace.access$1(var9)[1] * var25, 3);
            var4.putColorMultiplier(BlockModelRenderer.AmbientOcclusionFace.access$1(var9)[2] * var23, BlockModelRenderer.AmbientOcclusionFace.access$1(var9)[2] * var24, BlockModelRenderer.AmbientOcclusionFace.access$1(var9)[2] * var25, 2);
            var4.putColorMultiplier(BlockModelRenderer.AmbientOcclusionFace.access$1(var9)[3] * var23, BlockModelRenderer.AmbientOcclusionFace.access$1(var9)[3] * var24, BlockModelRenderer.AmbientOcclusionFace.access$1(var9)[3] * var25, 1);
         }
      }

   }

   public boolean renderModel(IBlockAccess var1, IBakedModel var2, IBlockState var3, BlockPos var4, WorldRenderer var5, boolean var6) {
      boolean var7 = Minecraft.isAmbientOcclusionEnabled() && var3.getBlock().getLightValue() == 0 && var2.isAmbientOcclusion();

      try {
         Block var8 = var3.getBlock();
         if (Config.isTreesSmart() && var3.getBlock() instanceof BlockLeavesBase) {
            var2 = SmartLeaves.getLeavesModel(var2);
         }

         return var7 ? this.renderModelAmbientOcclusion(var1, var2, var8, var3, var4, var5, var6) : this.renderModelStandard(var1, var2, var8, var3, var4, var5, var6);
      } catch (Throwable var11) {
         CrashReport var9 = CrashReport.makeCrashReport(var11, "Tesselating block model");
         CrashReportCategory var10 = var9.makeCategory("Block model being tesselated");
         CrashReportCategory.addBlockInfo(var10, var4, var3);
         var10.addCrashSection("Using AO", var7);
         throw new ReportedException(var9);
      }
   }

   public BlockModelRenderer() {
      if (Reflector.ForgeModContainer_forgeLightPipelineEnabled.exists()) {
         Reflector.setFieldValue(Reflector.ForgeModContainer_forgeLightPipelineEnabled, false);
      }

   }

   public boolean renderModelAmbientOcclusion(IBlockAccess var1, IBakedModel var2, Block var3, IBlockState var4, BlockPos var5, WorldRenderer var6, boolean var7) {
      boolean var8 = false;
      RenderEnv var9 = null;
      EnumFacing[] var13;
      int var12 = (var13 = EnumFacing.VALUES).length;

      for(int var11 = 0; var11 < var12; ++var11) {
         EnumFacing var10 = var13[var11];
         List var14 = var2.getFaceQuads(var10);
         if (!var14.isEmpty()) {
            BlockPos var15 = var5.offset(var10);
            if (!var7 || var3.shouldSideBeRendered(var1, var15, var10)) {
               if (var9 == null) {
                  var9 = RenderEnv.getInstance(var1, var4, var5);
               }

               if (!var9.isBreakingAnimation(var14) && Config.isBetterGrass()) {
                  var14 = BetterGrass.getFaceQuads(var1, var3, var5, var10, var14);
               }

               this.renderModelAmbientOcclusionQuads(var1, var3, var5, var6, var14, var9);
               var8 = true;
            }
         }
      }

      List var16 = var2.getGeneralQuads();
      if (var16.size() > 0) {
         if (var9 == null) {
            var9 = RenderEnv.getInstance(var1, var4, var5);
         }

         this.renderModelAmbientOcclusionQuads(var1, var3, var5, var6, var16, var9);
         var8 = true;
      }

      if (var9 != null && Config.isBetterSnow() && !var9.isBreakingAnimation() && BetterSnow.shouldRender(var1, var3, var4, var5)) {
         IBakedModel var17 = BetterSnow.getModelSnowLayer();
         IBlockState var18 = BetterSnow.getStateSnowLayer();
         this.renderModelAmbientOcclusion(var1, var17, var18.getBlock(), var18, var5, var6, true);
      }

      return var8;
   }

   public void renderModelBrightness(IBakedModel var1, IBlockState var2, float var3, boolean var4) {
      Block var5 = var2.getBlock();
      var5.setBlockBoundsForItemRender();
      GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
      int var6 = var5.getRenderColor(var5.getStateForEntityRender(var2));
      if (EntityRenderer.anaglyphEnable) {
         var6 = TextureUtil.anaglyphColor(var6);
      }

      float var7 = (float)(var6 >> 16 & 255) / 255.0F;
      float var8 = (float)(var6 >> 8 & 255) / 255.0F;
      float var9 = (float)(var6 & 255) / 255.0F;
      if (!var4) {
         GlStateManager.color(var3, var3, var3, 1.0F);
      }

      this.renderModelBrightnessColor(var1, var3, var7, var8, var9);
   }

   public boolean renderModelStandard(IBlockAccess var1, IBakedModel var2, Block var3, IBlockState var4, BlockPos var5, WorldRenderer var6, boolean var7) {
      boolean var8 = false;
      RenderEnv var9 = null;
      EnumFacing[] var13;
      int var12 = (var13 = EnumFacing.VALUES).length;

      for(int var11 = 0; var11 < var12; ++var11) {
         EnumFacing var10 = var13[var11];
         List var14 = var2.getFaceQuads(var10);
         if (!var14.isEmpty()) {
            BlockPos var15 = var5.offset(var10);
            if (!var7 || var3.shouldSideBeRendered(var1, var15, var10)) {
               if (var9 == null) {
                  var9 = RenderEnv.getInstance(var1, var4, var5);
               }

               if (!var9.isBreakingAnimation(var14) && Config.isBetterGrass()) {
                  var14 = BetterGrass.getFaceQuads(var1, var3, var5, var10, var14);
               }

               int var16 = var3.getMixedBrightnessForBlock(var1, var15);
               this.renderModelStandardQuads(var1, var3, var5, var10, var16, false, var6, var14, var9);
               var8 = true;
            }
         }
      }

      List var17 = var2.getGeneralQuads();
      if (var17.size() > 0) {
         if (var9 == null) {
            var9 = RenderEnv.getInstance(var1, var4, var5);
         }

         this.renderModelStandardQuads(var1, var3, var5, (EnumFacing)null, -1, true, var6, var17, var9);
         var8 = true;
      }

      if (var9 != null && Config.isBetterSnow() && !var9.isBreakingAnimation() && BetterSnow.shouldRender(var1, var3, var4, var5) && BetterSnow.shouldRender(var1, var3, var4, var5)) {
         IBakedModel var18 = BetterSnow.getModelSnowLayer();
         IBlockState var19 = BetterSnow.getStateSnowLayer();
         this.renderModelStandard(var1, var18, var19.getBlock(), var19, var5, var6, true);
      }

      return var8;
   }

   private void fillQuadBounds(Block var1, int[] var2, EnumFacing var3, float[] var4, BitSet var5) {
      float var6 = 32.0F;
      float var7 = 32.0F;
      float var8 = 32.0F;
      float var9 = -32.0F;
      float var10 = -32.0F;
      float var11 = -32.0F;
      int var12 = var2.length / 4;

      float var14;
      for(int var13 = 0; var13 < 4; ++var13) {
         var14 = Float.intBitsToFloat(var2[var13 * var12]);
         float var15 = Float.intBitsToFloat(var2[var13 * var12 + 1]);
         float var16 = Float.intBitsToFloat(var2[var13 * var12 + 2]);
         var6 = Math.min(var6, var14);
         var7 = Math.min(var7, var15);
         var8 = Math.min(var8, var16);
         var9 = Math.max(var9, var14);
         var10 = Math.max(var10, var15);
         var11 = Math.max(var11, var16);
      }

      if (var4 != null) {
         var4[EnumFacing.WEST.getIndex()] = var6;
         var4[EnumFacing.EAST.getIndex()] = var9;
         var4[EnumFacing.DOWN.getIndex()] = var7;
         var4[EnumFacing.UP.getIndex()] = var10;
         var4[EnumFacing.NORTH.getIndex()] = var8;
         var4[EnumFacing.SOUTH.getIndex()] = var11;
         var4[EnumFacing.WEST.getIndex() + EnumFacing.VALUES.length] = 1.0F - var6;
         var4[EnumFacing.EAST.getIndex() + EnumFacing.VALUES.length] = 1.0F - var9;
         var4[EnumFacing.DOWN.getIndex() + EnumFacing.VALUES.length] = 1.0F - var7;
         var4[EnumFacing.UP.getIndex() + EnumFacing.VALUES.length] = 1.0F - var10;
         var4[EnumFacing.NORTH.getIndex() + EnumFacing.VALUES.length] = 1.0F - var8;
         var4[EnumFacing.SOUTH.getIndex() + EnumFacing.VALUES.length] = 1.0F - var11;
      }

      float var17 = 1.0E-4F;
      var14 = 0.9999F;
      switch(var3) {
      case DOWN:
         var5.set(1, var6 >= 1.0E-4F || var8 >= 1.0E-4F || var9 <= 0.9999F || var11 <= 0.9999F);
         var5.set(0, (var7 < 1.0E-4F || var1.isFullCube()) && var7 == var10);
         break;
      case UP:
         var5.set(1, var6 >= 1.0E-4F || var8 >= 1.0E-4F || var9 <= 0.9999F || var11 <= 0.9999F);
         var5.set(0, (var10 > 0.9999F || var1.isFullCube()) && var7 == var10);
         break;
      case NORTH:
         var5.set(1, var6 >= 1.0E-4F || var7 >= 1.0E-4F || var9 <= 0.9999F || var10 <= 0.9999F);
         var5.set(0, (var8 < 1.0E-4F || var1.isFullCube()) && var8 == var11);
         break;
      case SOUTH:
         var5.set(1, var6 >= 1.0E-4F || var7 >= 1.0E-4F || var9 <= 0.9999F || var10 <= 0.9999F);
         var5.set(0, (var11 > 0.9999F || var1.isFullCube()) && var8 == var11);
         break;
      case WEST:
         var5.set(1, var7 >= 1.0E-4F || var8 >= 1.0E-4F || var10 <= 0.9999F || var11 <= 0.9999F);
         var5.set(0, (var6 < 1.0E-4F || var1.isFullCube()) && var6 == var9);
         break;
      case EAST:
         var5.set(1, var7 >= 1.0E-4F || var8 >= 1.0E-4F || var10 <= 0.9999F || var11 <= 0.9999F);
         var5.set(0, (var9 > 0.9999F || var1.isFullCube()) && var6 == var9);
      }

   }

   public boolean renderModel(IBlockAccess var1, IBakedModel var2, IBlockState var3, BlockPos var4, WorldRenderer var5) {
      Block var6 = var3.getBlock();
      var6.setBlockBoundsBasedOnState(var1, var4);
      return this.renderModel(var1, var2, var3, var4, var5, true);
   }

   public void renderModelBrightnessColor(IBakedModel var1, float var2, float var3, float var4, float var5) {
      EnumFacing[] var9;
      int var8 = (var9 = EnumFacing.VALUES).length;

      for(int var7 = 0; var7 < var8; ++var7) {
         EnumFacing var6 = var9[var7];
         this.renderModelBrightnessColorQuads(var2, var3, var4, var5, var1.getFaceQuads(var6));
      }

      this.renderModelBrightnessColorQuads(var2, var3, var4, var5, var1.getGeneralQuads());
   }

   public static void updateAoLightValue() {
      aoLightValueOpaque = 1.0F - Config.getAmbientOcclusionLevel() * 0.8F;
   }

   public boolean renderModelAmbientOcclusion(IBlockAccess var1, IBakedModel var2, Block var3, BlockPos var4, WorldRenderer var5, boolean var6) {
      return this.renderModelAmbientOcclusion(var1, var2, var3, var1.getBlockState(var4), var4, var5, var6);
   }

   private void renderModelStandardQuads(IBlockAccess var1, Block var2, BlockPos var3, EnumFacing var4, int var5, boolean var6, WorldRenderer var7, List var8, RenderEnv var9) {
      BitSet var10 = var9.getBoundsFlags();
      IBlockState var11 = var9.getBlockState();
      double var12 = (double)var3.getX();
      double var14 = (double)var3.getY();
      double var16 = (double)var3.getZ();
      Block.EnumOffsetType var18 = var2.getOffsetType();
      if (var18 != Block.EnumOffsetType.NONE) {
         int var19 = var3.getX();
         int var20 = var3.getZ();
         long var21 = (long)(var19 * 3129871) ^ (long)var20 * 116129781L;
         var21 = var21 * var21 * 42317861L + var21 * 11L;
         var12 += ((double)((float)(var21 >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
         var16 += ((double)((float)(var21 >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;
         if (var18 == Block.EnumOffsetType.XYZ) {
            var14 += ((double)((float)(var21 >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
         }
      }

      for(Iterator var28 = var8.iterator(); var28.hasNext(); var7.putPosition(var12, var14, var16)) {
         Object var27 = var28.next();
         BakedQuad var29 = (BakedQuad)var27;
         if (!var9.isBreakingAnimation(var29)) {
            BakedQuad var22 = var29;
            if (Config.isConnectedTextures()) {
               var29 = ConnectedTextures.getConnectedTexture(var1, var11, var3, var29, var9);
            }

            if (var29 == var22 && Config.isNaturalTextures()) {
               var29 = NaturalTextures.getNaturalTexture(var3, var29);
            }
         }

         if (var6) {
            this.fillQuadBounds(var2, var29.getVertexData(), var29.getFace(), (float[])null, var10);
            var5 = var10.get(0) ? var2.getMixedBrightnessForBlock(var1, var3.offset(var29.getFace())) : var2.getMixedBrightnessForBlock(var1, var3);
         }

         if (var7.isMultiTexture()) {
            var7.addVertexData(var29.getVertexDataSingle());
            var7.putSprite(var29.getSprite());
         } else {
            var7.addVertexData(var29.getVertexData());
         }

         var7.putBrightness4(var5, var5, var5, var5);
         int var30 = CustomColors.getColorMultiplier(var29, var2, var1, var3, var9);
         if (var29.hasTintIndex() || var30 != -1) {
            int var23;
            if (var30 != -1) {
               var23 = var30;
            } else {
               var23 = var2.colorMultiplier(var1, var3, var29.getTintIndex());
            }

            if (EntityRenderer.anaglyphEnable) {
               var23 = TextureUtil.anaglyphColor(var23);
            }

            float var24 = (float)(var23 >> 16 & 255) / 255.0F;
            float var25 = (float)(var23 >> 8 & 255) / 255.0F;
            float var26 = (float)(var23 & 255) / 255.0F;
            var7.putColorMultiplier(var24, var25, var26, 4);
            var7.putColorMultiplier(var24, var25, var26, 3);
            var7.putColorMultiplier(var24, var25, var26, 2);
            var7.putColorMultiplier(var24, var25, var26, 1);
         }
      }

   }

   public static float fixAoLightValue(float var0) {
      return var0 == 0.2F ? aoLightValueOpaque : var0;
   }

   public boolean renderModelStandard(IBlockAccess var1, IBakedModel var2, Block var3, BlockPos var4, WorldRenderer var5, boolean var6) {
      return this.renderModelStandard(var1, var2, var3, var1.getBlockState(var4), var4, var5, var6);
   }

   static final class BlockModelRenderer$1 {
      static final int[] field_178290_a = new int[EnumFacing.values().length];
      private static final String __OBFID = "CL_00002517";

      static {
         try {
            field_178290_a[EnumFacing.DOWN.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
         }

         try {
            field_178290_a[EnumFacing.UP.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
         }

         try {
            field_178290_a[EnumFacing.NORTH.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
         }

         try {
            field_178290_a[EnumFacing.SOUTH.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
         }

         try {
            field_178290_a[EnumFacing.WEST.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
         }

         try {
            field_178290_a[EnumFacing.EAST.ordinal()] = 6;
         } catch (NoSuchFieldError var1) {
         }

      }
   }

   public static enum EnumNeighborInfo {
      protected final BlockModelRenderer.Orientation[] field_178284_l;
      protected final BlockModelRenderer.Orientation[] field_178286_j;
      NORTH("NORTH", 2, new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.EAST, EnumFacing.WEST}, 0.8F, true, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_WEST}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_EAST}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_EAST}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_WEST});

      private static final String __OBFID = "CL_00002516";
      protected final BlockModelRenderer.Orientation[] field_178287_k;
      EAST("EAST", 5, new EnumFacing[]{EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH}, 0.6F, true, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.SOUTH, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.SOUTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.NORTH, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.NORTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.NORTH, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.NORTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.SOUTH, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.SOUTH});

      private static final BlockModelRenderer.EnumNeighborInfo[] field_178282_n = new BlockModelRenderer.EnumNeighborInfo[6];
      protected final BlockModelRenderer.Orientation[] field_178285_m;
      SOUTH("SOUTH", 3, new EnumFacing[]{EnumFacing.WEST, EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.UP}, 0.8F, true, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.WEST}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.WEST}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.EAST}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.EAST});

      protected final EnumFacing[] field_178276_g;
      UP("UP", 1, new EnumFacing[]{EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH}, 1.0F, true, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.SOUTH, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.SOUTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.NORTH, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.NORTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.NORTH, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.NORTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.SOUTH, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.SOUTH});

      protected final float field_178288_h;
      private static final BlockModelRenderer.EnumNeighborInfo[] $VALUES = new BlockModelRenderer.EnumNeighborInfo[]{DOWN, UP, NORTH, SOUTH, WEST, EAST};
      DOWN("DOWN", 0, new EnumFacing[]{EnumFacing.WEST, EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.SOUTH}, 0.5F, true, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.SOUTH, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.SOUTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.NORTH, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.NORTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.NORTH, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.NORTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.SOUTH, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.SOUTH}),
      WEST("WEST", 4, new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.SOUTH}, 0.6F, true, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.SOUTH, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.SOUTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.NORTH, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.NORTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.NORTH, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.NORTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.SOUTH, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.SOUTH});

      protected final boolean field_178289_i;
      private static final BlockModelRenderer.EnumNeighborInfo[] ENUM$VALUES = new BlockModelRenderer.EnumNeighborInfo[]{DOWN, UP, NORTH, SOUTH, WEST, EAST};

      public static BlockModelRenderer.EnumNeighborInfo getNeighbourInfo(EnumFacing var0) {
         return field_178282_n[var0.getIndex()];
      }

      private EnumNeighborInfo(String var3, int var4, EnumFacing[] var5, float var6, boolean var7, BlockModelRenderer.Orientation[] var8, BlockModelRenderer.Orientation[] var9, BlockModelRenderer.Orientation[] var10, BlockModelRenderer.Orientation[] var11) {
         this.field_178276_g = var5;
         this.field_178288_h = var6;
         this.field_178289_i = var7;
         this.field_178286_j = var8;
         this.field_178287_k = var9;
         this.field_178284_l = var10;
         this.field_178285_m = var11;
      }

      static {
         field_178282_n[EnumFacing.DOWN.getIndex()] = DOWN;
         field_178282_n[EnumFacing.UP.getIndex()] = UP;
         field_178282_n[EnumFacing.NORTH.getIndex()] = NORTH;
         field_178282_n[EnumFacing.SOUTH.getIndex()] = SOUTH;
         field_178282_n[EnumFacing.WEST.getIndex()] = WEST;
         field_178282_n[EnumFacing.EAST.getIndex()] = EAST;
      }
   }

   static enum VertexTranslations {
      SOUTH("SOUTH", 3, 0, 1, 2, 3);

      private final int field_178198_j;
      private final int field_178200_h;
      private static final BlockModelRenderer.VertexTranslations[] ENUM$VALUES = new BlockModelRenderer.VertexTranslations[]{DOWN, UP, NORTH, SOUTH, WEST, EAST};
      NORTH("NORTH", 2, 3, 0, 1, 2),
      UP("UP", 1, 2, 3, 0, 1);

      private static final BlockModelRenderer.VertexTranslations[] field_178199_k = new BlockModelRenderer.VertexTranslations[6];
      private static final String __OBFID = "CL_00002514";
      private final int field_178191_g;
      private static final BlockModelRenderer.VertexTranslations[] $VALUES = new BlockModelRenderer.VertexTranslations[]{DOWN, UP, NORTH, SOUTH, WEST, EAST};
      WEST("WEST", 4, 3, 0, 1, 2),
      EAST("EAST", 5, 1, 2, 3, 0);

      private final int field_178201_i;
      DOWN("DOWN", 0, 0, 1, 2, 3);

      static int access$5(BlockModelRenderer.VertexTranslations var0) {
         return var0.field_178198_j;
      }

      public static BlockModelRenderer.VertexTranslations getVertexTranslations(EnumFacing var0) {
         return field_178199_k[var0.getIndex()];
      }

      static int access$2(BlockModelRenderer.VertexTranslations var0) {
         return var0.field_178191_g;
      }

      static {
         field_178199_k[EnumFacing.DOWN.getIndex()] = DOWN;
         field_178199_k[EnumFacing.UP.getIndex()] = UP;
         field_178199_k[EnumFacing.NORTH.getIndex()] = NORTH;
         field_178199_k[EnumFacing.SOUTH.getIndex()] = SOUTH;
         field_178199_k[EnumFacing.WEST.getIndex()] = WEST;
         field_178199_k[EnumFacing.EAST.getIndex()] = EAST;
      }

      static int access$3(BlockModelRenderer.VertexTranslations var0) {
         return var0.field_178200_h;
      }

      private VertexTranslations(String var3, int var4, int var5, int var6, int var7, int var8) {
         this.field_178191_g = var5;
         this.field_178200_h = var6;
         this.field_178201_i = var7;
         this.field_178198_j = var8;
      }

      static int access$4(BlockModelRenderer.VertexTranslations var0) {
         return var0.field_178201_i;
      }
   }

   public static class AmbientOcclusionFace {
      private static final String __OBFID = "CL_00002515";
      private final int[] vertexBrightness = new int[4];
      private final float[] vertexColorMultiplier = new float[4];

      private int getVertexBrightness(int var1, int var2, int var3, int var4, float var5, float var6, float var7, float var8) {
         int var9 = (int)((float)(var1 >> 16 & 255) * var5 + (float)(var2 >> 16 & 255) * var6 + (float)(var3 >> 16 & 255) * var7 + (float)(var4 >> 16 & 255) * var8) & 255;
         int var10 = (int)((float)(var1 & 255) * var5 + (float)(var2 & 255) * var6 + (float)(var3 & 255) * var7 + (float)(var4 & 255) * var8) & 255;
         return var9 << 16 | var10;
      }

      static int[] access$0(BlockModelRenderer.AmbientOcclusionFace var0) {
         return var0.vertexBrightness;
      }

      static float[] access$1(BlockModelRenderer.AmbientOcclusionFace var0) {
         return var0.vertexColorMultiplier;
      }

      public void updateVertexBrightness(IBlockAccess var1, Block var2, BlockPos var3, EnumFacing var4, float[] var5, BitSet var6) {
         BlockPos var7 = var6.get(0) ? var3.offset(var4) : var3;
         BlockModelRenderer.EnumNeighborInfo var8 = BlockModelRenderer.EnumNeighborInfo.getNeighbourInfo(var4);
         BlockPos var9 = var7.offset(var8.field_178276_g[0]);
         BlockPos var10 = var7.offset(var8.field_178276_g[1]);
         BlockPos var11 = var7.offset(var8.field_178276_g[2]);
         BlockPos var12 = var7.offset(var8.field_178276_g[3]);
         int var13 = var2.getMixedBrightnessForBlock(var1, var9);
         int var14 = var2.getMixedBrightnessForBlock(var1, var10);
         int var15 = var2.getMixedBrightnessForBlock(var1, var11);
         int var16 = var2.getMixedBrightnessForBlock(var1, var12);
         float var17 = BlockModelRenderer.fixAoLightValue(var1.getBlockState(var9).getBlock().getAmbientOcclusionLightValue());
         float var18 = BlockModelRenderer.fixAoLightValue(var1.getBlockState(var10).getBlock().getAmbientOcclusionLightValue());
         float var19 = BlockModelRenderer.fixAoLightValue(var1.getBlockState(var11).getBlock().getAmbientOcclusionLightValue());
         float var20 = BlockModelRenderer.fixAoLightValue(var1.getBlockState(var12).getBlock().getAmbientOcclusionLightValue());
         boolean var21 = var1.getBlockState(var9.offset(var4)).getBlock().isTranslucent();
         boolean var22 = var1.getBlockState(var10.offset(var4)).getBlock().isTranslucent();
         boolean var23 = var1.getBlockState(var11.offset(var4)).getBlock().isTranslucent();
         boolean var24 = var1.getBlockState(var12.offset(var4)).getBlock().isTranslucent();
         float var25;
         int var26;
         if (!var23 && !var21) {
            var25 = var17;
            var26 = var13;
         } else {
            BlockPos var27 = var9.offset(var8.field_178276_g[2]);
            var25 = BlockModelRenderer.fixAoLightValue(var1.getBlockState(var27).getBlock().getAmbientOcclusionLightValue());
            var26 = var2.getMixedBrightnessForBlock(var1, var27);
         }

         int var28;
         float var61;
         if (!var24 && !var21) {
            var61 = var17;
            var28 = var13;
         } else {
            BlockPos var29 = var9.offset(var8.field_178276_g[3]);
            var61 = BlockModelRenderer.fixAoLightValue(var1.getBlockState(var29).getBlock().getAmbientOcclusionLightValue());
            var28 = var2.getMixedBrightnessForBlock(var1, var29);
         }

         int var30;
         float var62;
         if (!var23 && !var22) {
            var62 = var18;
            var30 = var14;
         } else {
            BlockPos var31 = var10.offset(var8.field_178276_g[2]);
            var62 = BlockModelRenderer.fixAoLightValue(var1.getBlockState(var31).getBlock().getAmbientOcclusionLightValue());
            var30 = var2.getMixedBrightnessForBlock(var1, var31);
         }

         int var32;
         float var63;
         if (!var24 && !var22) {
            var63 = var18;
            var32 = var14;
         } else {
            BlockPos var33 = var10.offset(var8.field_178276_g[3]);
            var63 = BlockModelRenderer.fixAoLightValue(var1.getBlockState(var33).getBlock().getAmbientOcclusionLightValue());
            var32 = var2.getMixedBrightnessForBlock(var1, var33);
         }

         int var64 = var2.getMixedBrightnessForBlock(var1, var3);
         if (var6.get(0) || !var1.getBlockState(var3.offset(var4)).getBlock().isOpaqueCube()) {
            var64 = var2.getMixedBrightnessForBlock(var1, var3.offset(var4));
         }

         float var34 = var6.get(0) ? var1.getBlockState(var7).getBlock().getAmbientOcclusionLightValue() : var1.getBlockState(var3).getBlock().getAmbientOcclusionLightValue();
         var34 = BlockModelRenderer.fixAoLightValue(var34);
         BlockModelRenderer.VertexTranslations var35 = BlockModelRenderer.VertexTranslations.getVertexTranslations(var4);
         float var36;
         float var37;
         float var38;
         float var39;
         if (var6.get(1) && var8.field_178289_i) {
            var36 = (var20 + var17 + var61 + var34) * 0.25F;
            var37 = (var19 + var17 + var25 + var34) * 0.25F;
            var38 = (var19 + var18 + var62 + var34) * 0.25F;
            var39 = (var20 + var18 + var63 + var34) * 0.25F;
            float var40 = var5[var8.field_178286_j[0].field_178229_m] * var5[var8.field_178286_j[1].field_178229_m];
            float var41 = var5[var8.field_178286_j[2].field_178229_m] * var5[var8.field_178286_j[3].field_178229_m];
            float var42 = var5[var8.field_178286_j[4].field_178229_m] * var5[var8.field_178286_j[5].field_178229_m];
            float var43 = var5[var8.field_178286_j[6].field_178229_m] * var5[var8.field_178286_j[7].field_178229_m];
            float var44 = var5[var8.field_178287_k[0].field_178229_m] * var5[var8.field_178287_k[1].field_178229_m];
            float var45 = var5[var8.field_178287_k[2].field_178229_m] * var5[var8.field_178287_k[3].field_178229_m];
            float var46 = var5[var8.field_178287_k[4].field_178229_m] * var5[var8.field_178287_k[5].field_178229_m];
            float var47 = var5[var8.field_178287_k[6].field_178229_m] * var5[var8.field_178287_k[7].field_178229_m];
            float var48 = var5[var8.field_178284_l[0].field_178229_m] * var5[var8.field_178284_l[1].field_178229_m];
            float var49 = var5[var8.field_178284_l[2].field_178229_m] * var5[var8.field_178284_l[3].field_178229_m];
            float var50 = var5[var8.field_178284_l[4].field_178229_m] * var5[var8.field_178284_l[5].field_178229_m];
            float var51 = var5[var8.field_178284_l[6].field_178229_m] * var5[var8.field_178284_l[7].field_178229_m];
            float var52 = var5[var8.field_178285_m[0].field_178229_m] * var5[var8.field_178285_m[1].field_178229_m];
            float var53 = var5[var8.field_178285_m[2].field_178229_m] * var5[var8.field_178285_m[3].field_178229_m];
            float var54 = var5[var8.field_178285_m[4].field_178229_m] * var5[var8.field_178285_m[5].field_178229_m];
            float var55 = var5[var8.field_178285_m[6].field_178229_m] * var5[var8.field_178285_m[7].field_178229_m];
            this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$2(var35)] = var36 * var40 + var37 * var41 + var38 * var42 + var39 * var43;
            this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$3(var35)] = var36 * var44 + var37 * var45 + var38 * var46 + var39 * var47;
            this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$4(var35)] = var36 * var48 + var37 * var49 + var38 * var50 + var39 * var51;
            this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$5(var35)] = var36 * var52 + var37 * var53 + var38 * var54 + var39 * var55;
            int var56 = this.getAoBrightness(var16, var13, var28, var64);
            int var57 = this.getAoBrightness(var15, var13, var26, var64);
            int var58 = this.getAoBrightness(var15, var14, var30, var64);
            int var59 = this.getAoBrightness(var16, var14, var32, var64);
            this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$2(var35)] = this.getVertexBrightness(var56, var57, var58, var59, var40, var41, var42, var43);
            this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$3(var35)] = this.getVertexBrightness(var56, var57, var58, var59, var44, var45, var46, var47);
            this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$4(var35)] = this.getVertexBrightness(var56, var57, var58, var59, var48, var49, var50, var51);
            this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$5(var35)] = this.getVertexBrightness(var56, var57, var58, var59, var52, var53, var54, var55);
         } else {
            var36 = (var20 + var17 + var61 + var34) * 0.25F;
            var37 = (var19 + var17 + var25 + var34) * 0.25F;
            var38 = (var19 + var18 + var62 + var34) * 0.25F;
            var39 = (var20 + var18 + var63 + var34) * 0.25F;
            this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$2(var35)] = this.getAoBrightness(var16, var13, var28, var64);
            this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$3(var35)] = this.getAoBrightness(var15, var13, var26, var64);
            this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$4(var35)] = this.getAoBrightness(var15, var14, var30, var64);
            this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$5(var35)] = this.getAoBrightness(var16, var14, var32, var64);
            this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$2(var35)] = var36;
            this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$3(var35)] = var37;
            this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$4(var35)] = var38;
            this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$5(var35)] = var39;
         }

      }

      private int getAoBrightness(int var1, int var2, int var3, int var4) {
         if (var1 == 0) {
            var1 = var4;
         }

         if (var2 == 0) {
            var2 = var4;
         }

         if (var3 == 0) {
            var3 = var4;
         }

         return var1 + var2 + var3 + var4 >> 2 & 16711935;
      }

      public AmbientOcclusionFace() {
      }

      public AmbientOcclusionFace(BlockModelRenderer var1) {
      }
   }

   public static enum Orientation {
      FLIP_WEST("FLIP_WEST", 10, EnumFacing.WEST, true),
      FLIP_NORTH("FLIP_NORTH", 8, EnumFacing.NORTH, true),
      FLIP_SOUTH("FLIP_SOUTH", 9, EnumFacing.SOUTH, true);

      private static final BlockModelRenderer.Orientation[] $VALUES = new BlockModelRenderer.Orientation[]{DOWN, UP, NORTH, SOUTH, WEST, EAST, FLIP_DOWN, FLIP_UP, FLIP_NORTH, FLIP_SOUTH, FLIP_WEST, FLIP_EAST};
      FLIP_EAST("FLIP_EAST", 11, EnumFacing.EAST, true);

      private static final String __OBFID = "CL_00002513";
      NORTH("NORTH", 2, EnumFacing.NORTH, false),
      FLIP_UP("FLIP_UP", 7, EnumFacing.UP, true),
      SOUTH("SOUTH", 3, EnumFacing.SOUTH, false);

      protected final int field_178229_m;
      private static final BlockModelRenderer.Orientation[] ENUM$VALUES = new BlockModelRenderer.Orientation[]{DOWN, UP, NORTH, SOUTH, WEST, EAST, FLIP_DOWN, FLIP_UP, FLIP_NORTH, FLIP_SOUTH, FLIP_WEST, FLIP_EAST};
      WEST("WEST", 4, EnumFacing.WEST, false),
      DOWN("DOWN", 0, EnumFacing.DOWN, false),
      EAST("EAST", 5, EnumFacing.EAST, false),
      UP("UP", 1, EnumFacing.UP, false),
      FLIP_DOWN("FLIP_DOWN", 6, EnumFacing.DOWN, true);

      private Orientation(String var3, int var4, EnumFacing var5, boolean var6) {
         this.field_178229_m = var5.getIndex() + (var6 ? EnumFacing.values().length : 0);
      }
   }
}
