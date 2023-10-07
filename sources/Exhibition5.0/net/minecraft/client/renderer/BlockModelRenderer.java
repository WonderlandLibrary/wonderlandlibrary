// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import java.util.Iterator;
import java.util.BitSet;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.optifine.CustomColorizer;
import net.minecraft.optifine.ConnectedTextures;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.Vec3i;
import net.minecraft.util.MathHelper;
import java.util.List;
import net.minecraft.optifine.BetterSnow;
import net.minecraft.optifine.BetterGrass;
import net.minecraft.optifine.RenderEnv;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.CrashReport;
import exhibition.module.Module;
import exhibition.module.impl.render.Xray;
import exhibition.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.world.IBlockAccess;
import net.minecraft.optifine.Config;

public class BlockModelRenderer
{
    private static final String __OBFID = "CL_00002518";
    private static float aoLightValueOpaque;
    
    public static void updateAoLightValue() {
        BlockModelRenderer.aoLightValueOpaque = 1.0f - Config.getAmbientOcclusionLevel() * 0.8f;
    }
    
    public boolean func_178259_a(final IBlockAccess blockAccessIn, final IBakedModel modelIn, final IBlockState blockStateIn, final BlockPos blockPosIn, final WorldRenderer worldRendererIn) {
        final Block var6 = blockStateIn.getBlock();
        var6.setBlockBoundsBasedOnState(blockAccessIn, blockPosIn);
        return this.renderBlockModel(blockAccessIn, modelIn, blockStateIn, blockPosIn, worldRendererIn, true);
    }
    
    public boolean renderBlockModel(final IBlockAccess blockAccessIn, final IBakedModel modelIn, final IBlockState blockState, final BlockPos blockPosIn, final WorldRenderer worldRendererIn, final boolean checkSides) {
        final boolean var7 = Minecraft.isAmbientOcclusionEnabled() && blockState.getBlock().getLightValue() == 0 && modelIn.isGui3d();
        try {
            final Block block = blockState.getBlock();
            if (Client.getModuleManager().isEnabled(Xray.class)) {
                return this.renderModelAmbientOcclusion(blockAccessIn, modelIn, block, blockState, blockPosIn, worldRendererIn, checkSides);
            }
            return var7 ? this.renderModelAmbientOcclusion(blockAccessIn, modelIn, block, blockState, blockPosIn, worldRendererIn, checkSides) : this.renderModelStandard(blockAccessIn, modelIn, block, blockState, blockPosIn, worldRendererIn, checkSides);
        }
        catch (Throwable var9) {
            final CrashReport var8 = CrashReport.makeCrashReport(var9, "Tesselating block model");
            final CrashReportCategory var10 = var8.makeCategory("Block model being tesselated");
            CrashReportCategory.addBlockInfo(var10, blockPosIn, blockState);
            var10.addCrashSection("Using AO", var7);
            throw new ReportedException(var8);
        }
    }
    
    public boolean func_178265_a(final IBlockAccess blockAccessIn, final IBakedModel modelIn, final Block blockIn, final BlockPos blockPosIn, final WorldRenderer worldRendererIn, final boolean checkSides) {
        return this.renderModelAmbientOcclusion(blockAccessIn, modelIn, blockIn, blockAccessIn.getBlockState(blockPosIn), blockPosIn, worldRendererIn, checkSides);
    }
    
    public boolean renderModelAmbientOcclusion(final IBlockAccess blockAccessIn, final IBakedModel modelIn, final Block blockIn, final IBlockState blockStateIn, final BlockPos blockPosIn, final WorldRenderer worldRendererIn, final boolean checkSides) {
        boolean var7 = false;
        worldRendererIn.func_178963_b(983055);
        RenderEnv renderEnv = null;
        for (final EnumFacing modelSnow : EnumFacing.VALUES) {
            List stateSnow = modelIn.func_177551_a(modelSnow);
            if (!stateSnow.isEmpty()) {
                final BlockPos var11 = blockPosIn.offset(modelSnow);
                if (!checkSides || blockIn.shouldSideBeRendered(blockAccessIn, var11, modelSnow)) {
                    if (renderEnv == null) {
                        renderEnv = RenderEnv.getInstance(blockAccessIn, blockStateIn, blockPosIn);
                    }
                    if (!renderEnv.isBreakingAnimation(stateSnow) && Config.isBetterGrass()) {
                        stateSnow = BetterGrass.getFaceQuads(blockAccessIn, blockIn, blockPosIn, modelSnow, stateSnow);
                    }
                    this.renderModelAmbientOcclusionQuads(blockAccessIn, blockIn, blockPosIn, worldRendererIn, stateSnow, renderEnv);
                    var7 = true;
                }
            }
        }
        final List var12 = modelIn.func_177550_a();
        if (var12.size() > 0) {
            if (renderEnv == null) {
                renderEnv = RenderEnv.getInstance(blockAccessIn, blockStateIn, blockPosIn);
            }
            this.renderModelAmbientOcclusionQuads(blockAccessIn, blockIn, blockPosIn, worldRendererIn, var12, renderEnv);
            var7 = true;
        }
        if (renderEnv != null && Config.isBetterSnow() && !renderEnv.isBreakingAnimation() && BetterSnow.shouldRender(blockAccessIn, blockIn, blockStateIn, blockPosIn)) {
            final IBakedModel var13 = BetterSnow.getModelSnowLayer();
            final IBlockState var14 = BetterSnow.getStateSnowLayer();
            this.renderModelAmbientOcclusion(blockAccessIn, var13, var14.getBlock(), var14, blockPosIn, worldRendererIn, true);
        }
        return var7;
    }
    
    public boolean func_178258_b(final IBlockAccess blockAccessIn, final IBakedModel modelIn, final Block blockIn, final BlockPos blockPosIn, final WorldRenderer worldRendererIn, final boolean checkSides) {
        return this.renderModelStandard(blockAccessIn, modelIn, blockIn, blockAccessIn.getBlockState(blockPosIn), blockPosIn, worldRendererIn, checkSides);
    }
    
