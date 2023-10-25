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
    private Minecraft mc;
    private ScaledResolution scaledResolution;
    private Framebuffer framebuffer;
    private boolean field_73724_e;
    private long systemTime = Minecraft.getSystemTime();
    private String message = "";
    private String currentlyDisplayedText = "";

    @Override
    public void setLoadingProgress(int n) {
        if (!this.mc.running) {
            if (!this.field_73724_e) {
                throw new MinecraftError();
            }
        } else {
            long l = Minecraft.getSystemTime();
            if (l - this.systemTime >= 100L) {
                this.systemTime = l;
                ScaledResolution scaledResolution = new ScaledResolution(this.mc);
                int n2 = scaledResolution.getScaleFactor();
                int n3 = scaledResolution.getScaledWidth();
                int n4 = scaledResolution.getScaledHeight();
                if (OpenGlHelper.isFramebufferEnabled()) {
                    this.framebuffer.framebufferClear();
                } else {
                    GlStateManager.clear(256);
                }
                this.framebuffer.bindFramebuffer(false);
                GlStateManager.matrixMode(5889);
                GlStateManager.loadIdentity();
                GlStateManager.ortho(0.0, scaledResolution.getScaledWidth_double(), scaledResolution.getScaledHeight_double(), 0.0, 100.0, 300.0);
                GlStateManager.matrixMode(5888);
                GlStateManager.loadIdentity();
                GlStateManager.translate(0.0f, 0.0f, -200.0f);
                if (!OpenGlHelper.isFramebufferEnabled()) {
                    GlStateManager.clear(16640);
                }
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldRenderer = tessellator.getWorldRenderer();
                this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
                float f = 32.0f;
                worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldRenderer.pos(0.0, n4, 0.0).tex(0.0, (float)n4 / f).color(64, 64, 64, 255).endVertex();
                worldRenderer.pos(n3, n4, 0.0).tex((float)n3 / f, (float)n4 / f).color(64, 64, 64, 255).endVertex();
                worldRenderer.pos(n3, 0.0, 0.0).tex((float)n3 / f, 0.0).color(64, 64, 64, 255).endVertex();
                worldRenderer.pos(0.0, 0.0, 0.0).tex(0.0, 0.0).color(64, 64, 64, 255).endVertex();
                tessellator.draw();
                if (n >= 0) {
                    int n5 = 100;
                    int n6 = 2;
                    int n7 = n3 / 2 - n5 / 2;
                    int n8 = n4 / 2 + 16;
                    GlStateManager.disableTexture2D();
                    worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                    worldRenderer.pos(n7, n8, 0.0).color(128, 128, 128, 255).endVertex();
                    worldRenderer.pos(n7, n8 + n6, 0.0).color(128, 128, 128, 255).endVertex();
                    worldRenderer.pos(n7 + n5, n8 + n6, 0.0).color(128, 128, 128, 255).endVertex();
                    worldRenderer.pos(n7 + n5, n8, 0.0).color(128, 128, 128, 255).endVertex();
                    worldRenderer.pos(n7, n8, 0.0).color(128, 255, 128, 255).endVertex();
                    worldRenderer.pos(n7, n8 + n6, 0.0).color(128, 255, 128, 255).endVertex();
                    worldRenderer.pos(n7 + n, n8 + n6, 0.0).color(128, 255, 128, 255).endVertex();
                    worldRenderer.pos(n7 + n, n8, 0.0).color(128, 255, 128, 255).endVertex();
                    tessellator.draw();
                    GlStateManager.enableTexture2D();
                }
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                Minecraft.fontRendererObj.drawStringWithShadow(this.currentlyDisplayedText, (n3 - Minecraft.fontRendererObj.getStringWidth(this.currentlyDisplayedText)) / 2, n4 / 2 - 4 - 16, 0xFFFFFF);
                Minecraft.fontRendererObj.drawStringWithShadow(this.message, (n3 - Minecraft.fontRendererObj.getStringWidth(this.message)) / 2, n4 / 2 - 4 + 8, 0xFFFFFF);
                this.framebuffer.unbindFramebuffer();
                if (OpenGlHelper.isFramebufferEnabled()) {
                    this.framebuffer.framebufferRender(n3 * n2, n4 * n2);
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

    public LoadingScreenRenderer(Minecraft minecraft) {
        this.mc = minecraft;
        this.scaledResolution = new ScaledResolution(minecraft);
        this.framebuffer = new Framebuffer(minecraft.displayWidth, Minecraft.displayHeight, false);
        this.framebuffer.setFramebufferFilter(9728);
    }

    @Override
    public void displayLoadingString(String string) {
        if (!this.mc.running) {
            if (!this.field_73724_e) {
                throw new MinecraftError();
            }
        } else {
            this.systemTime = 0L;
            this.message = string;
            this.setLoadingProgress(-1);
            this.systemTime = 0L;
        }
    }

    @Override
    public void setDoneWorking() {
    }

    private void displayString(String string) {
        this.currentlyDisplayedText = string;
        if (!this.mc.running) {
            if (!this.field_73724_e) {
                throw new MinecraftError();
            }
        } else {
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            if (OpenGlHelper.isFramebufferEnabled()) {
                int n = this.scaledResolution.getScaleFactor();
                GlStateManager.ortho(0.0, this.scaledResolution.getScaledWidth() * n, this.scaledResolution.getScaledHeight() * n, 0.0, 100.0, 300.0);
            } else {
                ScaledResolution scaledResolution = new ScaledResolution(this.mc);
                GlStateManager.ortho(0.0, scaledResolution.getScaledWidth_double(), scaledResolution.getScaledHeight_double(), 0.0, 100.0, 300.0);
            }
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0f, 0.0f, -200.0f);
        }
    }

    @Override
    public void resetProgressAndMessage(String string) {
        this.field_73724_e = false;
        this.displayString(string);
    }

    @Override
    public void displaySavingString(String string) {
        this.field_73724_e = true;
        this.displayString(string);
    }
}

