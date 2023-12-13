package net.minecraft.client.renderer;

import java.util.BitSet;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.BlockModelRenderer.AmbientOcclusionFace;
import net.minecraft.client.renderer.BlockModelRenderer.BlockModelRenderer.1;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.src.BetterGrass;
import net.minecraft.src.BetterSnow;
import net.minecraft.src.Config;
import net.minecraft.src.ConnectedTextures;
import net.minecraft.src.CustomColors;
import net.minecraft.src.NaturalTextures;
import net.minecraft.src.Reflector;
import net.minecraft.src.RenderEnv;
import net.minecraft.src.SmartLeaves;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3i;
import net.minecraft.world.IBlockAccess;

public class BlockModelRenderer {
   private static final String __OBFID = "CL_00002518";
   private static float aoLightValueOpaque = 0.2F;

   public BlockModelRenderer() {
      if(Reflector.ForgeModContainer_forgeLightPipelineEnabled.exists()) {
         Reflector.setFieldValue(Reflector.ForgeModContainer_forgeLightPipelineEnabled, Boolean.valueOf(false));
      }

   }

   public static void updateAoLightValue() {
      aoLightValueOpaque = 1.0F - Config.getAmbientOcclusionLevel() * 0.8F;
   }

   public void renderModelBrightness(IBakedModel p_178266_1_, IBlockState p_178266_2_, float p_178266_3_, boolean p_178266_4_) {
      Block block = p_178266_2_.getBlock();
      block.setBlockBoundsForItemRender();
      GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
      int i = block.getRenderColor(block.getStateForEntityRender(p_178266_2_));
      if(EntityRenderer.anaglyphEnable) {
         i = TextureUtil.anaglyphColor(i);
      }

      float f = (float)(i >> 16 & 255) / 255.0F;
      float f1 = (float)(i >> 8 & 255) / 255.0F;
      float f2 = (float)(i & 255) / 255.0F;
      if(!p_178266_4_) {
         GlStateManager.color(p_178266_3_, p_178266_3_, p_178266_3_, 1.0F);
      }

      this.renderModelBrightnessColor(p_178266_1_, p_178266_3_, f, f1, f2);
   }

   public boolean renderModel(IBlockAccess blockAccessIn, IBakedModel modelIn, IBlockState blockStateIn, BlockPos blockPosIn, WorldRenderer worldRendererIn) {
      Block block = blockStateIn.getBlock();
      block.setBlockBoundsBasedOnState(blockAccessIn, blockPosIn);
      return this.renderModel(blockAccessIn, modelIn, blockStateIn, blockPosIn, worldRendererIn, true);
   }

   public boolean renderModel(IBlockAccess blockAccessIn, IBakedModel modelIn, IBlockState blockStateIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, boolean checkSides) {
      boolean flag = Minecraft.isAmbientOcclusionEnabled() && blockStateIn.getBlock().getLightValue() == 0 && modelIn.isAmbientOcclusion();

      try {
         Block block = blockStateIn.getBlock();
         if(Config.isTreesSmart() && blockStateIn.getBlock() instanceof BlockLeavesBase) {
            modelIn = SmartLeaves.getLeavesModel(modelIn);
         }

         return flag?this.renderModelAmbientOcclusion(blockAccessIn, modelIn, block, blockStateIn, blockPosIn, worldRendererIn, checkSides):this.renderModelStandard(blockAccessIn, modelIn, block, blockStateIn, blockPosIn, worldRendererIn, checkSides);
      } catch (Throwable var11) {
         CrashReport crashreport = CrashReport.makeCrashReport(var11, "Tesselating block model");
         CrashReportCategory crashreportcategory = crashreport.makeCategory("Block model being tesselated");
         CrashReportCategory.addBlockInfo(crashreportcategory, blockPosIn, blockStateIn);
         crashreportcategory.addCrashSection("Using AO", Boolean.valueOf(flag));
         throw new ReportedException(crashreport);
      }
   }

   public void renderModelBrightnessColor(IBakedModel bakedModel, float p_178262_2_, float p_178262_3_, float p_178262_4_, float p_178262_5_) {
      for(EnumFacing enumfacing : EnumFacing.VALUES) {
         this.renderModelBrightnessColorQuads(p_178262_2_, p_178262_3_, p_178262_4_, p_178262_5_, bakedModel.getFaceQuads(enumfacing));
      }

      this.renderModelBrightnessColorQuads(p_178262_2_, p_178262_3_, p_178262_4_, p_178262_5_, bakedModel.getGeneralQuads());
   }

