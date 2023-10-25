package net.minecraft.client.renderer;

import java.util.BitSet;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.src.Config;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.optifine.BetterSnow;
import net.optifine.CustomColors;
import net.optifine.model.BlockModelCustomizer;
import net.optifine.model.ListQuadsOverlay;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorField;
import net.optifine.render.RenderEnv;
import net.optifine.shaders.SVertexBuilder;
import net.optifine.shaders.Shaders;

/*
 * Exception performing whole class analysis ignored.
 */
public class BlockModelRenderer {
    private static float aoLightValueOpaque = 0.2f;
    private static boolean separateAoLightValue = false;
    private static final EnumWorldBlockLayer[] OVERLAY_LAYERS = new EnumWorldBlockLayer[]{EnumWorldBlockLayer.CUTOUT, EnumWorldBlockLayer.CUTOUT_MIPPED, EnumWorldBlockLayer.TRANSLUCENT};

    public BlockModelRenderer() {
        if (Reflector.ForgeModContainer_forgeLightPipelineEnabled.exists()) {
            Reflector.setFieldValue((ReflectorField)Reflector.ForgeModContainer_forgeLightPipelineEnabled, (Object)false);
        }
    }

    public boolean renderModel(IBlockAccess blockAccessIn, IBakedModel modelIn, IBlockState blockStateIn, BlockPos blockPosIn, WorldRenderer worldRendererIn) {
        Block block = blockStateIn.getBlock();
        block.setBlockBoundsBasedOnState(blockAccessIn, blockPosIn);
        return this.renderModel(blockAccessIn, modelIn, blockStateIn, blockPosIn, worldRendererIn, true);
    }

    public boolean renderModel(IBlockAccess blockAccessIn, IBakedModel modelIn, IBlockState blockStateIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, boolean checkSides) {
        boolean flag = Minecraft.isAmbientOcclusionEnabled() && blockStateIn.getBlock().getLightValue() == 0 && modelIn.isAmbientOcclusion();
        try {
            boolean flag1;
            if (Config.isShaders()) {
                SVertexBuilder.pushEntity((IBlockState)blockStateIn, (BlockPos)blockPosIn, (IBlockAccess)blockAccessIn, (WorldRenderer)worldRendererIn);
            }
            RenderEnv renderenv = worldRendererIn.getRenderEnv(blockStateIn, blockPosIn);
            modelIn = BlockModelCustomizer.getRenderModel((IBakedModel)modelIn, (IBlockState)blockStateIn, (RenderEnv)renderenv);
            boolean bl = flag1 = flag ? this.renderModelSmooth(blockAccessIn, modelIn, blockStateIn, blockPosIn, worldRendererIn, checkSides) : this.renderModelFlat(blockAccessIn, modelIn, blockStateIn, blockPosIn, worldRendererIn, checkSides);
            if (flag1) {
                this.renderOverlayModels(blockAccessIn, modelIn, blockStateIn, blockPosIn, worldRendererIn, checkSides, 0L, renderenv, flag);
            }
            if (Config.isShaders()) {
                SVertexBuilder.popEntity((WorldRenderer)worldRendererIn);
            }
            return flag1;
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport((Throwable)throwable, (String)"Tesselating block model");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block model being tesselated");
            CrashReportCategory.addBlockInfo((CrashReportCategory)crashreportcategory, (BlockPos)blockPosIn, (IBlockState)blockStateIn);
            crashreportcategory.addCrashSection("Using AO", (Object)flag);
            throw new ReportedException(crashreport);
        }
    }

    public boolean renderModelAmbientOcclusion(IBlockAccess blockAccessIn, IBakedModel modelIn, Block blockIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, boolean checkSides) {
        IBlockState iblockstate = blockAccessIn.getBlockState(blockPosIn);
        return this.renderModelSmooth(blockAccessIn, modelIn, iblockstate, blockPosIn, worldRendererIn, checkSides);
    }

