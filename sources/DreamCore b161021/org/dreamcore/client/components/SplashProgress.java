package org.dreamcore.client.components;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.dreamcore.client.helpers.Helper;
import org.dreamcore.client.helpers.palette.PaletteHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;

import java.awt.*;

public class SplashProgress implements Helper {

    public static ResourceLocation resourceLocation = new ResourceLocation("dreamcore/launch.png");
    public static int Progress;
    public static int maxProgress = 7;
    public static FontRenderer fontRenderer;

    public static void update() {
        SplashProgress.drawSplash(mc.getTextureManager());
    }

    public static void setProgress(int progress) {
        Progress = progress;
        SplashProgress.update();
    }

    public static void drawSplash(TextureManager textureManager) {
        fontRenderer = new FontRenderer(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.getTextureManager(), false);
        if (mc.gameSettings.language != null) {
            fontRenderer.setUnicodeFlag(mc.isUnicode());
            fontRenderer.setBidiFlag(mc.mcLanguageManager.isCurrentLanguageBidirectional());
        }
        mc.mcResourceManager.registerReloadListener(fontRenderer);
        int scaleFactor = sr.getScaleFactor();
        Framebuffer framebuffer = new Framebuffer(sr.getScaledWidth() * scaleFactor, sr.getScaledHeight() * scaleFactor, true);
        framebuffer.bindFramebuffer(false);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0, sr.getScaledWidth(), sr.getScaledHeight(), 0, 1000, 3000);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0, 0, -2000);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        textureManager.bindTexture(resourceLocation);
        GlStateManager.resetColor();
        GlStateManager.color(1, 1, 1, 1);
        Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, sr.getScaledWidth(), sr.getScaledHeight(), sr.getScaledWidth(), sr.getScaledHeight(), sr.getScaledWidth(), sr.getScaledHeight());
        SplashProgress.drawProgress();
        framebuffer.unbindFramebuffer();
        framebuffer.framebufferRender(sr.getScaledWidth() * scaleFactor, sr.getScaledHeight() * scaleFactor);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        mc.updateDisplay();
    }

    private static void drawProgress() {
        if (mc.gameSettings == null) {
            return;
        }
        float calc = Progress / 7f * sr.getScaledWidth() * 0.595F;
        float color = PaletteHelper.getHealthColor(Progress, maxProgress);
        GlStateManager.resetColor();
        GlStateManager.TextureState.textureName = -1;
        RectHelper.drawSmoothRect(84, sr.getScaledHeight() - 110, sr.getScaledWidth() - 85, sr.getScaledHeight() - 95, new Color(0, 0, 0).getRGB());
        RectHelper.drawSmoothRect(86, sr.getScaledHeight() - 108, sr.getScaledWidth() - 87, sr.getScaledHeight() - 97, new Color(50, 50, 50).getRGB());
        RectHelper.drawRect(86, sr.getScaledHeight() - 108, 86 + calc, sr.getScaledHeight() - 97, (int) color);
    }
}
