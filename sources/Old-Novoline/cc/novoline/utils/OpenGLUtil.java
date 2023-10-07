package cc.novoline.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.OpenGLException;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public final class OpenGLUtil {

    private static final Minecraft mc = Minecraft.getInstance();

    private static ShaderGroup blurShader;
    private static Framebuffer blurShaderFramebuffer;

    private static final Object MUTEX = new Object();

    private static int lastDisplayWidth, lastDisplayHeight;

    public static void blurScissor(int right, int top, int left, int down, boolean overlay) {
        if (mc.gameSettings.guiScale != 2) {
            return;
        }
        glEnable(GL_SCISSOR_TEST);
        scaledScissor(right, top, left, down);

        glPushMatrix();
        blurShaderFramebuffer.framebufferRenderExt(Minecraft.getInstance().displayWidth, Minecraft.getInstance().displayHeight, true);
        glPopMatrix();

        if (overlay) {
            Minecraft.getInstance().entityRenderer.setupOverlayRendering();
        }

        GlStateManager.enableDepth();

        glDisable(GL_SCISSOR_TEST);
    }

    public static void blurFully(boolean overlay) {
        if (mc.gameSettings.guiScale != 2) {
            return;
        }
        mc.getFramebuffer().unbindFramebuffer();
        blurShaderFramebuffer.bindFramebuffer(true);
        mc.getFramebuffer().framebufferRenderExt(mc.displayWidth, mc.displayHeight, true);

        if (OpenGlHelper.shadersSupported && blurShader != null) {
            GlStateManager.matrixMode(GL_TEXTURE);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            blurShader.loadShaderGroup(mc.timer.renderPartialTicks);
            GlStateManager.popMatrix();
        }

        blurShaderFramebuffer.unbindFramebuffer();
        mc.getFramebuffer().bindFramebuffer(true);

        if (overlay) {
            mc.entityRenderer.setupOverlayRendering();
        }
    }

    public static void bindShader() {
        if (mc.gameSettings.guiScale != 2) {
            return;
        }

        if (blurShader != null &&
                mc.displayWidth == lastDisplayWidth &&
                mc.displayHeight == lastDisplayHeight) {
            return;
        }

        synchronized (MUTEX) {
            blurShaderFramebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, false);
            blurShaderFramebuffer.setFramebufferColor(0.0f, 0.0f, 0.0f, 0.0f);

            if (OpenGlHelper.isFramebufferEnabled()) {
                try {
                    blurShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), blurShaderFramebuffer, new ResourceLocation("shaders/post/blur.json"));
                    blurShader.createBindFramebuffers(mc.displayWidth, mc.displayHeight);

                    lastDisplayWidth = mc.displayWidth;
                    lastDisplayHeight = mc.displayHeight;
                } catch (Exception ex) {
                    throw new OpenGLException("Failed to create a shader object");
                }
            } else {
                throw new OpenGLException("Framebuffer is not supported");
            }
        }
    }

    public static void scaledScissor(int right, int top, int left, int down) {
        if (mc.gameSettings.guiScale != 2) {
            return;
        }
        left -= right;
        down -= top;

        Minecraft minecraft = Minecraft.getInstance();
        ScaledResolution scaledResolution = new ScaledResolution(minecraft);

        GL11.glScissor(
                right * scaledResolution.getScaleFactor(),
                minecraft.displayHeight - top * scaledResolution.getScaleFactor() - down * scaledResolution.getScaleFactor(),
                left * scaledResolution.getScaleFactor(),
                down * scaledResolution.getScaleFactor()
        );
    }

    public static int interpolateColor(Color color1, Color color2, float fraction) {
        int red = (int) (color1.getRed() + (color2.getRed() - color1.getRed()) * fraction);
        int green = (int) (color1.getGreen() + (color2.getGreen() - color1.getGreen()) * fraction);
        int blue = (int) (color1.getBlue() + (color2.getBlue() - color1.getBlue()) * fraction);
        int alpha = (int) (color1.getAlpha() + (color2.getAlpha() - color1.getAlpha()) * fraction);
        try {
            return new Color(red, green, blue, alpha).getRGB();
        } catch (Exception ex) {
            return 0xffffffff;
        }
    }

    private OpenGLUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}