    public boolean renderModelStandard(final IBlockAccess blockAccessIn, final IBakedModel modelIn, final Block blockIn, final IBlockState blockStateIn, final BlockPos blockPosIn, final WorldRenderer worldRendererIn, final boolean checkSides) {
        boolean var7 = false;
        RenderEnv renderEnv = null;
        for (final EnumFacing modelSnow : EnumFacing.VALUES) {
            List stateSnow = modelIn.func_177551_a(modelSnow);
            if (!stateSnow.isEmpty()) {
                final BlockPos var11 = blockPosIn.offset(modelSnow);
                if (!checkSides || blockIn.shouldSideBeRendered(blockAccessIn, var11, modelSnow)) {
                    if (renderEnv == null) {
                        renderEnv = RenderEnv.getInstance(blockAccessIn, blockStateIn, blockPosIn);
                    }
                    if (!renderEnv.isBreakingAnimation(stateSnow) && Config.isBetterGrass()) {
                        stateSnow = BetterGrass.getFaceQuads(blockAccessIn, blockIn, blockPosIn, modelSnow, stateSnow);
                    }
                    final int var12 = blockIn.getMixedBrightnessForBlock(blockAccessIn, var11);
                    this.renderModelStandardQuads(blockAccessIn, blockIn, blockPosIn, modelSnow, var12, false, worldRendererIn, stateSnow, renderEnv);
                    var7 = true;
                }
            }
        }
        final List var13 = modelIn.func_177550_a();
        if (var13.size() > 0) {
            if (renderEnv == null) {
                renderEnv = RenderEnv.getInstance(blockAccessIn, blockStateIn, blockPosIn);
            }
            this.renderModelStandardQuads(blockAccessIn, blockIn, blockPosIn, null, -1, true, worldRendererIn, var13, renderEnv);
            var7 = true;
        }
        if (renderEnv != null && Config.isBetterSnow() && !renderEnv.isBreakingAnimation() && BetterSnow.shouldRender(blockAccessIn, blockIn, blockStateIn, blockPosIn) && BetterSnow.shouldRender(blockAccessIn, blockIn, blockStateIn, blockPosIn)) {
            final IBakedModel var14 = BetterSnow.getModelSnowLayer();
            final IBlockState var15 = BetterSnow.getStateSnowLayer();
            this.renderModelStandard(blockAccessIn, var14, var15.getBlock(), var15, blockPosIn, worldRendererIn, true);
        }
        return var7;
    }
    
    private void renderModelAmbientOcclusionQuads(final IBlockAccess blockAccessIn, final Block blockIn, final BlockPos blockPosIn, final WorldRenderer worldRendererIn, final List listQuadsIn, final RenderEnv renderEnv) {
        final float[] quadBounds = renderEnv.getQuadBounds();
        final BitSet boundsFlags = renderEnv.getBoundsFlags();
        final AmbientOcclusionFace aoFaceIn = renderEnv.getAoFace();
        final IBlockState blockStateIn = renderEnv.getBlockState();
        double var9 = blockPosIn.getX();
        double var10 = blockPosIn.getY();
        double var11 = blockPosIn.getZ();
        final Block.EnumOffsetType var12 = blockIn.getOffsetType();
        if (var12 != Block.EnumOffsetType.NONE) {
            final long var13 = MathHelper.func_180186_a(blockPosIn);
            var9 += ((var13 >> 16 & 0xFL) / 15.0f - 0.5) * 0.5;
            var11 += ((var13 >> 24 & 0xFL) / 15.0f - 0.5) * 0.5;
            if (var12 == Block.EnumOffsetType.XYZ) {
                var10 += ((var13 >> 20 & 0xFL) / 15.0f - 1.0) * 0.2;
            }
        }
        for (BakedQuad var15 : listQuadsIn) {
            if (var15.getSprite() != null && Config.isConnectedTextures()) {
                var15 = ConnectedTextures.getConnectedTexture(blockAccessIn, blockStateIn, blockPosIn, var15, renderEnv);
            }
            this.func_178261_a(blockIn, var15.func_178209_a(), var15.getFace(), quadBounds, boundsFlags);
            aoFaceIn.func_178204_a(blockAccessIn, blockIn, blockPosIn, var15.getFace(), quadBounds, boundsFlags);
            worldRendererIn.func_178981_a(var15.func_178209_a());
            worldRendererIn.func_178962_a(aoFaceIn.field_178207_c[0], aoFaceIn.field_178207_c[1], aoFaceIn.field_178207_c[2], aoFaceIn.field_178207_c[3]);
            final int colorMultiplier = CustomColorizer.getColorMultiplier(var15, blockIn, blockAccessIn, blockPosIn, renderEnv);
            if (!var15.func_178212_b() && colorMultiplier < 0) {
                worldRendererIn.func_178978_a(aoFaceIn.field_178206_b[0], aoFaceIn.field_178206_b[0], aoFaceIn.field_178206_b[0], 4);
                worldRendererIn.func_178978_a(aoFaceIn.field_178206_b[1], aoFaceIn.field_178206_b[1], aoFaceIn.field_178206_b[1], 3);
                worldRendererIn.func_178978_a(aoFaceIn.field_178206_b[2], aoFaceIn.field_178206_b[2], aoFaceIn.field_178206_b[2], 2);
                worldRendererIn.func_178978_a(aoFaceIn.field_178206_b[3], aoFaceIn.field_178206_b[3], aoFaceIn.field_178206_b[3], 1);
            }
            else {
                int var16;
                if (colorMultiplier >= 0) {
                    var16 = colorMultiplier;
                }
                else {
                    var16 = blockIn.colorMultiplier(blockAccessIn, blockPosIn, var15.func_178211_c());
                }
                if (EntityRenderer.anaglyphEnable) {
                    var16 = TextureUtil.func_177054_c(var16);
                }
                final float var17 = (var16 >> 16 & 0xFF) / 255.0f;
                final float var18 = (var16 >> 8 & 0xFF) / 255.0f;
                final float var19 = (var16 & 0xFF) / 255.0f;
                worldRendererIn.func_178978_a(aoFaceIn.field_178206_b[0] * var17, aoFaceIn.field_178206_b[0] * var18, aoFaceIn.field_178206_b[0] * var19, 4);
                worldRendererIn.func_178978_a(aoFaceIn.field_178206_b[1] * var17, aoFaceIn.field_178206_b[1] * var18, aoFaceIn.field_178206_b[1] * var19, 3);
                worldRendererIn.func_178978_a(aoFaceIn.field_178206_b[2] * var17, aoFaceIn.field_178206_b[2] * var18, aoFaceIn.field_178206_b[2] * var19, 2);
                worldRendererIn.func_178978_a(aoFaceIn.field_178206_b[3] * var17, aoFaceIn.field_178206_b[3] * var18, aoFaceIn.field_178206_b[3] * var19, 1);
            }
            worldRendererIn.func_178987_a(var9, var10, var11);
        }
    }
    
