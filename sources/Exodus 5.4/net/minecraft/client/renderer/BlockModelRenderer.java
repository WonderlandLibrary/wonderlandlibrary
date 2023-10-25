/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer;

import java.util.BitSet;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
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

public class BlockModelRenderer {
    public boolean renderModel(IBlockAccess iBlockAccess, IBakedModel iBakedModel, IBlockState iBlockState, BlockPos blockPos, WorldRenderer worldRenderer) {
        Block block = iBlockState.getBlock();
        block.setBlockBoundsBasedOnState(iBlockAccess, blockPos);
        return this.renderModel(iBlockAccess, iBakedModel, iBlockState, blockPos, worldRenderer, true);
    }

    private void renderModelAmbientOcclusionQuads(IBlockAccess iBlockAccess, Block block, BlockPos blockPos, WorldRenderer worldRenderer, List<BakedQuad> list, float[] fArray, BitSet bitSet, AmbientOcclusionFace ambientOcclusionFace) {
        double d = blockPos.getX();
        double d2 = blockPos.getY();
        double d3 = blockPos.getZ();
        Block.EnumOffsetType enumOffsetType = block.getOffsetType();
        if (enumOffsetType != Block.EnumOffsetType.NONE) {
            long l = MathHelper.getPositionRandom(blockPos);
            d += ((double)((float)(l >> 16 & 0xFL) / 15.0f) - 0.5) * 0.5;
            d3 += ((double)((float)(l >> 24 & 0xFL) / 15.0f) - 0.5) * 0.5;
            if (enumOffsetType == Block.EnumOffsetType.XYZ) {
                d2 += ((double)((float)(l >> 20 & 0xFL) / 15.0f) - 1.0) * 0.2;
            }
        }
        for (BakedQuad bakedQuad : list) {
            this.fillQuadBounds(block, bakedQuad.getVertexData(), bakedQuad.getFace(), fArray, bitSet);
            ambientOcclusionFace.updateVertexBrightness(iBlockAccess, block, blockPos, bakedQuad.getFace(), fArray, bitSet);
            worldRenderer.addVertexData(bakedQuad.getVertexData());
            worldRenderer.putBrightness4(ambientOcclusionFace.vertexBrightness[0], ambientOcclusionFace.vertexBrightness[1], ambientOcclusionFace.vertexBrightness[2], ambientOcclusionFace.vertexBrightness[3]);
            if (bakedQuad.hasTintIndex()) {
                int n = block.colorMultiplier(iBlockAccess, blockPos, bakedQuad.getTintIndex());
                if (EntityRenderer.anaglyphEnable) {
                    n = TextureUtil.anaglyphColor(n);
                }
                float f = (float)(n >> 16 & 0xFF) / 255.0f;
                float f2 = (float)(n >> 8 & 0xFF) / 255.0f;
                float f3 = (float)(n & 0xFF) / 255.0f;
                worldRenderer.putColorMultiplier(ambientOcclusionFace.vertexColorMultiplier[0] * f, ambientOcclusionFace.vertexColorMultiplier[0] * f2, ambientOcclusionFace.vertexColorMultiplier[0] * f3, 4);
                worldRenderer.putColorMultiplier(ambientOcclusionFace.vertexColorMultiplier[1] * f, ambientOcclusionFace.vertexColorMultiplier[1] * f2, ambientOcclusionFace.vertexColorMultiplier[1] * f3, 3);
                worldRenderer.putColorMultiplier(ambientOcclusionFace.vertexColorMultiplier[2] * f, ambientOcclusionFace.vertexColorMultiplier[2] * f2, ambientOcclusionFace.vertexColorMultiplier[2] * f3, 2);
                worldRenderer.putColorMultiplier(ambientOcclusionFace.vertexColorMultiplier[3] * f, ambientOcclusionFace.vertexColorMultiplier[3] * f2, ambientOcclusionFace.vertexColorMultiplier[3] * f3, 1);
            } else {
                worldRenderer.putColorMultiplier(ambientOcclusionFace.vertexColorMultiplier[0], ambientOcclusionFace.vertexColorMultiplier[0], ambientOcclusionFace.vertexColorMultiplier[0], 4);
                worldRenderer.putColorMultiplier(ambientOcclusionFace.vertexColorMultiplier[1], ambientOcclusionFace.vertexColorMultiplier[1], ambientOcclusionFace.vertexColorMultiplier[1], 3);
                worldRenderer.putColorMultiplier(ambientOcclusionFace.vertexColorMultiplier[2], ambientOcclusionFace.vertexColorMultiplier[2], ambientOcclusionFace.vertexColorMultiplier[2], 2);
                worldRenderer.putColorMultiplier(ambientOcclusionFace.vertexColorMultiplier[3], ambientOcclusionFace.vertexColorMultiplier[3], ambientOcclusionFace.vertexColorMultiplier[3], 1);
            }
            worldRenderer.putPosition(d, d2, d3);
        }
    }

