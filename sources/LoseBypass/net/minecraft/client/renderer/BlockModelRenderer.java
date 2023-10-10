/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer;

import java.util.BitSet;
import java.util.Iterator;
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
    public boolean renderModel(IBlockAccess blockAccessIn, IBakedModel modelIn, IBlockState blockStateIn, BlockPos blockPosIn, WorldRenderer worldRendererIn) {
        Block block = blockStateIn.getBlock();
        block.setBlockBoundsBasedOnState(blockAccessIn, blockPosIn);
        return this.renderModel(blockAccessIn, modelIn, blockStateIn, blockPosIn, worldRendererIn, true);
    }

    public boolean renderModel(IBlockAccess blockAccessIn, IBakedModel modelIn, IBlockState blockStateIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, boolean checkSides) {
        boolean flag = Minecraft.isAmbientOcclusionEnabled() && blockStateIn.getBlock().getLightValue() == 0 && modelIn.isAmbientOcclusion();
        try {
            boolean bl;
            Block block = blockStateIn.getBlock();
            if (flag) {
                bl = this.renderModelAmbientOcclusion(blockAccessIn, modelIn, block, blockPosIn, worldRendererIn, checkSides);
                return bl;
            }
            bl = this.renderModelStandard(blockAccessIn, modelIn, block, blockPosIn, worldRendererIn, checkSides);
            return bl;
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Tesselating block model");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block model being tesselated");
            CrashReportCategory.addBlockInfo(crashreportcategory, blockPosIn, blockStateIn);
            crashreportcategory.addCrashSection("Using AO", flag);
            throw new ReportedException(crashreport);
        }
    }

    public boolean renderModelAmbientOcclusion(IBlockAccess blockAccessIn, IBakedModel modelIn, Block blockIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, boolean checkSides) {
        boolean flag = false;
        float[] afloat = new float[EnumFacing.values().length * 2];
        BitSet bitset = new BitSet(3);
        AmbientOcclusionFace blockmodelrenderer$ambientocclusionface = new AmbientOcclusionFace();
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (true) {
            if (n2 >= n) {
                List<BakedQuad> list1 = modelIn.getGeneralQuads();
                if (list1.size() <= 0) return flag;
                this.renderModelAmbientOcclusionQuads(blockAccessIn, blockIn, blockPosIn, worldRendererIn, list1, afloat, bitset, blockmodelrenderer$ambientocclusionface);
                return true;
            }
            EnumFacing enumfacing = enumFacingArray[n2];
            List<BakedQuad> list = modelIn.getFaceQuads(enumfacing);
            if (!list.isEmpty()) {
                BlockPos blockpos = blockPosIn.offset(enumfacing);
                if (!checkSides || blockIn.shouldSideBeRendered(blockAccessIn, blockpos, enumfacing)) {
                    this.renderModelAmbientOcclusionQuads(blockAccessIn, blockIn, blockPosIn, worldRendererIn, list, afloat, bitset, blockmodelrenderer$ambientocclusionface);
                    flag = true;
                }
            }
            ++n2;
        }
    }

    public boolean renderModelStandard(IBlockAccess blockAccessIn, IBakedModel modelIn, Block blockIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, boolean checkSides) {
        boolean flag = false;
        BitSet bitset = new BitSet(3);
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (true) {
            if (n2 >= n) {
                List<BakedQuad> list1 = modelIn.getGeneralQuads();
                if (list1.size() <= 0) return flag;
                this.renderModelStandardQuads(blockAccessIn, blockIn, blockPosIn, null, -1, true, worldRendererIn, list1, bitset);
                return true;
            }
            EnumFacing enumfacing = enumFacingArray[n2];
            List<BakedQuad> list = modelIn.getFaceQuads(enumfacing);
            if (!list.isEmpty()) {
                BlockPos blockpos = blockPosIn.offset(enumfacing);
                if (!checkSides || blockIn.shouldSideBeRendered(blockAccessIn, blockpos, enumfacing)) {
                    int i = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos);
                    this.renderModelStandardQuads(blockAccessIn, blockIn, blockPosIn, enumfacing, i, false, worldRendererIn, list, bitset);
                    flag = true;
                }
            }
            ++n2;
        }
    }

    private void renderModelAmbientOcclusionQuads(IBlockAccess blockAccessIn, Block blockIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, List<BakedQuad> listQuadsIn, float[] quadBounds, BitSet boundsFlags, AmbientOcclusionFace aoFaceIn) {
        double d0 = blockPosIn.getX();
        double d1 = blockPosIn.getY();
        double d2 = blockPosIn.getZ();
        Block.EnumOffsetType block$enumoffsettype = blockIn.getOffsetType();
        if (block$enumoffsettype != Block.EnumOffsetType.NONE) {
            long i = MathHelper.getPositionRandom(blockPosIn);
            d0 += ((double)((float)(i >> 16 & 0xFL) / 15.0f) - 0.5) * 0.5;
            d2 += ((double)((float)(i >> 24 & 0xFL) / 15.0f) - 0.5) * 0.5;
            if (block$enumoffsettype == Block.EnumOffsetType.XYZ) {
                d1 += ((double)((float)(i >> 20 & 0xFL) / 15.0f) - 1.0) * 0.2;
            }
        }
        Iterator<BakedQuad> iterator = listQuadsIn.iterator();
        while (iterator.hasNext()) {
            BakedQuad bakedquad = iterator.next();
            this.fillQuadBounds(blockIn, bakedquad.getVertexData(), bakedquad.getFace(), quadBounds, boundsFlags);
            aoFaceIn.updateVertexBrightness(blockAccessIn, blockIn, blockPosIn, bakedquad.getFace(), quadBounds, boundsFlags);
            worldRendererIn.addVertexData(bakedquad.getVertexData());
            worldRendererIn.putBrightness4(aoFaceIn.vertexBrightness[0], aoFaceIn.vertexBrightness[1], aoFaceIn.vertexBrightness[2], aoFaceIn.vertexBrightness[3]);
            if (bakedquad.hasTintIndex()) {
                int j = blockIn.colorMultiplier(blockAccessIn, blockPosIn, bakedquad.getTintIndex());
                if (EntityRenderer.anaglyphEnable) {
                    j = TextureUtil.anaglyphColor(j);
                }
                float f = (float)(j >> 16 & 0xFF) / 255.0f;
                float f1 = (float)(j >> 8 & 0xFF) / 255.0f;
                float f2 = (float)(j & 0xFF) / 255.0f;
                worldRendererIn.putColorMultiplier(aoFaceIn.vertexColorMultiplier[0] * f, aoFaceIn.vertexColorMultiplier[0] * f1, aoFaceIn.vertexColorMultiplier[0] * f2, 4);
                worldRendererIn.putColorMultiplier(aoFaceIn.vertexColorMultiplier[1] * f, aoFaceIn.vertexColorMultiplier[1] * f1, aoFaceIn.vertexColorMultiplier[1] * f2, 3);
                worldRendererIn.putColorMultiplier(aoFaceIn.vertexColorMultiplier[2] * f, aoFaceIn.vertexColorMultiplier[2] * f1, aoFaceIn.vertexColorMultiplier[2] * f2, 2);
                worldRendererIn.putColorMultiplier(aoFaceIn.vertexColorMultiplier[3] * f, aoFaceIn.vertexColorMultiplier[3] * f1, aoFaceIn.vertexColorMultiplier[3] * f2, 1);
            } else {
                worldRendererIn.putColorMultiplier(aoFaceIn.vertexColorMultiplier[0], aoFaceIn.vertexColorMultiplier[0], aoFaceIn.vertexColorMultiplier[0], 4);
                worldRendererIn.putColorMultiplier(aoFaceIn.vertexColorMultiplier[1], aoFaceIn.vertexColorMultiplier[1], aoFaceIn.vertexColorMultiplier[1], 3);
                worldRendererIn.putColorMultiplier(aoFaceIn.vertexColorMultiplier[2], aoFaceIn.vertexColorMultiplier[2], aoFaceIn.vertexColorMultiplier[2], 2);
                worldRendererIn.putColorMultiplier(aoFaceIn.vertexColorMultiplier[3], aoFaceIn.vertexColorMultiplier[3], aoFaceIn.vertexColorMultiplier[3], 1);
            }
            worldRendererIn.putPosition(d0, d1, d2);
        }
    }

    private void fillQuadBounds(Block blockIn, int[] vertexData, EnumFacing facingIn, float[] quadBounds, BitSet boundsFlags) {
        float f = 32.0f;
        float f1 = 32.0f;
        float f2 = 32.0f;
        float f3 = -32.0f;
        float f4 = -32.0f;
        float f5 = -32.0f;
        for (int i = 0; i < 4; ++i) {
            float f6 = Float.intBitsToFloat(vertexData[i * 7]);
            float f7 = Float.intBitsToFloat(vertexData[i * 7 + 1]);
            float f8 = Float.intBitsToFloat(vertexData[i * 7 + 2]);
            f = Math.min(f, f6);
            f1 = Math.min(f1, f7);
            f2 = Math.min(f2, f8);
            f3 = Math.max(f3, f6);
            f4 = Math.max(f4, f7);
            f5 = Math.max(f5, f8);
        }
        if (quadBounds != null) {
            quadBounds[EnumFacing.WEST.getIndex()] = f;
            quadBounds[EnumFacing.EAST.getIndex()] = f3;
            quadBounds[EnumFacing.DOWN.getIndex()] = f1;
            quadBounds[EnumFacing.UP.getIndex()] = f4;
            quadBounds[EnumFacing.NORTH.getIndex()] = f2;
            quadBounds[EnumFacing.SOUTH.getIndex()] = f5;
            quadBounds[EnumFacing.WEST.getIndex() + EnumFacing.values().length] = 1.0f - f;
            quadBounds[EnumFacing.EAST.getIndex() + EnumFacing.values().length] = 1.0f - f3;
            quadBounds[EnumFacing.DOWN.getIndex() + EnumFacing.values().length] = 1.0f - f1;
            quadBounds[EnumFacing.UP.getIndex() + EnumFacing.values().length] = 1.0f - f4;
            quadBounds[EnumFacing.NORTH.getIndex() + EnumFacing.values().length] = 1.0f - f2;
            quadBounds[EnumFacing.SOUTH.getIndex() + EnumFacing.values().length] = 1.0f - f5;
        }
        float f9 = 1.0E-4f;
        float f10 = 0.9999f;
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[facingIn.ordinal()]) {
            case 1: {
                boundsFlags.set(1, f >= 1.0E-4f || f2 >= 1.0E-4f || f3 <= 0.9999f || f5 <= 0.9999f);
                boundsFlags.set(0, (f1 < 1.0E-4f || blockIn.isFullCube()) && f1 == f4);
                return;
            }
            case 2: {
                boundsFlags.set(1, f >= 1.0E-4f || f2 >= 1.0E-4f || f3 <= 0.9999f || f5 <= 0.9999f);
                boundsFlags.set(0, (f4 > 0.9999f || blockIn.isFullCube()) && f1 == f4);
                return;
            }
            case 3: {
                boundsFlags.set(1, f >= 1.0E-4f || f1 >= 1.0E-4f || f3 <= 0.9999f || f4 <= 0.9999f);
                boundsFlags.set(0, (f2 < 1.0E-4f || blockIn.isFullCube()) && f2 == f5);
                return;
            }
            case 4: {
                boundsFlags.set(1, f >= 1.0E-4f || f1 >= 1.0E-4f || f3 <= 0.9999f || f4 <= 0.9999f);
                boundsFlags.set(0, (f5 > 0.9999f || blockIn.isFullCube()) && f2 == f5);
                return;
            }
            case 5: {
                boundsFlags.set(1, f1 >= 1.0E-4f || f2 >= 1.0E-4f || f4 <= 0.9999f || f5 <= 0.9999f);
                boundsFlags.set(0, (f < 1.0E-4f || blockIn.isFullCube()) && f == f3);
                return;
            }
            case 6: {
                boundsFlags.set(1, f1 >= 1.0E-4f || f2 >= 1.0E-4f || f4 <= 0.9999f || f5 <= 0.9999f);
                boundsFlags.set(0, (f3 > 0.9999f || blockIn.isFullCube()) && f == f3);
                return;
            }
        }
    }

    private void renderModelStandardQuads(IBlockAccess blockAccessIn, Block blockIn, BlockPos blockPosIn, EnumFacing faceIn, int brightnessIn, boolean ownBrightness, WorldRenderer worldRendererIn, List<BakedQuad> listQuadsIn, BitSet boundsFlags) {
        double d0 = blockPosIn.getX();
        double d1 = blockPosIn.getY();
        double d2 = blockPosIn.getZ();
        Block.EnumOffsetType block$enumoffsettype = blockIn.getOffsetType();
        if (block$enumoffsettype != Block.EnumOffsetType.NONE) {
            int i = blockPosIn.getX();
            int j = blockPosIn.getZ();
            long k = (long)(i * 3129871) ^ (long)j * 116129781L;
            k = k * k * 42317861L + k * 11L;
            d0 += ((double)((float)(k >> 16 & 0xFL) / 15.0f) - 0.5) * 0.5;
            d2 += ((double)((float)(k >> 24 & 0xFL) / 15.0f) - 0.5) * 0.5;
            if (block$enumoffsettype == Block.EnumOffsetType.XYZ) {
                d1 += ((double)((float)(k >> 20 & 0xFL) / 15.0f) - 1.0) * 0.2;
            }
        }
        Iterator<BakedQuad> iterator = listQuadsIn.iterator();
        while (iterator.hasNext()) {
            BakedQuad bakedquad = iterator.next();
            if (ownBrightness) {
                this.fillQuadBounds(blockIn, bakedquad.getVertexData(), bakedquad.getFace(), null, boundsFlags);
                brightnessIn = boundsFlags.get(0) ? blockIn.getMixedBrightnessForBlock(blockAccessIn, blockPosIn.offset(bakedquad.getFace())) : blockIn.getMixedBrightnessForBlock(blockAccessIn, blockPosIn);
            }
            worldRendererIn.addVertexData(bakedquad.getVertexData());
            worldRendererIn.putBrightness4(brightnessIn, brightnessIn, brightnessIn, brightnessIn);
            if (bakedquad.hasTintIndex()) {
                int l = blockIn.colorMultiplier(blockAccessIn, blockPosIn, bakedquad.getTintIndex());
                if (EntityRenderer.anaglyphEnable) {
                    l = TextureUtil.anaglyphColor(l);
                }
                float f = (float)(l >> 16 & 0xFF) / 255.0f;
                float f1 = (float)(l >> 8 & 0xFF) / 255.0f;
                float f2 = (float)(l & 0xFF) / 255.0f;
                worldRendererIn.putColorMultiplier(f, f1, f2, 4);
                worldRendererIn.putColorMultiplier(f, f1, f2, 3);
                worldRendererIn.putColorMultiplier(f, f1, f2, 2);
                worldRendererIn.putColorMultiplier(f, f1, f2, 1);
            }
            worldRendererIn.putPosition(d0, d1, d2);
        }
    }

    public void renderModelBrightnessColor(IBakedModel bakedModel, float p_178262_2_, float p_178262_3_, float p_178262_4_, float p_178262_5_) {
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (true) {
            if (n2 >= n) {
                this.renderModelBrightnessColorQuads(p_178262_2_, p_178262_3_, p_178262_4_, p_178262_5_, bakedModel.getGeneralQuads());
                return;
            }
            EnumFacing enumfacing = enumFacingArray[n2];
            this.renderModelBrightnessColorQuads(p_178262_2_, p_178262_3_, p_178262_4_, p_178262_5_, bakedModel.getFaceQuads(enumfacing));
            ++n2;
        }
    }

    public void renderModelBrightness(IBakedModel p_178266_1_, IBlockState p_178266_2_, float p_178266_3_, boolean p_178266_4_) {
        Block block = p_178266_2_.getBlock();
        block.setBlockBoundsForItemRender();
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        int i = block.getRenderColor(block.getStateForEntityRender(p_178266_2_));
        if (EntityRenderer.anaglyphEnable) {
            i = TextureUtil.anaglyphColor(i);
        }
        float f = (float)(i >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(i >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(i & 0xFF) / 255.0f;
        if (!p_178266_4_) {
            GlStateManager.color(p_178266_3_, p_178266_3_, p_178266_3_, 1.0f);
        }
        this.renderModelBrightnessColor(p_178266_1_, p_178266_3_, f, f1, f2);
    }

    private void renderModelBrightnessColorQuads(float p_178264_1_, float p_178264_2_, float p_178264_3_, float p_178264_4_, List<BakedQuad> p_178264_5_) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        Iterator<BakedQuad> iterator = p_178264_5_.iterator();
        while (iterator.hasNext()) {
            BakedQuad bakedquad = iterator.next();
            worldrenderer.begin(7, DefaultVertexFormats.ITEM);
            worldrenderer.addVertexData(bakedquad.getVertexData());
            if (bakedquad.hasTintIndex()) {
                worldrenderer.putColorRGB_F4(p_178264_2_ * p_178264_1_, p_178264_3_ * p_178264_1_, p_178264_4_ * p_178264_1_);
            } else {
                worldrenderer.putColorRGB_F4(p_178264_1_, p_178264_1_, p_178264_1_);
            }
            Vec3i vec3i = bakedquad.getFace().getDirectionVec();
            worldrenderer.putNormal(vec3i.getX(), vec3i.getY(), vec3i.getZ());
            tessellator.draw();
        }
    }

    static enum VertexTranslations {
        DOWN(0, 1, 2, 3),
        UP(2, 3, 0, 1),
        NORTH(3, 0, 1, 2),
        SOUTH(0, 1, 2, 3),
        WEST(3, 0, 1, 2),
        EAST(1, 2, 3, 0);

        private final int field_178191_g;
        private final int field_178200_h;
        private final int field_178201_i;
        private final int field_178198_j;
        private static final VertexTranslations[] field_178199_k;

        private VertexTranslations(int p_i46234_3_, int p_i46234_4_, int p_i46234_5_, int p_i46234_6_) {
            this.field_178191_g = p_i46234_3_;
            this.field_178200_h = p_i46234_4_;
            this.field_178201_i = p_i46234_5_;
            this.field_178198_j = p_i46234_6_;
        }

        public static VertexTranslations getVertexTranslations(EnumFacing p_178184_0_) {
            return field_178199_k[p_178184_0_.getIndex()];
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

        private Orientation(EnumFacing p_i46233_3_, boolean p_i46233_4_) {
            this.field_178229_m = p_i46233_3_.getIndex() + (p_i46233_4_ ? EnumFacing.values().length : 0);
        }
    }

    public static enum EnumNeighborInfo {
        DOWN(new EnumFacing[]{EnumFacing.WEST, EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.SOUTH}, 0.5f, false, new Orientation[0], new Orientation[0], new Orientation[0], new Orientation[0]),
        UP(new EnumFacing[]{EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH}, 1.0f, false, new Orientation[0], new Orientation[0], new Orientation[0], new Orientation[0]),
        NORTH(new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.EAST, EnumFacing.WEST}, 0.8f, true, new Orientation[]{Orientation.UP, Orientation.FLIP_WEST, Orientation.UP, Orientation.WEST, Orientation.FLIP_UP, Orientation.WEST, Orientation.FLIP_UP, Orientation.FLIP_WEST}, new Orientation[]{Orientation.UP, Orientation.FLIP_EAST, Orientation.UP, Orientation.EAST, Orientation.FLIP_UP, Orientation.EAST, Orientation.FLIP_UP, Orientation.FLIP_EAST}, new Orientation[]{Orientation.DOWN, Orientation.FLIP_EAST, Orientation.DOWN, Orientation.EAST, Orientation.FLIP_DOWN, Orientation.EAST, Orientation.FLIP_DOWN, Orientation.FLIP_EAST}, new Orientation[]{Orientation.DOWN, Orientation.FLIP_WEST, Orientation.DOWN, Orientation.WEST, Orientation.FLIP_DOWN, Orientation.WEST, Orientation.FLIP_DOWN, Orientation.FLIP_WEST}),
        SOUTH(new EnumFacing[]{EnumFacing.WEST, EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.UP}, 0.8f, true, new Orientation[]{Orientation.UP, Orientation.FLIP_WEST, Orientation.FLIP_UP, Orientation.FLIP_WEST, Orientation.FLIP_UP, Orientation.WEST, Orientation.UP, Orientation.WEST}, new Orientation[]{Orientation.DOWN, Orientation.FLIP_WEST, Orientation.FLIP_DOWN, Orientation.FLIP_WEST, Orientation.FLIP_DOWN, Orientation.WEST, Orientation.DOWN, Orientation.WEST}, new Orientation[]{Orientation.DOWN, Orientation.FLIP_EAST, Orientation.FLIP_DOWN, Orientation.FLIP_EAST, Orientation.FLIP_DOWN, Orientation.EAST, Orientation.DOWN, Orientation.EAST}, new Orientation[]{Orientation.UP, Orientation.FLIP_EAST, Orientation.FLIP_UP, Orientation.FLIP_EAST, Orientation.FLIP_UP, Orientation.EAST, Orientation.UP, Orientation.EAST}),
        WEST(new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.SOUTH}, 0.6f, true, new Orientation[]{Orientation.UP, Orientation.SOUTH, Orientation.UP, Orientation.FLIP_SOUTH, Orientation.FLIP_UP, Orientation.FLIP_SOUTH, Orientation.FLIP_UP, Orientation.SOUTH}, new Orientation[]{Orientation.UP, Orientation.NORTH, Orientation.UP, Orientation.FLIP_NORTH, Orientation.FLIP_UP, Orientation.FLIP_NORTH, Orientation.FLIP_UP, Orientation.NORTH}, new Orientation[]{Orientation.DOWN, Orientation.NORTH, Orientation.DOWN, Orientation.FLIP_NORTH, Orientation.FLIP_DOWN, Orientation.FLIP_NORTH, Orientation.FLIP_DOWN, Orientation.NORTH}, new Orientation[]{Orientation.DOWN, Orientation.SOUTH, Orientation.DOWN, Orientation.FLIP_SOUTH, Orientation.FLIP_DOWN, Orientation.FLIP_SOUTH, Orientation.FLIP_DOWN, Orientation.SOUTH}),
        EAST(new EnumFacing[]{EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH}, 0.6f, true, new Orientation[]{Orientation.FLIP_DOWN, Orientation.SOUTH, Orientation.FLIP_DOWN, Orientation.FLIP_SOUTH, Orientation.DOWN, Orientation.FLIP_SOUTH, Orientation.DOWN, Orientation.SOUTH}, new Orientation[]{Orientation.FLIP_DOWN, Orientation.NORTH, Orientation.FLIP_DOWN, Orientation.FLIP_NORTH, Orientation.DOWN, Orientation.FLIP_NORTH, Orientation.DOWN, Orientation.NORTH}, new Orientation[]{Orientation.FLIP_UP, Orientation.NORTH, Orientation.FLIP_UP, Orientation.FLIP_NORTH, Orientation.UP, Orientation.FLIP_NORTH, Orientation.UP, Orientation.NORTH}, new Orientation[]{Orientation.FLIP_UP, Orientation.SOUTH, Orientation.FLIP_UP, Orientation.FLIP_SOUTH, Orientation.UP, Orientation.FLIP_SOUTH, Orientation.UP, Orientation.SOUTH});

        protected final EnumFacing[] field_178276_g;
        protected final float field_178288_h;
        protected final boolean field_178289_i;
        protected final Orientation[] field_178286_j;
        protected final Orientation[] field_178287_k;
        protected final Orientation[] field_178284_l;
        protected final Orientation[] field_178285_m;
        private static final EnumNeighborInfo[] field_178282_n;

        private EnumNeighborInfo(EnumFacing[] p_i46236_3_, float p_i46236_4_, boolean p_i46236_5_, Orientation[] p_i46236_6_, Orientation[] p_i46236_7_, Orientation[] p_i46236_8_, Orientation[] p_i46236_9_) {
            this.field_178276_g = p_i46236_3_;
            this.field_178288_h = p_i46236_4_;
            this.field_178289_i = p_i46236_5_;
            this.field_178286_j = p_i46236_6_;
            this.field_178287_k = p_i46236_7_;
            this.field_178284_l = p_i46236_8_;
            this.field_178285_m = p_i46236_9_;
        }

        public static EnumNeighborInfo getNeighbourInfo(EnumFacing p_178273_0_) {
            return field_178282_n[p_178273_0_.getIndex()];
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
    }

    class AmbientOcclusionFace {
        private final float[] vertexColorMultiplier = new float[4];
        private final int[] vertexBrightness = new int[4];

        AmbientOcclusionFace() {
        }

        public void updateVertexBrightness(IBlockAccess blockAccessIn, Block blockIn, BlockPos blockPosIn, EnumFacing facingIn, float[] quadBounds, BitSet boundsFlags) {
            int l1;
            float f7;
            int k1;
            float f6;
            int j1;
            float f5;
            int i1;
            float f4;
            BlockPos blockpos = boundsFlags.get(0) ? blockPosIn.offset(facingIn) : blockPosIn;
            EnumNeighborInfo blockmodelrenderer$enumneighborinfo = EnumNeighborInfo.getNeighbourInfo(facingIn);
            BlockPos blockpos1 = blockpos.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[0]);
            BlockPos blockpos2 = blockpos.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[1]);
            BlockPos blockpos3 = blockpos.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[2]);
            BlockPos blockpos4 = blockpos.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[3]);
            int i = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos1);
            int j = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos2);
            int k = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos3);
            int l = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos4);
            float f = blockAccessIn.getBlockState(blockpos1).getBlock().getAmbientOcclusionLightValue();
            float f1 = blockAccessIn.getBlockState(blockpos2).getBlock().getAmbientOcclusionLightValue();
            float f2 = blockAccessIn.getBlockState(blockpos3).getBlock().getAmbientOcclusionLightValue();
            float f3 = blockAccessIn.getBlockState(blockpos4).getBlock().getAmbientOcclusionLightValue();
            boolean flag = blockAccessIn.getBlockState(blockpos1.offset(facingIn)).getBlock().isTranslucent();
            boolean flag1 = blockAccessIn.getBlockState(blockpos2.offset(facingIn)).getBlock().isTranslucent();
            boolean flag2 = blockAccessIn.getBlockState(blockpos3.offset(facingIn)).getBlock().isTranslucent();
            boolean flag3 = blockAccessIn.getBlockState(blockpos4.offset(facingIn)).getBlock().isTranslucent();
            if (!flag2 && !flag) {
                f4 = f;
                i1 = i;
            } else {
                BlockPos blockpos5 = blockpos1.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[2]);
                f4 = blockAccessIn.getBlockState(blockpos5).getBlock().getAmbientOcclusionLightValue();
                i1 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos5);
            }
            if (!flag3 && !flag) {
                f5 = f;
                j1 = i;
            } else {
                BlockPos blockpos6 = blockpos1.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[3]);
                f5 = blockAccessIn.getBlockState(blockpos6).getBlock().getAmbientOcclusionLightValue();
                j1 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos6);
            }
            if (!flag2 && !flag1) {
                f6 = f1;
                k1 = j;
            } else {
                BlockPos blockpos7 = blockpos2.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[2]);
                f6 = blockAccessIn.getBlockState(blockpos7).getBlock().getAmbientOcclusionLightValue();
                k1 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos7);
            }
            if (!flag3 && !flag1) {
                f7 = f1;
                l1 = j;
            } else {
                BlockPos blockpos8 = blockpos2.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[3]);
                f7 = blockAccessIn.getBlockState(blockpos8).getBlock().getAmbientOcclusionLightValue();
                l1 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos8);
            }
            int i3 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockPosIn);
            if (boundsFlags.get(0) || !blockAccessIn.getBlockState(blockPosIn.offset(facingIn)).getBlock().isOpaqueCube()) {
                i3 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockPosIn.offset(facingIn));
            }
            float f8 = boundsFlags.get(0) ? blockAccessIn.getBlockState(blockpos).getBlock().getAmbientOcclusionLightValue() : blockAccessIn.getBlockState(blockPosIn).getBlock().getAmbientOcclusionLightValue();
            VertexTranslations blockmodelrenderer$vertextranslations = VertexTranslations.getVertexTranslations(facingIn);
            if (boundsFlags.get(1) && blockmodelrenderer$enumneighborinfo.field_178289_i) {
                float f29 = (f3 + f + f5 + f8) * 0.25f;
                float f30 = (f2 + f + f4 + f8) * 0.25f;
                float f31 = (f2 + f1 + f6 + f8) * 0.25f;
                float f32 = (f3 + f1 + f7 + f8) * 0.25f;
                float f13 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[0].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[1].field_178229_m];
                float f14 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[2].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[3].field_178229_m];
                float f15 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[4].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[5].field_178229_m];
                float f16 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[6].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[7].field_178229_m];
                float f17 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[0].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[1].field_178229_m];
                float f18 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[2].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[3].field_178229_m];
                float f19 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[4].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[5].field_178229_m];
                float f20 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[6].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[7].field_178229_m];
                float f21 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[0].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[1].field_178229_m];
                float f22 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[2].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[3].field_178229_m];
                float f23 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[4].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[5].field_178229_m];
                float f24 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[6].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[7].field_178229_m];
                float f25 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[0].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[1].field_178229_m];
                float f26 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[2].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[3].field_178229_m];
                float f27 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[4].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[5].field_178229_m];
                float f28 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[6].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[7].field_178229_m];
                this.vertexColorMultiplier[((VertexTranslations)blockmodelrenderer$vertextranslations).field_178191_g] = f29 * f13 + f30 * f14 + f31 * f15 + f32 * f16;
                this.vertexColorMultiplier[((VertexTranslations)blockmodelrenderer$vertextranslations).field_178200_h] = f29 * f17 + f30 * f18 + f31 * f19 + f32 * f20;
                this.vertexColorMultiplier[((VertexTranslations)blockmodelrenderer$vertextranslations).field_178201_i] = f29 * f21 + f30 * f22 + f31 * f23 + f32 * f24;
                this.vertexColorMultiplier[((VertexTranslations)blockmodelrenderer$vertextranslations).field_178198_j] = f29 * f25 + f30 * f26 + f31 * f27 + f32 * f28;
                int i2 = this.getAoBrightness(l, i, j1, i3);
                int j2 = this.getAoBrightness(k, i, i1, i3);
                int k2 = this.getAoBrightness(k, j, k1, i3);
                int l2 = this.getAoBrightness(l, j, l1, i3);
                this.vertexBrightness[((VertexTranslations)blockmodelrenderer$vertextranslations).field_178191_g] = this.getVertexBrightness(i2, j2, k2, l2, f13, f14, f15, f16);
                this.vertexBrightness[((VertexTranslations)blockmodelrenderer$vertextranslations).field_178200_h] = this.getVertexBrightness(i2, j2, k2, l2, f17, f18, f19, f20);
                this.vertexBrightness[((VertexTranslations)blockmodelrenderer$vertextranslations).field_178201_i] = this.getVertexBrightness(i2, j2, k2, l2, f21, f22, f23, f24);
                this.vertexBrightness[((VertexTranslations)blockmodelrenderer$vertextranslations).field_178198_j] = this.getVertexBrightness(i2, j2, k2, l2, f25, f26, f27, f28);
                return;
            }
            float f9 = (f3 + f + f5 + f8) * 0.25f;
            float f10 = (f2 + f + f4 + f8) * 0.25f;
            float f11 = (f2 + f1 + f6 + f8) * 0.25f;
            float f12 = (f3 + f1 + f7 + f8) * 0.25f;
            this.vertexBrightness[((VertexTranslations)blockmodelrenderer$vertextranslations).field_178191_g] = this.getAoBrightness(l, i, j1, i3);
            this.vertexBrightness[((VertexTranslations)blockmodelrenderer$vertextranslations).field_178200_h] = this.getAoBrightness(k, i, i1, i3);
            this.vertexBrightness[((VertexTranslations)blockmodelrenderer$vertextranslations).field_178201_i] = this.getAoBrightness(k, j, k1, i3);
            this.vertexBrightness[((VertexTranslations)blockmodelrenderer$vertextranslations).field_178198_j] = this.getAoBrightness(l, j, l1, i3);
            this.vertexColorMultiplier[((VertexTranslations)blockmodelrenderer$vertextranslations).field_178191_g] = f9;
            this.vertexColorMultiplier[((VertexTranslations)blockmodelrenderer$vertextranslations).field_178200_h] = f10;
            this.vertexColorMultiplier[((VertexTranslations)blockmodelrenderer$vertextranslations).field_178201_i] = f11;
            this.vertexColorMultiplier[((VertexTranslations)blockmodelrenderer$vertextranslations).field_178198_j] = f12;
        }

        private int getAoBrightness(int p_147778_1_, int p_147778_2_, int p_147778_3_, int p_147778_4_) {
            if (p_147778_1_ == 0) {
                p_147778_1_ = p_147778_4_;
            }
            if (p_147778_2_ == 0) {
                p_147778_2_ = p_147778_4_;
            }
            if (p_147778_3_ != 0) return p_147778_1_ + p_147778_2_ + p_147778_3_ + p_147778_4_ >> 2 & 0xFF00FF;
            p_147778_3_ = p_147778_4_;
            return p_147778_1_ + p_147778_2_ + p_147778_3_ + p_147778_4_ >> 2 & 0xFF00FF;
        }

        private int getVertexBrightness(int p_178203_1_, int p_178203_2_, int p_178203_3_, int p_178203_4_, float p_178203_5_, float p_178203_6_, float p_178203_7_, float p_178203_8_) {
            int i = (int)((float)(p_178203_1_ >> 16 & 0xFF) * p_178203_5_ + (float)(p_178203_2_ >> 16 & 0xFF) * p_178203_6_ + (float)(p_178203_3_ >> 16 & 0xFF) * p_178203_7_ + (float)(p_178203_4_ >> 16 & 0xFF) * p_178203_8_) & 0xFF;
            int j = (int)((float)(p_178203_1_ & 0xFF) * p_178203_5_ + (float)(p_178203_2_ & 0xFF) * p_178203_6_ + (float)(p_178203_3_ & 0xFF) * p_178203_7_ + (float)(p_178203_4_ & 0xFF) * p_178203_8_) & 0xFF;
            return i << 16 | j;
        }
    }
}

