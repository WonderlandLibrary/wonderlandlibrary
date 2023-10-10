/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class GuiStreamIndicator {
    private static final ResourceLocation locationStreamIndicator = new ResourceLocation("textures/gui/stream_indicator.png");
    private final Minecraft mc;
    private float field_152443_c = 1.0f;
    private int field_152444_d = 1;

    public GuiStreamIndicator(Minecraft mcIn) {
        this.mc = mcIn;
    }

    public void render(int p_152437_1_, int p_152437_2_) {
        if (this.mc.getTwitchStream().isBroadcasting()) {
            GlStateManager.enableBlend();
            int i2 = this.mc.getTwitchStream().func_152920_A();
            if (i2 > 0) {
                String s2 = "" + i2;
                int j2 = this.mc.fontRendererObj.getStringWidth(s2);
                int k2 = 20;
                int l2 = p_152437_1_ - j2 - 1;
                int i1 = p_152437_2_ + 20 - 1;
                int j1 = p_152437_2_ + 20 + this.mc.fontRendererObj.FONT_HEIGHT - 1;
                GlStateManager.disableTexture2D();
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                GlStateManager.color(0.0f, 0.0f, 0.0f, (0.65f + 0.35000002f * this.field_152443_c) / 2.0f);
                worldrenderer.begin(7, DefaultVertexFormats.POSITION);
                worldrenderer.pos(l2, j1, 0.0).endVertex();
                worldrenderer.pos(p_152437_1_, j1, 0.0).endVertex();
                worldrenderer.pos(p_152437_1_, i1, 0.0).endVertex();
                worldrenderer.pos(l2, i1, 0.0).endVertex();
                tessellator.draw();
                GlStateManager.enableTexture2D();
                this.mc.fontRendererObj.drawString(s2, p_152437_1_ - j2, p_152437_2_ + 20, 0xFFFFFF);
            }
            this.render(p_152437_1_, p_152437_2_, this.func_152440_b(), 0);
            this.render(p_152437_1_, p_152437_2_, this.func_152438_c(), 17);
        }
    }

    private void render(int p_152436_1_, int p_152436_2_, int p_152436_3_, int p_152436_4_) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.65f + 0.35000002f * this.field_152443_c);
        this.mc.getTextureManager().bindTexture(locationStreamIndicator);
        float f2 = 150.0f;
        float f1 = 0.0f;
        float f22 = (float)p_152436_3_ * 0.015625f;
        float f3 = 1.0f;
        float f4 = (float)(p_152436_3_ + 16) * 0.015625f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(p_152436_1_ - 16 - p_152436_4_, p_152436_2_ + 16, f2).tex(f1, f4).endVertex();
        worldrenderer.pos(p_152436_1_ - p_152436_4_, p_152436_2_ + 16, f2).tex(f3, f4).endVertex();
        worldrenderer.pos(p_152436_1_ - p_152436_4_, p_152436_2_ + 0, f2).tex(f3, f22).endVertex();
        worldrenderer.pos(p_152436_1_ - 16 - p_152436_4_, p_152436_2_ + 0, f2).tex(f1, f22).endVertex();
        tessellator.draw();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private int func_152440_b() {
        return this.mc.getTwitchStream().isPaused() ? 16 : 0;
    }

    private int func_152438_c() {
        return this.mc.getTwitchStream().func_152929_G() ? 48 : 32;
    }

    public void func_152439_a() {
        if (this.mc.getTwitchStream().isBroadcasting()) {
            this.field_152443_c += 0.025f * (float)this.field_152444_d;
            if (this.field_152443_c < 0.0f) {
                this.field_152444_d *= -1;
                this.field_152443_c = 0.0f;
            } else if (this.field_152443_c > 1.0f) {
                this.field_152444_d *= -1;
                this.field_152443_c = 1.0f;
            }
        } else {
            this.field_152443_c = 1.0f;
            this.field_152444_d = 1;
        }
    }
}

