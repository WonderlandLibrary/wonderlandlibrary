// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.debug;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.src.Config;
import net.optifine.shaders.Shaders;
import net.minecraft.client.Minecraft;

public class DebugRendererChunkBorder implements DebugRenderer.IDebugRenderer
{
    private final Minecraft minecraft;
    
    public DebugRendererChunkBorder(final Minecraft minecraftIn) {
        this.minecraft = minecraftIn;
    }
    
    @Override
    public void render(final float partialTicks, final long finishTimeNano) {
        if (!Shaders.isShadowPass) {
            if (Config.isShaders()) {
                Shaders.beginLeash();
            }
            final Minecraft minecraft = this.minecraft;
            final EntityPlayer entityplayer = Minecraft.player;
            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder bufferbuilder = tessellator.getBuffer();
            final double d0 = entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * partialTicks;
            final double d2 = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * partialTicks;
            final double d3 = entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * partialTicks;
            final double d4 = 0.0 - d2;
            final double d5 = 256.0 - d2;
            GlStateManager.disableTexture2D();
            GlStateManager.disableBlend();
            final double d6 = (entityplayer.chunkCoordX << 4) - d0;
            final double d7 = (entityplayer.chunkCoordZ << 4) - d3;
            GlStateManager.glLineWidth(1.0f);
            bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
            for (int i = -16; i <= 32; i += 16) {
                for (int j = -16; j <= 32; j += 16) {
                    bufferbuilder.pos(d6 + i, d4, d7 + j).color(1.0f, 0.0f, 0.0f, 0.0f).endVertex();
                    bufferbuilder.pos(d6 + i, d4, d7 + j).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    bufferbuilder.pos(d6 + i, d5, d7 + j).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    bufferbuilder.pos(d6 + i, d5, d7 + j).color(1.0f, 0.0f, 0.0f, 0.0f).endVertex();
                }
            }
            for (int k = 2; k < 16; k += 2) {
                bufferbuilder.pos(d6 + k, d4, d7).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
                bufferbuilder.pos(d6 + k, d4, d7).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
                bufferbuilder.pos(d6 + k, d5, d7).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
                bufferbuilder.pos(d6 + k, d5, d7).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
                bufferbuilder.pos(d6 + k, d4, d7 + 16.0).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
                bufferbuilder.pos(d6 + k, d4, d7 + 16.0).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
                bufferbuilder.pos(d6 + k, d5, d7 + 16.0).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
                bufferbuilder.pos(d6 + k, d5, d7 + 16.0).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
            }
            for (int l = 2; l < 16; l += 2) {
                bufferbuilder.pos(d6, d4, d7 + l).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
                bufferbuilder.pos(d6, d4, d7 + l).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
                bufferbuilder.pos(d6, d5, d7 + l).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
                bufferbuilder.pos(d6, d5, d7 + l).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
                bufferbuilder.pos(d6 + 16.0, d4, d7 + l).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
                bufferbuilder.pos(d6 + 16.0, d4, d7 + l).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
                bufferbuilder.pos(d6 + 16.0, d5, d7 + l).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
                bufferbuilder.pos(d6 + 16.0, d5, d7 + l).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
            }
            for (int i2 = 0; i2 <= 256; i2 += 2) {
                final double d8 = i2 - d2;
                bufferbuilder.pos(d6, d8, d7).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
                bufferbuilder.pos(d6, d8, d7).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
                bufferbuilder.pos(d6, d8, d7 + 16.0).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
                bufferbuilder.pos(d6 + 16.0, d8, d7 + 16.0).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
                bufferbuilder.pos(d6 + 16.0, d8, d7).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
                bufferbuilder.pos(d6, d8, d7).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
                bufferbuilder.pos(d6, d8, d7).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
            }
            tessellator.draw();
            GlStateManager.glLineWidth(2.0f);
            bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
            for (int j2 = 0; j2 <= 16; j2 += 16) {
                for (int l2 = 0; l2 <= 16; l2 += 16) {
                    bufferbuilder.pos(d6 + j2, d4, d7 + l2).color(0.25f, 0.25f, 1.0f, 0.0f).endVertex();
                    bufferbuilder.pos(d6 + j2, d4, d7 + l2).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex();
                    bufferbuilder.pos(d6 + j2, d5, d7 + l2).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex();
                    bufferbuilder.pos(d6 + j2, d5, d7 + l2).color(0.25f, 0.25f, 1.0f, 0.0f).endVertex();
                }
            }
            for (int k2 = 0; k2 <= 256; k2 += 16) {
                final double d9 = k2 - d2;
                bufferbuilder.pos(d6, d9, d7).color(0.25f, 0.25f, 1.0f, 0.0f).endVertex();
                bufferbuilder.pos(d6, d9, d7).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex();
                bufferbuilder.pos(d6, d9, d7 + 16.0).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex();
                bufferbuilder.pos(d6 + 16.0, d9, d7 + 16.0).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex();
                bufferbuilder.pos(d6 + 16.0, d9, d7).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex();
                bufferbuilder.pos(d6, d9, d7).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex();
                bufferbuilder.pos(d6, d9, d7).color(0.25f, 0.25f, 1.0f, 0.0f).endVertex();
            }
            tessellator.draw();
            GlStateManager.glLineWidth(1.0f);
            GlStateManager.enableBlend();
            GlStateManager.enableTexture2D();
            if (Config.isShaders()) {
                Shaders.endLeash();
            }
        }
    }
}
