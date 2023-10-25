/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.util;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.nio.IntBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class ScreenShotHelper {
    private static int[] pixelValues;
    private static final DateFormat dateFormat;
    private static final Logger logger;
    private static IntBuffer pixelBuffer;

    public static IChatComponent saveScreenshot(File file, int n, int n2, Framebuffer framebuffer) {
        return ScreenShotHelper.saveScreenshot(file, null, n, n2, framebuffer);
    }

    public static IChatComponent saveScreenshot(File file, String string, int n, int n2, Framebuffer framebuffer) {
        try {
            File file2 = new File(file, "screenshots");
            file2.mkdir();
            if (OpenGlHelper.isFramebufferEnabled()) {
                n = framebuffer.framebufferTextureWidth;
                n2 = framebuffer.framebufferTextureHeight;
            }
            int n3 = n * n2;
            if (pixelBuffer == null || pixelBuffer.capacity() < n3) {
                pixelBuffer = BufferUtils.createIntBuffer((int)n3);
                pixelValues = new int[n3];
            }
            GL11.glPixelStorei((int)3333, (int)1);
            GL11.glPixelStorei((int)3317, (int)1);
            pixelBuffer.clear();
            if (OpenGlHelper.isFramebufferEnabled()) {
                GlStateManager.bindTexture(framebuffer.framebufferTexture);
                GL11.glGetTexImage((int)3553, (int)0, (int)32993, (int)33639, (IntBuffer)pixelBuffer);
            } else {
                GL11.glReadPixels((int)0, (int)0, (int)n, (int)n2, (int)32993, (int)33639, (IntBuffer)pixelBuffer);
            }
            pixelBuffer.get(pixelValues);
            TextureUtil.processPixelValues(pixelValues, n, n2);
            BufferedImage bufferedImage = null;
            if (OpenGlHelper.isFramebufferEnabled()) {
                int n4;
                bufferedImage = new BufferedImage(framebuffer.framebufferWidth, framebuffer.framebufferHeight, 1);
                int n5 = n4 = framebuffer.framebufferTextureHeight - framebuffer.framebufferHeight;
                while (n5 < framebuffer.framebufferTextureHeight) {
                    int n6 = 0;
                    while (n6 < framebuffer.framebufferWidth) {
                        bufferedImage.setRGB(n6, n5 - n4, pixelValues[n5 * framebuffer.framebufferTextureWidth + n6]);
                        ++n6;
                    }
                    ++n5;
                }
            } else {
                bufferedImage = new BufferedImage(n, n2, 1);
                bufferedImage.setRGB(0, 0, n, n2, pixelValues, 0, n);
            }
            File file3 = string == null ? ScreenShotHelper.getTimestampedPNGFileForDirectory(file2) : new File(file2, string);
            ImageIO.write((RenderedImage)bufferedImage, "png", file3);
            ChatComponentText chatComponentText = new ChatComponentText(file3.getName());
            chatComponentText.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file3.getAbsolutePath()));
            chatComponentText.getChatStyle().setUnderlined(true);
            return new ChatComponentTranslation("screenshot.success", chatComponentText);
        }
        catch (Exception exception) {
            logger.warn("Couldn't save screenshot", (Throwable)exception);
            return new ChatComponentTranslation("screenshot.failure", exception.getMessage());
        }
    }

    private static File getTimestampedPNGFileForDirectory(File file) {
        String string = dateFormat.format(new Date()).toString();
        int n = 1;
        File file2;
        while ((file2 = new File(file, String.valueOf(string) + (n == 1 ? "" : "_" + n) + ".png")).exists()) {
            ++n;
        }
        return file2;
    }

    static {
        logger = LogManager.getLogger();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    }
}