   public boolean renderModelStandard(IBlockAccess p_renderModelStandard_1_, IBakedModel p_renderModelStandard_2_, Block p_renderModelStandard_3_, IBlockState p_renderModelStandard_4_, BlockPos p_renderModelStandard_5_, WorldRenderer p_renderModelStandard_6_, boolean p_renderModelStandard_7_) {
      boolean flag = false;
      RenderEnv renderenv = null;

      for(EnumFacing enumfacing : EnumFacing.VALUES) {
         List list = p_renderModelStandard_2_.getFaceQuads(enumfacing);
         if(!list.isEmpty()) {
            BlockPos blockpos = p_renderModelStandard_5_.offset(enumfacing);
            if(!p_renderModelStandard_7_ || p_renderModelStandard_3_.shouldSideBeRendered(p_renderModelStandard_1_, blockpos, enumfacing)) {
               if(renderenv == null) {
                  renderenv = RenderEnv.getInstance(p_renderModelStandard_1_, p_renderModelStandard_4_, p_renderModelStandard_5_);
               }

               if(!renderenv.isBreakingAnimation(list) && Config.isBetterGrass()) {
                  list = BetterGrass.getFaceQuads(p_renderModelStandard_1_, p_renderModelStandard_3_, p_renderModelStandard_5_, enumfacing, list);
               }

               int i = p_renderModelStandard_3_.getMixedBrightnessForBlock(p_renderModelStandard_1_, blockpos);
               this.renderModelStandardQuads(p_renderModelStandard_1_, p_renderModelStandard_3_, p_renderModelStandard_5_, enumfacing, i, false, p_renderModelStandard_6_, list, renderenv);
               flag = true;
            }
         }
      }

      List list1 = p_renderModelStandard_2_.getGeneralQuads();
      if(list1.size() > 0) {
         if(renderenv == null) {
            renderenv = RenderEnv.getInstance(p_renderModelStandard_1_, p_renderModelStandard_4_, p_renderModelStandard_5_);
         }

         this.renderModelStandardQuads(p_renderModelStandard_1_, p_renderModelStandard_3_, p_renderModelStandard_5_, (EnumFacing)null, -1, true, p_renderModelStandard_6_, list1, renderenv);
         flag = true;
      }

      if(renderenv != null && Config.isBetterSnow() && !renderenv.isBreakingAnimation() && BetterSnow.shouldRender(p_renderModelStandard_1_, p_renderModelStandard_3_, p_renderModelStandard_4_, p_renderModelStandard_5_) && BetterSnow.shouldRender(p_renderModelStandard_1_, p_renderModelStandard_3_, p_renderModelStandard_4_, p_renderModelStandard_5_)) {
         IBakedModel ibakedmodel = BetterSnow.getModelSnowLayer();
         IBlockState iblockstate = BetterSnow.getStateSnowLayer();
         this.renderModelStandard(p_renderModelStandard_1_, ibakedmodel, iblockstate.getBlock(), iblockstate, p_renderModelStandard_5_, p_renderModelStandard_6_, true);
      }

      return flag;
   }

   public boolean renderModelStandard(IBlockAccess blockAccessIn, IBakedModel modelIn, Block blockIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, boolean checkSides) {
      return this.renderModelStandard(blockAccessIn, modelIn, blockIn, blockAccessIn.getBlockState(blockPosIn), blockPosIn, worldRendererIn, checkSides);
   }

