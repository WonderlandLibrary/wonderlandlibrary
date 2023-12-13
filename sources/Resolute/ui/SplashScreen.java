// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui;

import vip.Resolute.util.render.RenderUtils;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;

public class SplashScreen
{
    private final int max;
    private int progress;
    private String text;
    
    public SplashScreen() {
        this.max = 10;
    }
    
    public void drawScreen() {
        final ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());
        final int scaleFactor = sc.getScaleFactor();
        final Framebuffer framebuffer = new Framebuffer(sc.getScaledWidth() * scaleFactor, sc.getScaledHeight() * scaleFactor, true);
        framebuffer.bindFramebuffer(true);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0, sc.getScaledWidth(), sc.getScaledHeight(), 0.0, 1000.0, 3000.0);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0f, 0.0f, -2000.0f);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.color(0.0f, 0.0f, 0.0f, 0.0f);
        this.drawProgress(sc);
        GlStateManager.resetColor();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.resetTextureState();
        framebuffer.unbindFramebuffer();
        framebuffer.framebufferRender(sc.getScaledWidth() * scaleFactor, sc.getScaledHeight() * scaleFactor);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        Minecraft.getMinecraft().updateDisplay();
    }
    
    public void drawProgress(final ScaledResolution sc) {
        RenderUtils.drawRect(0.0, 0.0, sc.getScaledWidth(), sc.getScaledHeight(), new Color(18, 18, 18).getRGB());
        final int width = 200;
        final int progress = this.progress / this.max * width;
        RenderUtils.drawRect(sc.getScaledWidth() / 2.0f - 100.0f, sc.getScaledHeight() / 2.0f - 8.0f, sc.getScaledWidth() / 2.0f + 100.0f, sc.getScaledHeight() / 2.0f + 8.0f, -14408668);
        RenderUtils.drawRect(sc.getScaledWidth() / 2.0f - 100.0f + 2.0f, sc.getScaledHeight() / 2.0f - 8.0f + 2.0f, sc.getScaledWidth() / 2.0f - 100.0f + progress - 2.0f, sc.getScaledHeight() / 2.0f + 8.0f - 2.0f, -7272697);
    }
    
    private void resetTextureState() {
        GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName = -1;
    }
    
    public int progress() {
        return this.progress;
    }
    
    public void progress(final int progress, final String text) {
        this.progress = progress;
        this.text = text;
        this.drawScreen();
    }
}
