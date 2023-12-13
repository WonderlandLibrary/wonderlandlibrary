// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.Vec4b;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.util.Iterator;
import net.minecraft.world.storage.MapData;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class MapItemRenderer
{
    private static final ResourceLocation mapIcons;
    private final TextureManager textureManager;
    private final Map loadedMaps;
    private static final String __OBFID = "CL_00000663";
    
    public MapItemRenderer(final TextureManager p_i45009_1_) {
        this.loadedMaps = Maps.newHashMap();
        this.textureManager = p_i45009_1_;
    }
    
    public void func_148246_a(final MapData p_148246_1_) {
        this.func_148248_b(p_148246_1_).func_148236_a();
    }
    
    public void func_148250_a(final MapData p_148250_1_, final boolean p_148250_2_) {
        this.func_148248_b(p_148250_1_).func_148237_a(p_148250_2_);
    }
    
    private Instance func_148248_b(final MapData p_148248_1_) {
        Instance var2 = this.loadedMaps.get(p_148248_1_.mapName);
        if (var2 == null) {
            var2 = new Instance(p_148248_1_, null);
            this.loadedMaps.put(p_148248_1_.mapName, var2);
        }
        return var2;
    }
    
    public void func_148249_a() {
        for (final Instance var2 : this.loadedMaps.values()) {
            this.textureManager.deleteTexture(var2.field_148240_d);
        }
        this.loadedMaps.clear();
    }
    
    static {
        mapIcons = new ResourceLocation("textures/map/map_icons.png");
    }
    
    class Instance
    {
        private final MapData field_148242_b;
        private final DynamicTexture field_148243_c;
        private final ResourceLocation field_148240_d;
        private final int[] field_148241_e;
        private static final String __OBFID = "CL_00000665";
        
        private Instance(final MapData p_i45007_2_) {
            this.field_148242_b = p_i45007_2_;
            this.field_148243_c = new DynamicTexture(128, 128);
            this.field_148241_e = this.field_148243_c.getTextureData();
            this.field_148240_d = MapItemRenderer.this.textureManager.getDynamicTextureLocation("map/" + p_i45007_2_.mapName, this.field_148243_c);
            for (int var3 = 0; var3 < this.field_148241_e.length; ++var3) {
                this.field_148241_e[var3] = 0;
            }
        }
        
        private void func_148236_a() {
            for (int var1 = 0; var1 < 16384; ++var1) {
                final int var2 = this.field_148242_b.colors[var1] & 0xFF;
                if (var2 / 4 == 0) {
                    this.field_148241_e[var1] = (var1 + var1 / 128 & 0x1) * 8 + 16 << 24;
                }
                else {
                    this.field_148241_e[var1] = MapColor.mapColorArray[var2 / 4].func_151643_b(var2 & 0x3);
                }
            }
            this.field_148243_c.updateDynamicTexture();
        }
        
        private void func_148237_a(final boolean p_148237_1_) {
            final byte var2 = 0;
            final byte var3 = 0;
            final Tessellator var4 = Tessellator.getInstance();
            final WorldRenderer var5 = var4.getWorldRenderer();
            final float var6 = 0.0f;
            MapItemRenderer.this.textureManager.bindTexture(this.field_148240_d);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(1, 771, 0, 1);
            GlStateManager.disableAlpha();
            var5.startDrawingQuads();
            var5.addVertexWithUV(var2 + 0 + var6, var3 + 128 - var6, -0.009999999776482582, 0.0, 1.0);
            var5.addVertexWithUV(var2 + 128 - var6, var3 + 128 - var6, -0.009999999776482582, 1.0, 1.0);
            var5.addVertexWithUV(var2 + 128 - var6, var3 + 0 + var6, -0.009999999776482582, 1.0, 0.0);
            var5.addVertexWithUV(var2 + 0 + var6, var3 + 0 + var6, -0.009999999776482582, 0.0, 0.0);
            var4.draw();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            MapItemRenderer.this.textureManager.bindTexture(MapItemRenderer.mapIcons);
            int var7 = 0;
            for (final Vec4b var9 : this.field_148242_b.playersVisibleOnMap.values()) {
                if (!p_148237_1_ || var9.func_176110_a() == 1) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(var2 + var9.func_176112_b() / 2.0f + 64.0f, var3 + var9.func_176113_c() / 2.0f + 64.0f, -0.02f);
                    GlStateManager.rotate(var9.func_176111_d() * 360 / 16.0f, 0.0f, 0.0f, 1.0f);
                    GlStateManager.scale(4.0f, 4.0f, 3.0f);
                    GlStateManager.translate(-0.125f, 0.125f, 0.0f);
                    final byte var10 = var9.func_176110_a();
                    final float var11 = (var10 % 4 + 0) / 4.0f;
                    final float var12 = (var10 / 4 + 0) / 4.0f;
                    final float var13 = (var10 % 4 + 1) / 4.0f;
                    final float var14 = (var10 / 4 + 1) / 4.0f;
                    var5.startDrawingQuads();
                    var5.addVertexWithUV(-1.0, 1.0, var7 * 0.001f, var11, var12);
                    var5.addVertexWithUV(1.0, 1.0, var7 * 0.001f, var13, var12);
                    var5.addVertexWithUV(1.0, -1.0, var7 * 0.001f, var13, var14);
                    var5.addVertexWithUV(-1.0, -1.0, var7 * 0.001f, var11, var14);
                    var4.draw();
                    GlStateManager.popMatrix();
                    ++var7;
                }
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, 0.0f, -0.04f);
            GlStateManager.scale(1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
        
        Instance(final MapItemRenderer this$0, final MapData p_i45008_2_, final Object p_i45008_3_) {
            this(this$0, p_i45008_2_);
        }
    }
}
