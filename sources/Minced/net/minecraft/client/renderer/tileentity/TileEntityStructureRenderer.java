// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.state.IBlockState;
import java.util.Iterator;
import net.minecraft.world.World;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3i;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntityStructure;

public class TileEntityStructureRenderer extends TileEntitySpecialRenderer<TileEntityStructure>
{
    @Override
    public void render(final TileEntityStructure te, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final float alpha) {
        Minecraft.getMinecraft();
        if (!Minecraft.player.canUseCommandBlock()) {
            Minecraft.getMinecraft();
            if (!Minecraft.player.isSpectator()) {
                return;
            }
        }
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        final BlockPos blockpos = te.getPosition();
        final BlockPos blockpos2 = te.getStructureSize();
        if (blockpos2.getX() >= 1 && blockpos2.getY() >= 1 && blockpos2.getZ() >= 1 && (te.getMode() == TileEntityStructure.Mode.SAVE || te.getMode() == TileEntityStructure.Mode.LOAD)) {
            final double d0 = 0.01;
            final double d2 = blockpos.getX();
            final double d3 = blockpos.getZ();
            final double d4 = y + blockpos.getY() - 0.01;
            final double d5 = d4 + blockpos2.getY() + 0.02;
            double d6 = 0.0;
            double d7 = 0.0;
            switch (te.getMirror()) {
                case LEFT_RIGHT: {
                    d6 = blockpos2.getX() + 0.02;
                    d7 = -(blockpos2.getZ() + 0.02);
                    break;
                }
                case FRONT_BACK: {
                    d6 = -(blockpos2.getX() + 0.02);
                    d7 = blockpos2.getZ() + 0.02;
                    break;
                }
                default: {
                    d6 = blockpos2.getX() + 0.02;
                    d7 = blockpos2.getZ() + 0.02;
                    break;
                }
            }
            double d8 = 0.0;
            double d9 = 0.0;
            double d10 = 0.0;
            double d11 = 0.0;
            switch (te.getRotation()) {
                case CLOCKWISE_90: {
                    d8 = x + ((d7 < 0.0) ? (d2 - 0.01) : (d2 + 1.0 + 0.01));
                    d9 = z + ((d6 < 0.0) ? (d3 + 1.0 + 0.01) : (d3 - 0.01));
                    d10 = d8 - d7;
                    d11 = d9 + d6;
                    break;
                }
                case CLOCKWISE_180: {
                    d8 = x + ((d6 < 0.0) ? (d2 - 0.01) : (d2 + 1.0 + 0.01));
                    d9 = z + ((d7 < 0.0) ? (d3 - 0.01) : (d3 + 1.0 + 0.01));
                    d10 = d8 - d6;
                    d11 = d9 - d7;
                    break;
                }
                case COUNTERCLOCKWISE_90: {
                    d8 = x + ((d7 < 0.0) ? (d2 + 1.0 + 0.01) : (d2 - 0.01));
                    d9 = z + ((d6 < 0.0) ? (d3 - 0.01) : (d3 + 1.0 + 0.01));
                    d10 = d8 + d7;
                    d11 = d9 - d6;
                    break;
                }
                default: {
                    d8 = x + ((d6 < 0.0) ? (d2 + 1.0 + 0.01) : (d2 - 0.01));
                    d9 = z + ((d7 < 0.0) ? (d3 + 1.0 + 0.01) : (d3 - 0.01));
                    d10 = d8 + d6;
                    d11 = d9 + d7;
                    break;
                }
            }
            final int i = 255;
            final int j = 223;
            final int k = 127;
            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder bufferbuilder = tessellator.getBuffer();
            GlStateManager.disableFog();
            GlStateManager.disableLighting();
            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            this.setLightmapDisabled(true);
            if (te.getMode() == TileEntityStructure.Mode.SAVE || te.showsBoundingBox()) {
                this.renderBox(tessellator, bufferbuilder, d8, d4, d9, d10, d5, d11, 255, 223, 127);
            }
            if (te.getMode() == TileEntityStructure.Mode.SAVE && te.showsAir()) {
                this.renderInvisibleBlocks(te, x, y, z, blockpos, tessellator, bufferbuilder, true);
                this.renderInvisibleBlocks(te, x, y, z, blockpos, tessellator, bufferbuilder, false);
            }
            this.setLightmapDisabled(false);
            GlStateManager.glLineWidth(1.0f);
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.enableFog();
        }
    }
    