    public boolean renderModel(IBlockAccess iBlockAccess, IBakedModel iBakedModel, IBlockState iBlockState, BlockPos blockPos, WorldRenderer worldRenderer, boolean bl) {
        boolean bl2 = Minecraft.isAmbientOcclusionEnabled() && iBlockState.getBlock().getLightValue() == 0 && iBakedModel.isAmbientOcclusion();
        try {
            Block block = iBlockState.getBlock();
            return bl2 ? this.renderModelAmbientOcclusion(iBlockAccess, iBakedModel, block, blockPos, worldRenderer, bl) : this.renderModelStandard(iBlockAccess, iBakedModel, block, blockPos, worldRenderer, bl);
        }
        catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Tesselating block model");
            CrashReportCategory crashReportCategory = crashReport.makeCategory("Block model being tesselated");
            CrashReportCategory.addBlockInfo(crashReportCategory, blockPos, iBlockState);
            crashReportCategory.addCrashSection("Using AO", bl2);
            throw new ReportedException(crashReport);
        }
    }

    private void renderModelBrightnessColorQuads(float f, float f2, float f3, float f4, List<BakedQuad> list) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        for (BakedQuad bakedQuad : list) {
            worldRenderer.begin(7, DefaultVertexFormats.ITEM);
            worldRenderer.addVertexData(bakedQuad.getVertexData());
            if (bakedQuad.hasTintIndex()) {
                worldRenderer.putColorRGB_F4(f2 * f, f3 * f, f4 * f);
            } else {
                worldRenderer.putColorRGB_F4(f, f, f);
            }
            Vec3i vec3i = bakedQuad.getFace().getDirectionVec();
            worldRenderer.putNormal(vec3i.getX(), vec3i.getY(), vec3i.getZ());
            tessellator.draw();
        }
    }

    public void renderModelBrightness(IBakedModel iBakedModel, IBlockState iBlockState, float f, boolean bl) {
        Block block = iBlockState.getBlock();
        block.setBlockBoundsForItemRender();
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        int n = block.getRenderColor(block.getStateForEntityRender(iBlockState));
        if (EntityRenderer.anaglyphEnable) {
            n = TextureUtil.anaglyphColor(n);
        }
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        if (!bl) {
            GlStateManager.color(f, f, f, 1.0f);
        }
        this.renderModelBrightnessColor(iBakedModel, f, f2, f3, f4);
    }

    private void renderModelStandardQuads(IBlockAccess iBlockAccess, Block block, BlockPos blockPos, EnumFacing enumFacing, int n, boolean bl, WorldRenderer worldRenderer, List<BakedQuad> list, BitSet bitSet) {
        double d = blockPos.getX();
        double d2 = blockPos.getY();
        double d3 = blockPos.getZ();
        Block.EnumOffsetType enumOffsetType = block.getOffsetType();
        if (enumOffsetType != Block.EnumOffsetType.NONE) {
            int n2 = blockPos.getX();
            int n3 = blockPos.getZ();
            long l = (long)(n2 * 3129871) ^ (long)n3 * 116129781L;
            l = l * l * 42317861L + l * 11L;
            d += ((double)((float)(l >> 16 & 0xFL) / 15.0f) - 0.5) * 0.5;
            d3 += ((double)((float)(l >> 24 & 0xFL) / 15.0f) - 0.5) * 0.5;
            if (enumOffsetType == Block.EnumOffsetType.XYZ) {
                d2 += ((double)((float)(l >> 20 & 0xFL) / 15.0f) - 1.0) * 0.2;
            }
        }
        for (BakedQuad bakedQuad : list) {
            if (bl) {
                this.fillQuadBounds(block, bakedQuad.getVertexData(), bakedQuad.getFace(), null, bitSet);
                n = bitSet.get(0) ? block.getMixedBrightnessForBlock(iBlockAccess, blockPos.offset(bakedQuad.getFace())) : block.getMixedBrightnessForBlock(iBlockAccess, blockPos);
            }
            worldRenderer.addVertexData(bakedQuad.getVertexData());
            worldRenderer.putBrightness4(n, n, n, n);
            if (bakedQuad.hasTintIndex()) {
                int n4 = block.colorMultiplier(iBlockAccess, blockPos, bakedQuad.getTintIndex());
                if (EntityRenderer.anaglyphEnable) {
                    n4 = TextureUtil.anaglyphColor(n4);
                }
                float f = (float)(n4 >> 16 & 0xFF) / 255.0f;
                float f2 = (float)(n4 >> 8 & 0xFF) / 255.0f;
                float f3 = (float)(n4 & 0xFF) / 255.0f;
                worldRenderer.putColorMultiplier(f, f2, f3, 4);
                worldRenderer.putColorMultiplier(f, f2, f3, 3);
                worldRenderer.putColorMultiplier(f, f2, f3, 2);
                worldRenderer.putColorMultiplier(f, f2, f3, 1);
            }
            worldRenderer.putPosition(d, d2, d3);
        }
    }

    private void fillQuadBounds(Block block, int[] nArray, EnumFacing enumFacing, float[] fArray, BitSet bitSet) {
        float f;
        float f2 = 32.0f;
        float f3 = 32.0f;
        float f4 = 32.0f;
        float f5 = -32.0f;
        float f6 = -32.0f;
        float f7 = -32.0f;
        int n = 0;
        while (n < 4) {
            f = Float.intBitsToFloat(nArray[n * 7]);
            float f8 = Float.intBitsToFloat(nArray[n * 7 + 1]);
            float f9 = Float.intBitsToFloat(nArray[n * 7 + 2]);
            f2 = Math.min(f2, f);
            f3 = Math.min(f3, f8);
            f4 = Math.min(f4, f9);
            f5 = Math.max(f5, f);
            f6 = Math.max(f6, f8);
            f7 = Math.max(f7, f9);
            ++n;
        }
        if (fArray != null) {
            fArray[EnumFacing.WEST.getIndex()] = f2;
            fArray[EnumFacing.EAST.getIndex()] = f5;
            fArray[EnumFacing.DOWN.getIndex()] = f3;
            fArray[EnumFacing.UP.getIndex()] = f6;
            fArray[EnumFacing.NORTH.getIndex()] = f4;
            fArray[EnumFacing.SOUTH.getIndex()] = f7;
            fArray[EnumFacing.WEST.getIndex() + EnumFacing.values().length] = 1.0f - f2;
            fArray[EnumFacing.EAST.getIndex() + EnumFacing.values().length] = 1.0f - f5;
            fArray[EnumFacing.DOWN.getIndex() + EnumFacing.values().length] = 1.0f - f3;
            fArray[EnumFacing.UP.getIndex() + EnumFacing.values().length] = 1.0f - f6;
            fArray[EnumFacing.NORTH.getIndex() + EnumFacing.values().length] = 1.0f - f4;
            fArray[EnumFacing.SOUTH.getIndex() + EnumFacing.values().length] = 1.0f - f7;
        }
        float f10 = 1.0E-4f;
        f = 0.9999f;
        switch (enumFacing) {
            case DOWN: {
                bitSet.set(1, f2 >= 1.0E-4f || f4 >= 1.0E-4f || f5 <= 0.9999f || f7 <= 0.9999f);
                bitSet.set(0, (f3 < 1.0E-4f || block.isFullCube()) && f3 == f6);
                break;
            }
            case UP: {
                bitSet.set(1, f2 >= 1.0E-4f || f4 >= 1.0E-4f || f5 <= 0.9999f || f7 <= 0.9999f);
                bitSet.set(0, (f6 > 0.9999f || block.isFullCube()) && f3 == f6);
                break;
            }
            case NORTH: {
                bitSet.set(1, f2 >= 1.0E-4f || f3 >= 1.0E-4f || f5 <= 0.9999f || f6 <= 0.9999f);
                bitSet.set(0, (f4 < 1.0E-4f || block.isFullCube()) && f4 == f7);
                break;
            }
            case SOUTH: {
                bitSet.set(1, f2 >= 1.0E-4f || f3 >= 1.0E-4f || f5 <= 0.9999f || f6 <= 0.9999f);
                bitSet.set(0, (f7 > 0.9999f || block.isFullCube()) && f4 == f7);
                break;
            }
            case WEST: {
                bitSet.set(1, f3 >= 1.0E-4f || f4 >= 1.0E-4f || f6 <= 0.9999f || f7 <= 0.9999f);
                bitSet.set(0, (f2 < 1.0E-4f || block.isFullCube()) && f2 == f5);
                break;
            }
            case EAST: {
                bitSet.set(1, f3 >= 1.0E-4f || f4 >= 1.0E-4f || f6 <= 0.9999f || f7 <= 0.9999f);
                bitSet.set(0, (f5 > 0.9999f || block.isFullCube()) && f2 == f5);
            }
        }
    }

    public boolean renderModelStandard(IBlockAccess iBlockAccess, IBakedModel iBakedModel, Block block, BlockPos blockPos, WorldRenderer worldRenderer, boolean bl) {
        List<BakedQuad> list;
        boolean bl2 = false;
        BitSet bitSet = new BitSet(3);
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (n2 < n) {
            list = enumFacingArray[n2];
            List<BakedQuad> list2 = iBakedModel.getFaceQuads((EnumFacing)((Object)list));
            if (!list2.isEmpty()) {
                BlockPos blockPos2 = blockPos.offset((EnumFacing)((Object)list));
                if (!bl || block.shouldSideBeRendered(iBlockAccess, blockPos2, (EnumFacing)((Object)list))) {
                    int n3 = block.getMixedBrightnessForBlock(iBlockAccess, blockPos2);
                    this.renderModelStandardQuads(iBlockAccess, block, blockPos, (EnumFacing)((Object)list), n3, false, worldRenderer, list2, bitSet);
                    bl2 = true;
                }
            }
            ++n2;
        }
        list = iBakedModel.getGeneralQuads();
        if (list.size() > 0) {
            this.renderModelStandardQuads(iBlockAccess, block, blockPos, null, -1, true, worldRenderer, list, bitSet);
            bl2 = true;
        }
        return bl2;
    }

    public boolean renderModelAmbientOcclusion(IBlockAccess iBlockAccess, IBakedModel iBakedModel, Block block, BlockPos blockPos, WorldRenderer worldRenderer, boolean bl) {
        List<BakedQuad> list;
        boolean bl2 = false;
        float[] fArray = new float[EnumFacing.values().length * 2];
        BitSet bitSet = new BitSet(3);
        AmbientOcclusionFace ambientOcclusionFace = new AmbientOcclusionFace();
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (n2 < n) {
            list = enumFacingArray[n2];
            List<BakedQuad> list2 = iBakedModel.getFaceQuads((EnumFacing)((Object)list));
            if (!list2.isEmpty()) {
                BlockPos blockPos2 = blockPos.offset((EnumFacing)((Object)list));
                if (!bl || block.shouldSideBeRendered(iBlockAccess, blockPos2, (EnumFacing)((Object)list))) {
                    this.renderModelAmbientOcclusionQuads(iBlockAccess, block, blockPos, worldRenderer, list2, fArray, bitSet, ambientOcclusionFace);
                    bl2 = true;
                }
            }
            ++n2;
        }
        list = iBakedModel.getGeneralQuads();
        if (list.size() > 0) {
            this.renderModelAmbientOcclusionQuads(iBlockAccess, block, blockPos, worldRenderer, list, fArray, bitSet, ambientOcclusionFace);
            bl2 = true;
        }
        return bl2;
    }

    public void renderModelBrightnessColor(IBakedModel iBakedModel, float f, float f2, float f3, float f4) {
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing enumFacing = enumFacingArray[n2];
            this.renderModelBrightnessColorQuads(f, f2, f3, f4, iBakedModel.getFaceQuads(enumFacing));
            ++n2;
        }
        this.renderModelBrightnessColorQuads(f, f2, f3, f4, iBakedModel.getGeneralQuads());
    }

    static enum VertexTranslations {
        DOWN(0, 1, 2, 3),
        UP(2, 3, 0, 1),
        NORTH(3, 0, 1, 2),
        SOUTH(0, 1, 2, 3),
        WEST(3, 0, 1, 2),
        EAST(1, 2, 3, 0);

        private final int field_178200_h;
        private final int field_178201_i;
        private final int field_178198_j;
        private static final VertexTranslations[] field_178199_k;
        private final int field_178191_g;

        private VertexTranslations(int n2, int n3, int n4, int n5) {
            this.field_178191_g = n2;
            this.field_178200_h = n3;
            this.field_178201_i = n4;
            this.field_178198_j = n5;
        }

        public static VertexTranslations getVertexTranslations(EnumFacing enumFacing) {
            return field_178199_k[enumFacing.getIndex()];
        }

        static {
            field_178199_k = new VertexTranslations[6];
            VertexTranslations.field_178199_k[EnumFacing.DOWN.getIndex()] = DOWN;
            VertexTranslations.field_178199_k[EnumFacing.UP.getIndex()] = UP;
            VertexTranslations.field_178199_k[EnumFacing.NORTH.getIndex()] = NORTH;
            VertexTranslations.field_178199_k[EnumFacing.SOUTH.getIndex()] = SOUTH;
            VertexTranslations.field_178199_k[EnumFacing.WEST.getIndex()] = WEST;
            VertexTranslations.field_178199_k[EnumFacing.EAST.getIndex()] = EAST;
        }
    }

    class AmbientOcclusionFace {
        private final float[] vertexColorMultiplier = new float[4];
        private final int[] vertexBrightness = new int[4];

        private int getAoBrightness(int n, int n2, int n3, int n4) {
            if (n == 0) {
                n = n4;
            }
            if (n2 == 0) {
                n2 = n4;
            }
            if (n3 == 0) {
                n3 = n4;
            }
            return n + n2 + n3 + n4 >> 2 & 0xFF00FF;
        }

        public void updateVertexBrightness(IBlockAccess iBlockAccess, Block block, BlockPos blockPos, EnumFacing enumFacing, float[] fArray, BitSet bitSet) {
            int n;
            float f;
            int n2;
            float f2;
            int n3;
            float f3;
            int n4;
            float f4;
            BlockPos blockPos2 = bitSet.get(0) ? blockPos.offset(enumFacing) : blockPos;
            EnumNeighborInfo enumNeighborInfo = EnumNeighborInfo.getNeighbourInfo(enumFacing);
            BlockPos blockPos3 = blockPos2.offset(enumNeighborInfo.field_178276_g[0]);
            BlockPos blockPos4 = blockPos2.offset(enumNeighborInfo.field_178276_g[1]);
            BlockPos blockPos5 = blockPos2.offset(enumNeighborInfo.field_178276_g[2]);
            BlockPos blockPos6 = blockPos2.offset(enumNeighborInfo.field_178276_g[3]);
            int n5 = block.getMixedBrightnessForBlock(iBlockAccess, blockPos3);
            int n6 = block.getMixedBrightnessForBlock(iBlockAccess, blockPos4);
            int n7 = block.getMixedBrightnessForBlock(iBlockAccess, blockPos5);
            int n8 = block.getMixedBrightnessForBlock(iBlockAccess, blockPos6);
            float f5 = iBlockAccess.getBlockState(blockPos3).getBlock().getAmbientOcclusionLightValue();
            float f6 = iBlockAccess.getBlockState(blockPos4).getBlock().getAmbientOcclusionLightValue();
            float f7 = iBlockAccess.getBlockState(blockPos5).getBlock().getAmbientOcclusionLightValue();
            float f8 = iBlockAccess.getBlockState(blockPos6).getBlock().getAmbientOcclusionLightValue();
            boolean bl = iBlockAccess.getBlockState(blockPos3.offset(enumFacing)).getBlock().isTranslucent();
            boolean bl2 = iBlockAccess.getBlockState(blockPos4.offset(enumFacing)).getBlock().isTranslucent();
            boolean bl3 = iBlockAccess.getBlockState(blockPos5.offset(enumFacing)).getBlock().isTranslucent();
            boolean bl4 = iBlockAccess.getBlockState(blockPos6.offset(enumFacing)).getBlock().isTranslucent();
            if (!bl3 && !bl) {
                f4 = f5;
                n4 = n5;
            } else {
                BlockPos blockPos7 = blockPos3.offset(enumNeighborInfo.field_178276_g[2]);
                f4 = iBlockAccess.getBlockState(blockPos7).getBlock().getAmbientOcclusionLightValue();
                n4 = block.getMixedBrightnessForBlock(iBlockAccess, blockPos7);
            }
            if (!bl4 && !bl) {
                f3 = f5;
                n3 = n5;
            } else {
                BlockPos blockPos8 = blockPos3.offset(enumNeighborInfo.field_178276_g[3]);
                f3 = iBlockAccess.getBlockState(blockPos8).getBlock().getAmbientOcclusionLightValue();
                n3 = block.getMixedBrightnessForBlock(iBlockAccess, blockPos8);
            }
            if (!bl3 && !bl2) {
                f2 = f6;
                n2 = n6;
            } else {
                BlockPos blockPos9 = blockPos4.offset(enumNeighborInfo.field_178276_g[2]);
                f2 = iBlockAccess.getBlockState(blockPos9).getBlock().getAmbientOcclusionLightValue();
                n2 = block.getMixedBrightnessForBlock(iBlockAccess, blockPos9);
            }
            if (!bl4 && !bl2) {
                f = f6;
                n = n6;
            } else {
                BlockPos blockPos10 = blockPos4.offset(enumNeighborInfo.field_178276_g[3]);
                f = iBlockAccess.getBlockState(blockPos10).getBlock().getAmbientOcclusionLightValue();
                n = block.getMixedBrightnessForBlock(iBlockAccess, blockPos10);
            }
            int n9 = block.getMixedBrightnessForBlock(iBlockAccess, blockPos);
            if (bitSet.get(0) || !iBlockAccess.getBlockState(blockPos.offset(enumFacing)).getBlock().isOpaqueCube()) {
                n9 = block.getMixedBrightnessForBlock(iBlockAccess, blockPos.offset(enumFacing));
            }
            float f9 = bitSet.get(0) ? iBlockAccess.getBlockState(blockPos2).getBlock().getAmbientOcclusionLightValue() : iBlockAccess.getBlockState(blockPos).getBlock().getAmbientOcclusionLightValue();
            VertexTranslations vertexTranslations = VertexTranslations.getVertexTranslations(enumFacing);
            if (bitSet.get(1) && enumNeighborInfo.field_178289_i) {
                float f10 = (f8 + f5 + f3 + f9) * 0.25f;
                float f11 = (f7 + f5 + f4 + f9) * 0.25f;
                float f12 = (f7 + f6 + f2 + f9) * 0.25f;
                float f13 = (f8 + f6 + f + f9) * 0.25f;
                float f14 = fArray[enumNeighborInfo.field_178286_j[0].field_178229_m] * fArray[enumNeighborInfo.field_178286_j[1].field_178229_m];
                float f15 = fArray[enumNeighborInfo.field_178286_j[2].field_178229_m] * fArray[enumNeighborInfo.field_178286_j[3].field_178229_m];
                float f16 = fArray[enumNeighborInfo.field_178286_j[4].field_178229_m] * fArray[enumNeighborInfo.field_178286_j[5].field_178229_m];
                float f17 = fArray[enumNeighborInfo.field_178286_j[6].field_178229_m] * fArray[enumNeighborInfo.field_178286_j[7].field_178229_m];
                float f18 = fArray[enumNeighborInfo.field_178287_k[0].field_178229_m] * fArray[enumNeighborInfo.field_178287_k[1].field_178229_m];
                float f19 = fArray[enumNeighborInfo.field_178287_k[2].field_178229_m] * fArray[enumNeighborInfo.field_178287_k[3].field_178229_m];
                float f20 = fArray[enumNeighborInfo.field_178287_k[4].field_178229_m] * fArray[enumNeighborInfo.field_178287_k[5].field_178229_m];
                float f21 = fArray[enumNeighborInfo.field_178287_k[6].field_178229_m] * fArray[enumNeighborInfo.field_178287_k[7].field_178229_m];
                float f22 = fArray[enumNeighborInfo.field_178284_l[0].field_178229_m] * fArray[enumNeighborInfo.field_178284_l[1].field_178229_m];
                float f23 = fArray[enumNeighborInfo.field_178284_l[2].field_178229_m] * fArray[enumNeighborInfo.field_178284_l[3].field_178229_m];
                float f24 = fArray[enumNeighborInfo.field_178284_l[4].field_178229_m] * fArray[enumNeighborInfo.field_178284_l[5].field_178229_m];
                float f25 = fArray[enumNeighborInfo.field_178284_l[6].field_178229_m] * fArray[enumNeighborInfo.field_178284_l[7].field_178229_m];
                float f26 = fArray[enumNeighborInfo.field_178285_m[0].field_178229_m] * fArray[enumNeighborInfo.field_178285_m[1].field_178229_m];
                float f27 = fArray[enumNeighborInfo.field_178285_m[2].field_178229_m] * fArray[enumNeighborInfo.field_178285_m[3].field_178229_m];
                float f28 = fArray[enumNeighborInfo.field_178285_m[4].field_178229_m] * fArray[enumNeighborInfo.field_178285_m[5].field_178229_m];
                float f29 = fArray[enumNeighborInfo.field_178285_m[6].field_178229_m] * fArray[enumNeighborInfo.field_178285_m[7].field_178229_m];
                this.vertexColorMultiplier[((VertexTranslations)vertexTranslations).field_178191_g] = f10 * f14 + f11 * f15 + f12 * f16 + f13 * f17;
                this.vertexColorMultiplier[((VertexTranslations)vertexTranslations).field_178200_h] = f10 * f18 + f11 * f19 + f12 * f20 + f13 * f21;
                this.vertexColorMultiplier[((VertexTranslations)vertexTranslations).field_178201_i] = f10 * f22 + f11 * f23 + f12 * f24 + f13 * f25;
                this.vertexColorMultiplier[((VertexTranslations)vertexTranslations).field_178198_j] = f10 * f26 + f11 * f27 + f12 * f28 + f13 * f29;
                int n10 = this.getAoBrightness(n8, n5, n3, n9);
                int n11 = this.getAoBrightness(n7, n5, n4, n9);
                int n12 = this.getAoBrightness(n7, n6, n2, n9);
                int n13 = this.getAoBrightness(n8, n6, n, n9);
                this.vertexBrightness[((VertexTranslations)vertexTranslations).field_178191_g] = this.getVertexBrightness(n10, n11, n12, n13, f14, f15, f16, f17);
                this.vertexBrightness[((VertexTranslations)vertexTranslations).field_178200_h] = this.getVertexBrightness(n10, n11, n12, n13, f18, f19, f20, f21);
                this.vertexBrightness[((VertexTranslations)vertexTranslations).field_178201_i] = this.getVertexBrightness(n10, n11, n12, n13, f22, f23, f24, f25);
                this.vertexBrightness[((VertexTranslations)vertexTranslations).field_178198_j] = this.getVertexBrightness(n10, n11, n12, n13, f26, f27, f28, f29);
            } else {
                float f30 = (f8 + f5 + f3 + f9) * 0.25f;
                float f31 = (f7 + f5 + f4 + f9) * 0.25f;
                float f32 = (f7 + f6 + f2 + f9) * 0.25f;
                float f33 = (f8 + f6 + f + f9) * 0.25f;
                this.vertexBrightness[((VertexTranslations)vertexTranslations).field_178191_g] = this.getAoBrightness(n8, n5, n3, n9);
                this.vertexBrightness[((VertexTranslations)vertexTranslations).field_178200_h] = this.getAoBrightness(n7, n5, n4, n9);
                this.vertexBrightness[((VertexTranslations)vertexTranslations).field_178201_i] = this.getAoBrightness(n7, n6, n2, n9);
                this.vertexBrightness[((VertexTranslations)vertexTranslations).field_178198_j] = this.getAoBrightness(n8, n6, n, n9);
                this.vertexColorMultiplier[((VertexTranslations)vertexTranslations).field_178191_g] = f30;
                this.vertexColorMultiplier[((VertexTranslations)vertexTranslations).field_178200_h] = f31;
                this.vertexColorMultiplier[((VertexTranslations)vertexTranslations).field_178201_i] = f32;
                this.vertexColorMultiplier[((VertexTranslations)vertexTranslations).field_178198_j] = f33;
            }
        }

        AmbientOcclusionFace() {
        }

        private int getVertexBrightness(int n, int n2, int n3, int n4, float f, float f2, float f3, float f4) {
            int n5 = (int)((float)(n >> 16 & 0xFF) * f + (float)(n2 >> 16 & 0xFF) * f2 + (float)(n3 >> 16 & 0xFF) * f3 + (float)(n4 >> 16 & 0xFF) * f4) & 0xFF;
            int n6 = (int)((float)(n & 0xFF) * f + (float)(n2 & 0xFF) * f2 + (float)(n3 & 0xFF) * f3 + (float)(n4 & 0xFF) * f4) & 0xFF;
            return n5 << 16 | n6;
        }
    }

    public static enum EnumNeighborInfo {
        DOWN(new EnumFacing[]{EnumFacing.WEST, EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.SOUTH}, 0.5f, false, new Orientation[0], new Orientation[0], new Orientation[0], new Orientation[0]),
        UP(new EnumFacing[]{EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH}, 1.0f, false, new Orientation[0], new Orientation[0], new Orientation[0], new Orientation[0]),
        NORTH(new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.EAST, EnumFacing.WEST}, 0.8f, true, new Orientation[]{Orientation.UP, Orientation.FLIP_WEST, Orientation.UP, Orientation.WEST, Orientation.FLIP_UP, Orientation.WEST, Orientation.FLIP_UP, Orientation.FLIP_WEST}, new Orientation[]{Orientation.UP, Orientation.FLIP_EAST, Orientation.UP, Orientation.EAST, Orientation.FLIP_UP, Orientation.EAST, Orientation.FLIP_UP, Orientation.FLIP_EAST}, new Orientation[]{Orientation.DOWN, Orientation.FLIP_EAST, Orientation.DOWN, Orientation.EAST, Orientation.FLIP_DOWN, Orientation.EAST, Orientation.FLIP_DOWN, Orientation.FLIP_EAST}, new Orientation[]{Orientation.DOWN, Orientation.FLIP_WEST, Orientation.DOWN, Orientation.WEST, Orientation.FLIP_DOWN, Orientation.WEST, Orientation.FLIP_DOWN, Orientation.FLIP_WEST}),
        SOUTH(new EnumFacing[]{EnumFacing.WEST, EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.UP}, 0.8f, true, new Orientation[]{Orientation.UP, Orientation.FLIP_WEST, Orientation.FLIP_UP, Orientation.FLIP_WEST, Orientation.FLIP_UP, Orientation.WEST, Orientation.UP, Orientation.WEST}, new Orientation[]{Orientation.DOWN, Orientation.FLIP_WEST, Orientation.FLIP_DOWN, Orientation.FLIP_WEST, Orientation.FLIP_DOWN, Orientation.WEST, Orientation.DOWN, Orientation.WEST}, new Orientation[]{Orientation.DOWN, Orientation.FLIP_EAST, Orientation.FLIP_DOWN, Orientation.FLIP_EAST, Orientation.FLIP_DOWN, Orientation.EAST, Orientation.DOWN, Orientation.EAST}, new Orientation[]{Orientation.UP, Orientation.FLIP_EAST, Orientation.FLIP_UP, Orientation.FLIP_EAST, Orientation.FLIP_UP, Orientation.EAST, Orientation.UP, Orientation.EAST}),
        WEST(new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.SOUTH}, 0.6f, true, new Orientation[]{Orientation.UP, Orientation.SOUTH, Orientation.UP, Orientation.FLIP_SOUTH, Orientation.FLIP_UP, Orientation.FLIP_SOUTH, Orientation.FLIP_UP, Orientation.SOUTH}, new Orientation[]{Orientation.UP, Orientation.NORTH, Orientation.UP, Orientation.FLIP_NORTH, Orientation.FLIP_UP, Orientation.FLIP_NORTH, Orientation.FLIP_UP, Orientation.NORTH}, new Orientation[]{Orientation.DOWN, Orientation.NORTH, Orientation.DOWN, Orientation.FLIP_NORTH, Orientation.FLIP_DOWN, Orientation.FLIP_NORTH, Orientation.FLIP_DOWN, Orientation.NORTH}, new Orientation[]{Orientation.DOWN, Orientation.SOUTH, Orientation.DOWN, Orientation.FLIP_SOUTH, Orientation.FLIP_DOWN, Orientation.FLIP_SOUTH, Orientation.FLIP_DOWN, Orientation.SOUTH}),
        EAST(new EnumFacing[]{EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH}, 0.6f, true, new Orientation[]{Orientation.FLIP_DOWN, Orientation.SOUTH, Orientation.FLIP_DOWN, Orientation.FLIP_SOUTH, Orientation.DOWN, Orientation.FLIP_SOUTH, Orientation.DOWN, Orientation.SOUTH}, new Orientation[]{Orientation.FLIP_DOWN, Orientation.NORTH, Orientation.FLIP_DOWN, Orientation.FLIP_NORTH, Orientation.DOWN, Orientation.FLIP_NORTH, Orientation.DOWN, Orientation.NORTH}, new Orientation[]{Orientation.FLIP_UP, Orientation.NORTH, Orientation.FLIP_UP, Orientation.FLIP_NORTH, Orientation.UP, Orientation.FLIP_NORTH, Orientation.UP, Orientation.NORTH}, new Orientation[]{Orientation.FLIP_UP, Orientation.SOUTH, Orientation.FLIP_UP, Orientation.FLIP_SOUTH, Orientation.UP, Orientation.FLIP_SOUTH, Orientation.UP, Orientation.SOUTH});

        protected final boolean field_178289_i;
        protected final float field_178288_h;
        protected final EnumFacing[] field_178276_g;
        protected final Orientation[] field_178287_k;
        protected final Orientation[] field_178284_l;
        private static final EnumNeighborInfo[] field_178282_n;
        protected final Orientation[] field_178286_j;
        protected final Orientation[] field_178285_m;

        private EnumNeighborInfo(EnumFacing[] enumFacingArray, float f, boolean bl, Orientation[] orientationArray, Orientation[] orientationArray2, Orientation[] orientationArray3, Orientation[] orientationArray4) {
            this.field_178276_g = enumFacingArray;
            this.field_178288_h = f;
            this.field_178289_i = bl;
            this.field_178286_j = orientationArray;
            this.field_178287_k = orientationArray2;
            this.field_178284_l = orientationArray3;
            this.field_178285_m = orientationArray4;
        }

        static {
            field_178282_n = new EnumNeighborInfo[6];
            EnumNeighborInfo.field_178282_n[EnumFacing.DOWN.getIndex()] = DOWN;
            EnumNeighborInfo.field_178282_n[EnumFacing.UP.getIndex()] = UP;
            EnumNeighborInfo.field_178282_n[EnumFacing.NORTH.getIndex()] = NORTH;
            EnumNeighborInfo.field_178282_n[EnumFacing.SOUTH.getIndex()] = SOUTH;
            EnumNeighborInfo.field_178282_n[EnumFacing.WEST.getIndex()] = WEST;
            EnumNeighborInfo.field_178282_n[EnumFacing.EAST.getIndex()] = EAST;
        }

        public static EnumNeighborInfo getNeighbourInfo(EnumFacing enumFacing) {
            return field_178282_n[enumFacing.getIndex()];
        }
    }

    public static enum Orientation {
        DOWN(EnumFacing.DOWN, false),
        UP(EnumFacing.UP, false),
        NORTH(EnumFacing.NORTH, false),
        SOUTH(EnumFacing.SOUTH, false),
        WEST(EnumFacing.WEST, false),
        EAST(EnumFacing.EAST, false),
        FLIP_DOWN(EnumFacing.DOWN, true),
        FLIP_UP(EnumFacing.UP, true),
        FLIP_NORTH(EnumFacing.NORTH, true),
        FLIP_SOUTH(EnumFacing.SOUTH, true),
        FLIP_WEST(EnumFacing.WEST, true),
        FLIP_EAST(EnumFacing.EAST, true);

        protected final int field_178229_m;

        private Orientation(EnumFacing enumFacing, boolean bl) {
            this.field_178229_m = enumFacing.getIndex() + (bl ? EnumFacing.values().length : 0);
        }
    }
}

