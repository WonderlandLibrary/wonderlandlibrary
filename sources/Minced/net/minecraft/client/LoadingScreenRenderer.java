// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client;

import net.optifine.CustomLoadingScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.gui.Gui;
import net.optifine.CustomLoadingScreens;
import net.minecraft.client.renderer.Tessellator;
import net.optifine.reflect.Reflector;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MinecraftError;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.IProgressUpdate;

public class LoadingScreenRenderer implements IProgressUpdate
{
    private String message;
    private final Minecraft mc;
    private String currentlyDisplayedText;
    private long systemTime;
    private boolean loadingSuccess;
    private final ScaledResolution scaledResolution;
    private final Framebuffer framebuffer;
    
    public LoadingScreenRenderer(final Minecraft mcIn) {
        this.message = "";
        this.currentlyDisplayedText = "";
        this.systemTime = Minecraft.getSystemTime();
        this.mc = mcIn;
        this.scaledResolution = new ScaledResolution(mcIn);
        (this.framebuffer = new Framebuffer(mcIn.displayWidth, mcIn.displayHeight, false)).setFramebufferFilter(9728);
    }
    
    @Override
    public void resetProgressAndMessage(final String message) {
        this.loadingSuccess = false;
        this.displayString(message);
    }
    
    @Override
    public void displaySavingString(final String message) {
        this.loadingSuccess = true;
        this.displayString(message);
    }
    
    private void displayString(final String message) {
        this.currentlyDisplayedText = message;
        if (!this.mc.running) {
            if (!this.loadingSuccess) {
                throw new MinecraftError();
            }
        }
        else {
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            if (OpenGlHelper.isFramebufferEnabled()) {
                final ScaledResolution scaledResolution = this.scaledResolution;
                final int i = ScaledResolution.getScaleFactor();
                GlStateManager.ortho(0.0, this.scaledResolution.getScaledWidth() * i, this.scaledResolution.getScaledHeight() * i, 0.0, 100.0, 300.0);
            }
            else {
                final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
                GlStateManager.ortho(0.0, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0, 100.0, 300.0);
            }
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0f, 0.0f, -200.0f);
        }
    }
    
    @Override
    public void displayLoadingString(final String message) {
        if (!this.mc.running) {
            if (!this.loadingSuccess) {
                throw new MinecraftError();
            }
        }
        else {
            this.systemTime = 0L;
            this.message = message;
            this.setLoadingProgress(-1);
            this.systemTime = 0L;
        }
    }
    
    @Override
    public void setLoadingProgress(final int progress) {
        if (!this.mc.running) {
            if (!this.loadingSuccess) {
                throw new MinecraftError();
            }
        }
        else {
            final long i = Minecraft.getSystemTime();
            if (i - this.systemTime >= 100L) {
                this.systemTime = i;
                final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
                final int j = ScaledResolution.getScaleFactor();
                final int k = scaledresolution.getScaledWidth();
                final int l = scaledresolution.getScaledHeight();
                if (OpenGlHelper.isFramebufferEnabled()) {
                    this.framebuffer.framebufferClear();
                }
                else {
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
                boolean flag = true;
                if (Reflector.FMLClientHandler_handleLoadingScreen.exists()) {
                    final Object object = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
                    if (object != null) {
                        flag = !Reflector.callBoolean(object, Reflector.FMLClientHandler_handleLoadingScreen, scaledresolution);
                    }
                }
                if (flag) {
                    final Tessellator tessellator = Tessellator.getInstance();
                    final BufferBuilder bufferbuilder = tessellator.getBuffer();
                    final CustomLoadingScreen customloadingscreen = CustomLoadingScreens.getCustomLoadingScreen();
                    if (customloadingscreen != null) {
                        customloadingscreen.drawBackground(scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight());
                    }
                    else {
                        this.mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
                        final float f = 32.0f;
                        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                        bufferbuilder.pos(0.0, l, 0.0).tex(0.0, l / 32.0f).color(64, 64, 64, 255).endVertex();
                        bufferbuilder.pos(k, l, 0.0).tex(k / 32.0f, l / 32.0f).color(64, 64, 64, 255).endVertex();
                        bufferbuilder.pos(k, 0.0, 0.0).tex(k / 32.0f, 0.0).color(64, 64, 64, 255).endVertex();
                        bufferbuilder.pos(0.0, 0.0, 0.0).tex(0.0, 0.0).color(64, 64, 64, 255).endVertex();
                        tessellator.draw();
                    }
                    if (progress >= 0) {
                        final int l2 = 100;
                        final int i2 = 2;
                        final int j2 = k / 2 - 50;
                        final int k2 = l / 2 + 16;
                        GlStateManager.disableTexture2D();
                        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
                        bufferbuilder.pos(j2, k2, 0.0).color(128, 128, 128, 255).endVertex();
                        bufferbuilder.pos(j2, k2 + 2, 0.0).color(128, 128, 128, 255).endVertex();
                        bufferbuilder.pos(j2 + 100, k2 + 2, 0.0).color(128, 128, 128, 255).endVertex();
                        bufferbuilder.pos(j2 + 100, k2, 0.0).color(128, 128, 128, 255).endVertex();
                        bufferbuilder.pos(j2, k2, 0.0).color(128, 255, 128, 255).endVertex();
                        bufferbuilder.pos(j2, k2 + 2, 0.0).color(128, 255, 128, 255).endVertex();
                        bufferbuilder.pos(j2 + progress, k2 + 2, 0.0).color(128, 255, 128, 255).endVertex();
                        bufferbuilder.pos(j2 + progress, k2, 0.0).color(128, 255, 128, 255).endVertex();
                        tessellator.draw();
                        GlStateManager.enableTexture2D();
                    }
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    this.mc.fontRenderer.drawStringWithShadow(this.currentlyDisplayedText, (float)((k - this.mc.fontRenderer.getStringWidth(this.currentlyDisplayedText)) / 2), (float)(l / 2 - 4 - 16), 16777215);
                    this.mc.fontRenderer.drawStringWithShadow(this.message, (float)((k - this.mc.fontRenderer.getStringWidth(this.message)) / 2), (float)(l / 2 - 4 + 8), 16777215);
                }
                this.framebuffer.unbindFramebuffer();
                if (OpenGlHelper.isFramebufferEnabled()) {
                    this.framebuffer.framebufferRender(k * j, l * j);
                }
                this.mc.updateDisplay();
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