    private boolean renderModelSmooth(IBlockAccess p_renderModelSmooth_1_, IBakedModel p_renderModelSmooth_2_, IBlockState p_renderModelSmooth_3_, BlockPos p_renderModelSmooth_4_, WorldRenderer p_renderModelSmooth_5_, boolean p_renderModelSmooth_6_) {
        boolean flag = false;
        Block block = p_renderModelSmooth_3_.getBlock();
        RenderEnv renderenv = p_renderModelSmooth_5_.getRenderEnv(p_renderModelSmooth_3_, p_renderModelSmooth_4_);
        EnumWorldBlockLayer enumworldblocklayer = p_renderModelSmooth_5_.getBlockLayer();
        for (EnumFacing enumfacing : EnumFacing.VALUES) {
            List list = p_renderModelSmooth_2_.getFaceQuads(enumfacing);
            if (list.isEmpty()) continue;
            BlockPos blockpos = p_renderModelSmooth_4_.offset(enumfacing);
            if (p_renderModelSmooth_6_ && !block.shouldSideBeRendered(p_renderModelSmooth_1_, blockpos, enumfacing)) continue;
            list = BlockModelCustomizer.getRenderQuads((List)list, (IBlockAccess)p_renderModelSmooth_1_, (IBlockState)p_renderModelSmooth_3_, (BlockPos)p_renderModelSmooth_4_, (EnumFacing)enumfacing, (EnumWorldBlockLayer)enumworldblocklayer, (long)0L, (RenderEnv)renderenv);
            this.renderQuadsSmooth(p_renderModelSmooth_1_, p_renderModelSmooth_3_, p_renderModelSmooth_4_, p_renderModelSmooth_5_, (List<BakedQuad>)list, renderenv);
            flag = true;
        }
        List list1 = p_renderModelSmooth_2_.getGeneralQuads();
        if (list1.size() > 0) {
            list1 = BlockModelCustomizer.getRenderQuads((List)list1, (IBlockAccess)p_renderModelSmooth_1_, (IBlockState)p_renderModelSmooth_3_, (BlockPos)p_renderModelSmooth_4_, (EnumFacing)null, (EnumWorldBlockLayer)enumworldblocklayer, (long)0L, (RenderEnv)renderenv);
            this.renderQuadsSmooth(p_renderModelSmooth_1_, p_renderModelSmooth_3_, p_renderModelSmooth_4_, p_renderModelSmooth_5_, (List<BakedQuad>)list1, renderenv);
            flag = true;
        }
        return flag;
    }

    public boolean renderModelStandard(IBlockAccess blockAccessIn, IBakedModel modelIn, Block blockIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, boolean checkSides) {
        IBlockState iblockstate = blockAccessIn.getBlockState(blockPosIn);
        return this.renderModelFlat(blockAccessIn, modelIn, iblockstate, blockPosIn, worldRendererIn, checkSides);
    }

    public boolean renderModelFlat(IBlockAccess p_renderModelFlat_1_, IBakedModel p_renderModelFlat_2_, IBlockState p_renderModelFlat_3_, BlockPos p_renderModelFlat_4_, WorldRenderer p_renderModelFlat_5_, boolean p_renderModelFlat_6_) {
        boolean flag = false;
        Block block = p_renderModelFlat_3_.getBlock();
        RenderEnv renderenv = p_renderModelFlat_5_.getRenderEnv(p_renderModelFlat_3_, p_renderModelFlat_4_);
        EnumWorldBlockLayer enumworldblocklayer = p_renderModelFlat_5_.getBlockLayer();
        for (EnumFacing enumfacing : EnumFacing.VALUES) {
            List list = p_renderModelFlat_2_.getFaceQuads(enumfacing);
            if (list.isEmpty()) continue;
            BlockPos blockpos = p_renderModelFlat_4_.offset(enumfacing);
            if (p_renderModelFlat_6_ && !block.shouldSideBeRendered(p_renderModelFlat_1_, blockpos, enumfacing)) continue;
            int i = block.getMixedBrightnessForBlock(p_renderModelFlat_1_, blockpos);
            list = BlockModelCustomizer.getRenderQuads((List)list, (IBlockAccess)p_renderModelFlat_1_, (IBlockState)p_renderModelFlat_3_, (BlockPos)p_renderModelFlat_4_, (EnumFacing)enumfacing, (EnumWorldBlockLayer)enumworldblocklayer, (long)0L, (RenderEnv)renderenv);
            this.renderQuadsFlat(p_renderModelFlat_1_, p_renderModelFlat_3_, p_renderModelFlat_4_, enumfacing, i, false, p_renderModelFlat_5_, (List<BakedQuad>)list, renderenv);
            flag = true;
        }
        List list1 = p_renderModelFlat_2_.getGeneralQuads();
        if (list1.size() > 0) {
            list1 = BlockModelCustomizer.getRenderQuads((List)list1, (IBlockAccess)p_renderModelFlat_1_, (IBlockState)p_renderModelFlat_3_, (BlockPos)p_renderModelFlat_4_, (EnumFacing)null, (EnumWorldBlockLayer)enumworldblocklayer, (long)0L, (RenderEnv)renderenv);
            this.renderQuadsFlat(p_renderModelFlat_1_, p_renderModelFlat_3_, p_renderModelFlat_4_, null, -1, true, p_renderModelFlat_5_, (List<BakedQuad>)list1, renderenv);
            flag = true;
        }
        return flag;
    }

