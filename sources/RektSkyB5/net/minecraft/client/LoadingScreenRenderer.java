/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MinecraftError;

public class LoadingScreenRenderer
implements IProgressUpdate {
    private String message = "";
    private Minecraft mc;
    private String currentlyDisplayedText = "";
    private long systemTime = Minecraft.getSystemTime();
    private boolean field_73724_e;
    private ScaledResolution scaledResolution;
    private Framebuffer framebuffer;

    public LoadingScreenRenderer(Minecraft mcIn) {
        this.mc = mcIn;
        this.scaledResolution = new ScaledResolution(mcIn);
        this.framebuffer = new Framebuffer(mcIn.displayWidth, mcIn.displayHeight, false);
        this.framebuffer.setFramebufferFilter(9728);
    }

    @Override
    public void resetProgressAndMessage(String message) {
        this.field_73724_e = false;
        this.displayString(message);
    }

    @Override
    public void displaySavingString(String message) {
        this.field_73724_e = true;
        this.displayString(message);
    }

    private void displayString(String message) {
        this.currentlyDisplayedText = message;
        if (!this.mc.running) {
            if (!this.field_73724_e) {
                throw new MinecraftError();
            }
        } else {
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            if (OpenGlHelper.isFramebufferEnabled()) {
                int i2 = this.scaledResolution.getScaleFactor();
                GlStateManager.ortho(0.0, this.scaledResolution.getScaledWidth() * i2, this.scaledResolution.getScaledHeight() * i2, 0.0, 100.0, 300.0);
            } else {
                ScaledResolution scaledresolution = new ScaledResolution(this.mc);
                GlStateManager.ortho(0.0, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0, 100.0, 300.0);
            }
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0f, 0.0f, -200.0f);
        }
    }

    @Override
    public void displayLoadingString(String message) {
        if (!this.mc.running) {
            if (!this.field_73724_e) {
                throw new MinecraftError();
            }
        } else {
            this.systemTime = 0L;
            this.message = message;
            this.setLoadingProgress(-1);
            this.systemTime = 0L;
        }
    }

    @Override
    public void setLoadingProgress(int progress) {
        if (!this.mc.running) {
            if (!this.field_73724_e) {
                throw new MinecraftError();
            }
        } else {
            long i2 = Minecraft.getSystemTime();
            if (i2 - this.systemTime >= 100L) {
                this.systemTime = i2;
                ScaledResolution scaledresolution = new ScaledResolution(this.mc);
                int j2 = scaledresolution.getScaleFactor();
                int k2 = scaledresolution.getScaledWidth();
                int l2 = scaledresolution.getScaledHeight();
                if (OpenGlHelper.isFramebufferEnabled()) {
                    this.framebuffer.framebufferClear();
                } else {
                    GlStateManager.clear(256);
                }
                this.framebuffer.bindFramebuffer(false);
                GlStateManager.matrixMode(5889);
                GlStateManager.loadIdentity();
                GlStateManager.ortho(0.0, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0, 100.0, 300.0);
                GlStateManager.matrixMode(5888);
                GlStateManager.loadIdentity();
                GlStateManager.translate(0.0f, 0.0f, -200.0f);
                if (!OpenGlHelper.isFramebufferEnabled()) {
                    GlStateManager.clear(16640);
                }
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
                float f2 = 32.0f;
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldrenderer.pos(0.0, l2, 0.0).tex(0.0, (float)l2 / f2).color(64, 64, 64, 255).endVertex();
                worldrenderer.pos(k2, l2, 0.0).tex((float)k2 / f2, (float)l2 / f2).color(64, 64, 64, 255).endVertex();
                worldrenderer.pos(k2, 0.0, 0.0).tex((float)k2 / f2, 0.0).color(64, 64, 64, 255).endVertex();
                worldrenderer.pos(0.0, 0.0, 0.0).tex(0.0, 0.0).color(64, 64, 64, 255).endVertex();
                tessellator.draw();
                if (progress >= 0) {
                    int i1 = 100;
                    int j1 = 2;
                    int k1 = k2 / 2 - i1 / 2;
                    int l1 = l2 / 2 + 16;
                    GlStateManager.disableTexture2D();
                    worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                    worldrenderer.pos(k1, l1, 0.0).color(128, 128, 128, 255).endVertex();
                    worldrenderer.pos(k1, l1 + j1, 0.0).color(128, 128, 128, 255).endVertex();
                    worldrenderer.pos(k1 + i1, l1 + j1, 0.0).color(128, 128, 128, 255).endVertex();
                    worldrenderer.pos(k1 + i1, l1, 0.0).color(128, 128, 128, 255).endVertex();
                    worldrenderer.pos(k1, l1, 0.0).color(128, 255, 128, 255).endVertex();
                    worldrenderer.pos(k1, l1 + j1, 0.0).color(128, 255, 128, 255).endVertex();
                    worldrenderer.pos(k1 + progress, l1 + j1, 0.0).color(128, 255, 128, 255).endVertex();
                    worldrenderer.pos(k1 + progress, l1, 0.0).color(128, 255, 128, 255).endVertex();
                    tessellator.draw();
                    GlStateManager.enableTexture2D();
                }
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                this.mc.fontRendererObj.drawStringWithShadow(this.currentlyDisplayedText, (k2 - this.mc.fontRendererObj.getStringWidth(this.currentlyDisplayedText)) / 2, l2 / 2 - 4 - 16, 0xFFFFFF);
                this.mc.fontRendererObj.drawStringWithShadow(this.message, (k2 - this.mc.fontRendererObj.getStringWidth(this.message)) / 2, l2 / 2 - 4 + 8, 0xFFFFFF);
                this.framebuffer.unbindFramebuffer();
                if (OpenGlHelper.isFramebufferEnabled()) {
                    this.framebuffer.framebufferRender(k2 * j2, l2 * j2);
                }
                this.mc.updateDisplay();
                try {
                    Thread.yield();
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
    }

    @Override
    public void setDoneWorking() {
    }
}

