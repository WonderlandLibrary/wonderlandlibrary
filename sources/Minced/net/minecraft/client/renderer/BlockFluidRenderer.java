// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import net.minecraft.block.Block;
import net.optifine.render.RenderEnv;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockSlab;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.EnumFacing;
import net.optifine.CustomColors;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockLiquid;
import net.optifine.shaders.SVertexBuilder;
import net.minecraft.src.Config;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.IBlockAccess;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.color.BlockColors;

public class BlockFluidRenderer
{
    private final BlockColors blockColors;
    private final TextureAtlasSprite[] atlasSpritesLava;
    private final TextureAtlasSprite[] atlasSpritesWater;
    private TextureAtlasSprite atlasSpriteWaterOverlay;
    
    public BlockFluidRenderer(final BlockColors blockColorsIn) {
        this.atlasSpritesLava = new TextureAtlasSprite[2];
        this.atlasSpritesWater = new TextureAtlasSprite[2];
        this.blockColors = blockColorsIn;
        this.initAtlasSprites();
    }
    
    protected void initAtlasSprites() {
        final TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
        this.atlasSpritesLava[0] = texturemap.getAtlasSprite("minecraft:blocks/lava_still");
        this.atlasSpritesLava[1] = texturemap.getAtlasSprite("minecraft:blocks/lava_flow");
        this.atlasSpritesWater[0] = texturemap.getAtlasSprite("minecraft:blocks/water_still");
        this.atlasSpritesWater[1] = texturemap.getAtlasSprite("minecraft:blocks/water_flow");
        this.atlasSpriteWaterOverlay = texturemap.getAtlasSprite("minecraft:blocks/water_overlay");
    }
    
