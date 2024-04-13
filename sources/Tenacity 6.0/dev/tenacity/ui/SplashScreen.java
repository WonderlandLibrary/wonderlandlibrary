//package dev.tenacity.ui;
//
//
//import java.awt.Color;
//
//import org.lwjgl.opengl.GL11;
//
//import dev.client.tenacity.utils.render.ColorUtil;
//import dev.client.tenacity.utils.render.RenderUtil;
//import dev.utils.animations.Animation;
//import dev.utils.animations.Direction;
//import dev.utils.animations.impl.EaseInOutQuad;
//import dev.utils.font.FontUtil;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.Gui;
//import net.minecraft.client.gui.ScaledResolution;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.client.renderer.texture.TextureManager;
//import net.minecraft.client.shader.Framebuffer;
//import net.minecraft.util.ResourceLocation;
//
//public class SplashScreen {
//
//    private static int currentProgress;
//
//    // Background texture
//    private static ResourceLocation splash;
//    private static Animation expandFade;
//    private static Framebuffer framebuffer;
//
//    public static void update() {
//        if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().getLanguageManager() == null) return;
//      //  drawSplash(Minecraft.getMinecraft().getTextureManager(), false);
//    }
//
//    /**
//     * Use this after you use the other setProgress
//     */
//    public static void setProgress(int givenProgress) {
//        currentProgress = givenProgress;
//        update();
//    }
//
//    public static void init() {
//        expandFade = new EaseInOutQuad(1000, 1);
//    }
//
//    /**
//     * Render the splash screen background
//     */
//    public static void drawSplash() {
//
//        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
//        // Create the scale factor
//        int scaleFactor = sr.getScaleFactor();
//        // Bind the width and height to the framebuffer
//        framebuffer = RenderUtil.createFrameBuffer(framebuffer);
//
//
//            framebuffer.framebufferClear();
//            framebuffer.bindFramebuffer(true);
//
//            // Create the projected image to be rendered
//            GlStateManager.matrixMode(GL11.GL_PROJECTION);
//            GlStateManager.loadIdentity();
//            GlStateManager.ortho(0.0D, sr.getScaledWidth(), sr.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
//            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
//            GlStateManager.loadIdentity();
//            GlStateManager.translate(0.0F, 0.0F, -2000.0F);
//            GlStateManager.disableLighting();
//            GlStateManager.disableFog();
//            GlStateManager.disableDepth();
//            GlStateManager.enableTexture2D();
//
//            GlStateManager.color(0, 0, 0, 0);
//
//            Gui.drawRect2(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(18, 18, 18).getRGB());
//
//            GlStateManager.color(1, 1, 1, 1);
//            GL11.glEnable(GL11.GL_BLEND);
//            float textureWH = 78 / 2f;
//            float x = sr.getScaledWidth() / 2f;
//            float y = sr.getScaledHeight() / 2f;
//            float stringWidth = (float) FontUtil.tenacityBoldFont40.getStringWidth("Authenticating");
//            FontUtil.tenacityBoldFont40.drawSmoothString("Authenticating", x - stringWidth / 2f, y, -1);
//
//
//            // Unbind the width and height as it's no longer needed
//            framebuffer.unbindFramebuffer();
//
//            // Render the previously used frame buffer
//            framebuffer.framebufferRender(sr.getScaledWidth() * scaleFactor, sr.getScaledHeight() * scaleFactor);
//
//            // Update the texture to enable alpha drawing
//            GlStateManager.enableAlpha();
//            GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
//
//            // Update the users screen
//            Minecraft.getMinecraft().updateDisplay();
//
//    }
//
//
//}