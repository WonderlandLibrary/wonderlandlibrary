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
    private final Minecraft mc;
    private int field_152444_d = 1;
    private float field_152443_c = 1.0f;
    private static final ResourceLocation locationStreamIndicator = new ResourceLocation("textures/gui/stream_indicator.png");

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

    private int func_152438_c() {
        return this.mc.getTwitchStream().func_152929_G() ? 48 : 32;
    }

    public GuiStreamIndicator(Minecraft minecraft) {
        this.mc = minecraft;
    }

    private void render(int n, int n2, int n3, int n4) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.65f + 0.35000002f * this.field_152443_c);
        this.mc.getTextureManager().bindTexture(locationStreamIndicator);
        float f = 150.0f;
        float f2 = 0.0f;
        float f3 = (float)n3 * 0.015625f;
        float f4 = 1.0f;
        float f5 = (float)(n3 + 16) * 0.015625f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(n - 16 - n4, n2 + 16, f).tex(f2, f5).endVertex();
        worldRenderer.pos(n - n4, n2 + 16, f).tex(f4, f5).endVertex();
        worldRenderer.pos(n - n4, n2 + 0, f).tex(f4, f3).endVertex();
        worldRenderer.pos(n - 16 - n4, n2 + 0, f).tex(f2, f3).endVertex();
        tessellator.draw();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void render(int n, int n2) {
        if (this.mc.getTwitchStream().isBroadcasting()) {
            GlStateManager.enableBlend();
            int n3 = this.mc.getTwitchStream().func_152920_A();
            if (n3 > 0) {
                String string = "" + n3;
                int n4 = Minecraft.fontRendererObj.getStringWidth(string);
                int n5 = 20;
                int n6 = n - n4 - 1;
                int n7 = n2 + 20 - 1;
                int n8 = n2 + 20 + Minecraft.fontRendererObj.FONT_HEIGHT - 1;
                GlStateManager.disableTexture2D();
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldRenderer = tessellator.getWorldRenderer();
                GlStateManager.color(0.0f, 0.0f, 0.0f, (0.65f + 0.35000002f * this.field_152443_c) / 2.0f);
                worldRenderer.begin(7, DefaultVertexFormats.POSITION);
                worldRenderer.pos(n6, n8, 0.0).endVertex();
                worldRenderer.pos(n, n8, 0.0).endVertex();
                worldRenderer.pos(n, n7, 0.0).endVertex();
                worldRenderer.pos(n6, n7, 0.0).endVertex();
                tessellator.draw();
                GlStateManager.enableTexture2D();
                Minecraft.fontRendererObj.drawString(string, n - n4, n2 + 20, 0xFFFFFF);
            }
            this.render(n, n2, this.func_152440_b(), 0);
            this.render(n, n2, this.func_152438_c(), 17);
        }
    }

    private int func_152440_b() {
        return this.mc.getTwitchStream().isPaused() ? 16 : 0;
    }
}

