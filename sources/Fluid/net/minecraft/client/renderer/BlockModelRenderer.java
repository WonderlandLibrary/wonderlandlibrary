// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer;

import net.optifine.model.ListQuadsOverlay;
import net.optifine.BetterSnow;
import net.optifine.shaders.Shaders;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import java.util.Iterator;
import java.util.BitSet;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.optifine.CustomColors;
import net.minecraft.util.Vec3i;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.List;
import net.minecraft.util.EnumFacing;
import net.optifine.render.RenderEnv;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.CrashReport;
import net.optifine.model.BlockModelCustomizer;
import net.optifine.shaders.SVertexBuilder;
import net.minecraft.src.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.world.IBlockAccess;
import net.optifine.reflect.Reflector;
import net.minecraft.util.EnumWorldBlockLayer;

public class BlockModelRenderer
{
    private static float aoLightValueOpaque;
    private static boolean separateAoLightValue;
    private static final EnumWorldBlockLayer[] OVERLAY_LAYERS;
    
    static {
        BlockModelRenderer.aoLightValueOpaque = 0.2f;
        BlockModelRenderer.separateAoLightValue = false;
        OVERLAY_LAYERS = new EnumWorldBlockLayer[] { EnumWorldBlockLayer.CUTOUT, EnumWorldBlockLayer.CUTOUT_MIPPED, EnumWorldBlockLayer.TRANSLUCENT };
    }
    
    public BlockModelRenderer() {
        if (Reflector.ForgeModContainer_forgeLightPipelineEnabled.exists()) {
            Reflector.setFieldValue(Reflector.ForgeModContainer_forgeLightPipelineEnabled, false);
        }
    }
    
    public boolean renderModel(final IBlockAccess blockAccessIn, final IBakedModel modelIn, final IBlockState blockStateIn, final BlockPos blockPosIn, final WorldRenderer worldRendererIn) {
        final Block block = blockStateIn.getBlock();
        block.setBlockBoundsBasedOnState(blockAccessIn, blockPosIn);
        return this.renderModel(blockAccessIn, modelIn, blockStateIn, blockPosIn, worldRendererIn, true);
    }
    
