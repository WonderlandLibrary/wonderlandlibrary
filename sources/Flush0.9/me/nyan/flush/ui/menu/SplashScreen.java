package me.nyan.flush.ui.menu;

import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

public class SplashScreen {
    private final int max;
    private int progress;
    private final Minecraft mc;

    public SplashScreen(int max) {
        mc = Minecraft.getMinecraft();
        this.max = max;
    }

    public void update() {
        if (mc == null || mc.getLanguageManager() == null) {
            return;
        }

        drawSplash();
    }

    public void increment() {
        progress++;
        update();
    }

    public void drawSplash() {
        ScaledResolution sr = new ScaledResolution(mc);
        int scaleFactor = sr.getScaleFactor();
        Framebuffer framebuffer = new Framebuffer(sr.getScaledWidth(), sr.getScaledHeight() * scaleFactor, true);
        framebuffer.bindFramebuffer(false);

        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, sr.getScaledWidth(), sr.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.resetColor();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        Gui.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), Color.BLACK.getRGB());

        GlStateManager.resetColor();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        RenderUtils.drawImageCentered(
                new ResourceLocation("flush/logo_light_407x152.png"),
                sr.getScaledWidth() / 2F,
                sr.getScaledHeight() / 2F,
                407 / 2F,
                152 / 2F
        );

        Gui.drawRect(
                sr.getScaledWidth() / 2f - sr.getScaledWidth() / 4f,
                sr.getScaledHeight() - sr.getScaledHeight() / 8f - 8,
                sr.getScaledWidth() / 2f + sr.getScaledWidth() / 4f,
                sr.getScaledHeight() - sr.getScaledHeight() / 8f,
                0xFF1C1C1C
        );
        Gui.drawRect(
                sr.getScaledWidth() / 2f - sr.getScaledWidth() / 4f + 1,
                sr.getScaledHeight() - sr.getScaledHeight() / 8f - 7,
                sr.getScaledWidth() / 2f - sr.getScaledWidth() / 4f + 1 +
                        (progress / (float) max) * (sr.getScaledWidth() / 2f - 2),
                sr.getScaledHeight() - sr.getScaledHeight() / 8f - 1,
                0xFF0099FF
        );
        framebuffer.unbindFramebuffer();
        framebuffer.framebufferRender(sr.getScaledWidth() * scaleFactor, sr.getScaledHeight() * scaleFactor);

        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);

        mc.updateDisplay();
    }

    public int getMax() {
        return max;
    }

    public int getProgress() {
        return progress;
    }
}
