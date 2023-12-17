// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.debug;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import java.util.Iterator;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.Minecraft;

public class DebugRendererSolidFace implements DebugRenderer.IDebugRenderer
{
    private final Minecraft minecraft;
    
    public DebugRendererSolidFace(final Minecraft minecraftIn) {
        this.minecraft = minecraftIn;
    }
    
    @Override
    public void render(final float partialTicks, final long finishTimeNano) {
        final Minecraft minecraft = this.minecraft;
        final EntityPlayer entityplayer = Minecraft.player;
        final double d0 = entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * partialTicks;
        final double d2 = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * partialTicks;
        final double d3 = entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * partialTicks;
        final Minecraft minecraft2 = this.minecraft;
        final World world = Minecraft.player.world;
        final Iterable<BlockPos> iterable = BlockPos.getAllInBox(MathHelper.floor(entityplayer.posX - 6.0), MathHelper.floor(entityplayer.posY - 6.0), MathHelper.floor(entityplayer.posZ - 6.0), MathHelper.floor(entityplayer.posX + 6.0), MathHelper.floor(entityplayer.posY + 6.0), MathHelper.floor(entityplayer.posZ + 6.0));
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(2.0f);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        for (final BlockPos blockpos : iterable) {
            final IBlockState iblockstate = world.getBlockState(blockpos);
            if (iblockstate.getBlock() != Blocks.AIR) {
                final AxisAlignedBB axisalignedbb = iblockstate.getSelectedBoundingBox(world, blockpos).grow(0.002).offset(-d0, -d2, -d3);
                final double d4 = axisalignedbb.minX;
                final double d5 = axisalignedbb.minY;
                final double d6 = axisalignedbb.minZ;
                final double d7 = axisalignedbb.maxX;
                final double d8 = axisalignedbb.maxY;
                final double d9 = axisalignedbb.maxZ;
                final float f = 1.0f;
                final float f2 = 0.0f;
                final float f3 = 0.0f;
                final float f4 = 0.5f;
                if (iblockstate.getBlockFaceShape(world, blockpos, EnumFacing.WEST) == BlockFaceShape.SOLID) {
                    final Tessellator tessellator = Tessellator.getInstance();
                    final BufferBuilder bufferbuilder = tessellator.getBuffer();
                    bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
                    bufferbuilder.pos(d4, d5, d6).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    bufferbuilder.pos(d4, d5, d9).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    bufferbuilder.pos(d4, d8, d6).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    bufferbuilder.pos(d4, d8, d9).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    tessellator.draw();
                }
                if (iblockstate.getBlockFaceShape(world, blockpos, EnumFacing.SOUTH) == BlockFaceShape.SOLID) {
                    final Tessellator tessellator2 = Tessellator.getInstance();
                    final BufferBuilder bufferbuilder2 = tessellator2.getBuffer();
                    bufferbuilder2.begin(5, DefaultVertexFormats.POSITION_COLOR);
                    bufferbuilder2.pos(d4, d8, d9).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    bufferbuilder2.pos(d4, d5, d9).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    bufferbuilder2.pos(d7, d8, d9).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    bufferbuilder2.pos(d7, d5, d9).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    tessellator2.draw();
                }
                if (iblockstate.getBlockFaceShape(world, blockpos, EnumFacing.EAST) == BlockFaceShape.SOLID) {
                    final Tessellator tessellator3 = Tessellator.getInstance();
                    final BufferBuilder bufferbuilder3 = tessellator3.getBuffer();
                    bufferbuilder3.begin(5, DefaultVertexFormats.POSITION_COLOR);
                    bufferbuilder3.pos(d7, d5, d9).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    bufferbuilder3.pos(d7, d5, d6).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    bufferbuilder3.pos(d7, d8, d9).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    bufferbuilder3.pos(d7, d8, d6).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    tessellator3.draw();
                }
                if (iblockstate.getBlockFaceShape(world, blockpos, EnumFacing.NORTH) == BlockFaceShape.SOLID) {
                    final Tessellator tessellator4 = Tessellator.getInstance();
                    final BufferBuilder bufferbuilder4 = tessellator4.getBuffer();
                    bufferbuilder4.begin(5, DefaultVertexFormats.POSITION_COLOR);
                    bufferbuilder4.pos(d7, d8, d6).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    bufferbuilder4.pos(d7, d5, d6).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    bufferbuilder4.pos(d4, d8, d6).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    bufferbuilder4.pos(d4, d5, d6).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    tessellator4.draw();
                }
                if (iblockstate.getBlockFaceShape(world, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID) {
                    final Tessellator tessellator5 = Tessellator.getInstance();
                    final BufferBuilder bufferbuilder5 = tessellator5.getBuffer();
                    bufferbuilder5.begin(5, DefaultVertexFormats.POSITION_COLOR);
                    bufferbuilder5.pos(d4, d5, d6).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    bufferbuilder5.pos(d7, d5, d6).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    bufferbuilder5.pos(d4, d5, d9).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    bufferbuilder5.pos(d7, d5, d9).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    tessellator5.draw();
                }
                if (iblockstate.getBlockFaceShape(world, blockpos, EnumFacing.UP) != BlockFaceShape.SOLID) {
                    continue;
                }
                final Tessellator tessellator6 = Tessellator.getInstance();
                final BufferBuilder bufferbuilder6 = tessellator6.getBuffer();
                bufferbuilder6.begin(5, DefaultVertexFormats.POSITION_COLOR);
                bufferbuilder6.pos(d4, d8, d6).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                bufferbuilder6.pos(d4, d8, d9).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                bufferbuilder6.pos(d7, d8, d6).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                bufferbuilder6.pos(d7, d8, d9).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                tessellator6.draw();
            }
        }
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