    private void func_178261_a(final Block blockIn, final int[] vertexData, final EnumFacing facingIn, final float[] quadBounds, final BitSet boundsFlags) {
        float var6 = 32.0f;
        float var7 = 32.0f;
        float var8 = 32.0f;
        float var9 = -32.0f;
        float var10 = -32.0f;
        float var11 = -32.0f;
        for (int var12 = 0; var12 < 4; ++var12) {
            final float var13 = Float.intBitsToFloat(vertexData[var12 * 7]);
            final float var14 = Float.intBitsToFloat(vertexData[var12 * 7 + 1]);
            final float var15 = Float.intBitsToFloat(vertexData[var12 * 7 + 2]);
            var6 = Math.min(var6, var13);
            var7 = Math.min(var7, var14);
            var8 = Math.min(var8, var15);
            var9 = Math.max(var9, var13);
            var10 = Math.max(var10, var14);
            var11 = Math.max(var11, var15);
        }
        if (quadBounds != null) {
            quadBounds[EnumFacing.WEST.getIndex()] = var6;
            quadBounds[EnumFacing.EAST.getIndex()] = var9;
            quadBounds[EnumFacing.DOWN.getIndex()] = var7;
            quadBounds[EnumFacing.UP.getIndex()] = var10;
            quadBounds[EnumFacing.NORTH.getIndex()] = var8;
            quadBounds[EnumFacing.SOUTH.getIndex()] = var11;
            quadBounds[EnumFacing.WEST.getIndex() + EnumFacing.VALUES.length] = 1.0f - var6;
            quadBounds[EnumFacing.EAST.getIndex() + EnumFacing.VALUES.length] = 1.0f - var9;
            quadBounds[EnumFacing.DOWN.getIndex() + EnumFacing.VALUES.length] = 1.0f - var7;
            quadBounds[EnumFacing.UP.getIndex() + EnumFacing.VALUES.length] = 1.0f - var10;
            quadBounds[EnumFacing.NORTH.getIndex() + EnumFacing.VALUES.length] = 1.0f - var8;
            quadBounds[EnumFacing.SOUTH.getIndex() + EnumFacing.VALUES.length] = 1.0f - var11;
        }
        final float var16 = 1.0E-4f;
        final float var13 = 0.9999f;
        switch (SwitchEnumFacing.field_178290_a[facingIn.ordinal()]) {
            case 1: {
                boundsFlags.set(1, var6 >= 1.0E-4f || var8 >= 1.0E-4f || var9 <= 0.9999f || var11 <= 0.9999f);
                boundsFlags.set(0, (var7 < 1.0E-4f || blockIn.isFullCube()) && var7 == var10);
                break;
            }
            case 2: {
                boundsFlags.set(1, var6 >= 1.0E-4f || var8 >= 1.0E-4f || var9 <= 0.9999f || var11 <= 0.9999f);
                boundsFlags.set(0, (var10 > 0.9999f || blockIn.isFullCube()) && var7 == var10);
                break;
            }
            case 3: {
                boundsFlags.set(1, var6 >= 1.0E-4f || var7 >= 1.0E-4f || var9 <= 0.9999f || var10 <= 0.9999f);
                boundsFlags.set(0, (var8 < 1.0E-4f || blockIn.isFullCube()) && var8 == var11);
                break;
            }
            case 4: {
                boundsFlags.set(1, var6 >= 1.0E-4f || var7 >= 1.0E-4f || var9 <= 0.9999f || var10 <= 0.9999f);
                boundsFlags.set(0, (var11 > 0.9999f || blockIn.isFullCube()) && var8 == var11);
                break;
            }
            case 5: {
                boundsFlags.set(1, var7 >= 1.0E-4f || var8 >= 1.0E-4f || var10 <= 0.9999f || var11 <= 0.9999f);
                boundsFlags.set(0, (var6 < 1.0E-4f || blockIn.isFullCube()) && var6 == var9);
                break;
            }
            case 6: {
                boundsFlags.set(1, var7 >= 1.0E-4f || var8 >= 1.0E-4f || var10 <= 0.9999f || var11 <= 0.9999f);
                boundsFlags.set(0, (var9 > 0.9999f || blockIn.isFullCube()) && var6 == var9);
                break;
            }
        }
    }
    
