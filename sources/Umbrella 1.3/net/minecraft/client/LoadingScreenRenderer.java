/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MinecraftError;

public class LoadingScreenRenderer
implements IProgressUpdate {
    private String field_73727_a = "";
    private Minecraft mc;
    private String currentlyDisplayedText = "";
    private long field_73723_d = Minecraft.getSystemTime();
    private boolean field_73724_e;
    private ScaledResolution field_146587_f;
    private Framebuffer field_146588_g;
    private static final String __OBFID = "CL_00000655";

    public LoadingScreenRenderer(Minecraft mcIn) {
        this.mc = mcIn;
        this.field_146587_f = new ScaledResolution(mcIn, mcIn.displayWidth, mcIn.displayHeight);
        this.field_146588_g = new Framebuffer(mcIn.displayWidth, mcIn.displayHeight, false);
        this.field_146588_g.setFramebufferFilter(9728);
    }

    @Override
    public void resetProgressAndMessage(String p_73721_1_) {
        this.field_73724_e = false;
        this.func_73722_d(p_73721_1_);
    }

    @Override
    public void displaySavingString(String message) {
        this.field_73724_e = true;
        this.func_73722_d(message);
    }

    private void func_73722_d(String p_73722_1_) {
        this.currentlyDisplayedText = p_73722_1_;
        if (!this.mc.running) {
            if (!this.field_73724_e) {
                throw new MinecraftError();
            }
        } else {
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            if (OpenGlHelper.isFramebufferEnabled()) {
                int var2 = this.field_146587_f.getScaleFactor();
                GlStateManager.ortho(0.0, this.field_146587_f.getScaledWidth() * var2, this.field_146587_f.getScaledHeight() * var2, 0.0, 100.0, 300.0);
            } else {
                ScaledResolution var3 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
                GlStateManager.ortho(0.0, var3.getScaledWidth_double(), var3.getScaledHeight_double(), 0.0, 100.0, 300.0);
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
            this.field_73723_d = 0L;
            this.field_73727_a = message;
            this.setLoadingProgress(-1);
            this.field_73723_d = 0L;
        }
    }

    @Override
    public void setLoadingProgress(int progress) {
        if (!this.mc.running) {
            if (!this.field_73724_e) {
                throw new MinecraftError();
            }
        } else {
            long var2 = Minecraft.getSystemTime();
            if (var2 - this.field_73723_d >= 100L) {
                this.field_73723_d = var2;
                ScaledResolution var4 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
                int var5 = var4.getScaleFactor();
                int var6 = var4.getScaledWidth();
                int var7 = var4.getScaledHeight();
                if (OpenGlHelper.isFramebufferEnabled()) {
                    this.field_146588_g.framebufferClear();
                } else {
                    GlStateManager.clear(256);
                }
                this.field_146588_g.bindFramebuffer(false);
                GlStateManager.matrixMode(5889);
                GlStateManager.loadIdentity();
                GlStateManager.ortho(0.0, var4.getScaledWidth_double(), var4.getScaledHeight_double(), 0.0, 100.0, 300.0);
                GlStateManager.matrixMode(5888);
                GlStateManager.loadIdentity();
                GlStateManager.translate(0.0f, 0.0f, -200.0f);
                if (!OpenGlHelper.isFramebufferEnabled()) {
                    GlStateManager.clear(16640);
                }
                Tessellator var8 = Tessellator.getInstance();
                WorldRenderer var9 = var8.getWorldRenderer();
                this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
                float var10 = 32.0f;
                var9.startDrawingQuads();
                var9.func_178991_c(0x404040);
                var9.addVertexWithUV(0.0, var7, 0.0, 0.0, (float)var7 / var10);
                var9.addVertexWithUV(var6, var7, 0.0, (float)var6 / var10, (float)var7 / var10);
                var9.addVertexWithUV(var6, 0.0, 0.0, (float)var6 / var10, 0.0);
                var9.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0);
                var8.draw();
                if (progress >= 0) {
                    int var11 = 100;
                    int var12 = 2;
                    int var13 = var6 / 2 - var11 / 2;
                    int var14 = var7 / 2 + 16;
                    GlStateManager.func_179090_x();
                    var9.startDrawingQuads();
                    var9.func_178991_c(0x808080);
                    var9.addVertex(var13, var14, 0.0);
                    var9.addVertex(var13, var14 + var12, 0.0);
                    var9.addVertex(var13 + var11, var14 + var12, 0.0);
                    var9.addVertex(var13 + var11, var14, 0.0);
                    var9.func_178991_c(0x80FF80);
                    var9.addVertex(var13, var14, 0.0);
                    var9.addVertex(var13, var14 + var12, 0.0);
                    var9.addVertex(var13 + progress, var14 + var12, 0.0);
                    var9.addVertex(var13 + progress, var14, 0.0);
                    var8.draw();
                    GlStateManager.func_179098_w();
                }
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                this.mc.fontRendererObj.drawStringWithShadow(this.currentlyDisplayedText, (var6 - this.mc.fontRendererObj.getStringWidth(this.currentlyDisplayedText)) / 2, var7 / 2 - 4 - 16, 0xFFFFFF);
                this.mc.fontRendererObj.drawStringWithShadow(this.field_73727_a, (var6 - this.mc.fontRendererObj.getStringWidth(this.field_73727_a)) / 2, var7 / 2 - 4 + 8, 0xFFFFFF);
                this.field_146588_g.unbindFramebuffer();
                if (OpenGlHelper.isFramebufferEnabled()) {
                    this.field_146588_g.framebufferRender(var6 * var5, var7 * var5);
                }
                this.mc.func_175601_h();
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

