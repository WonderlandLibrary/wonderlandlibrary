// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.world.storage.MapDecoration;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.world.storage.MapData;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class MapItemRenderer
{
    private static final ResourceLocation TEXTURE_MAP_ICONS;
    private final TextureManager textureManager;
    private final Map<String, Instance> loadedMaps;
    
    public MapItemRenderer(final TextureManager textureManagerIn) {
        this.loadedMaps = (Map<String, Instance>)Maps.newHashMap();
        this.textureManager = textureManagerIn;
    }
    
    public void updateMapTexture(final MapData mapdataIn) {
        this.getMapRendererInstance(mapdataIn).updateMapTexture();
    }
    
    public void renderMap(final MapData mapdataIn, final boolean noOverlayRendering) {
        this.getMapRendererInstance(mapdataIn).render(noOverlayRendering);
    }
    
    private Instance getMapRendererInstance(final MapData mapdataIn) {
        Instance mapitemrenderer$instance = this.loadedMaps.get(mapdataIn.mapName);
        if (mapitemrenderer$instance == null) {
            mapitemrenderer$instance = new Instance(mapdataIn);
            this.loadedMaps.put(mapdataIn.mapName, mapitemrenderer$instance);
        }
        return mapitemrenderer$instance;
    }
    
    @Nullable
    public Instance getMapInstanceIfExists(final String p_191205_1_) {
        return this.loadedMaps.get(p_191205_1_);
    }
    
    public void clearLoadedMaps() {
        for (final Instance mapitemrenderer$instance : this.loadedMaps.values()) {
            this.textureManager.deleteTexture(mapitemrenderer$instance.location);
        }
        this.loadedMaps.clear();
    }
    
    @Nullable
    public MapData getData(@Nullable final Instance p_191207_1_) {
        return (p_191207_1_ != null) ? p_191207_1_.mapData : null;
    }
    
    static {
        TEXTURE_MAP_ICONS = new ResourceLocation("textures/map/map_icons.png");
    }
    
    class Instance
    {
        private final MapData mapData;
        private final DynamicTexture mapTexture;
        private final ResourceLocation location;
        private final int[] mapTextureData;
        
        private Instance(final MapData mapdataIn) {
            this.mapData = mapdataIn;
            this.mapTexture = new DynamicTexture(128, 128);
            this.mapTextureData = this.mapTexture.getTextureData();
            this.location = MapItemRenderer.this.textureManager.getDynamicTextureLocation("map/" + mapdataIn.mapName, this.mapTexture);
            for (int i = 0; i < this.mapTextureData.length; ++i) {
                this.mapTextureData[i] = 0;
            }
        }
        
        private void updateMapTexture() {
            for (int i = 0; i < 16384; ++i) {
                final int j = this.mapData.colors[i] & 0xFF;
                if (j / 4 == 0) {
                    this.mapTextureData[i] = (i + i / 128 & 0x1) * 8 + 16 << 24;
                }
                else {
                    this.mapTextureData[i] = MapColor.COLORS[j / 4].getMapColor(j & 0x3);
                }
            }
            this.mapTexture.updateDynamicTexture();
        }
        
        private void render(final boolean noOverlayRendering) {
            final int i = 0;
            final int j = 0;
            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder bufferbuilder = tessellator.getBuffer();
            final float f = 0.0f;
            MapItemRenderer.this.textureManager.bindTexture(this.location);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
            GlStateManager.disableAlpha();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(0.0, 128.0, -0.009999999776482582).tex(0.0, 1.0).endVertex();
            bufferbuilder.pos(128.0, 128.0, -0.009999999776482582).tex(1.0, 1.0).endVertex();
            bufferbuilder.pos(128.0, 0.0, -0.009999999776482582).tex(1.0, 0.0).endVertex();
            bufferbuilder.pos(0.0, 0.0, -0.009999999776482582).tex(0.0, 0.0).endVertex();
            tessellator.draw();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            MapItemRenderer.this.textureManager.bindTexture(MapItemRenderer.TEXTURE_MAP_ICONS);
            int k = 0;
            for (final MapDecoration mapdecoration : this.mapData.mapDecorations.values()) {
                if (!noOverlayRendering || mapdecoration.renderOnFrame()) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(0.0f + mapdecoration.getX() / 2.0f + 64.0f, 0.0f + mapdecoration.getY() / 2.0f + 64.0f, -0.02f);
                    GlStateManager.rotate(mapdecoration.getRotation() * 360 / 16.0f, 0.0f, 0.0f, 1.0f);
                    GlStateManager.scale(4.0f, 4.0f, 3.0f);
                    GlStateManager.translate(-0.125f, 0.125f, 0.0f);
                    final byte b0 = mapdecoration.getImage();
                    final float f2 = (b0 % 4 + 0) / 4.0f;
                    final float f3 = (b0 / 4 + 0) / 4.0f;
                    final float f4 = (b0 % 4 + 1) / 4.0f;
                    final float f5 = (b0 / 4 + 1) / 4.0f;
                    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
                    final float f6 = -0.001f;
                    bufferbuilder.pos(-1.0, 1.0, k * -0.001f).tex(f2, f3).endVertex();
                    bufferbuilder.pos(1.0, 1.0, k * -0.001f).tex(f4, f3).endVertex();
                    bufferbuilder.pos(1.0, -1.0, k * -0.001f).tex(f4, f5).endVertex();
                    bufferbuilder.pos(-1.0, -1.0, k * -0.001f).tex(f2, f5).endVertex();
                    tessellator.draw();
                    GlStateManager.popMatrix();
                    ++k;
                }
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, 0.0f, -0.04f);
            GlStateManager.scale(1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }
}
