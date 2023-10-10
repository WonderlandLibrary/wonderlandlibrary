/*
 * Decompiled with CFR 0.152.
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
    private static final Logger logger = LogManager.getLogger();
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    private static IntBuffer pixelBuffer;
    private static int[] pixelValues;

    public static IChatComponent saveScreenshot(File gameDirectory, int width, int height, Framebuffer buffer) {
        return ScreenShotHelper.saveScreenshot(gameDirectory, null, width, height, buffer);
    }

    public static IChatComponent saveScreenshot(File gameDirectory, String screenshotName, int width, int height, Framebuffer buffer) {
        try {
            File file1 = new File(gameDirectory, "screenshots");
            file1.mkdir();
            if (OpenGlHelper.isFramebufferEnabled()) {
                width = buffer.framebufferTextureWidth;
                height = buffer.framebufferTextureHeight;
            }
            int i2 = width * height;
            if (pixelBuffer == null || pixelBuffer.capacity() < i2) {
                pixelBuffer = BufferUtils.createIntBuffer(i2);
                pixelValues = new int[i2];
            }
            GL11.glPixelStorei(3333, 1);
            GL11.glPixelStorei(3317, 1);
            pixelBuffer.clear();
            if (OpenGlHelper.isFramebufferEnabled()) {
                GlStateManager.bindTexture(buffer.framebufferTexture);
                GL11.glGetTexImage(3553, 0, 32993, 33639, pixelBuffer);
            } else {
                GL11.glReadPixels(0, 0, width, height, 32993, 33639, pixelBuffer);
            }
            pixelBuffer.get(pixelValues);
            TextureUtil.processPixelValues(pixelValues, width, height);
            BufferedImage bufferedimage = null;
            if (OpenGlHelper.isFramebufferEnabled()) {
                int j2;
                bufferedimage = new BufferedImage(buffer.framebufferWidth, buffer.framebufferHeight, 1);
                for (int k2 = j2 = buffer.framebufferTextureHeight - buffer.framebufferHeight; k2 < buffer.framebufferTextureHeight; ++k2) {
                    for (int l2 = 0; l2 < buffer.framebufferWidth; ++l2) {
                        bufferedimage.setRGB(l2, k2 - j2, pixelValues[k2 * buffer.framebufferTextureWidth + l2]);
                    }
                }
            } else {
                bufferedimage = new BufferedImage(width, height, 1);
                bufferedimage.setRGB(0, 0, width, height, pixelValues, 0, width);
            }
            File file2 = screenshotName == null ? ScreenShotHelper.getTimestampedPNGFileForDirectory(file1) : new File(file1, screenshotName);
            ImageIO.write((RenderedImage)bufferedimage, "png", file2);
            ChatComponentText ichatcomponent = new ChatComponentText(file2.getName());
            ichatcomponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file2.getAbsolutePath()));
            ichatcomponent.getChatStyle().setUnderlined(true);
            return new ChatComponentTranslation("screenshot.success", ichatcomponent);
        }
        catch (Exception exception) {
            logger.warn("Couldn't save screenshot", (Throwable)exception);
            return new ChatComponentTranslation("screenshot.failure", exception.getMessage());
        }
    }

    private static File getTimestampedPNGFileForDirectory(File gameDirectory) {
        String s2 = dateFormat.format(new Date()).toString();
        int i2 = 1;
        File file1;
        while ((file1 = new File(gameDirectory, s2 + (i2 == 1 ? "" : "_" + i2) + ".png")).exists()) {
            ++i2;
        }
        return file1;
    }
}