    public boolean renderModel(final IBlockAccess blockAccessIn, IBakedModel modelIn, final IBlockState blockStateIn, final BlockPos blockPosIn, final WorldRenderer worldRendererIn, final boolean checkSides) {
        final boolean flag = Minecraft.isAmbientOcclusionEnabled() && blockStateIn.getBlock().getLightValue() == 0 && modelIn.isAmbientOcclusion();
        try {
            if (Config.isShaders()) {
                SVertexBuilder.pushEntity(blockStateIn, blockPosIn, blockAccessIn, worldRendererIn);
            }
            final RenderEnv renderenv = worldRendererIn.getRenderEnv(blockStateIn, blockPosIn);
            modelIn = BlockModelCustomizer.getRenderModel(modelIn, blockStateIn, renderenv);
            final boolean flag2 = flag ? this.renderModelSmooth(blockAccessIn, modelIn, blockStateIn, blockPosIn, worldRendererIn, checkSides) : this.renderModelFlat(blockAccessIn, modelIn, blockStateIn, blockPosIn, worldRendererIn, checkSides);
            if (flag2) {
                this.renderOverlayModels(blockAccessIn, modelIn, blockStateIn, blockPosIn, worldRendererIn, checkSides, 0L, renderenv, flag);
            }
            if (Config.isShaders()) {
                SVertexBuilder.popEntity(worldRendererIn);
            }
            return flag2;
        }
        catch (final Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Tesselating block model");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Block model being tesselated");
            CrashReportCategory.addBlockInfo(crashreportcategory, blockPosIn, blockStateIn);
            crashreportcategory.addCrashSection("Using AO", flag);
            throw new ReportedException(crashreport);
        }
    }
    
    public boolean renderModelAmbientOcclusion(final IBlockAccess blockAccessIn, final IBakedModel modelIn, final Block blockIn, final BlockPos blockPosIn, final WorldRenderer worldRendererIn, final boolean checkSides) {
        final IBlockState iblockstate = blockAccessIn.getBlockState(blockPosIn);
        return this.renderModelSmooth(blockAccessIn, modelIn, iblockstate, blockPosIn, worldRendererIn, checkSides);
    }
    
    private boolean renderModelSmooth(final IBlockAccess p_renderModelSmooth_1_, final IBakedModel p_renderModelSmooth_2_, final IBlockState p_renderModelSmooth_3_, final BlockPos p_renderModelSmooth_4_, final WorldRenderer p_renderModelSmooth_5_, final boolean p_renderModelSmooth_6_) {
        boolean flag = false;
        final Block block = p_renderModelSmooth_3_.getBlock();
        final RenderEnv renderenv = p_renderModelSmooth_5_.getRenderEnv(p_renderModelSmooth_3_, p_renderModelSmooth_4_);
        final EnumWorldBlockLayer enumworldblocklayer = p_renderModelSmooth_5_.getBlockLayer();
        EnumFacing[] values;
        for (int length = (values = EnumFacing.VALUES).length, i = 0; i < length; ++i) {
            final EnumFacing enumfacing = values[i];
            List<BakedQuad> list = p_renderModelSmooth_2_.getFaceQuads(enumfacing);
            if (!list.isEmpty()) {
                final BlockPos blockpos = p_renderModelSmooth_4_.offset(enumfacing);
                if (!p_renderModelSmooth_6_ || block.shouldSideBeRendered(p_renderModelSmooth_1_, blockpos, enumfacing)) {
                    list = BlockModelCustomizer.getRenderQuads(list, p_renderModelSmooth_1_, p_renderModelSmooth_3_, p_renderModelSmooth_4_, enumfacing, enumworldblocklayer, 0L, renderenv);
                    this.renderQuadsSmooth(p_renderModelSmooth_1_, p_renderModelSmooth_3_, p_renderModelSmooth_4_, p_renderModelSmooth_5_, list, renderenv);
                    flag = true;
                }
            }
        }
        List<BakedQuad> list2 = p_renderModelSmooth_2_.getGeneralQuads();
        if (list2.size() > 0) {
            list2 = BlockModelCustomizer.getRenderQuads(list2, p_renderModelSmooth_1_, p_renderModelSmooth_3_, p_renderModelSmooth_4_, null, enumworldblocklayer, 0L, renderenv);
            this.renderQuadsSmooth(p_renderModelSmooth_1_, p_renderModelSmooth_3_, p_renderModelSmooth_4_, p_renderModelSmooth_5_, list2, renderenv);
            flag = true;
        }
        return flag;
    }
    
    public boolean renderModelStandard(final IBlockAccess blockAccessIn, final IBakedModel modelIn, final Block blockIn, final BlockPos blockPosIn, final WorldRenderer worldRendererIn, final boolean checkSides) {
        final IBlockState iblockstate = blockAccessIn.getBlockState(blockPosIn);
        return this.renderModelFlat(blockAccessIn, modelIn, iblockstate, blockPosIn, worldRendererIn, checkSides);
    }
    
    public boolean renderModelFlat(final IBlockAccess p_renderModelFlat_1_, final IBakedModel p_renderModelFlat_2_, final IBlockState p_renderModelFlat_3_, final BlockPos p_renderModelFlat_4_, final WorldRenderer p_renderModelFlat_5_, final boolean p_renderModelFlat_6_) {
        boolean flag = false;
        final Block block = p_renderModelFlat_3_.getBlock();
        final RenderEnv renderenv = p_renderModelFlat_5_.getRenderEnv(p_renderModelFlat_3_, p_renderModelFlat_4_);
        final EnumWorldBlockLayer enumworldblocklayer = p_renderModelFlat_5_.getBlockLayer();
        EnumFacing[] values;
        for (int length = (values = EnumFacing.VALUES).length, j = 0; j < length; ++j) {
            final EnumFacing enumfacing = values[j];
            List<BakedQuad> list = p_renderModelFlat_2_.getFaceQuads(enumfacing);
            if (!list.isEmpty()) {
                final BlockPos blockpos = p_renderModelFlat_4_.offset(enumfacing);
                if (!p_renderModelFlat_6_ || block.shouldSideBeRendered(p_renderModelFlat_1_, blockpos, enumfacing)) {
                    final int i = block.getMixedBrightnessForBlock(p_renderModelFlat_1_, blockpos);
                    list = BlockModelCustomizer.getRenderQuads(list, p_renderModelFlat_1_, p_renderModelFlat_3_, p_renderModelFlat_4_, enumfacing, enumworldblocklayer, 0L, renderenv);
                    this.renderQuadsFlat(p_renderModelFlat_1_, p_renderModelFlat_3_, p_renderModelFlat_4_, enumfacing, i, false, p_renderModelFlat_5_, list, renderenv);
                    flag = true;
                }
            }
        }
        List<BakedQuad> list2 = p_renderModelFlat_2_.getGeneralQuads();
        if (list2.size() > 0) {
            list2 = BlockModelCustomizer.getRenderQuads(list2, p_renderModelFlat_1_, p_renderModelFlat_3_, p_renderModelFlat_4_, null, enumworldblocklayer, 0L, renderenv);
            this.renderQuadsFlat(p_renderModelFlat_1_, p_renderModelFlat_3_, p_renderModelFlat_4_, null, -1, true, p_renderModelFlat_5_, list2, renderenv);
            flag = true;
        }
        return flag;
    }
    
    private void renderQuadsSmooth(final IBlockAccess p_renderQuadsSmooth_1_, final IBlockState p_renderQuadsSmooth_2_, final BlockPos p_renderQuadsSmooth_3_, final WorldRenderer p_renderQuadsSmooth_4_, final List<BakedQuad> p_renderQuadsSmooth_5_, final RenderEnv p_renderQuadsSmooth_6_) {
        final Block block = p_renderQuadsSmooth_2_.getBlock();
        final float[] afloat = p_renderQuadsSmooth_6_.getQuadBounds();
        final BitSet bitset = p_renderQuadsSmooth_6_.getBoundsFlags();
        final AmbientOcclusionFace blockmodelrenderer$ambientocclusionface = p_renderQuadsSmooth_6_.getAoFace();
        double d0 = p_renderQuadsSmooth_3_.getX();
        double d2 = p_renderQuadsSmooth_3_.getY();
        double d3 = p_renderQuadsSmooth_3_.getZ();
        final Block.EnumOffsetType block$enumoffsettype = block.getOffsetType();
        if (block$enumoffsettype != Block.EnumOffsetType.NONE) {
            final long i = MathHelper.getPositionRandom(p_renderQuadsSmooth_3_);
            d0 += ((i >> 16 & 0xFL) / 15.0f - 0.5) * 0.5;
            d3 += ((i >> 24 & 0xFL) / 15.0f - 0.5) * 0.5;
            if (block$enumoffsettype == Block.EnumOffsetType.XYZ) {
                d2 += ((i >> 20 & 0xFL) / 15.0f - 1.0) * 0.2;
            }
        }
        for (final BakedQuad bakedquad : p_renderQuadsSmooth_5_) {
            this.fillQuadBounds(block, bakedquad.getVertexData(), bakedquad.getFace(), afloat, bitset);
            blockmodelrenderer$ambientocclusionface.updateVertexBrightness(p_renderQuadsSmooth_1_, block, p_renderQuadsSmooth_3_, bakedquad.getFace(), afloat, bitset);
            if (bakedquad.getSprite().isEmissive) {
                blockmodelrenderer$ambientocclusionface.setMaxBlockLight();
            }
            if (p_renderQuadsSmooth_4_.isMultiTexture()) {
                p_renderQuadsSmooth_4_.addVertexData(bakedquad.getVertexDataSingle());
            }
            else {
                p_renderQuadsSmooth_4_.addVertexData(bakedquad.getVertexData());
            }
            p_renderQuadsSmooth_4_.putSprite(bakedquad.getSprite());
            p_renderQuadsSmooth_4_.putBrightness4(blockmodelrenderer$ambientocclusionface.vertexBrightness[0], blockmodelrenderer$ambientocclusionface.vertexBrightness[1], blockmodelrenderer$ambientocclusionface.vertexBrightness[2], blockmodelrenderer$ambientocclusionface.vertexBrightness[3]);
            final int j = CustomColors.getColorMultiplier(bakedquad, p_renderQuadsSmooth_2_, p_renderQuadsSmooth_1_, p_renderQuadsSmooth_3_, p_renderQuadsSmooth_6_);
            if (!bakedquad.hasTintIndex() && j == -1) {
                if (BlockModelRenderer.separateAoLightValue) {
                    p_renderQuadsSmooth_4_.putColorMultiplierRgba(1.0f, 1.0f, 1.0f, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0], 4);
                    p_renderQuadsSmooth_4_.putColorMultiplierRgba(1.0f, 1.0f, 1.0f, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1], 3);
                    p_renderQuadsSmooth_4_.putColorMultiplierRgba(1.0f, 1.0f, 1.0f, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2], 2);
                    p_renderQuadsSmooth_4_.putColorMultiplierRgba(1.0f, 1.0f, 1.0f, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3], 1);
                }
                else {
                    p_renderQuadsSmooth_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0], 4);
                    p_renderQuadsSmooth_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1], 3);
                    p_renderQuadsSmooth_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2], 2);
                    p_renderQuadsSmooth_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3], 1);
                }
            }
            else {
                int k;
                if (j != -1) {
                    k = j;
                }
                else {
                    k = block.colorMultiplier(p_renderQuadsSmooth_1_, p_renderQuadsSmooth_3_, bakedquad.getTintIndex());
                }
                if (EntityRenderer.anaglyphEnable) {
                    k = TextureUtil.anaglyphColor(k);
                }
                final float f = (k >> 16 & 0xFF) / 255.0f;
                final float f2 = (k >> 8 & 0xFF) / 255.0f;
                final float f3 = (k & 0xFF) / 255.0f;
                if (BlockModelRenderer.separateAoLightValue) {
                    p_renderQuadsSmooth_4_.putColorMultiplierRgba(f, f2, f3, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0], 4);
                    p_renderQuadsSmooth_4_.putColorMultiplierRgba(f, f2, f3, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1], 3);
                    p_renderQuadsSmooth_4_.putColorMultiplierRgba(f, f2, f3, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2], 2);
                    p_renderQuadsSmooth_4_.putColorMultiplierRgba(f, f2, f3, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3], 1);
                }
                else {
                    p_renderQuadsSmooth_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0] * f, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0] * f2, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0] * f3, 4);
                    p_renderQuadsSmooth_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1] * f, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1] * f2, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1] * f3, 3);
                    p_renderQuadsSmooth_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2] * f, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2] * f2, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2] * f3, 2);
                    p_renderQuadsSmooth_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3] * f, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3] * f2, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3] * f3, 1);
                }
            }
            p_renderQuadsSmooth_4_.putPosition(d0, d2, d3);
        }
    }
    
    private void fillQuadBounds(final Block blockIn, final int[] vertexData, final EnumFacing facingIn, final float[] quadBounds, final BitSet boundsFlags) {
        float f = 32.0f;
        float f2 = 32.0f;
        float f3 = 32.0f;
        float f4 = -32.0f;
        float f5 = -32.0f;
        float f6 = -32.0f;
        final int i = vertexData.length / 4;
        for (int j = 0; j < 4; ++j) {
            final float f7 = Float.intBitsToFloat(vertexData[j * i]);
            final float f8 = Float.intBitsToFloat(vertexData[j * i + 1]);
            final float f9 = Float.intBitsToFloat(vertexData[j * i + 2]);
            f = Math.min(f, f7);
            f2 = Math.min(f2, f8);
            f3 = Math.min(f3, f9);
            f4 = Math.max(f4, f7);
            f5 = Math.max(f5, f8);
            f6 = Math.max(f6, f9);
        }
        if (quadBounds != null) {
            quadBounds[EnumFacing.WEST.getIndex()] = f;
            quadBounds[EnumFacing.EAST.getIndex()] = f4;
            quadBounds[EnumFacing.DOWN.getIndex()] = f2;
            quadBounds[EnumFacing.UP.getIndex()] = f5;
            quadBounds[EnumFacing.NORTH.getIndex()] = f3;
            quadBounds[EnumFacing.SOUTH.getIndex()] = f6;
            final int k = EnumFacing.VALUES.length;
            quadBounds[EnumFacing.WEST.getIndex() + k] = 1.0f - f;
            quadBounds[EnumFacing.EAST.getIndex() + k] = 1.0f - f4;
            quadBounds[EnumFacing.DOWN.getIndex() + k] = 1.0f - f2;
            quadBounds[EnumFacing.UP.getIndex() + k] = 1.0f - f5;
            quadBounds[EnumFacing.NORTH.getIndex() + k] = 1.0f - f3;
            quadBounds[EnumFacing.SOUTH.getIndex() + k] = 1.0f - f6;
        }
        final float f10 = 1.0E-4f;
        final float f11 = 0.9999f;
        switch (facingIn) {
            case DOWN: {
                boundsFlags.set(1, f >= 1.0E-4f || f3 >= 1.0E-4f || f4 <= 0.9999f || f6 <= 0.9999f);
                boundsFlags.set(0, (f2 < 1.0E-4f || blockIn.isFullCube()) && f2 == f5);
                break;
            }
            case UP: {
                boundsFlags.set(1, f >= 1.0E-4f || f3 >= 1.0E-4f || f4 <= 0.9999f || f6 <= 0.9999f);
                boundsFlags.set(0, (f5 > 0.9999f || blockIn.isFullCube()) && f2 == f5);
                break;
            }
            case NORTH: {
                boundsFlags.set(1, f >= 1.0E-4f || f2 >= 1.0E-4f || f4 <= 0.9999f || f5 <= 0.9999f);
                boundsFlags.set(0, (f3 < 1.0E-4f || blockIn.isFullCube()) && f3 == f6);
                break;
            }
            case SOUTH: {
                boundsFlags.set(1, f >= 1.0E-4f || f2 >= 1.0E-4f || f4 <= 0.9999f || f5 <= 0.9999f);
                boundsFlags.set(0, (f6 > 0.9999f || blockIn.isFullCube()) && f3 == f6);
                break;
            }
            case WEST: {
                boundsFlags.set(1, f2 >= 1.0E-4f || f3 >= 1.0E-4f || f5 <= 0.9999f || f6 <= 0.9999f);
                boundsFlags.set(0, (f < 1.0E-4f || blockIn.isFullCube()) && f == f4);
                break;
            }
            case EAST: {
                boundsFlags.set(1, f2 >= 1.0E-4f || f3 >= 1.0E-4f || f5 <= 0.9999f || f6 <= 0.9999f);
                boundsFlags.set(0, (f4 > 0.9999f || blockIn.isFullCube()) && f == f4);
                break;
            }
        }
    }
    
    private void renderQuadsFlat(final IBlockAccess p_renderQuadsFlat_1_, final IBlockState p_renderQuadsFlat_2_, final BlockPos p_renderQuadsFlat_3_, final EnumFacing p_renderQuadsFlat_4_, int p_renderQuadsFlat_5_, final boolean p_renderQuadsFlat_6_, final WorldRenderer p_renderQuadsFlat_7_, final List<BakedQuad> p_renderQuadsFlat_8_, final RenderEnv p_renderQuadsFlat_9_) {
        final Block block = p_renderQuadsFlat_2_.getBlock();
        final BitSet bitset = p_renderQuadsFlat_9_.getBoundsFlags();
        double d0 = p_renderQuadsFlat_3_.getX();
        double d2 = p_renderQuadsFlat_3_.getY();
        double d3 = p_renderQuadsFlat_3_.getZ();
        final Block.EnumOffsetType block$enumoffsettype = block.getOffsetType();
        if (block$enumoffsettype != Block.EnumOffsetType.NONE) {
            final int i = p_renderQuadsFlat_3_.getX();
            final int j = p_renderQuadsFlat_3_.getZ();
            long k = (long)(i * 3129871) ^ j * 116129781L;
            k = k * k * 42317861L + k * 11L;
            d0 += ((k >> 16 & 0xFL) / 15.0f - 0.5) * 0.5;
            d3 += ((k >> 24 & 0xFL) / 15.0f - 0.5) * 0.5;
            if (block$enumoffsettype == Block.EnumOffsetType.XYZ) {
                d2 += ((k >> 20 & 0xFL) / 15.0f - 1.0) * 0.2;
            }
        }
        for (final BakedQuad bakedquad : p_renderQuadsFlat_8_) {
            if (p_renderQuadsFlat_6_) {
                this.fillQuadBounds(block, bakedquad.getVertexData(), bakedquad.getFace(), null, bitset);
                p_renderQuadsFlat_5_ = (bitset.get(0) ? block.getMixedBrightnessForBlock(p_renderQuadsFlat_1_, p_renderQuadsFlat_3_.offset(bakedquad.getFace())) : block.getMixedBrightnessForBlock(p_renderQuadsFlat_1_, p_renderQuadsFlat_3_));
            }
            if (bakedquad.getSprite().isEmissive) {
                p_renderQuadsFlat_5_ |= 0xF0;
            }
            if (p_renderQuadsFlat_7_.isMultiTexture()) {
                p_renderQuadsFlat_7_.addVertexData(bakedquad.getVertexDataSingle());
            }
            else {
                p_renderQuadsFlat_7_.addVertexData(bakedquad.getVertexData());
            }
            p_renderQuadsFlat_7_.putSprite(bakedquad.getSprite());
            p_renderQuadsFlat_7_.putBrightness4(p_renderQuadsFlat_5_, p_renderQuadsFlat_5_, p_renderQuadsFlat_5_, p_renderQuadsFlat_5_);
            final int i2 = CustomColors.getColorMultiplier(bakedquad, p_renderQuadsFlat_2_, p_renderQuadsFlat_1_, p_renderQuadsFlat_3_, p_renderQuadsFlat_9_);
            if (bakedquad.hasTintIndex() || i2 != -1) {
                int l;
                if (i2 != -1) {
                    l = i2;
                }
                else {
                    l = block.colorMultiplier(p_renderQuadsFlat_1_, p_renderQuadsFlat_3_, bakedquad.getTintIndex());
                }
                if (EntityRenderer.anaglyphEnable) {
                    l = TextureUtil.anaglyphColor(l);
                }
                final float f = (l >> 16 & 0xFF) / 255.0f;
                final float f2 = (l >> 8 & 0xFF) / 255.0f;
                final float f3 = (l & 0xFF) / 255.0f;
                p_renderQuadsFlat_7_.putColorMultiplier(f, f2, f3, 4);
                p_renderQuadsFlat_7_.putColorMultiplier(f, f2, f3, 3);
                p_renderQuadsFlat_7_.putColorMultiplier(f, f2, f3, 2);
                p_renderQuadsFlat_7_.putColorMultiplier(f, f2, f3, 1);
            }
            p_renderQuadsFlat_7_.putPosition(d0, d2, d3);
        }
    }
    
    public void renderModelBrightnessColor(final IBakedModel bakedModel, final float p_178262_2_, final float red, final float green, final float blue) {
        EnumFacing[] values;
        for (int length = (values = EnumFacing.VALUES).length, i = 0; i < length; ++i) {
            final EnumFacing enumfacing = values[i];
            this.renderModelBrightnessColorQuads(p_178262_2_, red, green, blue, bakedModel.getFaceQuads(enumfacing));
        }
        this.renderModelBrightnessColorQuads(p_178262_2_, red, green, blue, bakedModel.getGeneralQuads());
    }
    
    public void renderModelBrightness(final IBakedModel model, final IBlockState p_178266_2_, final float brightness, final boolean p_178266_4_) {
        final Block block = p_178266_2_.getBlock();
        block.setBlockBoundsForItemRender();
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        int i = block.getRenderColor(block.getStateForEntityRender(p_178266_2_));
        if (EntityRenderer.anaglyphEnable) {
            i = TextureUtil.anaglyphColor(i);
        }
        final float f = (i >> 16 & 0xFF) / 255.0f;
        final float f2 = (i >> 8 & 0xFF) / 255.0f;
        final float f3 = (i & 0xFF) / 255.0f;
        if (!p_178266_4_) {
            GlStateManager.color(brightness, brightness, brightness, 1.0f);
        }
        this.renderModelBrightnessColor(model, brightness, f, f2, f3);
    }
    
    private void renderModelBrightnessColorQuads(final float brightness, final float red, final float green, final float blue, final List<BakedQuad> listQuads) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        for (final BakedQuad bakedquad : listQuads) {
            worldrenderer.begin(7, DefaultVertexFormats.ITEM);
            worldrenderer.addVertexData(bakedquad.getVertexData());
            worldrenderer.putSprite(bakedquad.getSprite());
            if (bakedquad.hasTintIndex()) {
                worldrenderer.putColorRGB_F4(red * brightness, green * brightness, blue * brightness);
            }
            else {
                worldrenderer.putColorRGB_F4(brightness, brightness, brightness);
            }
            final Vec3i vec3i = bakedquad.getFace().getDirectionVec();
            worldrenderer.putNormal((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
            tessellator.draw();
        }
    }
    
    public static float fixAoLightValue(final float p_fixAoLightValue_0_) {
        return (p_fixAoLightValue_0_ == 0.2f) ? BlockModelRenderer.aoLightValueOpaque : p_fixAoLightValue_0_;
    }
    
    public static void updateAoLightValue() {
        BlockModelRenderer.aoLightValueOpaque = 1.0f - Config.getAmbientOcclusionLevel() * 0.8f;
        BlockModelRenderer.separateAoLightValue = (Config.isShaders() && Shaders.isSeparateAo());
    }
    
    private void renderOverlayModels(final IBlockAccess p_renderOverlayModels_1_, final IBakedModel p_renderOverlayModels_2_, final IBlockState p_renderOverlayModels_3_, final BlockPos p_renderOverlayModels_4_, final WorldRenderer p_renderOverlayModels_5_, final boolean p_renderOverlayModels_6_, final long p_renderOverlayModels_7_, final RenderEnv p_renderOverlayModels_9_, final boolean p_renderOverlayModels_10_) {
        if (p_renderOverlayModels_9_.isOverlaysRendered()) {
            for (int i = 0; i < BlockModelRenderer.OVERLAY_LAYERS.length; ++i) {
                final EnumWorldBlockLayer enumworldblocklayer = BlockModelRenderer.OVERLAY_LAYERS[i];
                final ListQuadsOverlay listquadsoverlay = p_renderOverlayModels_9_.getListQuadsOverlay(enumworldblocklayer);
                if (listquadsoverlay.size() > 0) {
                    final RegionRenderCacheBuilder regionrendercachebuilder = p_renderOverlayModels_9_.getRegionRenderCacheBuilder();
                    if (regionrendercachebuilder != null) {
                        final WorldRenderer worldrenderer = regionrendercachebuilder.getWorldRendererByLayer(enumworldblocklayer);
                        if (!worldrenderer.isDrawing()) {
                            worldrenderer.begin(7, DefaultVertexFormats.BLOCK);
                            worldrenderer.setTranslation(p_renderOverlayModels_5_.getXOffset(), p_renderOverlayModels_5_.getYOffset(), p_renderOverlayModels_5_.getZOffset());
                        }
                        for (int j = 0; j < listquadsoverlay.size(); ++j) {
                            final BakedQuad bakedquad = listquadsoverlay.getQuad(j);
                            final List<BakedQuad> list = listquadsoverlay.getListQuadsSingle(bakedquad);
                            final IBlockState iblockstate = listquadsoverlay.getBlockState(j);
                            if (bakedquad.getQuadEmissive() != null) {
                                listquadsoverlay.addQuad(bakedquad.getQuadEmissive(), iblockstate);
                            }
                            p_renderOverlayModels_9_.reset(iblockstate, p_renderOverlayModels_4_);
                            if (p_renderOverlayModels_10_) {
                                this.renderQuadsSmooth(p_renderOverlayModels_1_, iblockstate, p_renderOverlayModels_4_, worldrenderer, list, p_renderOverlayModels_9_);
                            }
                            else {
                                final int k = iblockstate.getBlock().getMixedBrightnessForBlock(p_renderOverlayModels_1_, p_renderOverlayModels_4_.offset(bakedquad.getFace()));
                                this.renderQuadsFlat(p_renderOverlayModels_1_, iblockstate, p_renderOverlayModels_4_, bakedquad.getFace(), k, false, worldrenderer, list, p_renderOverlayModels_9_);
                            }
                        }
                    }
                    listquadsoverlay.clear();
                }
            }
        }
        if (Config.isBetterSnow() && !p_renderOverlayModels_9_.isBreakingAnimation() && BetterSnow.shouldRender(p_renderOverlayModels_1_, p_renderOverlayModels_3_, p_renderOverlayModels_4_)) {
            final IBakedModel ibakedmodel = BetterSnow.getModelSnowLayer();
            final IBlockState iblockstate2 = BetterSnow.getStateSnowLayer();
            this.renderModel(p_renderOverlayModels_1_, ibakedmodel, iblockstate2, p_renderOverlayModels_4_, p_renderOverlayModels_5_, p_renderOverlayModels_6_);
        }
    }
    
    public enum EnumNeighborInfo
    {
        DOWN("DOWN", 0, new EnumFacing[] { EnumFacing.WEST, EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.SOUTH }, 0.5f, false, new Orientation[0], new Orientation[0], new Orientation[0], new Orientation[0]), 
        UP("UP", 1, new EnumFacing[] { EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH }, 1.0f, false, new Orientation[0], new Orientation[0], new Orientation[0], new Orientation[0]), 
        NORTH("NORTH", 2, new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN, EnumFacing.EAST, EnumFacing.WEST }, 0.8f, true, new Orientation[] { Orientation.UP, Orientation.FLIP_WEST, Orientation.UP, Orientation.WEST, Orientation.FLIP_UP, Orientation.WEST, Orientation.FLIP_UP, Orientation.FLIP_WEST }, new Orientation[] { Orientation.UP, Orientation.FLIP_EAST, Orientation.UP, Orientation.EAST, Orientation.FLIP_UP, Orientation.EAST, Orientation.FLIP_UP, Orientation.FLIP_EAST }, new Orientation[] { Orientation.DOWN, Orientation.FLIP_EAST, Orientation.DOWN, Orientation.EAST, Orientation.FLIP_DOWN, Orientation.EAST, Orientation.FLIP_DOWN, Orientation.FLIP_EAST }, new Orientation[] { Orientation.DOWN, Orientation.FLIP_WEST, Orientation.DOWN, Orientation.WEST, Orientation.FLIP_DOWN, Orientation.WEST, Orientation.FLIP_DOWN, Orientation.FLIP_WEST }), 
        SOUTH("SOUTH", 3, new EnumFacing[] { EnumFacing.WEST, EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.UP }, 0.8f, true, new Orientation[] { Orientation.UP, Orientation.FLIP_WEST, Orientation.FLIP_UP, Orientation.FLIP_WEST, Orientation.FLIP_UP, Orientation.WEST, Orientation.UP, Orientation.WEST }, new Orientation[] { Orientation.DOWN, Orientation.FLIP_WEST, Orientation.FLIP_DOWN, Orientation.FLIP_WEST, Orientation.FLIP_DOWN, Orientation.WEST, Orientation.DOWN, Orientation.WEST }, new Orientation[] { Orientation.DOWN, Orientation.FLIP_EAST, Orientation.FLIP_DOWN, Orientation.FLIP_EAST, Orientation.FLIP_DOWN, Orientation.EAST, Orientation.DOWN, Orientation.EAST }, new Orientation[] { Orientation.UP, Orientation.FLIP_EAST, Orientation.FLIP_UP, Orientation.FLIP_EAST, Orientation.FLIP_UP, Orientation.EAST, Orientation.UP, Orientation.EAST }), 
        WEST("WEST", 4, new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.SOUTH }, 0.6f, true, new Orientation[] { Orientation.UP, Orientation.SOUTH, Orientation.UP, Orientation.FLIP_SOUTH, Orientation.FLIP_UP, Orientation.FLIP_SOUTH, Orientation.FLIP_UP, Orientation.SOUTH }, new Orientation[] { Orientation.UP, Orientation.NORTH, Orientation.UP, Orientation.FLIP_NORTH, Orientation.FLIP_UP, Orientation.FLIP_NORTH, Orientation.FLIP_UP, Orientation.NORTH }, new Orientation[] { Orientation.DOWN, Orientation.NORTH, Orientation.DOWN, Orientation.FLIP_NORTH, Orientation.FLIP_DOWN, Orientation.FLIP_NORTH, Orientation.FLIP_DOWN, Orientation.NORTH }, new Orientation[] { Orientation.DOWN, Orientation.SOUTH, Orientation.DOWN, Orientation.FLIP_SOUTH, Orientation.FLIP_DOWN, Orientation.FLIP_SOUTH, Orientation.FLIP_DOWN, Orientation.SOUTH }), 
        EAST("EAST", 5, new EnumFacing[] { EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH }, 0.6f, true, new Orientation[] { Orientation.FLIP_DOWN, Orientation.SOUTH, Orientation.FLIP_DOWN, Orientation.FLIP_SOUTH, Orientation.DOWN, Orientation.FLIP_SOUTH, Orientation.DOWN, Orientation.SOUTH }, new Orientation[] { Orientation.FLIP_DOWN, Orientation.NORTH, Orientation.FLIP_DOWN, Orientation.FLIP_NORTH, Orientation.DOWN, Orientation.FLIP_NORTH, Orientation.DOWN, Orientation.NORTH }, new Orientation[] { Orientation.FLIP_UP, Orientation.NORTH, Orientation.FLIP_UP, Orientation.FLIP_NORTH, Orientation.UP, Orientation.FLIP_NORTH, Orientation.UP, Orientation.NORTH }, new Orientation[] { Orientation.FLIP_UP, Orientation.SOUTH, Orientation.FLIP_UP, Orientation.FLIP_SOUTH, Orientation.UP, Orientation.FLIP_SOUTH, Orientation.UP, Orientation.SOUTH });
        
        protected final EnumFacing[] field_178276_g;
        protected final float field_178288_h;
        protected final boolean field_178289_i;
        protected final Orientation[] field_178286_j;
        protected final Orientation[] field_178287_k;
        protected final Orientation[] field_178284_l;
        protected final Orientation[] field_178285_m;
        private static final EnumNeighborInfo[] VALUES;
        
        static {
            (VALUES = new EnumNeighborInfo[6])[EnumFacing.DOWN.getIndex()] = EnumNeighborInfo.DOWN;
            EnumNeighborInfo.VALUES[EnumFacing.UP.getIndex()] = EnumNeighborInfo.UP;
            EnumNeighborInfo.VALUES[EnumFacing.NORTH.getIndex()] = EnumNeighborInfo.NORTH;
            EnumNeighborInfo.VALUES[EnumFacing.SOUTH.getIndex()] = EnumNeighborInfo.SOUTH;
            EnumNeighborInfo.VALUES[EnumFacing.WEST.getIndex()] = EnumNeighborInfo.WEST;
            EnumNeighborInfo.VALUES[EnumFacing.EAST.getIndex()] = EnumNeighborInfo.EAST;
        }
        
        private EnumNeighborInfo(final String s, final int n, final EnumFacing[] p_i46236_3_, final float p_i46236_4_, final boolean p_i46236_5_, final Orientation[] p_i46236_6_, final Orientation[] p_i46236_7_, final Orientation[] p_i46236_8_, final Orientation[] p_i46236_9_) {
            this.field_178276_g = p_i46236_3_;
            this.field_178288_h = p_i46236_4_;
            this.field_178289_i = p_i46236_5_;
            this.field_178286_j = p_i46236_6_;
            this.field_178287_k = p_i46236_7_;
            this.field_178284_l = p_i46236_8_;
            this.field_178285_m = p_i46236_9_;
        }
        
        public static EnumNeighborInfo getNeighbourInfo(final EnumFacing p_178273_0_) {
            return EnumNeighborInfo.VALUES[p_178273_0_.getIndex()];
        }
    }
    
    public enum Orientation
    {
        DOWN("DOWN", 0, EnumFacing.DOWN, false), 
        UP("UP", 1, EnumFacing.UP, false), 
        NORTH("NORTH", 2, EnumFacing.NORTH, false), 
        SOUTH("SOUTH", 3, EnumFacing.SOUTH, false), 
        WEST("WEST", 4, EnumFacing.WEST, false), 
        EAST("EAST", 5, EnumFacing.EAST, false), 
        FLIP_DOWN("FLIP_DOWN", 6, EnumFacing.DOWN, true), 
        FLIP_UP("FLIP_UP", 7, EnumFacing.UP, true), 
        FLIP_NORTH("FLIP_NORTH", 8, EnumFacing.NORTH, true), 
        FLIP_SOUTH("FLIP_SOUTH", 9, EnumFacing.SOUTH, true), 
        FLIP_WEST("FLIP_WEST", 10, EnumFacing.WEST, true), 
        FLIP_EAST("FLIP_EAST", 11, EnumFacing.EAST, true);
        
        protected final int field_178229_m;
        
        private Orientation(final String s, final int n, final EnumFacing p_i46233_3_, final boolean p_i46233_4_) {
            this.field_178229_m = p_i46233_3_.getIndex() + (p_i46233_4_ ? EnumFacing.values().length : 0);
        }
    }
    
    enum VertexTranslations
    {
        DOWN("DOWN", 0, 0, 1, 2, 3), 
        UP("UP", 1, 2, 3, 0, 1), 
        NORTH("NORTH", 2, 3, 0, 1, 2), 
        SOUTH("SOUTH", 3, 0, 1, 2, 3), 
        WEST("WEST", 4, 3, 0, 1, 2), 
        EAST("EAST", 5, 1, 2, 3, 0);
        
        private final int field_178191_g;
        private final int field_178200_h;
        private final int field_178201_i;
        private final int field_178198_j;
        private static final VertexTranslations[] VALUES;
        
        static {
            (VALUES = new VertexTranslations[6])[EnumFacing.DOWN.getIndex()] = VertexTranslations.DOWN;
            VertexTranslations.VALUES[EnumFacing.UP.getIndex()] = VertexTranslations.UP;
            VertexTranslations.VALUES[EnumFacing.NORTH.getIndex()] = VertexTranslations.NORTH;
            VertexTranslations.VALUES[EnumFacing.SOUTH.getIndex()] = VertexTranslations.SOUTH;
            VertexTranslations.VALUES[EnumFacing.WEST.getIndex()] = VertexTranslations.WEST;
            VertexTranslations.VALUES[EnumFacing.EAST.getIndex()] = VertexTranslations.EAST;
        }
        
        private VertexTranslations(final String s, final int n, final int p_i46234_3_, final int p_i46234_4_, final int p_i46234_5_, final int p_i46234_6_) {
            this.field_178191_g = p_i46234_3_;
            this.field_178200_h = p_i46234_4_;
            this.field_178201_i = p_i46234_5_;
            this.field_178198_j = p_i46234_6_;
        }
        
        public static VertexTranslations getVertexTranslations(final EnumFacing p_178184_0_) {
            return VertexTranslations.VALUES[p_178184_0_.getIndex()];
        }
    }
    
    public static class AmbientOcclusionFace
    {
        private final float[] vertexColorMultiplier;
        private final int[] vertexBrightness;
        
        public AmbientOcclusionFace() {
            this(null);
        }
        
        public AmbientOcclusionFace(final BlockModelRenderer p_i46235_1_) {
            this.vertexColorMultiplier = new float[4];
            this.vertexBrightness = new int[4];
        }
        
        public void setMaxBlockLight() {
            final int i = 240;
            final int[] vertexBrightness = this.vertexBrightness;
            final int n = 0;
            vertexBrightness[n] |= i;
            final int[] vertexBrightness2 = this.vertexBrightness;
            final int n2 = 1;
            vertexBrightness2[n2] |= i;
            final int[] vertexBrightness3 = this.vertexBrightness;
            final int n3 = 2;
            vertexBrightness3[n3] |= i;
            final int[] vertexBrightness4 = this.vertexBrightness;
            final int n4 = 3;
            vertexBrightness4[n4] |= i;
            this.vertexColorMultiplier[0] = 1.0f;
            this.vertexColorMultiplier[1] = 1.0f;
            this.vertexColorMultiplier[2] = 1.0f;
            this.vertexColorMultiplier[3] = 1.0f;
        }
        
        public void updateVertexBrightness(final IBlockAccess blockAccessIn, final Block blockIn, final BlockPos blockPosIn, final EnumFacing facingIn, final float[] quadBounds, final BitSet boundsFlags) {
            final BlockPos blockpos = boundsFlags.get(0) ? blockPosIn.offset(facingIn) : blockPosIn;
            final EnumNeighborInfo blockmodelrenderer$enumneighborinfo = EnumNeighborInfo.getNeighbourInfo(facingIn);
            final BlockPos blockpos2 = blockpos.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[0]);
            final BlockPos blockpos3 = blockpos.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[1]);
            final BlockPos blockpos4 = blockpos.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[2]);
            final BlockPos blockpos5 = blockpos.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[3]);
            final int i = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos2);
            final int j = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos3);
            final int k = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos4);
            final int l = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos5);
            final float f = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(blockpos2).getBlock().getAmbientOcclusionLightValue());
            final float f2 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(blockpos3).getBlock().getAmbientOcclusionLightValue());
            final float f3 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(blockpos4).getBlock().getAmbientOcclusionLightValue());
            final float f4 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(blockpos5).getBlock().getAmbientOcclusionLightValue());
            final boolean flag = blockAccessIn.getBlockState(blockpos2.offset(facingIn)).getBlock().isTranslucent();
            final boolean flag2 = blockAccessIn.getBlockState(blockpos3.offset(facingIn)).getBlock().isTranslucent();
            final boolean flag3 = blockAccessIn.getBlockState(blockpos4.offset(facingIn)).getBlock().isTranslucent();
            final boolean flag4 = blockAccessIn.getBlockState(blockpos5.offset(facingIn)).getBlock().isTranslucent();
            float f5;
            int i2;
            if (!flag3 && !flag) {
                f5 = f;
                i2 = i;
            }
            else {
                final BlockPos blockpos6 = blockpos2.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[2]);
                f5 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(blockpos6).getBlock().getAmbientOcclusionLightValue());
                i2 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos6);
            }
            float f6;
            int j2;
            if (!flag4 && !flag) {
                f6 = f;
                j2 = i;
            }
            else {
                final BlockPos blockpos7 = blockpos2.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[3]);
                f6 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(blockpos7).getBlock().getAmbientOcclusionLightValue());
                j2 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos7);
            }
            float f7;
            int k2;
            if (!flag3 && !flag2) {
                f7 = f2;
                k2 = j;
            }
            else {
                final BlockPos blockpos8 = blockpos3.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[2]);
                f7 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(blockpos8).getBlock().getAmbientOcclusionLightValue());
                k2 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos8);
            }
            float f8;
            int l2;
            if (!flag4 && !flag2) {
                f8 = f2;
                l2 = j;
            }
            else {
                final BlockPos blockpos9 = blockpos3.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[3]);
                f8 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(blockpos9).getBlock().getAmbientOcclusionLightValue());
                l2 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos9);
            }
            int i3 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockPosIn);
            if (boundsFlags.get(0) || !blockAccessIn.getBlockState(blockPosIn.offset(facingIn)).getBlock().isOpaqueCube()) {
                i3 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockPosIn.offset(facingIn));
            }
            float f9 = boundsFlags.get(0) ? blockAccessIn.getBlockState(blockpos).getBlock().getAmbientOcclusionLightValue() : blockAccessIn.getBlockState(blockPosIn).getBlock().getAmbientOcclusionLightValue();
            f9 = BlockModelRenderer.fixAoLightValue(f9);
            final VertexTranslations blockmodelrenderer$vertextranslations = VertexTranslations.getVertexTranslations(facingIn);
            if (boundsFlags.get(1) && blockmodelrenderer$enumneighborinfo.field_178289_i) {
                final float f10 = (f4 + f + f6 + f9) * 0.25f;
                final float f11 = (f3 + f + f5 + f9) * 0.25f;
                final float f12 = (f3 + f2 + f7 + f9) * 0.25f;
                final float f13 = (f4 + f2 + f8 + f9) * 0.25f;
                final float f14 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[0].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[1].field_178229_m];
                final float f15 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[2].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[3].field_178229_m];
                final float f16 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[4].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[5].field_178229_m];
                final float f17 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[6].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[7].field_178229_m];
                final float f18 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[0].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[1].field_178229_m];
                final float f19 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[2].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[3].field_178229_m];
                final float f20 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[4].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[5].field_178229_m];
                final float f21 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[6].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[7].field_178229_m];
                final float f22 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[0].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[1].field_178229_m];
                final float f23 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[2].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[3].field_178229_m];
                final float f24 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[4].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[5].field_178229_m];
                final float f25 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[6].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[7].field_178229_m];
                final float f26 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[0].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[1].field_178229_m];
                final float f27 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[2].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[3].field_178229_m];
                final float f28 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[4].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[5].field_178229_m];
                final float f29 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[6].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[7].field_178229_m];
                this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.field_178191_g] = f10 * f14 + f11 * f15 + f12 * f16 + f13 * f17;
                this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.field_178200_h] = f10 * f18 + f11 * f19 + f12 * f20 + f13 * f21;
                this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.field_178201_i] = f10 * f22 + f11 * f23 + f12 * f24 + f13 * f25;
                this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.field_178198_j] = f10 * f26 + f11 * f27 + f12 * f28 + f13 * f29;
                final int i4 = this.getAoBrightness(l, i, j2, i3);
                final int j3 = this.getAoBrightness(k, i, i2, i3);
                final int k3 = this.getAoBrightness(k, j, k2, i3);
                final int l3 = this.getAoBrightness(l, j, l2, i3);
                this.vertexBrightness[blockmodelrenderer$vertextranslations.field_178191_g] = this.getVertexBrightness(i4, j3, k3, l3, f14, f15, f16, f17);
                this.vertexBrightness[blockmodelrenderer$vertextranslations.field_178200_h] = this.getVertexBrightness(i4, j3, k3, l3, f18, f19, f20, f21);
                this.vertexBrightness[blockmodelrenderer$vertextranslations.field_178201_i] = this.getVertexBrightness(i4, j3, k3, l3, f22, f23, f24, f25);
                this.vertexBrightness[blockmodelrenderer$vertextranslations.field_178198_j] = this.getVertexBrightness(i4, j3, k3, l3, f26, f27, f28, f29);
            }
            else {
                final float f30 = (f4 + f + f6 + f9) * 0.25f;
                final float f31 = (f3 + f + f5 + f9) * 0.25f;
                final float f32 = (f3 + f2 + f7 + f9) * 0.25f;
                final float f33 = (f4 + f2 + f8 + f9) * 0.25f;
                this.vertexBrightness[blockmodelrenderer$vertextranslations.field_178191_g] = this.getAoBrightness(l, i, j2, i3);
                this.vertexBrightness[blockmodelrenderer$vertextranslations.field_178200_h] = this.getAoBrightness(k, i, i2, i3);
                this.vertexBrightness[blockmodelrenderer$vertextranslations.field_178201_i] = this.getAoBrightness(k, j, k2, i3);
                this.vertexBrightness[blockmodelrenderer$vertextranslations.field_178198_j] = this.getAoBrightness(l, j, l2, i3);
                this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.field_178191_g] = f30;
                this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.field_178200_h] = f31;
                this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.field_178201_i] = f32;
                this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.field_178198_j] = f33;
            }
        }
        
        private int getAoBrightness(int br1, int br2, int br3, final int br4) {
            if (br1 == 0) {
                br1 = br4;
            }
            if (br2 == 0) {
                br2 = br4;
            }
            if (br3 == 0) {
                br3 = br4;
            }
            return br1 + br2 + br3 + br4 >> 2 & 0xFF00FF;
        }
        
        private int getVertexBrightness(final int p_178203_1_, final int p_178203_2_, final int p_178203_3_, final int p_178203_4_, final float p_178203_5_, final float p_178203_6_, final float p_178203_7_, final float p_178203_8_) {
            final int i = (int)((p_178203_1_ >> 16 & 0xFF) * p_178203_5_ + (p_178203_2_ >> 16 & 0xFF) * p_178203_6_ + (p_178203_3_ >> 16 & 0xFF) * p_178203_7_ + (p_178203_4_ >> 16 & 0xFF) * p_178203_8_) & 0xFF;
            final int j = (int)((p_178203_1_ & 0xFF) * p_178203_5_ + (p_178203_2_ & 0xFF) * p_178203_6_ + (p_178203_3_ & 0xFF) * p_178203_7_ + (p_178203_4_ & 0xFF) * p_178203_8_) & 0xFF;
            return i << 16 | j;
        }
    }
}
