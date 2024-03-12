// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui;

import dev.tenacity.utils.animations.Direction;
import dev.tenacity.Tenacity;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.animations.impl.EaseBackIn;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import dev.tenacity.utils.font.CustomFont;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import dev.tenacity.utils.render.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import dev.tenacity.utils.animations.Animation;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import dev.tenacity.utils.Utils;

public class SplashScreen implements Utils
{
    private static int currentProgress;
    private static ResourceLocation splash;
    private static Framebuffer framebuffer;
    static ResourceLocation image;
    private static int count;
    private static Animation fadeAnim;
    private static Animation moveAnim;
    private static Animation versionAnim;
    private static Animation progressAnim;
    private static Animation progress2Anim;
    
    public static void continueCount() {
        continueCount(true);
    }
    
    public static void continueCount(final boolean continueCount) {
        drawSplash();
        if (continueCount) {
            ++SplashScreen.count;
        }
    }
    
    private static void drawSplash() {
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        final int scaleFactor = sr.getScaleFactor();
        (SplashScreen.framebuffer = RenderUtil.createFrameBuffer(SplashScreen.framebuffer)).framebufferClear();
        SplashScreen.framebuffer.bindFramebuffer(true);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0, sr.getScaledWidth(), sr.getScaledHeight(), 0.0, 1000.0, 3000.0);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0f, 0.0f, -2000.0f);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.color(0.0f, 0.0f, 0.0f, 0.0f);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        drawSplashBackground((float)sr.getScaledWidth(), (float)sr.getScaledHeight(), 1.0f);
        RenderUtil.resetColor();
        GL11.glEnable(3042);
        RenderUtil.setAlphaLimit(0.0f);
        final CustomFont fr = SplashScreen.tenacityBoldFont80;
        if (SplashScreen.count > 3) {
            SplashScreen.count = 0;
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SplashScreen.count; ++i) {
            sb.append(".");
        }
        fr.drawCenteredString("Loading" + (Object)sb, sr.getScaledWidth() / 2.0f, fr.getMiddleOfBox((float)sr.getScaledHeight()), -1);
        SplashScreen.framebuffer.unbindFramebuffer();
        SplashScreen.framebuffer.framebufferRender(sr.getScaledWidth() * scaleFactor, sr.getScaledHeight() * scaleFactor);
        RenderUtil.setAlphaLimit(1.0f);
        Minecraft.getMinecraft().updateDisplay();
    }
    
    private static void drawScreen(final float width, final float height) {
        Gui.drawRect2(0.0, 0.0, width, height, Color.BLACK.getRGB());
        if (SplashScreen.fadeAnim == null) {
            SplashScreen.fadeAnim = new DecelerateAnimation(600, 1.0);
            SplashScreen.moveAnim = new DecelerateAnimation(600, 1.0);
        }
        drawSplashBackground(width, height, 1.0f);
        final CustomFont fr = SplashScreen.tenacityBoldFont80;
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SplashScreen.count; ++i) {
            sb.append(".");
        }
        fr.drawCenteredString("Finshed!", width / 2.0f, fr.getMiddleOfBox(height), ColorUtil.applyOpacity(-1, 1.0f - SplashScreen.fadeAnim.getOutput().floatValue()));
        final float yMovement = (SplashScreen.progressAnim != null && SplashScreen.progressAnim.getDirection().backwards()) ? (1.0f - SplashScreen.progressAnim.getOutput().floatValue()) : 0.0f;
        final float actualY = fr.getMiddleOfBox(height) - 40.0f - 48.0f * yMovement;
        fr.drawCenteredString("Tenacity", width / 2.0f, actualY, ColorUtil.applyOpacity(-1, SplashScreen.moveAnim.getOutput().floatValue()));
        if (SplashScreen.moveAnim.isDone() && SplashScreen.versionAnim == null) {
            SplashScreen.versionAnim = new EaseBackIn(500, 1.0, 2.0f);
        }
        if (SplashScreen.versionAnim != null) {
            final float versionWidth = SplashScreen.tenacityFont32.getStringWidth("6.0") / 2.0f;
            final float versionX = width / 2.0f + fr.getStringWidth("Tenacity") / 2.0f - versionWidth;
            final float versionY = SplashScreen.tenacityFont32.getMiddleOfBox(height) - 57.0f - 48.0f * yMovement;
            RenderUtil.scaleStart(versionX + versionWidth, versionY + SplashScreen.tenacityFont32.getHeight() / 2.0f, SplashScreen.versionAnim.getOutput().floatValue());
            SplashScreen.tenacityFont32.drawSmoothString("6.0", versionX, versionY, ColorUtil.applyOpacity(-1, SplashScreen.versionAnim.getOutput().floatValue()));
            RenderUtil.scaleEnd();
        }
        if (SplashScreen.progressAnim == null) {
            if (SplashScreen.moveAnim.isDone()) {
                SplashScreen.progressAnim = new DecelerateAnimation(250, 1.0);
                SplashScreen.progress2Anim = new DecelerateAnimation(1800, 1.0);
            }
        }
        else {
            final float rectWidth = fr.getStringWidth("Tenacity") + 10.0f;
            final float rectHeight = 5.0f;
            final float roundX = width / 2.0f - rectWidth / 2.0f;
            final float roundY = height / 2.0f - rectHeight / 2.0f;
            final float roundAlpha = SplashScreen.progressAnim.getOutput().floatValue();
            RoundedUtil.drawRound(roundX, height / 2.0f - rectHeight / 2.0f, rectWidth, rectHeight, rectHeight / 2.0f - 0.25f, ColorUtil.tripleColor(50, roundAlpha));
            final float progress = SplashScreen.progress2Anim.getOutput().floatValue();
            final Color color1 = ColorUtil.interpolateColorC(Tenacity.INSTANCE.getClientColor(), Tenacity.INSTANCE.getAlternateClientColor(), progress);
            final Color color2 = ColorUtil.interpolateColorC(Tenacity.INSTANCE.getAlternateClientColor(), Tenacity.INSTANCE.getClientColor(), progress);
            RoundedUtil.drawGradientHorizontal(roundX, roundY, rectWidth * progress, rectHeight, rectHeight / 2.0f - 0.25f, ColorUtil.applyOpacity(color1, roundAlpha), ColorUtil.applyOpacity(color2, roundAlpha));
            final float textAlpha = SplashScreen.progress2Anim.getDirection().forwards() ? SplashScreen.progress2Anim.getOutput().floatValue() : 1.0f;
            SplashScreen.tenacityFont18.drawCenteredString("edited by Razzle", width / 2.0f, actualY + 42.0f, ColorUtil.applyOpacity(-1, textAlpha));
            if (SplashScreen.progressAnim.finished(Direction.FORWARDS) && SplashScreen.progress2Anim.finished(Direction.FORWARDS)) {
                SplashScreen.progressAnim.changeDirection();
            }
        }
    }
    
    public static void drawScreen() {
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        final int scaleFactor = sr.getScaleFactor();
        SplashScreen.framebuffer = RenderUtil.createFrameBuffer(SplashScreen.framebuffer);
        while (SplashScreen.progressAnim == null || !SplashScreen.progressAnim.finished(Direction.BACKWARDS)) {
            SplashScreen.framebuffer.framebufferClear();
            SplashScreen.framebuffer.bindFramebuffer(true);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0, sr.getScaledWidth(), sr.getScaledHeight(), 0.0, 1000.0, 3000.0);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0f, 0.0f, -2000.0f);
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            GlStateManager.disableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.color(0.0f, 0.0f, 0.0f, 0.0f);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            drawScreen((float)sr.getScaledWidth(), (float)sr.getScaledHeight());
            SplashScreen.framebuffer.unbindFramebuffer();
            SplashScreen.framebuffer.framebufferRender(sr.getScaledWidth() * scaleFactor, sr.getScaledHeight() * scaleFactor);
            RenderUtil.setAlphaLimit(1.0f);
            Minecraft.getMinecraft().updateDisplay();
        }
    }
    
    public static void drawSplashBackground(final float width, final float height, final float alpha) {
        RenderUtil.resetColor();
        GlStateManager.color(1.0f, 1.0f, 1.0f, alpha);
        SplashScreen.mc.getTextureManager().bindTexture(new ResourceLocation("Tenacity/splashscreen.png"));
        Gui.drawModalRectWithCustomSizedTexture(0.0f, 0.0f, 0.0f, 0.0f, width, height, width, height);
    }
    
    static {
        SplashScreen.image = new ResourceLocation("Tenacity/splashscreen.png");
    }
}
