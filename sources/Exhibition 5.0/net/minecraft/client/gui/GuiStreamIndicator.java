// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiStreamIndicator
{
    private static final ResourceLocation locationStreamIndicator;
    private final Minecraft mc;
    private float field_152443_c;
    private int field_152444_d;
    private static final String __OBFID = "CL_00001849";
    
    public GuiStreamIndicator(final Minecraft mcIn) {
        this.field_152443_c = 1.0f;
        this.field_152444_d = 1;
        this.mc = mcIn;
    }
    
    public void render(final int p_152437_1_, final int p_152437_2_) {
        if (this.mc.getTwitchStream().func_152934_n()) {
            GlStateManager.enableBlend();
            final int var3 = this.mc.getTwitchStream().func_152920_A();
            if (var3 > 0) {
                final String var4 = "" + var3;
                final int var5 = this.mc.fontRendererObj.getStringWidth(var4);
                final boolean var6 = true;
                final int var7 = p_152437_1_ - var5 - 1;
                final int var8 = p_152437_2_ + 20 - 1;
                final int var9 = p_152437_2_ + 20 + this.mc.fontRendererObj.FONT_HEIGHT - 1;
                GlStateManager.disableTextures();
                final Tessellator var10 = Tessellator.getInstance();
                final WorldRenderer var11 = var10.getWorldRenderer();
                GlStateManager.color(0.0f, 0.0f, 0.0f, (0.65f + 0.35000002f * this.field_152443_c) / 2.0f);
                var11.startDrawingQuads();
                var11.addVertex(var7, var9, 0.0);
                var11.addVertex(p_152437_1_, var9, 0.0);
                var11.addVertex(p_152437_1_, var8, 0.0);
                var11.addVertex(var7, var8, 0.0);
                var10.draw();
                GlStateManager.enableTextures();
                this.mc.fontRendererObj.drawString(var4, p_152437_1_ - var5, p_152437_2_ + 20, 16777215);
            }
            this.render(p_152437_1_, p_152437_2_, this.func_152440_b(), 0);
            this.render(p_152437_1_, p_152437_2_, this.func_152438_c(), 17);
        }
    }
    
    private void render(final int p_152436_1_, final int p_152436_2_, final int p_152436_3_, final int p_152436_4_) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.65f + 0.35000002f * this.field_152443_c);
        this.mc.getTextureManager().bindTexture(GuiStreamIndicator.locationStreamIndicator);
        final float var5 = 150.0f;
        final float var6 = 0.0f;
        final float var7 = p_152436_3_ * 0.015625f;
        final float var8 = 1.0f;
        final float var9 = (p_152436_3_ + 16) * 0.015625f;
        final Tessellator var10 = Tessellator.getInstance();
        final WorldRenderer var11 = var10.getWorldRenderer();
        var11.startDrawingQuads();
        var11.addVertexWithUV(p_152436_1_ - 16 - p_152436_4_, p_152436_2_ + 16, var5, var6, var9);
        var11.addVertexWithUV(p_152436_1_ - p_152436_4_, p_152436_2_ + 16, var5, var8, var9);
        var11.addVertexWithUV(p_152436_1_ - p_152436_4_, p_152436_2_ + 0, var5, var8, var7);
        var11.addVertexWithUV(p_152436_1_ - 16 - p_152436_4_, p_152436_2_ + 0, var5, var6, var7);
        var10.draw();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private int func_152440_b() {
        return this.mc.getTwitchStream().isPaused() ? 16 : 0;
    }
    
    private int func_152438_c() {
        return this.mc.getTwitchStream().func_152929_G() ? 48 : 32;
    }
    
    public void func_152439_a() {
        if (this.mc.getTwitchStream().func_152934_n()) {
            this.field_152443_c += 0.025f * this.field_152444_d;
            if (this.field_152443_c < 0.0f) {
                this.field_152444_d *= -1;
                this.field_152443_c = 0.0f;
            }
            else if (this.field_152443_c > 1.0f) {
                this.field_152444_d *= -1;
                this.field_152443_c = 1.0f;
            }
        }
        else {
            this.field_152443_c = 1.0f;
            this.field_152444_d = 1;
        }
    }
    
    static {
        locationStreamIndicator = new ResourceLocation("textures/gui/stream_indicator.png");
    }
}
