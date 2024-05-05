package net.minecraft.client.renderer;

import java.util.BitSet;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

/*
 * Exception performing whole class analysis ignored.
 */
public static class BlockModelRenderer.AmbientOcclusionFace {
    private final float[] vertexColorMultiplier = new float[4];
    private final int[] vertexBrightness = new int[4];

    public BlockModelRenderer.AmbientOcclusionFace() {
        this(null);
    }

    public BlockModelRenderer.AmbientOcclusionFace(BlockModelRenderer p_i46235_1_) {
    }

    public void setMaxBlockLight() {
        int i = 240;
        this.vertexBrightness[0] = this.vertexBrightness[0] | i;
        this.vertexBrightness[1] = this.vertexBrightness[1] | i;
        this.vertexBrightness[2] = this.vertexBrightness[2] | i;
        this.vertexBrightness[3] = this.vertexBrightness[3] | i;
        this.vertexColorMultiplier[0] = 1.0f;
        this.vertexColorMultiplier[1] = 1.0f;
        this.vertexColorMultiplier[2] = 1.0f;
        this.vertexColorMultiplier[3] = 1.0f;
    }

    public void updateVertexBrightness(IBlockAccess blockAccessIn, Block blockIn, BlockPos blockPosIn, EnumFacing facingIn, float[] quadBounds, BitSet boundsFlags) {
        int l1;
        float f28;
        int k1;
        float f27;
        int j1;
        float f26;
        int i1;
        float f4;
        BlockPos blockpos = boundsFlags.get(0) ? blockPosIn.offset(facingIn) : blockPosIn;
        BlockModelRenderer.EnumNeighborInfo blockmodelrenderer$enumneighborinfo = BlockModelRenderer.EnumNeighborInfo.getNeighbourInfo((EnumFacing)facingIn);
        BlockPos blockpos1 = blockpos.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[0]);
        BlockPos blockpos2 = blockpos.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[1]);
        BlockPos blockpos3 = blockpos.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[2]);
        BlockPos blockpos4 = blockpos.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[3]);
        int i = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos1);
        int j = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos2);
        int k = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos3);
        int l = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos4);
        float f = BlockModelRenderer.fixAoLightValue((float)blockAccessIn.getBlockState(blockpos1).getBlock().getAmbientOcclusionLightValue());
        float f1 = BlockModelRenderer.fixAoLightValue((float)blockAccessIn.getBlockState(blockpos2).getBlock().getAmbientOcclusionLightValue());
        float f2 = BlockModelRenderer.fixAoLightValue((float)blockAccessIn.getBlockState(blockpos3).getBlock().getAmbientOcclusionLightValue());
        float f3 = BlockModelRenderer.fixAoLightValue((float)blockAccessIn.getBlockState(blockpos4).getBlock().getAmbientOcclusionLightValue());
        boolean flag = blockAccessIn.getBlockState(blockpos1.offset(facingIn)).getBlock().isTranslucent();
        boolean flag1 = blockAccessIn.getBlockState(blockpos2.offset(facingIn)).getBlock().isTranslucent();
        boolean flag2 = blockAccessIn.getBlockState(blockpos3.offset(facingIn)).getBlock().isTranslucent();
        boolean flag3 = blockAccessIn.getBlockState(blockpos4.offset(facingIn)).getBlock().isTranslucent();
        if (!flag2 && !flag) {
            f4 = f;
            i1 = i;
        } else {
            BlockPos blockpos5 = blockpos1.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[2]);
            f4 = BlockModelRenderer.fixAoLightValue((float)blockAccessIn.getBlockState(blockpos5).getBlock().getAmbientOcclusionLightValue());
            i1 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos5);
        }
        if (!flag3 && !flag) {
            f26 = f;
            j1 = i;
        } else {
            BlockPos blockpos6 = blockpos1.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[3]);
            f26 = BlockModelRenderer.fixAoLightValue((float)blockAccessIn.getBlockState(blockpos6).getBlock().getAmbientOcclusionLightValue());
            j1 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos6);
        }
        if (!flag2 && !flag1) {
            f27 = f1;
            k1 = j;
        } else {
            BlockPos blockpos7 = blockpos2.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[2]);
            f27 = BlockModelRenderer.fixAoLightValue((float)blockAccessIn.getBlockState(blockpos7).getBlock().getAmbientOcclusionLightValue());
            k1 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos7);
        }
        if (!flag3 && !flag1) {
            f28 = f1;
            l1 = j;
        } else {
            BlockPos blockpos8 = blockpos2.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[3]);
            f28 = BlockModelRenderer.fixAoLightValue((float)blockAccessIn.getBlockState(blockpos8).getBlock().getAmbientOcclusionLightValue());
            l1 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos8);
        }
        int i3 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockPosIn);
        if (boundsFlags.get(0) || !blockAccessIn.getBlockState(blockPosIn.offset(facingIn)).getBlock().isOpaqueCube()) {
            i3 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockPosIn.offset(facingIn));
        }
        float f5 = boundsFlags.get(0) ? blockAccessIn.getBlockState(blockpos).getBlock().getAmbientOcclusionLightValue() : blockAccessIn.getBlockState(blockPosIn).getBlock().getAmbientOcclusionLightValue();
        f5 = BlockModelRenderer.fixAoLightValue((float)f5);
        BlockModelRenderer.VertexTranslations blockmodelrenderer$vertextranslations = BlockModelRenderer.VertexTranslations.getVertexTranslations((EnumFacing)facingIn);
        if (boundsFlags.get(1) && blockmodelrenderer$enumneighborinfo.field_178289_i) {
            float f29 = (f3 + f + f26 + f5) * 0.25f;
            float f30 = (f2 + f + f4 + f5) * 0.25f;
            float f31 = (f2 + f1 + f27 + f5) * 0.25f;
            float f32 = (f3 + f1 + f28 + f5) * 0.25f;
            float f10 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[0].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[1].field_178229_m];
            float f11 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[2].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[3].field_178229_m];
            float f12 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[4].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[5].field_178229_m];
            float f13 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[6].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[7].field_178229_m];
            float f14 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[0].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[1].field_178229_m];
            float f15 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[2].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[3].field_178229_m];
            float f16 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[4].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[5].field_178229_m];
            float f17 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[6].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[7].field_178229_m];
            float f18 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[0].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[1].field_178229_m];
            float f19 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[2].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[3].field_178229_m];
            float f20 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[4].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[5].field_178229_m];
            float f21 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[6].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[7].field_178229_m];
            float f22 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[0].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[1].field_178229_m];
            float f23 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[2].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[3].field_178229_m];
            float f24 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[4].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[5].field_178229_m];
            float f25 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[6].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[7].field_178229_m];
            this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$200((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = f29 * f10 + f30 * f11 + f31 * f12 + f32 * f13;
            this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$300((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = f29 * f14 + f30 * f15 + f31 * f16 + f32 * f17;
            this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$400((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = f29 * f18 + f30 * f19 + f31 * f20 + f32 * f21;
            this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$500((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = f29 * f22 + f30 * f23 + f31 * f24 + f32 * f25;
            int i2 = this.getAoBrightness(l, i, j1, i3);
            int j2 = this.getAoBrightness(k, i, i1, i3);
            int k2 = this.getAoBrightness(k, j, k1, i3);
            int l2 = this.getAoBrightness(l, j, l1, i3);
            this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$200((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = this.getVertexBrightness(i2, j2, k2, l2, f10, f11, f12, f13);
            this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$300((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = this.getVertexBrightness(i2, j2, k2, l2, f14, f15, f16, f17);
            this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$400((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = this.getVertexBrightness(i2, j2, k2, l2, f18, f19, f20, f21);
            this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$500((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = this.getVertexBrightness(i2, j2, k2, l2, f22, f23, f24, f25);
        } else {
            float f6 = (f3 + f + f26 + f5) * 0.25f;
            float f7 = (f2 + f + f4 + f5) * 0.25f;
            float f8 = (f2 + f1 + f27 + f5) * 0.25f;
            float f9 = (f3 + f1 + f28 + f5) * 0.25f;
            this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$200((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = this.getAoBrightness(l, i, j1, i3);
            this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$300((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = this.getAoBrightness(k, i, i1, i3);
            this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$400((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = this.getAoBrightness(k, j, k1, i3);
            this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$500((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = this.getAoBrightness(l, j, l1, i3);
            this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$200((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = f6;
            this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$300((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = f7;
            this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$400((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = f8;
            this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$500((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = f9;
        }
    }

    private int getAoBrightness(int br1, int br2, int br3, int br4) {
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

    private int getVertexBrightness(int p_178203_1_, int p_178203_2_, int p_178203_3_, int p_178203_4_, float p_178203_5_, float p_178203_6_, float p_178203_7_, float p_178203_8_) {
        int i = (int)((float)(p_178203_1_ >> 16 & 0xFF) * p_178203_5_ + (float)(p_178203_2_ >> 16 & 0xFF) * p_178203_6_ + (float)(p_178203_3_ >> 16 & 0xFF) * p_178203_7_ + (float)(p_178203_4_ >> 16 & 0xFF) * p_178203_8_) & 0xFF;
        int j = (int)((float)(p_178203_1_ & 0xFF) * p_178203_5_ + (float)(p_178203_2_ & 0xFF) * p_178203_6_ + (float)(p_178203_3_ & 0xFF) * p_178203_7_ + (float)(p_178203_4_ & 0xFF) * p_178203_8_) & 0xFF;
        return i << 16 | j;
    }

    static /* synthetic */ int[] access$000(BlockModelRenderer.AmbientOcclusionFace x0) {
        return x0.vertexBrightness;
    }

    static /* synthetic */ float[] access$100(BlockModelRenderer.AmbientOcclusionFace x0) {
        return x0.vertexColorMultiplier;
    }
}
