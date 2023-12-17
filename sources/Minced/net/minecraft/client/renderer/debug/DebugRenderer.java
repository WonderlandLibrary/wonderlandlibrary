// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.debug;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;

public class DebugRenderer
{
    public final IDebugRenderer pathfinding;
    public final IDebugRenderer water;
    public final IDebugRenderer chunkBorder;
    public final IDebugRenderer heightMap;
    public final IDebugRenderer collisionBox;
    public final IDebugRenderer neighborsUpdate;
    public final IDebugRenderer solidFace;
    private boolean chunkBorderEnabled;
    private boolean pathfindingEnabled;
    private boolean waterEnabled;
    private boolean heightMapEnabled;
    private boolean collisionBoxEnabled;
    private boolean neighborsUpdateEnabled;
    private boolean solidFaceEnabled;
    
    public DebugRenderer(final Minecraft clientIn) {
        this.pathfinding = new DebugRendererPathfinding(clientIn);
        this.water = new DebugRendererWater(clientIn);
        this.chunkBorder = new DebugRendererChunkBorder(clientIn);
        this.heightMap = new DebugRendererHeightMap(clientIn);
        this.collisionBox = new DebugRendererCollisionBox(clientIn);
        this.neighborsUpdate = new DebugRendererNeighborsUpdate(clientIn);
        this.solidFace = new DebugRendererSolidFace(clientIn);
    }
    
    public boolean shouldRender() {
        return this.chunkBorderEnabled || this.pathfindingEnabled || this.waterEnabled || this.heightMapEnabled || this.collisionBoxEnabled || this.neighborsUpdateEnabled || this.solidFaceEnabled;
    }
    
    public boolean toggleChunkBorders() {
        return this.chunkBorderEnabled = !this.chunkBorderEnabled;
    }
    
    public void renderDebug(final float partialTicks, final long finishTimeNano) {
        if (this.pathfindingEnabled) {
            this.pathfinding.render(partialTicks, finishTimeNano);
        }
        if (this.chunkBorderEnabled && !Minecraft.getMinecraft().isReducedDebug()) {
            this.chunkBorder.render(partialTicks, finishTimeNano);
        }
        if (this.waterEnabled) {
            this.water.render(partialTicks, finishTimeNano);
        }
        if (this.heightMapEnabled) {
            this.heightMap.render(partialTicks, finishTimeNano);
        }
        if (this.collisionBoxEnabled) {
            this.collisionBox.render(partialTicks, finishTimeNano);
        }
        if (this.neighborsUpdateEnabled) {
            this.neighborsUpdate.render(partialTicks, finishTimeNano);
        }
        if (this.solidFaceEnabled) {
            this.solidFace.render(partialTicks, finishTimeNano);
        }
    }
    
    public static void renderDebugText(final String str, final int x, final int y, final int z, final float partialTicks, final int color) {
        renderDebugText(str, x + 0.5, y + 0.5, z + 0.5, partialTicks, color);
    }
    
    public static void renderDebugText(final String str, final double x, final double y, final double z, final float partialTicks, final int color) {
        final Minecraft minecraft = Minecraft.getMinecraft();
        if (Minecraft.player != null && minecraft.getRenderManager() != null && minecraft.getRenderManager().options != null) {
            final FontRenderer fontrenderer = minecraft.fontRenderer;
            final EntityPlayer entityplayer = Minecraft.player;
            final double d0 = entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * partialTicks;
            final double d2 = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * partialTicks;
            final double d3 = entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * partialTicks;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(x - d0), (float)(y - d2) + 0.07f, (float)(z - d3));
            GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
            GlStateManager.scale(0.02f, -0.02f, 0.02f);
            final RenderManager rendermanager = minecraft.getRenderManager();
            GlStateManager.rotate(-rendermanager.playerViewY, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(((rendermanager.options.thirdPersonView == 2) ? 1 : -1) * rendermanager.playerViewX, 1.0f, 0.0f, 0.0f);
            GlStateManager.disableLighting();
            GlStateManager.enableTexture2D();
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.scale(-1.0f, 1.0f, 1.0f);
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, 0, color);
            GlStateManager.enableLighting();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }
    
    public interface IDebugRenderer
    {
        void render(final float p0, final long p1);
    }
}
