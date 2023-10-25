/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.debug;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.debug.DebugRendererChunkBorder;
import net.minecraft.client.renderer.debug.DebugRendererCollisionBox;
import net.minecraft.client.renderer.debug.DebugRendererHeightMap;
import net.minecraft.client.renderer.debug.DebugRendererNeighborsUpdate;
import net.minecraft.client.renderer.debug.DebugRendererPathfinding;
import net.minecraft.client.renderer.debug.DebugRendererSolidFace;
import net.minecraft.client.renderer.debug.DebugRendererWater;
import net.minecraft.client.renderer.entity.RenderManager;

public class DebugRenderer {
    public final IDebugRenderer debugRendererPathfinding;
    public final IDebugRenderer debugRendererWater;
    public final IDebugRenderer debugRendererChunkBorder;
    public final IDebugRenderer debugRendererHeightMap;
    public final IDebugRenderer field_191325_e;
    public final IDebugRenderer field_191557_f;
    public final IDebugRenderer field_193852_g;
    private boolean chunkBordersEnabled;
    private boolean pathfindingEnabled;
    private boolean waterEnabled;
    private boolean heightmapEnabled;
    private boolean field_191326_j;
    private boolean field_191558_l;
    private boolean field_193853_n;

    public DebugRenderer(Minecraft clientIn) {
        this.debugRendererPathfinding = new DebugRendererPathfinding(clientIn);
        this.debugRendererWater = new DebugRendererWater(clientIn);
        this.debugRendererChunkBorder = new DebugRendererChunkBorder(clientIn);
        this.debugRendererHeightMap = new DebugRendererHeightMap(clientIn);
        this.field_191325_e = new DebugRendererCollisionBox(clientIn);
        this.field_191557_f = new DebugRendererNeighborsUpdate(clientIn);
        this.field_193852_g = new DebugRendererSolidFace(clientIn);
    }

    public boolean shouldRender() {
        return this.chunkBordersEnabled || this.pathfindingEnabled || this.waterEnabled || this.heightmapEnabled || this.field_191326_j || this.field_191558_l || this.field_193853_n;
    }

    public boolean toggleDebugScreen() {
        this.chunkBordersEnabled = !this.chunkBordersEnabled;
        return this.chunkBordersEnabled;
    }

    public void renderDebug(float partialTicks, long finishTimeNano) {
        if (this.pathfindingEnabled) {
            this.debugRendererPathfinding.render(partialTicks, finishTimeNano);
        }
        if (this.chunkBordersEnabled && !Minecraft.getMinecraft().isReducedDebug()) {
            this.debugRendererChunkBorder.render(partialTicks, finishTimeNano);
        }
        if (this.waterEnabled) {
            this.debugRendererWater.render(partialTicks, finishTimeNano);
        }
        if (this.heightmapEnabled) {
            this.debugRendererHeightMap.render(partialTicks, finishTimeNano);
        }
        if (this.field_191326_j) {
            this.field_191325_e.render(partialTicks, finishTimeNano);
        }
        if (this.field_191558_l) {
            this.field_191557_f.render(partialTicks, finishTimeNano);
        }
        if (this.field_193853_n) {
            this.field_193852_g.render(partialTicks, finishTimeNano);
        }
    }

    public static void func_191556_a(String p_191556_0_, int p_191556_1_, int p_191556_2_, int p_191556_3_, float p_191556_4_, int p_191556_5_) {
        DebugRenderer.renderDebugText(p_191556_0_, (double)p_191556_1_ + 0.5, (double)p_191556_2_ + 0.5, (double)p_191556_3_ + 0.5, p_191556_4_, p_191556_5_);
    }

    public static void renderDebugText(String str, double x, double y, double z, float partialTicks, int color) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.player != null && minecraft.getRenderManager() != null && minecraft.getRenderManager().options != null) {
            FontRenderer fontrenderer = minecraft.fontRendererObj;
            EntityPlayerSP entityplayer = minecraft.player;
            double d0 = entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * (double)partialTicks;
            double d1 = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * (double)partialTicks;
            double d2 = entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * (double)partialTicks;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(x - d0), (float)(y - d1) + 0.07f, (float)(z - d2));
            GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
            GlStateManager.scale(0.02f, -0.02f, 0.02f);
            RenderManager rendermanager = minecraft.getRenderManager();
            GlStateManager.rotate(-rendermanager.playerViewY, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate((float)(rendermanager.options.thirdPersonView == 2 ? 1 : -1) * rendermanager.playerViewX, 1.0f, 0.0f, 0.0f);
            GlStateManager.disableLighting();
            GlStateManager.enableTexture2D();
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.scale(-1.0f, 1.0f, 1.0f);
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, 0.0f, color);
            GlStateManager.enableLighting();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }

    public static interface IDebugRenderer {
        public void render(float var1, long var2);
    }
}

