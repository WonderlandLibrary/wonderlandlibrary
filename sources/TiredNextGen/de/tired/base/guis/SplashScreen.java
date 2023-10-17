package de.tired.base.guis;

import de.tired.util.animation.Animation;
import de.tired.util.animation.Easings;
import de.tired.util.render.RenderUtil;
import de.tired.base.font.FontManager;
import de.tired.util.render.ColorUtil;
import de.tired.util.render.shaderloader.KoksFramebuffer;
import de.tired.util.render.shaderloader.ShaderManager;
import de.tired.util.render.shaderloader.list.RoundedRectShader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class SplashScreen {

    private static Framebuffer framebuffer;

    private static ResourceLocation splash;

    private static Animation animation = new Animation();

    private static int process;

    public static void update() {
        if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().getLanguageManager() == null) return;
        animation.update();
    }

    public static void setProgress(int givenProgress) {
        process = givenProgress;
        update();
    }

    public static void init() {
        animation.animate(500, .2, Easings.NONE);
    }

    public static void drawSplash() {

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        int scaleFactor = sr.getScaleFactor();

        framebuffer = KoksFramebuffer.doFrameBuffer(framebuffer);


        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);

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

        GlStateManager.color(1, 1, 1, 1);

        if (splash == null) {
            splash = new ResourceLocation("minecraft:textures/startup.png");
        }

        GlStateManager.resetColor();
        GlStateManager.color(1, 1, 1, 1);
        Gui.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(19, 19, 19).getRGB());
        final double widthA = 110;

        Color firstColor = ColorUtil.interpolateColorsBackAndForth(12, 0, new Color(84, 51, 158).brighter(), new Color(104, 127, 203), true);
        Color secondColor = ColorUtil.interpolateColorsBackAndForth(12, 90, new Color(84, 51, 158), new Color(104, 127, 203).darker(), false);

        ShaderManager.shaderBy(RoundedRectShader.class).drawRound((float) (sr.getScaledWidth() / 2f - widthA), sr.getScaledHeight() / 4f + 70, (float) widthA * 2, 4, 2, new Color(255, 255, 255, 255));
        ShaderManager.shaderBy(RoundedRectShader.class).drawRound((float) (sr.getScaledWidth() / 2f - widthA), sr.getScaledHeight() / 4f + 70, process, 4, 2, firstColor);

        RenderUtil.instance.applyGradientHorizontal(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 4f + 50, 200, 120, 1f, firstColor, secondColor, () -> {
            FontManager.raleWay40.drawCenteredString("TIRED", sr.getScaledWidth() / 2f, sr.getScaledHeight() / 4f + 30, -1);
        });
        framebuffer.unbindFramebuffer();
        framebuffer.framebufferRender(sr.getScaledWidth() * scaleFactor, sr.getScaledHeight() * scaleFactor);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        Minecraft.getMinecraft().updateDisplay();

    }

}