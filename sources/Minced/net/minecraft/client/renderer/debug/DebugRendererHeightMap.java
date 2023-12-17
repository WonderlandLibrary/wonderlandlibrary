// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.debug;

import java.util.Iterator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;

public class DebugRendererHeightMap implements DebugRenderer.IDebugRenderer
{
    private final Minecraft minecraft;
    
    public DebugRendererHeightMap(final Minecraft minecraftIn) {
        this.minecraft = minecraftIn;
    }
    
    @Override
    public void render(final float partialTicks, final long finishTimeNano) {
        final Minecraft minecraft = this.minecraft;
        final EntityPlayer entityplayer = Minecraft.player;
        final World world = this.minecraft.world;
        final double d0 = entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * partialTicks;
        final double d2 = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * partialTicks;
        final double d3 = entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * partialTicks;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.disableTexture2D();
        final BlockPos blockpos = new BlockPos(entityplayer.posX, 0.0, entityplayer.posZ);
        final Iterable<BlockPos> iterable = BlockPos.getAllInBox(blockpos.add(-40, 0, -40), blockpos.add(40, 0, 40));
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
        for (final BlockPos blockpos2 : iterable) {
            final int i = world.getHeight(blockpos2.getX(), blockpos2.getZ());
            if (world.getBlockState(blockpos2.add(0, i, 0).down()) == Blocks.AIR.getDefaultState()) {
                RenderGlobal.addChainedFilledBoxVertices(bufferbuilder, blockpos2.getX() + 0.25f - d0, i - d2, blockpos2.getZ() + 0.25f - d3, blockpos2.getX() + 0.75f - d0, i + 0.09375 - d2, blockpos2.getZ() + 0.75f - d3, 0.0f, 0.0f, 1.0f, 0.5f);
            }
            else {
                RenderGlobal.addChainedFilledBoxVertices(bufferbuilder, blockpos2.getX() + 0.25f - d0, i - d2, blockpos2.getZ() + 0.25f - d3, blockpos2.getX() + 0.75f - d0, i + 0.09375 - d2, blockpos2.getZ() + 0.75f - d3, 0.0f, 1.0f, 0.0f, 0.5f);
            }
        }
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
}
