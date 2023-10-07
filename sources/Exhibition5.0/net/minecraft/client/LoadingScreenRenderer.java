// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MinecraftError;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.IProgressUpdate;

public class LoadingScreenRenderer implements IProgressUpdate
{
    private String field_73727_a;
    private Minecraft mc;
    private String currentlyDisplayedText;
    private long field_73723_d;
    private boolean field_73724_e;
    private ScaledResolution field_146587_f;
    private Framebuffer field_146588_g;
    private static final String __OBFID = "CL_00000655";
    
    public LoadingScreenRenderer(final Minecraft mcIn) {
        this.field_73727_a = "";
        this.currentlyDisplayedText = "";
        this.field_73723_d = Minecraft.getSystemTime();
        this.mc = mcIn;
        this.field_146587_f = new ScaledResolution(mcIn, mcIn.displayWidth, mcIn.displayHeight);
        (this.field_146588_g = new Framebuffer(mcIn.displayWidth, mcIn.displayHeight, false)).setFramebufferFilter(9728);
    }
    
    @Override
    public void resetProgressAndMessage(final String p_73721_1_) {
        this.field_73724_e = false;
        this.func_73722_d(p_73721_1_);
    }
    
    @Override
    public void displaySavingString(final String message) {
        this.field_73724_e = true;
        this.func_73722_d(message);
    }
    
    private void func_73722_d(final String p_73722_1_) {
        this.currentlyDisplayedText = p_73722_1_;
        if (!this.mc.running) {
            if (!this.field_73724_e) {
                throw new MinecraftError();
            }
        }
        else {
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            if (OpenGlHelper.isFramebufferEnabled()) {
                final int var2 = this.field_146587_f.getScaleFactor();
                GlStateManager.ortho(0.0, this.field_146587_f.getScaledWidth() * var2, this.field_146587_f.getScaledHeight() * var2, 0.0, 100.0, 300.0);
            }
            else {
                final ScaledResolution var3 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
                GlStateManager.ortho(0.0, var3.getScaledWidth_double(), var3.getScaledHeight_double(), 0.0, 100.0, 300.0);
            }
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0f, 0.0f, -200.0f);
        }
    }
    
    @Override
    public void displayLoadingString(final String message) {
        if (!this.mc.running) {
            if (!this.field_73724_e) {
                throw new MinecraftError();
            }
        }
        else {
            this.field_73723_d = 0L;
            this.field_73727_a = message;
            this.setLoadingProgress(-1);
            this.field_73723_d = 0L;
        }
    }
    
    @Override
    public void setLoadingProgress(final int progress) {
        if (!this.mc.running) {
            if (!this.field_73724_e) {
                throw new MinecraftError();
            }
        }
        else {
            final long var2 = Minecraft.getSystemTime();
            if (var2 - this.field_73723_d >= 100L) {
                this.field_73723_d = var2;
                final ScaledResolution var3 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
                final int var4 = var3.getScaleFactor();
                final int var5 = var3.getScaledWidth();
                final int var6 = var3.getScaledHeight();
                if (OpenGlHelper.isFramebufferEnabled()) {
                    this.field_146588_g.framebufferClear();
                }
                else {
                    GlStateManager.clear(256);
                }
                this.field_146588_g.bindFramebuffer(false);
                GlStateManager.matrixMode(5889);
                GlStateManager.loadIdentity();
                GlStateManager.ortho(0.0, var3.getScaledWidth_double(), var3.getScaledHeight_double(), 0.0, 100.0, 300.0);
                GlStateManager.matrixMode(5888);
                GlStateManager.loadIdentity();
                GlStateManager.translate(0.0f, 0.0f, -200.0f);
                if (!OpenGlHelper.isFramebufferEnabled()) {
                    GlStateManager.clear(16640);
                }
                final Tessellator var7 = Tessellator.getInstance();
                final WorldRenderer var8 = var7.getWorldRenderer();
                this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
                final float var9 = 32.0f;
                var8.startDrawingQuads();
                var8.func_178991_c(4210752);
                var8.addVertexWithUV(0.0, var6, 0.0, 0.0, var6 / var9);
                var8.addVertexWithUV(var5, var6, 0.0, var5 / var9, var6 / var9);
                var8.addVertexWithUV(var5, 0.0, 0.0, var5 / var9, 0.0);
                var8.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0);
                var7.draw();
                if (progress >= 0) {
                    final byte var10 = 100;
                    final byte var11 = 2;
                    final int var12 = var5 / 2 - var10 / 2;
                    final int var13 = var6 / 2 + 16;
                    GlStateManager.disableTextures();
                    var8.startDrawingQuads();
                    var8.func_178991_c(8421504);
                    var8.addVertex(var12, var13, 0.0);
                    var8.addVertex(var12, var13 + var11, 0.0);
                    var8.addVertex(var12 + var10, var13 + var11, 0.0);
                    var8.addVertex(var12 + var10, var13, 0.0);
                    var8.func_178991_c(8454016);
                    var8.addVertex(var12, var13, 0.0);
                    var8.addVertex(var12, var13 + var11, 0.0);
                    var8.addVertex(var12 + progress, var13 + var11, 0.0);
                    var8.addVertex(var12 + progress, var13, 0.0);
                    var7.draw();
                    GlStateManager.enableTextures();
                }
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                this.mc.fontRendererObj.drawStringWithShadow(this.currentlyDisplayedText, (var5 - this.mc.fontRendererObj.getStringWidth(this.currentlyDisplayedText)) / 2, var6 / 2 - 4 - 16, 16777215);
                this.mc.fontRendererObj.drawStringWithShadow(this.field_73727_a, (var5 - this.mc.fontRendererObj.getStringWidth(this.field_73727_a)) / 2, var6 / 2 - 4 + 8, 16777215);
                this.field_146588_g.unbindFramebuffer();
                if (OpenGlHelper.isFramebufferEnabled()) {
                    this.field_146588_g.framebufferRender(var5 * var4, var6 * var4);
                }
                this.mc.func_175601_h();
                try {
                    Thread.yield();
                }
                catch (Exception ex) {}
            }
        }
    }
    
    @Override
    public void setDoneWorking() {
    }
}