    private void renderInvisibleBlocks(final TileEntityStructure p_190054_1_, final double p_190054_2_, final double p_190054_4_, final double p_190054_6_, final BlockPos p_190054_8_, final Tessellator p_190054_9_, final BufferBuilder p_190054_10_, final boolean p_190054_11_) {
        GlStateManager.glLineWidth(p_190054_11_ ? 3.0f : 1.0f);
        p_190054_10_.begin(3, DefaultVertexFormats.POSITION_COLOR);
        final World world = p_190054_1_.getWorld();
        final BlockPos blockpos = p_190054_1_.getPos();
        final BlockPos blockpos2 = blockpos.add(p_190054_8_);
        for (final BlockPos blockpos3 : BlockPos.getAllInBox(blockpos2, blockpos2.add(p_190054_1_.getStructureSize()).add(-1, -1, -1))) {
            final IBlockState iblockstate = world.getBlockState(blockpos3);
            final boolean flag = iblockstate == Blocks.AIR.getDefaultState();
            final boolean flag2 = iblockstate == Blocks.STRUCTURE_VOID.getDefaultState();
            if (flag || flag2) {
                final float f = flag ? 0.05f : 0.0f;
                final double d0 = blockpos3.getX() - blockpos.getX() + 0.45f + p_190054_2_ - f;
                final double d2 = blockpos3.getY() - blockpos.getY() + 0.45f + p_190054_4_ - f;
                final double d3 = blockpos3.getZ() - blockpos.getZ() + 0.45f + p_190054_6_ - f;
                final double d4 = blockpos3.getX() - blockpos.getX() + 0.55f + p_190054_2_ + f;
                final double d5 = blockpos3.getY() - blockpos.getY() + 0.55f + p_190054_4_ + f;
                final double d6 = blockpos3.getZ() - blockpos.getZ() + 0.55f + p_190054_6_ + f;
                if (p_190054_11_) {
                    RenderGlobal.drawBoundingBox(p_190054_10_, d0, d2, d3, d4, d5, d6, 0.0f, 0.0f, 0.0f, 1.0f);
                }
                else if (flag) {
                    RenderGlobal.drawBoundingBox(p_190054_10_, d0, d2, d3, d4, d5, d6, 0.5f, 0.5f, 1.0f, 1.0f);
                }
                else {
                    RenderGlobal.drawBoundingBox(p_190054_10_, d0, d2, d3, d4, d5, d6, 1.0f, 0.25f, 0.25f, 1.0f);
                }
            }
        }
        p_190054_9_.draw();
    }
    
    private void renderBox(final Tessellator p_190055_1_, final BufferBuilder p_190055_2_, final double p_190055_3_, final double p_190055_5_, final double p_190055_7_, final double p_190055_9_, final double p_190055_11_, final double p_190055_13_, final int p_190055_15_, final int p_190055_16_, final int p_190055_17_) {
        GlStateManager.glLineWidth(2.0f);
        p_190055_2_.begin(3, DefaultVertexFormats.POSITION_COLOR);
        p_190055_2_.pos(p_190055_3_, p_190055_5_, p_190055_7_).color((float)p_190055_16_, (float)p_190055_16_, (float)p_190055_16_, 0.0f).endVertex();
        p_190055_2_.pos(p_190055_3_, p_190055_5_, p_190055_7_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_5_, p_190055_7_).color(p_190055_16_, p_190055_17_, p_190055_17_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_5_, p_190055_13_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_3_, p_190055_5_, p_190055_13_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_3_, p_190055_5_, p_190055_7_).color(p_190055_17_, p_190055_17_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_3_, p_190055_11_, p_190055_7_).color(p_190055_17_, p_190055_16_, p_190055_17_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_11_, p_190055_7_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_11_, p_190055_13_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_3_, p_190055_11_, p_190055_13_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_3_, p_190055_11_, p_190055_7_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_3_, p_190055_11_, p_190055_13_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_3_, p_190055_5_, p_190055_13_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_5_, p_190055_13_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_11_, p_190055_13_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_11_, p_190055_7_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_5_, p_190055_7_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_5_, p_190055_7_).color((float)p_190055_16_, (float)p_190055_16_, (float)p_190055_16_, 0.0f).endVertex();
        p_190055_1_.draw();
        GlStateManager.glLineWidth(1.0f);
    }
    
    @Override
    public boolean isGlobalRenderer(final TileEntityStructure te) {
        return true;
    }
}
