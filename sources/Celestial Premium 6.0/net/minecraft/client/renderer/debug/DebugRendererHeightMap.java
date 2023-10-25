/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.debug;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class DebugRendererHeightMap
implements DebugRenderer.IDebugRenderer {
    private final Minecraft minecraft;

    public DebugRendererHeightMap(Minecraft minecraftIn) {
        this.minecraft = minecraftIn;
    }

    @Override
    public void render(float p_190060_1_, long p_190060_2_) {
        EntityPlayerSP entityplayer = this.minecraft.player;
        WorldClient world = this.minecraft.world;
        double d0 = entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * (double)p_190060_1_;
        double d1 = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * (double)p_190060_1_;
        double d2 = entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * (double)p_190060_1_;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.disableTexture2D();
        BlockPos blockpos = new BlockPos(entityplayer.posX, 0.0, entityplayer.posZ);
        Iterable<BlockPos> iterable = BlockPos.getAllInBox(blockpos.add(-40, 0, -40), blockpos.add(40, 0, 40));
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
        for (BlockPos blockpos1 : iterable) {
            int i;
            if (world.getBlockState(blockpos1.add(0, i = world.getHeight(blockpos1.getX(), blockpos1.getZ()), 0).down()) == Blocks.AIR.getDefaultState()) {
                RenderGlobal.addChainedFilledBoxVertices(bufferbuilder, (double)((float)blockpos1.getX() + 0.25f) - d0, (double)i - d1, (double)((float)blockpos1.getZ() + 0.25f) - d2, (double)((float)blockpos1.getX() + 0.75f) - d0, (double)i + 0.09375 - d1, (double)((float)blockpos1.getZ() + 0.75f) - d2, 0.0f, 0.0f, 1.0f, 0.5f);
                continue;
            }
            RenderGlobal.addChainedFilledBoxVertices(bufferbuilder, (double)((float)blockpos1.getX() + 0.25f) - d0, (double)i - d1, (double)((float)blockpos1.getZ() + 0.25f) - d2, (double)((float)blockpos1.getX() + 0.75f) - d0, (double)i + 0.09375 - d1, (double)((float)blockpos1.getZ() + 0.75f) - d2, 0.0f, 1.0f, 0.0f, 0.5f);
        }
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
}

