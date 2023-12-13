package dev.rise.ui.splash;

import dev.rise.Rise;
import dev.rise.font.CustomFont;
import dev.rise.font.fontrenderer.TTFFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public final class SplashScreen {
    // Max amount of progress updates
    private static final int DEFAULT_MAX = 14;
    // Font renderer
    private static final TTFFontRenderer fontRenderer = CustomFont.FONT_MANAGER.getFont("Dreamscape 96");
    // Current progress
    private static int PROGRESS;
    // Currently displayed progress text
    private static String CURRENT = "";
    // Background texture
    private static ResourceLocation splash;
    // Texture manager
    private static TextureManager ctm;

    /**
     * Update the splash text
     */
    public static void update() {
        if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().getLanguageManager() == null) return;
        drawSplash(Minecraft.getMinecraft().getTextureManager());
    }

    /**
     * Update the splash progress
     *
     * @param givenProgress Stage displayed on the splash
     * @param givenSplash   Text displayed on the splash
     */
    public static void setProgress(final int givenProgress, final String givenSplash) {
        PROGRESS = givenProgress;
        CURRENT = givenSplash;
        update();
    }

    /**
     * Render the splash screen background
     *
     * @param tm {@link TextureManager}
     */
    public static void drawSplash(final TextureManager tm) {
        // Initialize the texture manager if null
        if (ctm == null) ctm = tm;

        // Get the users screen width and height to apply
        final ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());

        // Create the scale factor
        final int scaleFactor = scaledresolution.getScaleFactor();

        // Bind the width and height to the framebuffer
        final Framebuffer framebuffer = new Framebuffer(scaledresolution.getScaledWidth() * scaleFactor,
                scaledresolution.getScaledHeight() * scaleFactor, true);
        framebuffer.bindFramebuffer(false);

        // Create the projected image to be rendered
        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();

        // Initialize the splash texture
        if (splash == null) {
            splash = new ResourceLocation("rise/backgrounds/blue.png");
        }

        // Bind the texture
        tm.bindTexture(splash);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // Draw the image
        Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, 1920, 1080,
                scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), 1920, 1080);

        // Draw the progress bar
        drawProgress();

        // Unbind the width and height as it's no longer needed
        framebuffer.unbindFramebuffer();

        // Render the previously used frame buffer
        framebuffer.framebufferRender(scaledresolution.getScaledWidth() * scaleFactor, scaledresolution.getScaledHeight() * scaleFactor);

        // Update the texture to enable alpha drawing
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);

        // Update the users screen
        Minecraft.getMinecraft().updateDisplay();
    }

    /**
     * Render the progress bar and text
     */
    private static void drawProgress() {
        if (Minecraft.getMinecraft().gameSettings == null || Minecraft.getMinecraft().getTextureManager() == null)
            return;

        // Get the users screen width and height to apply
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        // Get the wanted x position
        final float startX = (sr.getScaledWidth() / 2.0F - 55);
        final float endX = (sr.getScaledWidth() / 2.0F + 55);

        // Calculate the progress bar
        final double nProgress = PROGRESS;
        final double calc = (nProgress / DEFAULT_MAX) * 110;

        // Draw the transparent bar before the green bar
        Gui.drawRect(startX, sr.getScaledHeight() / 2.0F + 15.0F, endX, sr.getScaledHeight() / 2.0F + 20.0F, new Color(255, 255, 255, 60).getRGB());

        // Render the blue progress bar
        Gui.drawRect(startX, sr.getScaledHeight() / 2.0F + 15.0F, (float) (startX + calc), sr.getScaledHeight() / 2.0F + 20.0F, new Color(255, 255, 255, 60).getRGB());

        // Render the rise text
        final float width = fontRenderer.getWidth(Rise.CLIENT_NAME);
        final float height = fontRenderer.getHeight(Rise.CLIENT_NAME);

        final float x = (sr.getScaledWidth() / 2.0F) - (width / 2.0F);
        final float y = (sr.getScaledHeight() / 2.0F) - (height / 2.0F) - 5;

        fontRenderer.drawString(Rise.CLIENT_NAME, x, y, new Color(255, 255, 255, 150).getRGB());
    }
}
