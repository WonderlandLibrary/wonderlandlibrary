package net.minecraft.client.gui;

import net.minecraft.block.material.MapColor;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec4b;
import net.minecraft.world.storage.MapData;

/*
 * Exception performing whole class analysis ignored.
 */
class MapItemRenderer.Instance {
    private final MapData mapData;
    private final DynamicTexture mapTexture;
    private final ResourceLocation location;
    private final int[] mapTextureData;

    private MapItemRenderer.Instance(MapData mapdataIn) {
        this.mapData = mapdataIn;
        this.mapTexture = new DynamicTexture(128, 128);
        this.mapTextureData = this.mapTexture.getTextureData();
        this.location = MapItemRenderer.access$400((MapItemRenderer)MapItemRenderer.this).getDynamicTextureLocation("map/" + mapdataIn.mapName, this.mapTexture);
        for (int i = 0; i < this.mapTextureData.length; ++i) {
            this.mapTextureData[i] = 0;
        }
    }

    private void updateMapTexture() {
        for (int i = 0; i < 16384; ++i) {
            int j = this.mapData.colors[i] & 0xFF;
            this.mapTextureData[i] = j / 4 == 0 ? (i + i / 128 & 1) * 8 + 16 << 24 : MapColor.mapColorArray[j / 4].getMapColor(j & 3);
        }
        this.mapTexture.updateDynamicTexture();
    }

    private void render(boolean noOverlayRendering) {
        int i = 0;
        int j = 0;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        float f = 0.0f;
        MapItemRenderer.access$400((MapItemRenderer)MapItemRenderer.this).bindTexture(this.location);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((int)1, (int)771, (int)0, (int)1);
        GlStateManager.disableAlpha();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)((float)(i + 0) + f), (double)((float)(j + 128) - f), (double)-0.01f).tex(0.0, 1.0).endVertex();
        worldrenderer.pos((double)((float)(i + 128) - f), (double)((float)(j + 128) - f), (double)-0.01f).tex(1.0, 1.0).endVertex();
        worldrenderer.pos((double)((float)(i + 128) - f), (double)((float)(j + 0) + f), (double)-0.01f).tex(1.0, 0.0).endVertex();
        worldrenderer.pos((double)((float)(i + 0) + f), (double)((float)(j + 0) + f), (double)-0.01f).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        MapItemRenderer.access$400((MapItemRenderer)MapItemRenderer.this).bindTexture(MapItemRenderer.access$500());
        int k = 0;
        for (Vec4b vec4b : this.mapData.mapDecorations.values()) {
            if (noOverlayRendering && vec4b.func_176110_a() != 1) continue;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)((float)i + (float)vec4b.func_176112_b() / 2.0f + 64.0f), (float)((float)j + (float)vec4b.func_176113_c() / 2.0f + 64.0f), (float)-0.02f);
            GlStateManager.rotate((float)((float)(vec4b.func_176111_d() * 360) / 16.0f), (float)0.0f, (float)0.0f, (float)1.0f);
            GlStateManager.scale((float)4.0f, (float)4.0f, (float)3.0f);
            GlStateManager.translate((float)-0.125f, (float)0.125f, (float)0.0f);
            byte b0 = vec4b.func_176110_a();
            float f1 = (float)(b0 % 4 + 0) / 4.0f;
            float f2 = (float)(b0 / 4 + 0) / 4.0f;
            float f3 = (float)(b0 % 4 + 1) / 4.0f;
            float f4 = (float)(b0 / 4 + 1) / 4.0f;
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            float f5 = -0.001f;
            worldrenderer.pos(-1.0, 1.0, (double)((float)k * -0.001f)).tex((double)f1, (double)f2).endVertex();
            worldrenderer.pos(1.0, 1.0, (double)((float)k * -0.001f)).tex((double)f3, (double)f2).endVertex();
            worldrenderer.pos(1.0, -1.0, (double)((float)k * -0.001f)).tex((double)f3, (double)f4).endVertex();
            worldrenderer.pos(-1.0, -1.0, (double)((float)k * -0.001f)).tex((double)f1, (double)f4).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
            ++k;
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)0.0f, (float)0.0f, (float)-0.04f);
        GlStateManager.scale((float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.popMatrix();
    }

    static /* synthetic */ void access$000(MapItemRenderer.Instance x0) {
        x0.updateMapTexture();
    }

    static /* synthetic */ void access$100(MapItemRenderer.Instance x0, boolean x1) {
        x0.render(x1);
    }

    static /* synthetic */ ResourceLocation access$300(MapItemRenderer.Instance x0) {
        return x0.location;
    }
}