    private void renderModelStandardQuads(final IBlockAccess blockAccessIn, final Block blockIn, final BlockPos blockPosIn, final EnumFacing faceIn, int brightnessIn, final boolean ownBrightness, final WorldRenderer worldRendererIn, final List listQuadsIn, final RenderEnv renderEnv) {
        final BitSet boundsFlags = renderEnv.getBoundsFlags();
        final IBlockState blockStateIn = renderEnv.getBlockState();
        double var10 = blockPosIn.getX();
        double var11 = blockPosIn.getY();
        double var12 = blockPosIn.getZ();
        final Block.EnumOffsetType var13 = blockIn.getOffsetType();
        if (var13 != Block.EnumOffsetType.NONE) {
            final int var14 = blockPosIn.getX();
            final int var15 = blockPosIn.getZ();
            long colorMultiplier = var14 * 3129871 ^ var15 * 116129781L;
            colorMultiplier = colorMultiplier * colorMultiplier * 42317861L + colorMultiplier * 11L;
            var10 += ((colorMultiplier >> 16 & 0xFL) / 15.0f - 0.5) * 0.5;
            var12 += ((colorMultiplier >> 24 & 0xFL) / 15.0f - 0.5) * 0.5;
            if (var13 == Block.EnumOffsetType.XYZ) {
                var11 += ((colorMultiplier >> 20 & 0xFL) / 15.0f - 1.0) * 0.2;
            }
        }
        for (BakedQuad var17 : listQuadsIn) {
            if (var17.getSprite() != null && Config.isConnectedTextures()) {
                var17 = ConnectedTextures.getConnectedTexture(blockAccessIn, blockStateIn, blockPosIn, var17, renderEnv);
            }
            if (ownBrightness) {
                this.func_178261_a(blockIn, var17.func_178209_a(), var17.getFace(), null, boundsFlags);
                brightnessIn = (boundsFlags.get(0) ? blockIn.getMixedBrightnessForBlock(blockAccessIn, blockPosIn.offset(var17.getFace())) : blockIn.getMixedBrightnessForBlock(blockAccessIn, blockPosIn));
            }
            worldRendererIn.func_178981_a(var17.func_178209_a());
            worldRendererIn.func_178962_a(brightnessIn, brightnessIn, brightnessIn, brightnessIn);
            final int colorMultiplier2 = CustomColorizer.getColorMultiplier(var17, blockIn, blockAccessIn, blockPosIn, renderEnv);
            if (var17.func_178212_b() || colorMultiplier2 >= 0) {
                int var18;
                if (colorMultiplier2 >= 0) {
                    var18 = colorMultiplier2;
                }
                else {
                    var18 = blockIn.colorMultiplier(blockAccessIn, blockPosIn, var17.func_178211_c());
                }
                if (EntityRenderer.anaglyphEnable) {
                    var18 = TextureUtil.func_177054_c(var18);
                }
                final float var19 = (var18 >> 16 & 0xFF) / 255.0f;
                final float var20 = (var18 >> 8 & 0xFF) / 255.0f;
                final float var21 = (var18 & 0xFF) / 255.0f;
                worldRendererIn.func_178978_a(var19, var20, var21, 4);
                worldRendererIn.func_178978_a(var19, var20, var21, 3);
                worldRendererIn.func_178978_a(var19, var20, var21, 2);
                worldRendererIn.func_178978_a(var19, var20, var21, 1);
            }
            worldRendererIn.func_178987_a(var10, var11, var12);
        }
    }
    
    public void func_178262_a(final IBakedModel p_178262_1_, final float p_178262_2_, final float p_178262_3_, final float p_178262_4_, final float p_178262_5_) {
        for (final EnumFacing var9 : EnumFacing.VALUES) {
            this.func_178264_a(p_178262_2_, p_178262_3_, p_178262_4_, p_178262_5_, p_178262_1_.func_177551_a(var9));
        }
        this.func_178264_a(p_178262_2_, p_178262_3_, p_178262_4_, p_178262_5_, p_178262_1_.func_177550_a());
    }
    
    public void func_178266_a(final IBakedModel p_178266_1_, final IBlockState p_178266_2_, final float p_178266_3_, final boolean p_178266_4_) {
        final Block var5 = p_178266_2_.getBlock();
        var5.setBlockBoundsForItemRender();
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        int var6 = var5.getRenderColor(var5.getStateForEntityRender(p_178266_2_));
        if (EntityRenderer.anaglyphEnable) {
            var6 = TextureUtil.func_177054_c(var6);
        }
        final float var7 = (var6 >> 16 & 0xFF) / 255.0f;
        final float var8 = (var6 >> 8 & 0xFF) / 255.0f;
        final float var9 = (var6 & 0xFF) / 255.0f;
        if (!p_178266_4_) {
            GlStateManager.color(p_178266_3_, p_178266_3_, p_178266_3_, 1.0f);
        }
        this.func_178262_a(p_178266_1_, p_178266_3_, var7, var8, var9);
    }
    
    private void func_178264_a(final float p_178264_1_, final float p_178264_2_, final float p_178264_3_, final float p_178264_4_, final List p_178264_5_) {
        final Tessellator var6 = Tessellator.getInstance();
        final WorldRenderer var7 = var6.getWorldRenderer();
        for (final BakedQuad var9 : p_178264_5_) {
            var7.startDrawingQuads();
            var7.setVertexFormat(DefaultVertexFormats.field_176599_b);
            var7.func_178981_a(var9.func_178209_a());
            if (var9.func_178212_b()) {
                var7.func_178990_f(p_178264_2_ * p_178264_1_, p_178264_3_ * p_178264_1_, p_178264_4_ * p_178264_1_);
            }
            else {
                var7.func_178990_f(p_178264_1_, p_178264_1_, p_178264_1_);
            }
            final Vec3i var10 = var9.getFace().getDirectionVec();
            var7.func_178975_e(var10.getX(), var10.getY(), var10.getZ());
            var6.draw();
        }
    }
    
    public static float fixAoLightValue(final float val) {
        return (val == 0.2f) ? BlockModelRenderer.aoLightValueOpaque : val;
    }
    
    static {
        BlockModelRenderer.aoLightValueOpaque = 0.2f;
    }
    
    public static class AmbientOcclusionFace
    {
        private final float[] field_178206_b;
        private final int[] field_178207_c;
        private static final String __OBFID = "CL_00002515";
        
        public AmbientOcclusionFace(final BlockModelRenderer bmr) {
            this.field_178206_b = new float[4];
            this.field_178207_c = new int[4];
        }
        
        public AmbientOcclusionFace() {
            this.field_178206_b = new float[4];
            this.field_178207_c = new int[4];
        }
        