    public boolean renderFluid(final IBlockAccess blockAccess, final IBlockState blockStateIn, final BlockPos blockPosIn, final BufferBuilder bufferBuilderIn) {
        boolean flag4;
        try {
            if (Config.isShaders()) {
                SVertexBuilder.pushEntity(blockStateIn, blockPosIn, blockAccess, bufferBuilderIn);
            }
            final BlockLiquid blockliquid = (BlockLiquid)blockStateIn.getBlock();
            final boolean flag = blockStateIn.getMaterial() == Material.LAVA;
            final TextureAtlasSprite[] atextureatlassprite = flag ? this.atlasSpritesLava : this.atlasSpritesWater;
            final RenderEnv renderenv = bufferBuilderIn.getRenderEnv(blockStateIn, blockPosIn);
            final int i = CustomColors.getFluidColor(blockAccess, blockStateIn, blockPosIn, renderenv);
            final float f = (i >> 16 & 0xFF) / 255.0f;
            final float f2 = (i >> 8 & 0xFF) / 255.0f;
            final float f3 = (i & 0xFF) / 255.0f;
            final boolean flag2 = blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.UP);
            final boolean flag3 = blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.DOWN);
            final boolean[] aboolean = renderenv.getBorderFlags();
            aboolean[0] = blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.NORTH);
            aboolean[1] = blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.SOUTH);
            aboolean[2] = blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.WEST);
            aboolean[3] = blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.EAST);
            if (flag2 || flag3 || aboolean[0] || aboolean[1] || aboolean[2] || aboolean[3]) {
                flag4 = false;
                final float f4 = 0.5f;
                final float f5 = 1.0f;
                final float f6 = 0.8f;
                final float f7 = 0.6f;
                final Material material = blockStateIn.getMaterial();
                float f8 = this.getFluidHeight(blockAccess, blockPosIn, material);
                float f9 = this.getFluidHeight(blockAccess, blockPosIn.south(), material);
                float f10 = this.getFluidHeight(blockAccess, blockPosIn.east().south(), material);
                float f11 = this.getFluidHeight(blockAccess, blockPosIn.east(), material);
                final double d0 = blockPosIn.getX();
                final double d2 = blockPosIn.getY();
                final double d3 = blockPosIn.getZ();
                final float f12 = 0.001f;
                if (flag2) {
                    flag4 = true;
                    final float f13 = BlockLiquid.getSlopeAngle(blockAccess, blockPosIn, material, blockStateIn);
                    final TextureAtlasSprite textureatlassprite = (f13 > -999.0f) ? atextureatlassprite[1] : atextureatlassprite[0];
                    bufferBuilderIn.setSprite(textureatlassprite);
                    f8 -= 0.001f;
                    f9 -= 0.001f;
                    f10 -= 0.001f;
                    f11 -= 0.001f;
                    float f14;
                    float f15;
                    float f16;
                    float f17;
                    float f18;
                    float f19;
                    float f20;
                    float f21;
                    if (f13 < -999.0f) {
                        f14 = textureatlassprite.getInterpolatedU(0.0);
                        f15 = textureatlassprite.getInterpolatedV(0.0);
                        f16 = f14;
                        f17 = textureatlassprite.getInterpolatedV(16.0);
                        f18 = textureatlassprite.getInterpolatedU(16.0);
                        f19 = f17;
                        f20 = f18;
                        f21 = f15;
                    }
                    else {
                        final float f22 = MathHelper.sin(f13) * 0.25f;
                        final float f23 = MathHelper.cos(f13) * 0.25f;
                        final float f24 = 8.0f;
                        f14 = textureatlassprite.getInterpolatedU(8.0f + (-f23 - f22) * 16.0f);
                        f15 = textureatlassprite.getInterpolatedV(8.0f + (-f23 + f22) * 16.0f);
                        f16 = textureatlassprite.getInterpolatedU(8.0f + (-f23 + f22) * 16.0f);
                        f17 = textureatlassprite.getInterpolatedV(8.0f + (f23 + f22) * 16.0f);
                        f18 = textureatlassprite.getInterpolatedU(8.0f + (f23 + f22) * 16.0f);
                        f19 = textureatlassprite.getInterpolatedV(8.0f + (f23 - f22) * 16.0f);
                        f20 = textureatlassprite.getInterpolatedU(8.0f + (f23 - f22) * 16.0f);
                        f21 = textureatlassprite.getInterpolatedV(8.0f + (-f23 - f22) * 16.0f);
                    }
                    final int k2 = blockStateIn.getPackedLightmapCoords(blockAccess, blockPosIn);
                    final int l2 = k2 >> 16 & 0xFFFF;
                    final int i2 = k2 & 0xFFFF;
                    final float f25 = 1.0f * f;
                    final float f26 = 1.0f * f2;
                    final float f27 = 1.0f * f3;
                    bufferBuilderIn.pos(d0 + 0.0, d2 + f8, d3 + 0.0).color(f25, f26, f27, 1.0f).tex(f14, f15).lightmap(l2, i2).endVertex();
                    bufferBuilderIn.pos(d0 + 0.0, d2 + f9, d3 + 1.0).color(f25, f26, f27, 1.0f).tex(f16, f17).lightmap(l2, i2).endVertex();
                    bufferBuilderIn.pos(d0 + 1.0, d2 + f10, d3 + 1.0).color(f25, f26, f27, 1.0f).tex(f18, f19).lightmap(l2, i2).endVertex();
                    bufferBuilderIn.pos(d0 + 1.0, d2 + f11, d3 + 0.0).color(f25, f26, f27, 1.0f).tex(f20, f21).lightmap(l2, i2).endVertex();
                    if (blockliquid.shouldRenderSides(blockAccess, blockPosIn.up())) {
                        bufferBuilderIn.pos(d0 + 0.0, d2 + f8, d3 + 0.0).color(f25, f26, f27, 1.0f).tex(f14, f15).lightmap(l2, i2).endVertex();
                        bufferBuilderIn.pos(d0 + 1.0, d2 + f11, d3 + 0.0).color(f25, f26, f27, 1.0f).tex(f20, f21).lightmap(l2, i2).endVertex();
                        bufferBuilderIn.pos(d0 + 1.0, d2 + f10, d3 + 1.0).color(f25, f26, f27, 1.0f).tex(f18, f19).lightmap(l2, i2).endVertex();
                        bufferBuilderIn.pos(d0 + 0.0, d2 + f9, d3 + 1.0).color(f25, f26, f27, 1.0f).tex(f16, f17).lightmap(l2, i2).endVertex();
                    }
                }
                if (flag3) {
                    bufferBuilderIn.setSprite(atextureatlassprite[0]);
                    final float f28 = atextureatlassprite[0].getMinU();
                    final float f29 = atextureatlassprite[0].getMaxU();
                    final float f30 = atextureatlassprite[0].getMinV();
                    final float f31 = atextureatlassprite[0].getMaxV();
                    final int l3 = blockStateIn.getPackedLightmapCoords(blockAccess, blockPosIn.down());
                    final int i3 = l3 >> 16 & 0xFFFF;
                    final int j2 = l3 & 0xFFFF;
                    final float f32 = FaceBakery.getFaceBrightness(EnumFacing.DOWN);
                    bufferBuilderIn.pos(d0, d2, d3 + 1.0).color(f * f32, f2 * f32, f3 * f32, 1.0f).tex(f28, f31).lightmap(i3, j2).endVertex();
                    bufferBuilderIn.pos(d0, d2, d3).color(f * f32, f2 * f32, f3 * f32, 1.0f).tex(f28, f30).lightmap(i3, j2).endVertex();
                    bufferBuilderIn.pos(d0 + 1.0, d2, d3).color(f * f32, f2 * f32, f3 * f32, 1.0f).tex(f29, f30).lightmap(i3, j2).endVertex();
                    bufferBuilderIn.pos(d0 + 1.0, d2, d3 + 1.0).color(f * f32, f2 * f32, f3 * f32, 1.0f).tex(f29, f31).lightmap(i3, j2).endVertex();
                    flag4 = true;
                }
                for (int i4 = 0; i4 < 4; ++i4) {
                    int j3 = 0;
                    int k3 = 0;
                    if (i4 == 0) {
                        --k3;
                    }
                    if (i4 == 1) {
                        ++k3;
                    }
                    if (i4 == 2) {
                        --j3;
                    }
                    if (i4 == 3) {
                        ++j3;
                    }
                    final BlockPos blockpos = blockPosIn.add(j3, 0, k3);
                    TextureAtlasSprite textureatlassprite2 = atextureatlassprite[1];
                    bufferBuilderIn.setSprite(textureatlassprite2);
                    float f33 = 0.0f;
                    float f34 = 0.0f;
                    if (!flag) {
                        final IBlockState iblockstate = blockAccess.getBlockState(blockpos);
                        final Block block = iblockstate.getBlock();
                        if (block == Blocks.GLASS || block == Blocks.STAINED_GLASS || block == Blocks.BEACON || block == Blocks.SLIME_BLOCK) {
                            textureatlassprite2 = this.atlasSpriteWaterOverlay;
                            bufferBuilderIn.setSprite(textureatlassprite2);
                        }
                        if (block == Blocks.FARMLAND || block == Blocks.GRASS_PATH) {
                            f33 = 0.9375f;
                            f34 = 0.9375f;
                        }
                        if (block instanceof BlockSlab) {
                            final BlockSlab blockslab = (BlockSlab)block;
                            if (!blockslab.isDouble() && iblockstate.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.BOTTOM) {
                                f33 = 0.5f;
                                f34 = 0.5f;
                            }
                        }
                    }
                    if (aboolean[i4]) {
                        float f35;
                        float f36;
                        double d4;
                        double d5;
                        double d6;
                        double d7;
                        if (i4 == 0) {
                            f35 = f8;
                            f36 = f11;
                            d4 = d0;
                            d5 = d0 + 1.0;
                            d6 = d3 + 0.0010000000474974513;
                            d7 = d3 + 0.0010000000474974513;
                        }
                        else if (i4 == 1) {
                            f35 = f10;
                            f36 = f9;
                            d4 = d0 + 1.0;
                            d5 = d0;
                            d6 = d3 + 1.0 - 0.0010000000474974513;
                            d7 = d3 + 1.0 - 0.0010000000474974513;
                        }
                        else if (i4 == 2) {
                            f35 = f9;
                            f36 = f8;
                            d4 = d0 + 0.0010000000474974513;
                            d5 = d0 + 0.0010000000474974513;
                            d6 = d3 + 1.0;
                            d7 = d3;
                        }
                        else {
                            f35 = f11;
                            f36 = f10;
                            d4 = d0 + 1.0 - 0.0010000000474974513;
                            d5 = d0 + 1.0 - 0.0010000000474974513;
                            d6 = d3;
                            d7 = d3 + 1.0;
                        }
                        if (f35 > f33 || f36 > f34) {
                            f33 = Math.min(f33, f35);
                            f34 = Math.min(f34, f36);
                            if (f33 > f12) {
                                f33 -= f12;
                            }
                            if (f34 > f12) {
                                f34 -= f12;
                            }
                            flag4 = true;
                            final float f37 = textureatlassprite2.getInterpolatedU(0.0);
                            final float f38 = textureatlassprite2.getInterpolatedU(8.0);
                            final float f39 = textureatlassprite2.getInterpolatedV((1.0f - f35) * 16.0f * 0.5f);
                            final float f40 = textureatlassprite2.getInterpolatedV((1.0f - f36) * 16.0f * 0.5f);
                            final float f41 = textureatlassprite2.getInterpolatedV(8.0);
                            final float f42 = textureatlassprite2.getInterpolatedV((1.0f - f33) * 16.0f * 0.5f);
                            final float f43 = textureatlassprite2.getInterpolatedV((1.0f - f34) * 16.0f * 0.5f);
                            final int m = blockStateIn.getPackedLightmapCoords(blockAccess, blockpos);
                            final int k4 = m >> 16 & 0xFFFF;
                            final int l4 = m & 0xFFFF;
                            final float f44 = (i4 < 2) ? FaceBakery.getFaceBrightness(EnumFacing.NORTH) : FaceBakery.getFaceBrightness(EnumFacing.WEST);
                            final float f45 = 1.0f * f44 * f;
                            final float f46 = 1.0f * f44 * f2;
                            final float f47 = 1.0f * f44 * f3;
                            bufferBuilderIn.pos(d4, d2 + f35, d6).color(f45, f46, f47, 1.0f).tex(f37, f39).lightmap(k4, l4).endVertex();
                            bufferBuilderIn.pos(d5, d2 + f36, d7).color(f45, f46, f47, 1.0f).tex(f38, f40).lightmap(k4, l4).endVertex();
                            bufferBuilderIn.pos(d5, d2 + f34, d7).color(f45, f46, f47, 1.0f).tex(f38, f43).lightmap(k4, l4).endVertex();
                            bufferBuilderIn.pos(d4, d2 + f33, d6).color(f45, f46, f47, 1.0f).tex(f37, f42).lightmap(k4, l4).endVertex();
                            if (textureatlassprite2 != this.atlasSpriteWaterOverlay) {
                                bufferBuilderIn.pos(d4, d2 + f33, d6).color(f45, f46, f47, 1.0f).tex(f37, f42).lightmap(k4, l4).endVertex();
                                bufferBuilderIn.pos(d5, d2 + f34, d7).color(f45, f46, f47, 1.0f).tex(f38, f43).lightmap(k4, l4).endVertex();
                                bufferBuilderIn.pos(d5, d2 + f36, d7).color(f45, f46, f47, 1.0f).tex(f38, f40).lightmap(k4, l4).endVertex();
                                bufferBuilderIn.pos(d4, d2 + f35, d6).color(f45, f46, f47, 1.0f).tex(f37, f39).lightmap(k4, l4).endVertex();
                            }
                        }
                    }
                }
                bufferBuilderIn.setSprite(null);
                final boolean flag5 = flag4;
                return flag5;
            }
            flag4 = false;
        }
        finally {
            if (Config.isShaders()) {
                SVertexBuilder.popEntity(bufferBuilderIn);
            }
        }
        return flag4;
    }
    
    private float getFluidHeight(final IBlockAccess blockAccess, final BlockPos blockPosIn, final Material blockMaterial) {
        int i = 0;
        float f = 0.0f;
        for (int j = 0; j < 4; ++j) {
            final BlockPos blockpos = blockPosIn.add(-(j & 0x1), 0, -(j >> 1 & 0x1));
            if (blockAccess.getBlockState(blockpos.up()).getMaterial() == blockMaterial) {
                return 1.0f;
            }
            final IBlockState iblockstate = blockAccess.getBlockState(blockpos);
            final Material material = iblockstate.getMaterial();
            if (material != blockMaterial) {
                if (!material.isSolid()) {
                    ++f;
                    ++i;
                }
            }
            else {
                final int k = iblockstate.getValue((IProperty<Integer>)BlockLiquid.LEVEL);
                if (k >= 8 || k == 0) {
                    f += BlockLiquid.getLiquidHeightPercent(k) * 10.0f;
                    i += 10;
                }
                f += BlockLiquid.getLiquidHeightPercent(k);
                ++i;
            }
        }
        return 1.0f - f / i;
    }
}
