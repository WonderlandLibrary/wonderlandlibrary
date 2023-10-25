/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public class BlockFluidRenderer {
    private TextureAtlasSprite[] atlasSpritesLava = new TextureAtlasSprite[2];
    private TextureAtlasSprite[] atlasSpritesWater = new TextureAtlasSprite[2];

    protected void initAtlasSprites() {
        TextureMap textureMap = Minecraft.getMinecraft().getTextureMapBlocks();
        this.atlasSpritesLava[0] = textureMap.getAtlasSprite("minecraft:blocks/lava_still");
        this.atlasSpritesLava[1] = textureMap.getAtlasSprite("minecraft:blocks/lava_flow");
        this.atlasSpritesWater[0] = textureMap.getAtlasSprite("minecraft:blocks/water_still");
        this.atlasSpritesWater[1] = textureMap.getAtlasSprite("minecraft:blocks/water_flow");
    }

    public boolean renderFluid(IBlockAccess iBlockAccess, IBlockState iBlockState, BlockPos blockPos, WorldRenderer worldRenderer) {
        float f;
        float f2;
        float f3;
        float f4;
        BlockLiquid blockLiquid = (BlockLiquid)iBlockState.getBlock();
        blockLiquid.setBlockBoundsBasedOnState(iBlockAccess, blockPos);
        TextureAtlasSprite[] textureAtlasSpriteArray = blockLiquid.getMaterial() == Material.lava ? this.atlasSpritesLava : this.atlasSpritesWater;
        int n = blockLiquid.colorMultiplier(iBlockAccess, blockPos);
        float f5 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(n & 0xFF) / 255.0f;
        boolean bl = blockLiquid.shouldSideBeRendered(iBlockAccess, blockPos.up(), EnumFacing.UP);
        boolean bl2 = blockLiquid.shouldSideBeRendered(iBlockAccess, blockPos.down(), EnumFacing.DOWN);
        boolean[] blArray = new boolean[]{blockLiquid.shouldSideBeRendered(iBlockAccess, blockPos.north(), EnumFacing.NORTH), blockLiquid.shouldSideBeRendered(iBlockAccess, blockPos.south(), EnumFacing.SOUTH), blockLiquid.shouldSideBeRendered(iBlockAccess, blockPos.west(), EnumFacing.WEST), blockLiquid.shouldSideBeRendered(iBlockAccess, blockPos.east(), EnumFacing.EAST)};
        if (!(bl || bl2 || blArray[0] || blArray[1] || blArray[2] || blArray[3])) {
            return false;
        }
        boolean bl3 = false;
        float f8 = 0.5f;
        float f9 = 1.0f;
        float f10 = 0.8f;
        float f11 = 0.6f;
        Material material = blockLiquid.getMaterial();
        float f12 = this.getFluidHeight(iBlockAccess, blockPos, material);
        float f13 = this.getFluidHeight(iBlockAccess, blockPos.south(), material);
        float f14 = this.getFluidHeight(iBlockAccess, blockPos.east().south(), material);
        float f15 = this.getFluidHeight(iBlockAccess, blockPos.east(), material);
        double d = blockPos.getX();
        double d2 = blockPos.getY();
        double d3 = blockPos.getZ();
        float f16 = 0.001f;
        if (bl) {
            float f17;
            float f18;
            float f19;
            float f20;
            float f21;
            float f22;
            bl3 = true;
            TextureAtlasSprite textureAtlasSprite = textureAtlasSpriteArray[0];
            f4 = (float)BlockLiquid.getFlowDirection(iBlockAccess, blockPos, material);
            if (f4 > -999.0f) {
                textureAtlasSprite = textureAtlasSpriteArray[1];
            }
            f12 -= f16;
            f13 -= f16;
            f14 -= f16;
            f15 -= f16;
            if (f4 < -999.0f) {
                f3 = textureAtlasSprite.getInterpolatedU(0.0);
                f22 = textureAtlasSprite.getInterpolatedV(0.0);
                f2 = f3;
                f21 = textureAtlasSprite.getInterpolatedV(16.0);
                f20 = textureAtlasSprite.getInterpolatedU(16.0);
                f19 = f21;
                f18 = f20;
                f17 = f22;
            } else {
                float f23 = MathHelper.sin(f4) * 0.25f;
                float f24 = MathHelper.cos(f4) * 0.25f;
                float f25 = 8.0f;
                f3 = textureAtlasSprite.getInterpolatedU(8.0f + (-f24 - f23) * 16.0f);
                f22 = textureAtlasSprite.getInterpolatedV(8.0f + (-f24 + f23) * 16.0f);
                f2 = textureAtlasSprite.getInterpolatedU(8.0f + (-f24 + f23) * 16.0f);
                f21 = textureAtlasSprite.getInterpolatedV(8.0f + (f24 + f23) * 16.0f);
                f20 = textureAtlasSprite.getInterpolatedU(8.0f + (f24 + f23) * 16.0f);
                f19 = textureAtlasSprite.getInterpolatedV(8.0f + (f24 - f23) * 16.0f);
                f18 = textureAtlasSprite.getInterpolatedU(8.0f + (f24 - f23) * 16.0f);
                f17 = textureAtlasSprite.getInterpolatedV(8.0f + (-f24 - f23) * 16.0f);
            }
            int n2 = blockLiquid.getMixedBrightnessForBlock(iBlockAccess, blockPos);
            int n3 = n2 >> 16 & 0xFFFF;
            int n4 = n2 & 0xFFFF;
            float f26 = f9 * f5;
            float f27 = f9 * f6;
            f = f9 * f7;
            worldRenderer.pos(d + 0.0, d2 + (double)f12, d3 + 0.0).color(f26, f27, f, 1.0f).tex(f3, f22).lightmap(n3, n4).endVertex();
            worldRenderer.pos(d + 0.0, d2 + (double)f13, d3 + 1.0).color(f26, f27, f, 1.0f).tex(f2, f21).lightmap(n3, n4).endVertex();
            worldRenderer.pos(d + 1.0, d2 + (double)f14, d3 + 1.0).color(f26, f27, f, 1.0f).tex(f20, f19).lightmap(n3, n4).endVertex();
            worldRenderer.pos(d + 1.0, d2 + (double)f15, d3 + 0.0).color(f26, f27, f, 1.0f).tex(f18, f17).lightmap(n3, n4).endVertex();
            if (blockLiquid.func_176364_g(iBlockAccess, blockPos.up())) {
                worldRenderer.pos(d + 0.0, d2 + (double)f12, d3 + 0.0).color(f26, f27, f, 1.0f).tex(f3, f22).lightmap(n3, n4).endVertex();
                worldRenderer.pos(d + 1.0, d2 + (double)f15, d3 + 0.0).color(f26, f27, f, 1.0f).tex(f18, f17).lightmap(n3, n4).endVertex();
                worldRenderer.pos(d + 1.0, d2 + (double)f14, d3 + 1.0).color(f26, f27, f, 1.0f).tex(f20, f19).lightmap(n3, n4).endVertex();
                worldRenderer.pos(d + 0.0, d2 + (double)f13, d3 + 1.0).color(f26, f27, f, 1.0f).tex(f2, f21).lightmap(n3, n4).endVertex();
            }
        }
        if (bl2) {
            float f28 = textureAtlasSpriteArray[0].getMinU();
            f4 = textureAtlasSpriteArray[0].getMaxU();
            f3 = textureAtlasSpriteArray[0].getMinV();
            f2 = textureAtlasSpriteArray[0].getMaxV();
            int n5 = blockLiquid.getMixedBrightnessForBlock(iBlockAccess, blockPos.down());
            int n6 = n5 >> 16 & 0xFFFF;
            int n7 = n5 & 0xFFFF;
            worldRenderer.pos(d, d2, d3 + 1.0).color(f8, f8, f8, 1.0f).tex(f28, f2).lightmap(n6, n7).endVertex();
            worldRenderer.pos(d, d2, d3).color(f8, f8, f8, 1.0f).tex(f28, f3).lightmap(n6, n7).endVertex();
            worldRenderer.pos(d + 1.0, d2, d3).color(f8, f8, f8, 1.0f).tex(f4, f3).lightmap(n6, n7).endVertex();
            worldRenderer.pos(d + 1.0, d2, d3 + 1.0).color(f8, f8, f8, 1.0f).tex(f4, f2).lightmap(n6, n7).endVertex();
            bl3 = true;
        }
        int n8 = 0;
        while (n8 < 4) {
            int n9 = 0;
            int n10 = 0;
            if (n8 == 0) {
                --n10;
            }
            if (n8 == 1) {
                ++n10;
            }
            if (n8 == 2) {
                --n9;
            }
            if (n8 == 3) {
                ++n9;
            }
            BlockPos blockPos2 = blockPos.add(n9, 0, n10);
            TextureAtlasSprite textureAtlasSprite = textureAtlasSpriteArray[1];
            if (blArray[n8]) {
                double d4;
                double d5;
                double d6;
                double d7;
                float f29;
                float f30;
                if (n8 == 0) {
                    f30 = f12;
                    f29 = f15;
                    d7 = d;
                    d6 = d + 1.0;
                    d5 = d3 + (double)f16;
                    d4 = d3 + (double)f16;
                } else if (n8 == 1) {
                    f30 = f14;
                    f29 = f13;
                    d7 = d + 1.0;
                    d6 = d;
                    d5 = d3 + 1.0 - (double)f16;
                    d4 = d3 + 1.0 - (double)f16;
                } else if (n8 == 2) {
                    f30 = f13;
                    f29 = f12;
                    d7 = d + (double)f16;
                    d6 = d + (double)f16;
                    d5 = d3 + 1.0;
                    d4 = d3;
                } else {
                    f30 = f15;
                    f29 = f14;
                    d7 = d + 1.0 - (double)f16;
                    d6 = d + 1.0 - (double)f16;
                    d5 = d3;
                    d4 = d3 + 1.0;
                }
                bl3 = true;
                f = textureAtlasSprite.getInterpolatedU(0.0);
                float f31 = textureAtlasSprite.getInterpolatedU(8.0);
                float f32 = textureAtlasSprite.getInterpolatedV((1.0f - f30) * 16.0f * 0.5f);
                float f33 = textureAtlasSprite.getInterpolatedV((1.0f - f29) * 16.0f * 0.5f);
                float f34 = textureAtlasSprite.getInterpolatedV(8.0);
                int n11 = blockLiquid.getMixedBrightnessForBlock(iBlockAccess, blockPos2);
                int n12 = n11 >> 16 & 0xFFFF;
                int n13 = n11 & 0xFFFF;
                float f35 = n8 < 2 ? f10 : f11;
                float f36 = f9 * f35 * f5;
                float f37 = f9 * f35 * f6;
                float f38 = f9 * f35 * f7;
                worldRenderer.pos(d7, d2 + (double)f30, d5).color(f36, f37, f38, 1.0f).tex(f, f32).lightmap(n12, n13).endVertex();
                worldRenderer.pos(d6, d2 + (double)f29, d4).color(f36, f37, f38, 1.0f).tex(f31, f33).lightmap(n12, n13).endVertex();
                worldRenderer.pos(d6, d2 + 0.0, d4).color(f36, f37, f38, 1.0f).tex(f31, f34).lightmap(n12, n13).endVertex();
                worldRenderer.pos(d7, d2 + 0.0, d5).color(f36, f37, f38, 1.0f).tex(f, f34).lightmap(n12, n13).endVertex();
                worldRenderer.pos(d7, d2 + 0.0, d5).color(f36, f37, f38, 1.0f).tex(f, f34).lightmap(n12, n13).endVertex();
                worldRenderer.pos(d6, d2 + 0.0, d4).color(f36, f37, f38, 1.0f).tex(f31, f34).lightmap(n12, n13).endVertex();
                worldRenderer.pos(d6, d2 + (double)f29, d4).color(f36, f37, f38, 1.0f).tex(f31, f33).lightmap(n12, n13).endVertex();
                worldRenderer.pos(d7, d2 + (double)f30, d5).color(f36, f37, f38, 1.0f).tex(f, f32).lightmap(n12, n13).endVertex();
            }
            ++n8;
        }
        return bl3;
    }

    private float getFluidHeight(IBlockAccess iBlockAccess, BlockPos blockPos, Material material) {
        int n = 0;
        float f = 0.0f;
        int n2 = 0;
        while (n2 < 4) {
            BlockPos blockPos2 = blockPos.add(-(n2 & 1), 0, -(n2 >> 1 & 1));
            if (iBlockAccess.getBlockState(blockPos2.up()).getBlock().getMaterial() == material) {
                return 1.0f;
            }
            IBlockState iBlockState = iBlockAccess.getBlockState(blockPos2);
            Material material2 = iBlockState.getBlock().getMaterial();
            if (material2 != material) {
                if (!material2.isSolid()) {
                    f += 1.0f;
                    ++n;
                }
            } else {
                int n3 = iBlockState.getValue(BlockLiquid.LEVEL);
                if (n3 >= 8 || n3 == 0) {
                    f += BlockLiquid.getLiquidHeightPercent(n3) * 10.0f;
                    n += 10;
                }
                f += BlockLiquid.getLiquidHeightPercent(n3);
                ++n;
            }
            ++n2;
        }
        return 1.0f - f / (float)n;
    }

    public BlockFluidRenderer() {
        this.initAtlasSprites();
    }
}