        public void func_178204_a(final IBlockAccess blockAccessIn, final Block blockIn, final BlockPos blockPosIn, final EnumFacing facingIn, final float[] quadBounds, final BitSet boundsFlags) {
            final BlockPos var7 = boundsFlags.get(0) ? blockPosIn.offset(facingIn) : blockPosIn;
            final EnumNeighborInfo var8 = EnumNeighborInfo.func_178273_a(facingIn);
            final BlockPos var9 = var7.offset(var8.field_178276_g[0]);
            final BlockPos var10 = var7.offset(var8.field_178276_g[1]);
            final BlockPos var11 = var7.offset(var8.field_178276_g[2]);
            final BlockPos var12 = var7.offset(var8.field_178276_g[3]);
            final int var13 = blockIn.getMixedBrightnessForBlock(blockAccessIn, var9);
            final int var14 = blockIn.getMixedBrightnessForBlock(blockAccessIn, var10);
            final int var15 = blockIn.getMixedBrightnessForBlock(blockAccessIn, var11);
            final int var16 = blockIn.getMixedBrightnessForBlock(blockAccessIn, var12);
            final float var17 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(var9).getBlock().getAmbientOcclusionLightValue());
            final float var18 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(var10).getBlock().getAmbientOcclusionLightValue());
            final float var19 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(var11).getBlock().getAmbientOcclusionLightValue());
            final float var20 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(var12).getBlock().getAmbientOcclusionLightValue());
            final boolean var21 = blockAccessIn.getBlockState(var9.offset(facingIn)).getBlock().isTranslucent();
            final boolean var22 = blockAccessIn.getBlockState(var10.offset(facingIn)).getBlock().isTranslucent();
            final boolean var23 = blockAccessIn.getBlockState(var11.offset(facingIn)).getBlock().isTranslucent();
            final boolean var24 = blockAccessIn.getBlockState(var12.offset(facingIn)).getBlock().isTranslucent();
            float var25;
            int var26;
            if (!var23 && !var21) {
                var25 = var17;
                var26 = var13;
            }
            else {
                final BlockPos var27 = var9.offset(var8.field_178276_g[2]);
                var25 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(var27).getBlock().getAmbientOcclusionLightValue());
                var26 = blockIn.getMixedBrightnessForBlock(blockAccessIn, var27);
            }
            float var28;
            int var29;
            if (!var24 && !var21) {
                var28 = var17;
                var29 = var13;
            }
            else {
                final BlockPos var27 = var9.offset(var8.field_178276_g[3]);
                var28 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(var27).getBlock().getAmbientOcclusionLightValue());
                var29 = blockIn.getMixedBrightnessForBlock(blockAccessIn, var27);
            }
            float var30;
            int var31;
            if (!var23 && !var22) {
                var30 = var18;
                var31 = var14;
            }
            else {
                final BlockPos var27 = var10.offset(var8.field_178276_g[2]);
                var30 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(var27).getBlock().getAmbientOcclusionLightValue());
                var31 = blockIn.getMixedBrightnessForBlock(blockAccessIn, var27);
            }
            float var32;
            int var33;
            if (!var24 && !var22) {
                var32 = var18;
                var33 = var14;
            }
            else {
                final BlockPos var27 = var10.offset(var8.field_178276_g[3]);
                var32 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(var27).getBlock().getAmbientOcclusionLightValue());
                var33 = blockIn.getMixedBrightnessForBlock(blockAccessIn, var27);
            }
            int var34 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockPosIn);
            if (boundsFlags.get(0) || !blockAccessIn.getBlockState(blockPosIn.offset(facingIn)).getBlock().isOpaqueCube()) {
                var34 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockPosIn.offset(facingIn));
            }
            float var35 = boundsFlags.get(0) ? blockAccessIn.getBlockState(var7).getBlock().getAmbientOcclusionLightValue() : blockAccessIn.getBlockState(blockPosIn).getBlock().getAmbientOcclusionLightValue();
            var35 = BlockModelRenderer.fixAoLightValue(var35);
            final VertexTranslations var36 = VertexTranslations.func_178184_a(facingIn);
            if (boundsFlags.get(1) && var8.field_178289_i) {
                final float var37 = (var20 + var17 + var28 + var35) * 0.25f;
                final float var38 = (var19 + var17 + var25 + var35) * 0.25f;
                final float var39 = (var19 + var18 + var30 + var35) * 0.25f;
                final float var40 = (var20 + var18 + var32 + var35) * 0.25f;
                final float var41 = quadBounds[var8.field_178286_j[0].field_178229_m] * quadBounds[var8.field_178286_j[1].field_178229_m];
                final float var42 = quadBounds[var8.field_178286_j[2].field_178229_m] * quadBounds[var8.field_178286_j[3].field_178229_m];
                final float var43 = quadBounds[var8.field_178286_j[4].field_178229_m] * quadBounds[var8.field_178286_j[5].field_178229_m];
                final float var44 = quadBounds[var8.field_178286_j[6].field_178229_m] * quadBounds[var8.field_178286_j[7].field_178229_m];
                final float var45 = quadBounds[var8.field_178287_k[0].field_178229_m] * quadBounds[var8.field_178287_k[1].field_178229_m];
                final float var46 = quadBounds[var8.field_178287_k[2].field_178229_m] * quadBounds[var8.field_178287_k[3].field_178229_m];
                final float var47 = quadBounds[var8.field_178287_k[4].field_178229_m] * quadBounds[var8.field_178287_k[5].field_178229_m];
                final float var48 = quadBounds[var8.field_178287_k[6].field_178229_m] * quadBounds[var8.field_178287_k[7].field_178229_m];
                final float var49 = quadBounds[var8.field_178284_l[0].field_178229_m] * quadBounds[var8.field_178284_l[1].field_178229_m];
                final float var50 = quadBounds[var8.field_178284_l[2].field_178229_m] * quadBounds[var8.field_178284_l[3].field_178229_m];
                final float var51 = quadBounds[var8.field_178284_l[4].field_178229_m] * quadBounds[var8.field_178284_l[5].field_178229_m];
                final float var52 = quadBounds[var8.field_178284_l[6].field_178229_m] * quadBounds[var8.field_178284_l[7].field_178229_m];
                final float var53 = quadBounds[var8.field_178285_m[0].field_178229_m] * quadBounds[var8.field_178285_m[1].field_178229_m];
                final float var54 = quadBounds[var8.field_178285_m[2].field_178229_m] * quadBounds[var8.field_178285_m[3].field_178229_m];
                final float var55 = quadBounds[var8.field_178285_m[4].field_178229_m] * quadBounds[var8.field_178285_m[5].field_178229_m];
                final float var56 = quadBounds[var8.field_178285_m[6].field_178229_m] * quadBounds[var8.field_178285_m[7].field_178229_m];
                this.field_178206_b[var36.field_178191_g] = var37 * var41 + var38 * var42 + var39 * var43 + var40 * var44;
                this.field_178206_b[var36.field_178200_h] = var37 * var45 + var38 * var46 + var39 * var47 + var40 * var48;
                this.field_178206_b[var36.field_178201_i] = var37 * var49 + var38 * var50 + var39 * var51 + var40 * var52;
                this.field_178206_b[var36.field_178198_j] = var37 * var53 + var38 * var54 + var39 * var55 + var40 * var56;
                final int var57 = this.getAoBrightness(var16, var13, var29, var34);
                final int var58 = this.getAoBrightness(var15, var13, var26, var34);
                final int var59 = this.getAoBrightness(var15, var14, var31, var34);
                final int var60 = this.getAoBrightness(var16, var14, var33, var34);
                this.field_178207_c[var36.field_178191_g] = this.func_178203_a(var57, var58, var59, var60, var41, var42, var43, var44);
                this.field_178207_c[var36.field_178200_h] = this.func_178203_a(var57, var58, var59, var60, var45, var46, var47, var48);
                this.field_178207_c[var36.field_178201_i] = this.func_178203_a(var57, var58, var59, var60, var49, var50, var51, var52);
                this.field_178207_c[var36.field_178198_j] = this.func_178203_a(var57, var58, var59, var60, var53, var54, var55, var56);
            }
            else {
                final float var37 = (var20 + var17 + var28 + var35) * 0.25f;
                final float var38 = (var19 + var17 + var25 + var35) * 0.25f;
                final float var39 = (var19 + var18 + var30 + var35) * 0.25f;
                final float var40 = (var20 + var18 + var32 + var35) * 0.25f;
                this.field_178207_c[var36.field_178191_g] = this.getAoBrightness(var16, var13, var29, var34);
                this.field_178207_c[var36.field_178200_h] = this.getAoBrightness(var15, var13, var26, var34);
                this.field_178207_c[var36.field_178201_i] = this.getAoBrightness(var15, var14, var31, var34);
                this.field_178207_c[var36.field_178198_j] = this.getAoBrightness(var16, var14, var33, var34);
                this.field_178206_b[var36.field_178191_g] = var37;
                this.field_178206_b[var36.field_178200_h] = var38;
                this.field_178206_b[var36.field_178201_i] = var39;
                this.field_178206_b[var36.field_178198_j] = var40;
            }
        }
        
        private int getAoBrightness(int p_147778_1_, int p_147778_2_, int p_147778_3_, final int p_147778_4_) {
            if (p_147778_1_ == 0) {
                p_147778_1_ = p_147778_4_;
            }
            if (p_147778_2_ == 0) {
                p_147778_2_ = p_147778_4_;
            }
            if (p_147778_3_ == 0) {
                p_147778_3_ = p_147778_4_;
            }
            return p_147778_1_ + p_147778_2_ + p_147778_3_ + p_147778_4_ >> 2 & 0xFF00FF;
        }
        
        private int func_178203_a(final int p_178203_1_, final int p_178203_2_, final int p_178203_3_, final int p_178203_4_, final float p_178203_5_, final float p_178203_6_, final float p_178203_7_, final float p_178203_8_) {
            final int var9 = (int)((p_178203_1_ >> 16 & 0xFF) * p_178203_5_ + (p_178203_2_ >> 16 & 0xFF) * p_178203_6_ + (p_178203_3_ >> 16 & 0xFF) * p_178203_7_ + (p_178203_4_ >> 16 & 0xFF) * p_178203_8_) & 0xFF;
            final int var10 = (int)((p_178203_1_ & 0xFF) * p_178203_5_ + (p_178203_2_ & 0xFF) * p_178203_6_ + (p_178203_3_ & 0xFF) * p_178203_7_ + (p_178203_4_ & 0xFF) * p_178203_8_) & 0xFF;
            return var9 << 16 | var10;
        }
    }
    
    public enum EnumNeighborInfo
    {
        DOWN("DOWN", 0, "DOWN", 0, new EnumFacing[] { EnumFacing.WEST, EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.SOUTH }, 0.5f, false, new Orientation[0], new Orientation[0], new Orientation[0], new Orientation[0]), 
        UP("UP", 1, "UP", 1, new EnumFacing[] { EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH }, 1.0f, false, new Orientation[0], new Orientation[0], new Orientation[0], new Orientation[0]), 
        NORTH("NORTH", 2, "NORTH", 2, new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN, EnumFacing.EAST, EnumFacing.WEST }, 0.8f, true, new Orientation[] { Orientation.UP, Orientation.FLIP_WEST, Orientation.UP, Orientation.WEST, Orientation.FLIP_UP, Orientation.WEST, Orientation.FLIP_UP, Orientation.FLIP_WEST }, new Orientation[] { Orientation.UP, Orientation.FLIP_EAST, Orientation.UP, Orientation.EAST, Orientation.FLIP_UP, Orientation.EAST, Orientation.FLIP_UP, Orientation.FLIP_EAST }, new Orientation[] { Orientation.DOWN, Orientation.FLIP_EAST, Orientation.DOWN, Orientation.EAST, Orientation.FLIP_DOWN, Orientation.EAST, Orientation.FLIP_DOWN, Orientation.FLIP_EAST }, new Orientation[] { Orientation.DOWN, Orientation.FLIP_WEST, Orientation.DOWN, Orientation.WEST, Orientation.FLIP_DOWN, Orientation.WEST, Orientation.FLIP_DOWN, Orientation.FLIP_WEST }), 
        SOUTH("SOUTH", 3, "SOUTH", 3, new EnumFacing[] { EnumFacing.WEST, EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.UP }, 0.8f, true, new Orientation[] { Orientation.UP, Orientation.FLIP_WEST, Orientation.FLIP_UP, Orientation.FLIP_WEST, Orientation.FLIP_UP, Orientation.WEST, Orientation.UP, Orientation.WEST }, new Orientation[] { Orientation.DOWN, Orientation.FLIP_WEST, Orientation.FLIP_DOWN, Orientation.FLIP_WEST, Orientation.FLIP_DOWN, Orientation.WEST, Orientation.DOWN, Orientation.WEST }, new Orientation[] { Orientation.DOWN, Orientation.FLIP_EAST, Orientation.FLIP_DOWN, Orientation.FLIP_EAST, Orientation.FLIP_DOWN, Orientation.EAST, Orientation.DOWN, Orientation.EAST }, new Orientation[] { Orientation.UP, Orientation.FLIP_EAST, Orientation.FLIP_UP, Orientation.FLIP_EAST, Orientation.FLIP_UP, Orientation.EAST, Orientation.UP, Orientation.EAST }), 
        WEST("WEST", 4, "WEST", 4, new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.SOUTH }, 0.6f, true, new Orientation[] { Orientation.UP, Orientation.SOUTH, Orientation.UP, Orientation.FLIP_SOUTH, Orientation.FLIP_UP, Orientation.FLIP_SOUTH, Orientation.FLIP_UP, Orientation.SOUTH }, new Orientation[] { Orientation.UP, Orientation.NORTH, Orientation.UP, Orientation.FLIP_NORTH, Orientation.FLIP_UP, Orientation.FLIP_NORTH, Orientation.FLIP_UP, Orientation.NORTH }, new Orientation[] { Orientation.DOWN, Orientation.NORTH, Orientation.DOWN, Orientation.FLIP_NORTH, Orientation.FLIP_DOWN, Orientation.FLIP_NORTH, Orientation.FLIP_DOWN, Orientation.NORTH }, new Orientation[] { Orientation.DOWN, Orientation.SOUTH, Orientation.DOWN, Orientation.FLIP_SOUTH, Orientation.FLIP_DOWN, Orientation.FLIP_SOUTH, Orientation.FLIP_DOWN, Orientation.SOUTH }), 
        EAST("EAST", 5, "EAST", 5, new EnumFacing[] { EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH }, 0.6f, true, new Orientation[] { Orientation.FLIP_DOWN, Orientation.SOUTH, Orientation.FLIP_DOWN, Orientation.FLIP_SOUTH, Orientation.DOWN, Orientation.FLIP_SOUTH, Orientation.DOWN, Orientation.SOUTH }, new Orientation[] { Orientation.FLIP_DOWN, Orientation.NORTH, Orientation.FLIP_DOWN, Orientation.FLIP_NORTH, Orientation.DOWN, Orientation.FLIP_NORTH, Orientation.DOWN, Orientation.NORTH }, new Orientation[] { Orientation.FLIP_UP, Orientation.NORTH, Orientation.FLIP_UP, Orientation.FLIP_NORTH, Orientation.UP, Orientation.FLIP_NORTH, Orientation.UP, Orientation.NORTH }, new Orientation[] { Orientation.FLIP_UP, Orientation.SOUTH, Orientation.FLIP_UP, Orientation.FLIP_SOUTH, Orientation.UP, Orientation.FLIP_SOUTH, Orientation.UP, Orientation.SOUTH });
        
        protected final EnumFacing[] field_178276_g;
        protected final float field_178288_h;
        protected final boolean field_178289_i;
        protected final Orientation[] field_178286_j;
        protected final Orientation[] field_178287_k;
        protected final Orientation[] field_178284_l;
        protected final Orientation[] field_178285_m;
        private static final EnumNeighborInfo[] field_178282_n;
        private static final EnumNeighborInfo[] $VALUES;
        private static final String __OBFID = "CL_00002516";
        private static final EnumNeighborInfo[] $VALUES$;
        
        private EnumNeighborInfo(final String p_i46387_1_, final int p_i46387_2_, final String p_i46236_1_, final int p_i46236_2_, final EnumFacing[] p_i46236_3_, final float p_i46236_4_, final boolean p_i46236_5_, final Orientation[] p_i46236_6_, final Orientation[] p_i46236_7_, final Orientation[] p_i46236_8_, final Orientation[] p_i46236_9_) {
            this.field_178276_g = p_i46236_3_;
            this.field_178288_h = p_i46236_4_;
            this.field_178289_i = p_i46236_5_;
            this.field_178286_j = p_i46236_6_;
            this.field_178287_k = p_i46236_7_;
            this.field_178284_l = p_i46236_8_;
            this.field_178285_m = p_i46236_9_;
        }
        
        public static EnumNeighborInfo func_178273_a(final EnumFacing p_178273_0_) {
            return EnumNeighborInfo.field_178282_n[p_178273_0_.getIndex()];
        }
        
        static {
            field_178282_n = new EnumNeighborInfo[6];
            $VALUES = new EnumNeighborInfo[] { EnumNeighborInfo.DOWN, EnumNeighborInfo.UP, EnumNeighborInfo.NORTH, EnumNeighborInfo.SOUTH, EnumNeighborInfo.WEST, EnumNeighborInfo.EAST };
            $VALUES$ = new EnumNeighborInfo[] { EnumNeighborInfo.DOWN, EnumNeighborInfo.UP, EnumNeighborInfo.NORTH, EnumNeighborInfo.SOUTH, EnumNeighborInfo.WEST, EnumNeighborInfo.EAST };
            EnumNeighborInfo.field_178282_n[EnumFacing.DOWN.getIndex()] = EnumNeighborInfo.DOWN;
            EnumNeighborInfo.field_178282_n[EnumFacing.UP.getIndex()] = EnumNeighborInfo.UP;
            EnumNeighborInfo.field_178282_n[EnumFacing.NORTH.getIndex()] = EnumNeighborInfo.NORTH;
            EnumNeighborInfo.field_178282_n[EnumFacing.SOUTH.getIndex()] = EnumNeighborInfo.SOUTH;
            EnumNeighborInfo.field_178282_n[EnumFacing.WEST.getIndex()] = EnumNeighborInfo.WEST;
            EnumNeighborInfo.field_178282_n[EnumFacing.EAST.getIndex()] = EnumNeighborInfo.EAST;
        }
    }
    
    public enum Orientation
    {
        DOWN("DOWN", 0, "DOWN", 0, EnumFacing.DOWN, false), 
        UP("UP", 1, "UP", 1, EnumFacing.UP, false), 
        NORTH("NORTH", 2, "NORTH", 2, EnumFacing.NORTH, false), 
        SOUTH("SOUTH", 3, "SOUTH", 3, EnumFacing.SOUTH, false), 
        WEST("WEST", 4, "WEST", 4, EnumFacing.WEST, false), 
        EAST("EAST", 5, "EAST", 5, EnumFacing.EAST, false), 
        FLIP_DOWN("FLIP_DOWN", 6, "FLIP_DOWN", 6, EnumFacing.DOWN, true), 
        FLIP_UP("FLIP_UP", 7, "FLIP_UP", 7, EnumFacing.UP, true), 
        FLIP_NORTH("FLIP_NORTH", 8, "FLIP_NORTH", 8, EnumFacing.NORTH, true), 
        FLIP_SOUTH("FLIP_SOUTH", 9, "FLIP_SOUTH", 9, EnumFacing.SOUTH, true), 
        FLIP_WEST("FLIP_WEST", 10, "FLIP_WEST", 10, EnumFacing.WEST, true), 
        FLIP_EAST("FLIP_EAST", 11, "FLIP_EAST", 11, EnumFacing.EAST, true);
        
        protected final int field_178229_m;
        private static final Orientation[] $VALUES;
        private static final String __OBFID = "CL_00002513";
        private static final Orientation[] $VALUES$;
        
        private Orientation(final String p_i46389_1_, final int p_i46389_2_, final String p_i46233_1_, final int p_i46233_2_, final EnumFacing p_i46233_3_, final boolean p_i46233_4_) {
            this.field_178229_m = p_i46233_3_.getIndex() + (p_i46233_4_ ? EnumFacing.values().length : 0);
        }
        
        static {
            $VALUES = new Orientation[] { Orientation.DOWN, Orientation.UP, Orientation.NORTH, Orientation.SOUTH, Orientation.WEST, Orientation.EAST, Orientation.FLIP_DOWN, Orientation.FLIP_UP, Orientation.FLIP_NORTH, Orientation.FLIP_SOUTH, Orientation.FLIP_WEST, Orientation.FLIP_EAST };
            $VALUES$ = new Orientation[] { Orientation.DOWN, Orientation.UP, Orientation.NORTH, Orientation.SOUTH, Orientation.WEST, Orientation.EAST, Orientation.FLIP_DOWN, Orientation.FLIP_UP, Orientation.FLIP_NORTH, Orientation.FLIP_SOUTH, Orientation.FLIP_WEST, Orientation.FLIP_EAST };
        }
    }
    
    static final class SwitchEnumFacing
    {
        static final int[] field_178290_a;
        private static final String __OBFID = "CL_00002517";
        
        static {
            field_178290_a = new int[EnumFacing.values().length];
            try {
                SwitchEnumFacing.field_178290_a[EnumFacing.DOWN.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumFacing.field_178290_a[EnumFacing.UP.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumFacing.field_178290_a[EnumFacing.NORTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumFacing.field_178290_a[EnumFacing.SOUTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                SwitchEnumFacing.field_178290_a[EnumFacing.WEST.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
            try {
                SwitchEnumFacing.field_178290_a[EnumFacing.EAST.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError6) {}
        }
    }
    
    enum VertexTranslations
    {
        DOWN("DOWN", 0, "DOWN", 0, 0, 1, 2, 3), 
        UP("UP", 1, "UP", 1, 2, 3, 0, 1), 
        NORTH("NORTH", 2, "NORTH", 2, 3, 0, 1, 2), 
        SOUTH("SOUTH", 3, "SOUTH", 3, 0, 1, 2, 3), 
        WEST("WEST", 4, "WEST", 4, 3, 0, 1, 2), 
        EAST("EAST", 5, "EAST", 5, 1, 2, 3, 0);
        
        private final int field_178191_g;
        private final int field_178200_h;
        private final int field_178201_i;
        private final int field_178198_j;
        private static final VertexTranslations[] field_178199_k;
        private static final VertexTranslations[] $VALUES;
        private static final String __OBFID = "CL_00002514";
        private static final VertexTranslations[] $VALUES$;
        
        private VertexTranslations(final String p_i46388_1_, final int p_i46388_2_, final String p_i46234_1_, final int p_i46234_2_, final int p_i46234_3_, final int p_i46234_4_, final int p_i46234_5_, final int p_i46234_6_) {
            this.field_178191_g = p_i46234_3_;
            this.field_178200_h = p_i46234_4_;
            this.field_178201_i = p_i46234_5_;
            this.field_178198_j = p_i46234_6_;
        }
        
        public static VertexTranslations func_178184_a(final EnumFacing p_178184_0_) {
            return VertexTranslations.field_178199_k[p_178184_0_.getIndex()];
        }
        
        static {
            field_178199_k = new VertexTranslations[6];
            $VALUES = new VertexTranslations[] { VertexTranslations.DOWN, VertexTranslations.UP, VertexTranslations.NORTH, VertexTranslations.SOUTH, VertexTranslations.WEST, VertexTranslations.EAST };
            $VALUES$ = new VertexTranslations[] { VertexTranslations.DOWN, VertexTranslations.UP, VertexTranslations.NORTH, VertexTranslations.SOUTH, VertexTranslations.WEST, VertexTranslations.EAST };
            VertexTranslations.field_178199_k[EnumFacing.DOWN.getIndex()] = VertexTranslations.DOWN;
            VertexTranslations.field_178199_k[EnumFacing.UP.getIndex()] = VertexTranslations.UP;
            VertexTranslations.field_178199_k[EnumFacing.NORTH.getIndex()] = VertexTranslations.NORTH;
            VertexTranslations.field_178199_k[EnumFacing.SOUTH.getIndex()] = VertexTranslations.SOUTH;
            VertexTranslations.field_178199_k[EnumFacing.WEST.getIndex()] = VertexTranslations.WEST;
            VertexTranslations.field_178199_k[EnumFacing.EAST.getIndex()] = VertexTranslations.EAST;
        }
    }
}