   private void renderModelStandardQuads(IBlockAccess p_renderModelStandardQuads_1_, Block p_renderModelStandardQuads_2_, BlockPos p_renderModelStandardQuads_3_, EnumFacing p_renderModelStandardQuads_4_, int p_renderModelStandardQuads_5_, boolean p_renderModelStandardQuads_6_, WorldRenderer p_renderModelStandardQuads_7_, List p_renderModelStandardQuads_8_, RenderEnv p_renderModelStandardQuads_9_) {
      BitSet bitset = p_renderModelStandardQuads_9_.getBoundsFlags();
      IBlockState iblockstate = p_renderModelStandardQuads_9_.getBlockState();
      double d0 = (double)p_renderModelStandardQuads_3_.getX();
      double d1 = (double)p_renderModelStandardQuads_3_.getY();
      double d2 = (double)p_renderModelStandardQuads_3_.getZ();
      Block.EnumOffsetType block$enumoffsettype = p_renderModelStandardQuads_2_.getOffsetType();
      if(block$enumoffsettype != Block.EnumOffsetType.NONE) {
         int i = p_renderModelStandardQuads_3_.getX();
         int j = p_renderModelStandardQuads_3_.getZ();
         long k = (long)(i * 3129871) ^ (long)j * 116129781L;
         k = k * k * 42317861L + k * 11L;
         d0 += ((double)((float)(k >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
         d2 += ((double)((float)(k >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;
         if(block$enumoffsettype == Block.EnumOffsetType.XYZ) {
            d1 += ((double)((float)(k >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
         }
      }

      for(BakedQuad bakedquad : p_renderModelStandardQuads_8_) {
         if(!p_renderModelStandardQuads_9_.isBreakingAnimation(bakedquad)) {
            BakedQuad bakedquad1 = bakedquad;
            if(Config.isConnectedTextures()) {
               bakedquad = ConnectedTextures.getConnectedTexture(p_renderModelStandardQuads_1_, iblockstate, p_renderModelStandardQuads_3_, bakedquad, p_renderModelStandardQuads_9_);
            }

            if(bakedquad == bakedquad1 && Config.isNaturalTextures()) {
               bakedquad = NaturalTextures.getNaturalTexture(p_renderModelStandardQuads_3_, bakedquad);
            }
         }

         if(p_renderModelStandardQuads_6_) {
            this.fillQuadBounds(p_renderModelStandardQuads_2_, bakedquad.getVertexData(), bakedquad.getFace(), (float[])null, bitset);
            p_renderModelStandardQuads_5_ = bitset.get(0)?p_renderModelStandardQuads_2_.getMixedBrightnessForBlock(p_renderModelStandardQuads_1_, p_renderModelStandardQuads_3_.offset(bakedquad.getFace())):p_renderModelStandardQuads_2_.getMixedBrightnessForBlock(p_renderModelStandardQuads_1_, p_renderModelStandardQuads_3_);
         }

         if(p_renderModelStandardQuads_7_.isMultiTexture()) {
            p_renderModelStandardQuads_7_.addVertexData(bakedquad.getVertexDataSingle());
            p_renderModelStandardQuads_7_.putSprite(bakedquad.getSprite());
         } else {
            p_renderModelStandardQuads_7_.addVertexData(bakedquad.getVertexData());
         }

         p_renderModelStandardQuads_7_.putBrightness4(p_renderModelStandardQuads_5_, p_renderModelStandardQuads_5_, p_renderModelStandardQuads_5_, p_renderModelStandardQuads_5_);
         int i1 = CustomColors.getColorMultiplier(bakedquad, p_renderModelStandardQuads_2_, p_renderModelStandardQuads_1_, p_renderModelStandardQuads_3_, p_renderModelStandardQuads_9_);
         if(bakedquad.hasTintIndex() || i1 != -1) {
            int l;
            if(i1 != -1) {
               l = i1;
            } else {
               l = p_renderModelStandardQuads_2_.colorMultiplier(p_renderModelStandardQuads_1_, p_renderModelStandardQuads_3_, bakedquad.getTintIndex());
            }

            if(EntityRenderer.anaglyphEnable) {
               l = TextureUtil.anaglyphColor(l);
            }

            float f = (float)(l >> 16 & 255) / 255.0F;
            float f1 = (float)(l >> 8 & 255) / 255.0F;
            float f2 = (float)(l & 255) / 255.0F;
            p_renderModelStandardQuads_7_.putColorMultiplier(f, f1, f2, 4);
            p_renderModelStandardQuads_7_.putColorMultiplier(f, f1, f2, 3);
            p_renderModelStandardQuads_7_.putColorMultiplier(f, f1, f2, 2);
            p_renderModelStandardQuads_7_.putColorMultiplier(f, f1, f2, 1);
         }

         p_renderModelStandardQuads_7_.putPosition(d0, d1, d2);
      }

   }

   public boolean renderModelAmbientOcclusion(IBlockAccess blockAccessIn, IBakedModel modelIn, Block blockIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, boolean checkSides) {
      return this.renderModelAmbientOcclusion(blockAccessIn, modelIn, blockIn, blockAccessIn.getBlockState(blockPosIn), blockPosIn, worldRendererIn, checkSides);
   }

   public boolean renderModelAmbientOcclusion(IBlockAccess p_renderModelAmbientOcclusion_1_, IBakedModel p_renderModelAmbientOcclusion_2_, Block p_renderModelAmbientOcclusion_3_, IBlockState p_renderModelAmbientOcclusion_4_, BlockPos p_renderModelAmbientOcclusion_5_, WorldRenderer p_renderModelAmbientOcclusion_6_, boolean p_renderModelAmbientOcclusion_7_) {
      boolean flag = false;
      RenderEnv renderenv = null;

      for(EnumFacing enumfacing : EnumFacing.VALUES) {
         List list = p_renderModelAmbientOcclusion_2_.getFaceQuads(enumfacing);
         if(!list.isEmpty()) {
            BlockPos blockpos = p_renderModelAmbientOcclusion_5_.offset(enumfacing);
            if(!p_renderModelAmbientOcclusion_7_ || p_renderModelAmbientOcclusion_3_.shouldSideBeRendered(p_renderModelAmbientOcclusion_1_, blockpos, enumfacing)) {
               if(renderenv == null) {
                  renderenv = RenderEnv.getInstance(p_renderModelAmbientOcclusion_1_, p_renderModelAmbientOcclusion_4_, p_renderModelAmbientOcclusion_5_);
               }

               if(!renderenv.isBreakingAnimation(list) && Config.isBetterGrass()) {
                  list = BetterGrass.getFaceQuads(p_renderModelAmbientOcclusion_1_, p_renderModelAmbientOcclusion_3_, p_renderModelAmbientOcclusion_5_, enumfacing, list);
               }

               this.renderModelAmbientOcclusionQuads(p_renderModelAmbientOcclusion_1_, p_renderModelAmbientOcclusion_3_, p_renderModelAmbientOcclusion_5_, p_renderModelAmbientOcclusion_6_, list, renderenv);
               flag = true;
            }
         }
      }

      List list1 = p_renderModelAmbientOcclusion_2_.getGeneralQuads();
      if(list1.size() > 0) {
         if(renderenv == null) {
            renderenv = RenderEnv.getInstance(p_renderModelAmbientOcclusion_1_, p_renderModelAmbientOcclusion_4_, p_renderModelAmbientOcclusion_5_);
         }

         this.renderModelAmbientOcclusionQuads(p_renderModelAmbientOcclusion_1_, p_renderModelAmbientOcclusion_3_, p_renderModelAmbientOcclusion_5_, p_renderModelAmbientOcclusion_6_, list1, renderenv);
         flag = true;
      }

      if(renderenv != null && Config.isBetterSnow() && !renderenv.isBreakingAnimation() && BetterSnow.shouldRender(p_renderModelAmbientOcclusion_1_, p_renderModelAmbientOcclusion_3_, p_renderModelAmbientOcclusion_4_, p_renderModelAmbientOcclusion_5_)) {
         IBakedModel ibakedmodel = BetterSnow.getModelSnowLayer();
         IBlockState iblockstate = BetterSnow.getStateSnowLayer();
         this.renderModelAmbientOcclusion(p_renderModelAmbientOcclusion_1_, ibakedmodel, iblockstate.getBlock(), iblockstate, p_renderModelAmbientOcclusion_5_, p_renderModelAmbientOcclusion_6_, true);
      }

      return flag;
   }

   private void renderModelAmbientOcclusionQuads(IBlockAccess p_renderModelAmbientOcclusionQuads_1_, Block p_renderModelAmbientOcclusionQuads_2_, BlockPos p_renderModelAmbientOcclusionQuads_3_, WorldRenderer p_renderModelAmbientOcclusionQuads_4_, List p_renderModelAmbientOcclusionQuads_5_, RenderEnv p_renderModelAmbientOcclusionQuads_6_) {
      float[] afloat = p_renderModelAmbientOcclusionQuads_6_.getQuadBounds();
      BitSet bitset = p_renderModelAmbientOcclusionQuads_6_.getBoundsFlags();
      AmbientOcclusionFace blockmodelrenderer$ambientocclusionface = p_renderModelAmbientOcclusionQuads_6_.getAoFace();
      IBlockState iblockstate = p_renderModelAmbientOcclusionQuads_6_.getBlockState();
      double d0 = (double)p_renderModelAmbientOcclusionQuads_3_.getX();
      double d1 = (double)p_renderModelAmbientOcclusionQuads_3_.getY();
      double d2 = (double)p_renderModelAmbientOcclusionQuads_3_.getZ();
      Block.EnumOffsetType block$enumoffsettype = p_renderModelAmbientOcclusionQuads_2_.getOffsetType();
      if(block$enumoffsettype != Block.EnumOffsetType.NONE) {
         long i = MathHelper.getPositionRandom(p_renderModelAmbientOcclusionQuads_3_);
         d0 += ((double)((float)(i >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
         d2 += ((double)((float)(i >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;
         if(block$enumoffsettype == Block.EnumOffsetType.XYZ) {
            d1 += ((double)((float)(i >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
         }
      }

      for(BakedQuad bakedquad : p_renderModelAmbientOcclusionQuads_5_) {
         if(!p_renderModelAmbientOcclusionQuads_6_.isBreakingAnimation(bakedquad)) {
            BakedQuad bakedquad1 = bakedquad;
            if(Config.isConnectedTextures()) {
               bakedquad = ConnectedTextures.getConnectedTexture(p_renderModelAmbientOcclusionQuads_1_, iblockstate, p_renderModelAmbientOcclusionQuads_3_, bakedquad, p_renderModelAmbientOcclusionQuads_6_);
            }

            if(bakedquad == bakedquad1 && Config.isNaturalTextures()) {
               bakedquad = NaturalTextures.getNaturalTexture(p_renderModelAmbientOcclusionQuads_3_, bakedquad);
            }
         }

         this.fillQuadBounds(p_renderModelAmbientOcclusionQuads_2_, bakedquad.getVertexData(), bakedquad.getFace(), afloat, bitset);
         blockmodelrenderer$ambientocclusionface.updateVertexBrightness(p_renderModelAmbientOcclusionQuads_1_, p_renderModelAmbientOcclusionQuads_2_, p_renderModelAmbientOcclusionQuads_3_, bakedquad.getFace(), afloat, bitset);
         if(p_renderModelAmbientOcclusionQuads_4_.isMultiTexture()) {
            p_renderModelAmbientOcclusionQuads_4_.addVertexData(bakedquad.getVertexDataSingle());
            p_renderModelAmbientOcclusionQuads_4_.putSprite(bakedquad.getSprite());
         } else {
            p_renderModelAmbientOcclusionQuads_4_.addVertexData(bakedquad.getVertexData());
         }

         p_renderModelAmbientOcclusionQuads_4_.putBrightness4(AmbientOcclusionFace.access$000(blockmodelrenderer$ambientocclusionface)[0], AmbientOcclusionFace.access$000(blockmodelrenderer$ambientocclusionface)[1], AmbientOcclusionFace.access$000(blockmodelrenderer$ambientocclusionface)[2], AmbientOcclusionFace.access$000(blockmodelrenderer$ambientocclusionface)[3]);
         int k = CustomColors.getColorMultiplier(bakedquad, p_renderModelAmbientOcclusionQuads_2_, p_renderModelAmbientOcclusionQuads_1_, p_renderModelAmbientOcclusionQuads_3_, p_renderModelAmbientOcclusionQuads_6_);
         if(!bakedquad.hasTintIndex() && k == -1) {
            p_renderModelAmbientOcclusionQuads_4_.putColorMultiplier(AmbientOcclusionFace.access$100(blockmodelrenderer$ambientocclusionface)[0], AmbientOcclusionFace.access$100(blockmodelrenderer$ambientocclusionface)[0], AmbientOcclusionFace.access$100(blockmodelrenderer$ambientocclusionface)[0], 4);
            p_renderModelAmbientOcclusionQuads_4_.putColorMultiplier(AmbientOcclusionFace.access$100(blockmodelrenderer$ambientocclusionface)[1], AmbientOcclusionFace.access$100(blockmodelrenderer$ambientocclusionface)[1], AmbientOcclusionFace.access$100(blockmodelrenderer$ambientocclusionface)[1], 3);
            p_renderModelAmbientOcclusionQuads_4_.putColorMultiplier(AmbientOcclusionFace.access$100(blockmodelrenderer$ambientocclusionface)[2], AmbientOcclusionFace.access$100(blockmodelrenderer$ambientocclusionface)[2], AmbientOcclusionFace.access$100(blockmodelrenderer$ambientocclusionface)[2], 2);
            p_renderModelAmbientOcclusionQuads_4_.putColorMultiplier(AmbientOcclusionFace.access$100(blockmodelrenderer$ambientocclusionface)[3], AmbientOcclusionFace.access$100(blockmodelrenderer$ambientocclusionface)[3], AmbientOcclusionFace.access$100(blockmodelrenderer$ambientocclusionface)[3], 1);
         } else {
            int j;
            if(k != -1) {
               j = k;
            } else {
               j = p_renderModelAmbientOcclusionQuads_2_.colorMultiplier(p_renderModelAmbientOcclusionQuads_1_, p_renderModelAmbientOcclusionQuads_3_, bakedquad.getTintIndex());
            }

            if(EntityRenderer.anaglyphEnable) {
               j = TextureUtil.anaglyphColor(j);
            }

            float f = (float)(j >> 16 & 255) / 255.0F;
            float f1 = (float)(j >> 8 & 255) / 255.0F;
            float f2 = (float)(j & 255) / 255.0F;
            p_renderModelAmbientOcclusionQuads_4_.putColorMultiplier(AmbientOcclusionFace.access$100(blockmodelrenderer$ambientocclusionface)[0] * f, AmbientOcclusionFace.access$100(blockmodelrenderer$ambientocclusionface)[0] * f1, AmbientOcclusionFace.access$100(blockmodelrenderer$ambientocclusionface)[0] * f2, 4);
            p_renderModelAmbientOcclusionQuads_4_.putColorMultiplier(AmbientOcclusionFace.access$100(blockmodelrenderer$ambientocclusionface)[1] * f, AmbientOcclusionFace.access$100(blockmodelrenderer$ambientocclusionface)[1] * f1, AmbientOcclusionFace.access$100(blockmodelrenderer$ambientocclusionface)[1] * f2, 3);
            p_renderModelAmbientOcclusionQuads_4_.putColorMultiplier(AmbientOcclusionFace.access$100(blockmodelrenderer$ambientocclusionface)[2] * f, AmbientOcclusionFace.access$100(blockmodelrenderer$ambientocclusionface)[2] * f1, AmbientOcclusionFace.access$100(blockmodelrenderer$ambientocclusionface)[2] * f2, 2);
            p_renderModelAmbientOcclusionQuads_4_.putColorMultiplier(AmbientOcclusionFace.access$100(blockmodelrenderer$ambientocclusionface)[3] * f, AmbientOcclusionFace.access$100(blockmodelrenderer$ambientocclusionface)[3] * f1, AmbientOcclusionFace.access$100(blockmodelrenderer$ambientocclusionface)[3] * f2, 1);
         }

         p_renderModelAmbientOcclusionQuads_4_.putPosition(d0, d1, d2);
      }

   }

   private void renderModelBrightnessColorQuads(float p_178264_1_, float p_178264_2_, float p_178264_3_, float p_178264_4_, List p_178264_5_) {
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();

      for(BakedQuad bakedquad : p_178264_5_) {
         worldrenderer.begin(7, DefaultVertexFormats.ITEM);
         worldrenderer.addVertexData(bakedquad.getVertexData());
         if(bakedquad.hasTintIndex()) {
            worldrenderer.putColorRGB_F4(p_178264_2_ * p_178264_1_, p_178264_3_ * p_178264_1_, p_178264_4_ * p_178264_1_);
         } else {
            worldrenderer.putColorRGB_F4(p_178264_1_, p_178264_1_, p_178264_1_);
         }

         Vec3i vec3i = bakedquad.getFace().getDirectionVec();
         worldrenderer.putNormal((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
         tessellator.draw();
      }

   }

   private void fillQuadBounds(Block blockIn, int[] vertexData, EnumFacing facingIn, float[] quadBounds, BitSet boundsFlags) {
      float f = 32.0F;
      float f1 = 32.0F;
      float f2 = 32.0F;
      float f3 = -32.0F;
      float f4 = -32.0F;
      float f5 = -32.0F;
      int i = vertexData.length / 4;

      for(int j = 0; j < 4; ++j) {
         float f6 = Float.intBitsToFloat(vertexData[j * i]);
         float f7 = Float.intBitsToFloat(vertexData[j * i + 1]);
         float f8 = Float.intBitsToFloat(vertexData[j * i + 2]);
         f = Math.min(f, f6);
         f1 = Math.min(f1, f7);
         f2 = Math.min(f2, f8);
         f3 = Math.max(f3, f6);
         f4 = Math.max(f4, f7);
         f5 = Math.max(f5, f8);
      }

      if(quadBounds != null) {
         quadBounds[EnumFacing.WEST.getIndex()] = f;
         quadBounds[EnumFacing.EAST.getIndex()] = f3;
         quadBounds[EnumFacing.DOWN.getIndex()] = f1;
         quadBounds[EnumFacing.UP.getIndex()] = f4;
         quadBounds[EnumFacing.NORTH.getIndex()] = f2;
         quadBounds[EnumFacing.SOUTH.getIndex()] = f5;
         quadBounds[EnumFacing.WEST.getIndex() + EnumFacing.VALUES.length] = 1.0F - f;
         quadBounds[EnumFacing.EAST.getIndex() + EnumFacing.VALUES.length] = 1.0F - f3;
         quadBounds[EnumFacing.DOWN.getIndex() + EnumFacing.VALUES.length] = 1.0F - f1;
         quadBounds[EnumFacing.UP.getIndex() + EnumFacing.VALUES.length] = 1.0F - f4;
         quadBounds[EnumFacing.NORTH.getIndex() + EnumFacing.VALUES.length] = 1.0F - f2;
         quadBounds[EnumFacing.SOUTH.getIndex() + EnumFacing.VALUES.length] = 1.0F - f5;
      }

      float f10 = 1.0E-4F;
      float f9 = 0.9999F;
      switch(1.field_178290_a[facingIn.ordinal()]) {
      case 1:
         boundsFlags.set(1, f >= 1.0E-4F || f2 >= 1.0E-4F || f3 <= 0.9999F || f5 <= 0.9999F);
         boundsFlags.set(0, (f1 < 1.0E-4F || blockIn.isFullCube()) && f1 == f4);
         break;
      case 2:
         boundsFlags.set(1, f >= 1.0E-4F || f2 >= 1.0E-4F || f3 <= 0.9999F || f5 <= 0.9999F);
         boundsFlags.set(0, (f4 > 0.9999F || blockIn.isFullCube()) && f1 == f4);
         break;
      case 3:
         boundsFlags.set(1, f >= 1.0E-4F || f1 >= 1.0E-4F || f3 <= 0.9999F || f4 <= 0.9999F);
         boundsFlags.set(0, (f2 < 1.0E-4F || blockIn.isFullCube()) && f2 == f5);
         break;
      case 4:
         boundsFlags.set(1, f >= 1.0E-4F || f1 >= 1.0E-4F || f3 <= 0.9999F || f4 <= 0.9999F);
         boundsFlags.set(0, (f5 > 0.9999F || blockIn.isFullCube()) && f2 == f5);
         break;
      case 5:
         boundsFlags.set(1, f1 >= 1.0E-4F || f2 >= 1.0E-4F || f4 <= 0.9999F || f5 <= 0.9999F);
         boundsFlags.set(0, (f < 1.0E-4F || blockIn.isFullCube()) && f == f3);
         break;
      case 6:
         boundsFlags.set(1, f1 >= 1.0E-4F || f2 >= 1.0E-4F || f4 <= 0.9999F || f5 <= 0.9999F);
         boundsFlags.set(0, (f3 > 0.9999F || blockIn.isFullCube()) && f == f3);
      }

   }

   public static float fixAoLightValue(float p_fixAoLightValue_0_) {
      return p_fixAoLightValue_0_ == 0.2F?aoLightValueOpaque:p_fixAoLightValue_0_;
   }
}