    private void renderQuadsSmooth(IBlockAccess p_renderQuadsSmooth_1_, IBlockState p_renderQuadsSmooth_2_, BlockPos p_renderQuadsSmooth_3_, WorldRenderer p_renderQuadsSmooth_4_, List<BakedQuad> p_renderQuadsSmooth_5_, RenderEnv p_renderQuadsSmooth_6_) {
        Block block = p_renderQuadsSmooth_2_.getBlock();
        float[] afloat = p_renderQuadsSmooth_6_.getQuadBounds();
        BitSet bitset = p_renderQuadsSmooth_6_.getBoundsFlags();
        AmbientOcclusionFace blockmodelrenderer$ambientocclusionface = p_renderQuadsSmooth_6_.getAoFace();
        double d0 = p_renderQuadsSmooth_3_.getX();
        double d1 = p_renderQuadsSmooth_3_.getY();
        double d2 = p_renderQuadsSmooth_3_.getZ();
        Block.EnumOffsetType block$enumoffsettype = block.getOffsetType();
        if (block$enumoffsettype != Block.EnumOffsetType.NONE) {
            long i = MathHelper.getPositionRandom((Vec3i)p_renderQuadsSmooth_3_);
            d0 += ((double)((float)(i >> 16 & 0xFL) / 15.0f) - 0.5) * 0.5;
            d2 += ((double)((float)(i >> 24 & 0xFL) / 15.0f) - 0.5) * 0.5;
            if (block$enumoffsettype == Block.EnumOffsetType.XYZ) {
                d1 += ((double)((float)(i >> 20 & 0xFL) / 15.0f) - 1.0) * 0.2;
            }
        }
        for (BakedQuad bakedquad : p_renderQuadsSmooth_5_) {
            this.fillQuadBounds(block, bakedquad.getVertexData(), bakedquad.getFace(), afloat, bitset);
            blockmodelrenderer$ambientocclusionface.updateVertexBrightness(p_renderQuadsSmooth_1_, block, p_renderQuadsSmooth_3_, bakedquad.getFace(), afloat, bitset);
            if (bakedquad.getSprite().isEmissive) {
                blockmodelrenderer$ambientocclusionface.setMaxBlockLight();
            }
            if (p_renderQuadsSmooth_4_.isMultiTexture()) {
                p_renderQuadsSmooth_4_.addVertexData(bakedquad.getVertexDataSingle());
            } else {
                p_renderQuadsSmooth_4_.addVertexData(bakedquad.getVertexData());
            }
            p_renderQuadsSmooth_4_.putSprite(bakedquad.getSprite());
            p_renderQuadsSmooth_4_.putBrightness4(AmbientOcclusionFace.access$000((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[0], AmbientOcclusionFace.access$000((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[1], AmbientOcclusionFace.access$000((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[2], AmbientOcclusionFace.access$000((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[3]);
            int j = CustomColors.getColorMultiplier((BakedQuad)bakedquad, (IBlockState)p_renderQuadsSmooth_2_, (IBlockAccess)p_renderQuadsSmooth_1_, (BlockPos)p_renderQuadsSmooth_3_, (RenderEnv)p_renderQuadsSmooth_6_);
            if (!bakedquad.hasTintIndex() && j == -1) {
                if (separateAoLightValue) {
                    p_renderQuadsSmooth_4_.putColorMultiplierRgba(1.0f, 1.0f, 1.0f, AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[0], 4);
                    p_renderQuadsSmooth_4_.putColorMultiplierRgba(1.0f, 1.0f, 1.0f, AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[1], 3);
                    p_renderQuadsSmooth_4_.putColorMultiplierRgba(1.0f, 1.0f, 1.0f, AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[2], 2);
                    p_renderQuadsSmooth_4_.putColorMultiplierRgba(1.0f, 1.0f, 1.0f, AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[3], 1);
                } else {
                    p_renderQuadsSmooth_4_.putColorMultiplier(AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[0], AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[0], AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[0], 4);
                    p_renderQuadsSmooth_4_.putColorMultiplier(AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[1], AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[1], AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[1], 3);
                    p_renderQuadsSmooth_4_.putColorMultiplier(AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[2], AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[2], AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[2], 2);
                    p_renderQuadsSmooth_4_.putColorMultiplier(AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[3], AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[3], AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[3], 1);
                }
            } else {
                int k = j != -1 ? j : block.colorMultiplier(p_renderQuadsSmooth_1_, p_renderQuadsSmooth_3_, bakedquad.getTintIndex());
                if (EntityRenderer.anaglyphEnable) {
                    k = TextureUtil.anaglyphColor((int)k);
                }
                float f = (float)(k >> 16 & 0xFF) / 255.0f;
                float f1 = (float)(k >> 8 & 0xFF) / 255.0f;
                float f2 = (float)(k & 0xFF) / 255.0f;
                if (separateAoLightValue) {
                    p_renderQuadsSmooth_4_.putColorMultiplierRgba(f, f1, f2, AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[0], 4);
                    p_renderQuadsSmooth_4_.putColorMultiplierRgba(f, f1, f2, AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[1], 3);
                    p_renderQuadsSmooth_4_.putColorMultiplierRgba(f, f1, f2, AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[2], 2);
                    p_renderQuadsSmooth_4_.putColorMultiplierRgba(f, f1, f2, AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[3], 1);
                } else {
                    p_renderQuadsSmooth_4_.putColorMultiplier(AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[0] * f, AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[0] * f1, AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[0] * f2, 4);
                    p_renderQuadsSmooth_4_.putColorMultiplier(AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[1] * f, AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[1] * f1, AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[1] * f2, 3);
                    p_renderQuadsSmooth_4_.putColorMultiplier(AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[2] * f, AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[2] * f1, AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[2] * f2, 2);
                    p_renderQuadsSmooth_4_.putColorMultiplier(AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[3] * f, AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[3] * f1, AmbientOcclusionFace.access$100((AmbientOcclusionFace)blockmodelrenderer$ambientocclusionface)[3] * f2, 1);
                }
            }
            p_renderQuadsSmooth_4_.putPosition(d0, d1, d2);
        }
    }

    private void fillQuadBounds(Block blockIn, int[] vertexData, EnumFacing facingIn, float[] quadBounds, BitSet boundsFlags) {
        float f = 32.0f;
        float f1 = 32.0f;
        float f2 = 32.0f;
        float f3 = -32.0f;
        float f4 = -32.0f;
        float f5 = -32.0f;
        int i = vertexData.length / 4;
        for (int j = 0; j < 4; ++j) {
            float f6 = Float.intBitsToFloat((int)vertexData[j * i]);
            float f7 = Float.intBitsToFloat((int)vertexData[j * i + 1]);
            float f8 = Float.intBitsToFloat((int)vertexData[j * i + 2]);
            f = Math.min((float)f, (float)f6);
            f1 = Math.min((float)f1, (float)f7);
            f2 = Math.min((float)f2, (float)f8);
            f3 = Math.max((float)f3, (float)f6);
            f4 = Math.max((float)f4, (float)f7);
            f5 = Math.max((float)f5, (float)f8);
        }
        if (quadBounds != null) {
            quadBounds[EnumFacing.WEST.getIndex()] = f;
            quadBounds[EnumFacing.EAST.getIndex()] = f3;
            quadBounds[EnumFacing.DOWN.getIndex()] = f1;
            quadBounds[EnumFacing.UP.getIndex()] = f4;
            quadBounds[EnumFacing.NORTH.getIndex()] = f2;
            quadBounds[EnumFacing.SOUTH.getIndex()] = f5;
            int k = EnumFacing.VALUES.length;
            quadBounds[EnumFacing.WEST.getIndex() + k] = 1.0f - f;
            quadBounds[EnumFacing.EAST.getIndex() + k] = 1.0f - f3;
            quadBounds[EnumFacing.DOWN.getIndex() + k] = 1.0f - f1;
            quadBounds[EnumFacing.UP.getIndex() + k] = 1.0f - f4;
            quadBounds[EnumFacing.NORTH.getIndex() + k] = 1.0f - f2;
            quadBounds[EnumFacing.SOUTH.getIndex() + k] = 1.0f - f5;
        }
        float f9 = 1.0E-4f;
        float f10 = 0.9999f;
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[facingIn.ordinal()]) {
            case 1: {
                boundsFlags.set(1, f >= 1.0E-4f || f2 >= 1.0E-4f || f3 <= 0.9999f || f5 <= 0.9999f);
                boundsFlags.set(0, (f1 < 1.0E-4f || blockIn.isFullCube()) && f1 == f4);
                break;
            }
            case 2: {
                boundsFlags.set(1, f >= 1.0E-4f || f2 >= 1.0E-4f || f3 <= 0.9999f || f5 <= 0.9999f);
                boundsFlags.set(0, (f4 > 0.9999f || blockIn.isFullCube()) && f1 == f4);
                break;
            }
            case 3: {
                boundsFlags.set(1, f >= 1.0E-4f || f1 >= 1.0E-4f || f3 <= 0.9999f || f4 <= 0.9999f);
                boundsFlags.set(0, (f2 < 1.0E-4f || blockIn.isFullCube()) && f2 == f5);
                break;
            }
            case 4: {
                boundsFlags.set(1, f >= 1.0E-4f || f1 >= 1.0E-4f || f3 <= 0.9999f || f4 <= 0.9999f);
                boundsFlags.set(0, (f5 > 0.9999f || blockIn.isFullCube()) && f2 == f5);
                break;
            }
            case 5: {
                boundsFlags.set(1, f1 >= 1.0E-4f || f2 >= 1.0E-4f || f4 <= 0.9999f || f5 <= 0.9999f);
                boundsFlags.set(0, (f < 1.0E-4f || blockIn.isFullCube()) && f == f3);
                break;
            }
            case 6: {
                boundsFlags.set(1, f1 >= 1.0E-4f || f2 >= 1.0E-4f || f4 <= 0.9999f || f5 <= 0.9999f);
                boundsFlags.set(0, (f3 > 0.9999f || blockIn.isFullCube()) && f == f3);
            }
        }
    }

    private void renderQuadsFlat(IBlockAccess p_renderQuadsFlat_1_, IBlockState p_renderQuadsFlat_2_, BlockPos p_renderQuadsFlat_3_, EnumFacing p_renderQuadsFlat_4_, int p_renderQuadsFlat_5_, boolean p_renderQuadsFlat_6_, WorldRenderer p_renderQuadsFlat_7_, List<BakedQuad> p_renderQuadsFlat_8_, RenderEnv p_renderQuadsFlat_9_) {
        Block block = p_renderQuadsFlat_2_.getBlock();
        BitSet bitset = p_renderQuadsFlat_9_.getBoundsFlags();
        double d0 = p_renderQuadsFlat_3_.getX();
        double d1 = p_renderQuadsFlat_3_.getY();
        double d2 = p_renderQuadsFlat_3_.getZ();
        Block.EnumOffsetType block$enumoffsettype = block.getOffsetType();
        if (block$enumoffsettype != Block.EnumOffsetType.NONE) {
            int i = p_renderQuadsFlat_3_.getX();
            int j = p_renderQuadsFlat_3_.getZ();
            long k = (long)(i * 3129871) ^ (long)j * 116129781L;
            k = k * k * 42317861L + k * 11L;
            d0 += ((double)((float)(k >> 16 & 0xFL) / 15.0f) - 0.5) * 0.5;
            d2 += ((double)((float)(k >> 24 & 0xFL) / 15.0f) - 0.5) * 0.5;
            if (block$enumoffsettype == Block.EnumOffsetType.XYZ) {
                d1 += ((double)((float)(k >> 20 & 0xFL) / 15.0f) - 1.0) * 0.2;
            }
        }
        for (BakedQuad bakedquad : p_renderQuadsFlat_8_) {
            if (p_renderQuadsFlat_6_) {
                this.fillQuadBounds(block, bakedquad.getVertexData(), bakedquad.getFace(), null, bitset);
                int n = p_renderQuadsFlat_5_ = bitset.get(0) ? block.getMixedBrightnessForBlock(p_renderQuadsFlat_1_, p_renderQuadsFlat_3_.offset(bakedquad.getFace())) : block.getMixedBrightnessForBlock(p_renderQuadsFlat_1_, p_renderQuadsFlat_3_);
            }
            if (bakedquad.getSprite().isEmissive) {
                p_renderQuadsFlat_5_ |= 0xF0;
            }
            if (p_renderQuadsFlat_7_.isMultiTexture()) {
                p_renderQuadsFlat_7_.addVertexData(bakedquad.getVertexDataSingle());
            } else {
                p_renderQuadsFlat_7_.addVertexData(bakedquad.getVertexData());
            }
            p_renderQuadsFlat_7_.putSprite(bakedquad.getSprite());
            p_renderQuadsFlat_7_.putBrightness4(p_renderQuadsFlat_5_, p_renderQuadsFlat_5_, p_renderQuadsFlat_5_, p_renderQuadsFlat_5_);
            int i1 = CustomColors.getColorMultiplier((BakedQuad)bakedquad, (IBlockState)p_renderQuadsFlat_2_, (IBlockAccess)p_renderQuadsFlat_1_, (BlockPos)p_renderQuadsFlat_3_, (RenderEnv)p_renderQuadsFlat_9_);
            if (bakedquad.hasTintIndex() || i1 != -1) {
                int l = i1 != -1 ? i1 : block.colorMultiplier(p_renderQuadsFlat_1_, p_renderQuadsFlat_3_, bakedquad.getTintIndex());
                if (EntityRenderer.anaglyphEnable) {
                    l = TextureUtil.anaglyphColor((int)l);
                }
                float f = (float)(l >> 16 & 0xFF) / 255.0f;
                float f1 = (float)(l >> 8 & 0xFF) / 255.0f;
                float f2 = (float)(l & 0xFF) / 255.0f;
                p_renderQuadsFlat_7_.putColorMultiplier(f, f1, f2, 4);
                p_renderQuadsFlat_7_.putColorMultiplier(f, f1, f2, 3);
                p_renderQuadsFlat_7_.putColorMultiplier(f, f1, f2, 2);
                p_renderQuadsFlat_7_.putColorMultiplier(f, f1, f2, 1);
            }
            p_renderQuadsFlat_7_.putPosition(d0, d1, d2);
        }
    }

    public void renderModelBrightnessColor(IBakedModel bakedModel, float p_178262_2_, float red, float green, float blue) {
        for (EnumFacing enumfacing : EnumFacing.VALUES) {
            this.renderModelBrightnessColorQuads(p_178262_2_, red, green, blue, (List<BakedQuad>)bakedModel.getFaceQuads(enumfacing));
        }
        this.renderModelBrightnessColorQuads(p_178262_2_, red, green, blue, (List<BakedQuad>)bakedModel.getGeneralQuads());
    }

    public void renderModelBrightness(IBakedModel model, IBlockState p_178266_2_, float brightness, boolean p_178266_4_) {
        Block block = p_178266_2_.getBlock();
        block.setBlockBoundsForItemRender();
        GlStateManager.rotate((float)90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        int i = block.getRenderColor(block.getStateForEntityRender(p_178266_2_));
        if (EntityRenderer.anaglyphEnable) {
            i = TextureUtil.anaglyphColor((int)i);
        }
        float f = (float)(i >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(i >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(i & 0xFF) / 255.0f;
        if (!p_178266_4_) {
            GlStateManager.color((float)brightness, (float)brightness, (float)brightness, (float)1.0f);
        }
        this.renderModelBrightnessColor(model, brightness, f, f1, f2);
    }

    private void renderModelBrightnessColorQuads(float brightness, float red, float green, float blue, List<BakedQuad> listQuads) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        for (BakedQuad bakedquad : listQuads) {
            worldrenderer.begin(7, DefaultVertexFormats.ITEM);
            worldrenderer.addVertexData(bakedquad.getVertexData());
            worldrenderer.putSprite(bakedquad.getSprite());
            if (bakedquad.hasTintIndex()) {
                worldrenderer.putColorRGB_F4(red * brightness, green * brightness, blue * brightness);
            } else {
                worldrenderer.putColorRGB_F4(brightness, brightness, brightness);
            }
            Vec3i vec3i = bakedquad.getFace().getDirectionVec();
            worldrenderer.putNormal((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
            tessellator.draw();
        }
    }

    public static float fixAoLightValue(float p_fixAoLightValue_0_) {
        return p_fixAoLightValue_0_ == 0.2f ? aoLightValueOpaque : p_fixAoLightValue_0_;
    }

    public static void updateAoLightValue() {
        aoLightValueOpaque = 1.0f - Config.getAmbientOcclusionLevel() * 0.8f;
        separateAoLightValue = Config.isShaders() && Shaders.isSeparateAo();
    }

    private void renderOverlayModels(IBlockAccess p_renderOverlayModels_1_, IBakedModel p_renderOverlayModels_2_, IBlockState p_renderOverlayModels_3_, BlockPos p_renderOverlayModels_4_, WorldRenderer p_renderOverlayModels_5_, boolean p_renderOverlayModels_6_, long p_renderOverlayModels_7_, RenderEnv p_renderOverlayModels_9_, boolean p_renderOverlayModels_10_) {
        if (p_renderOverlayModels_9_.isOverlaysRendered()) {
            for (int i = 0; i < OVERLAY_LAYERS.length; ++i) {
                EnumWorldBlockLayer enumworldblocklayer = OVERLAY_LAYERS[i];
                ListQuadsOverlay listquadsoverlay = p_renderOverlayModels_9_.getListQuadsOverlay(enumworldblocklayer);
                if (listquadsoverlay.size() <= 0) continue;
                RegionRenderCacheBuilder regionrendercachebuilder = p_renderOverlayModels_9_.getRegionRenderCacheBuilder();
                if (regionrendercachebuilder != null) {
                    WorldRenderer worldrenderer = regionrendercachebuilder.getWorldRendererByLayer(enumworldblocklayer);
                    if (!worldrenderer.isDrawing()) {
                        worldrenderer.begin(7, DefaultVertexFormats.BLOCK);
                        worldrenderer.setTranslation(p_renderOverlayModels_5_.getXOffset(), p_renderOverlayModels_5_.getYOffset(), p_renderOverlayModels_5_.getZOffset());
                    }
                    for (int j = 0; j < listquadsoverlay.size(); ++j) {
                        BakedQuad bakedquad = listquadsoverlay.getQuad(j);
                        List list = listquadsoverlay.getListQuadsSingle(bakedquad);
                        IBlockState iblockstate = listquadsoverlay.getBlockState(j);
                        if (bakedquad.getQuadEmissive() != null) {
                            listquadsoverlay.addQuad(bakedquad.getQuadEmissive(), iblockstate);
                        }
                        p_renderOverlayModels_9_.reset(iblockstate, p_renderOverlayModels_4_);
                        if (p_renderOverlayModels_10_) {
                            this.renderQuadsSmooth(p_renderOverlayModels_1_, iblockstate, p_renderOverlayModels_4_, worldrenderer, (List<BakedQuad>)list, p_renderOverlayModels_9_);
                            continue;
                        }
                        int k = iblockstate.getBlock().getMixedBrightnessForBlock(p_renderOverlayModels_1_, p_renderOverlayModels_4_.offset(bakedquad.getFace()));
                        this.renderQuadsFlat(p_renderOverlayModels_1_, iblockstate, p_renderOverlayModels_4_, bakedquad.getFace(), k, false, worldrenderer, (List<BakedQuad>)list, p_renderOverlayModels_9_);
                    }
                }
                listquadsoverlay.clear();
            }
        }
        if (Config.isBetterSnow() && !p_renderOverlayModels_9_.isBreakingAnimation() && BetterSnow.shouldRender((IBlockAccess)p_renderOverlayModels_1_, (IBlockState)p_renderOverlayModels_3_, (BlockPos)p_renderOverlayModels_4_)) {
            IBakedModel ibakedmodel = BetterSnow.getModelSnowLayer();
            IBlockState iblockstate1 = BetterSnow.getStateSnowLayer();
            this.renderModel(p_renderOverlayModels_1_, ibakedmodel, iblockstate1, p_renderOverlayModels_4_, p_renderOverlayModels_5_, p_renderOverlayModels_6_);
        }
    }
}
