package ru.smertnix.celestial.ui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import ru.smertnix.celestial.ui.font.FontUtil;
import ru.smertnix.celestial.ui.font.MCFontRenderer;
import ru.smertnix.celestial.utils.Helper;
import ru.smertnix.celestial.utils.render.RenderUtils;

import java.awt.*;

public class SplashProgress implements Helper {

    private static final ResourceLocation LOCATION_MOJANG_PNG = new ResourceLocation("textures/gui/title/mojang.png");
    public static int Progress;
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
        textureManager.bindTexture(LOCATION_MOJANG_PNG);
        GlStateManager.resetColor();
        GlStateManager.color(1, 1, 1, 1);
        Gui.drawScaledCustomSizeModalRect((sr.getScaledWidth() - 256) / 2, (sr.getScaledHeight() - 256) / 2, 0, 0, 256, 256, 255, 255, 255, 255);
        framebuffer.unbindFramebuffer();
        framebuffer.framebufferRender(sr.getScaledWidth() * scaleFactor, sr.getScaledHeight() * scaleFactor);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        mc.updateDisplay();
    }
}