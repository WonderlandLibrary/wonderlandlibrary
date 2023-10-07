package net.minecraft.util;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ScreenShotHelper {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");

    /**
     * A buffer to hold pixel values returned by OpenGL.
     */
    private static IntBuffer pixelBuffer;

    /**
     * The built-up array that contains all the pixel values returned by OpenGL.
     */
    private static int[] pixelValues;

    /**
     * Saves a screenshot in the game directory with a time-stamped filename.  Args: gameDirectory,
     * requestedWidthInPixels, requestedHeightInPixels, frameBuffer
     */
    public static void saveScreenshot(File gameDirectory, int width, int height, Framebuffer buffer) {
        saveScreenshot(gameDirectory, null, width, height, buffer);
    }

    /**
     * Saves a screenshot in the game directory with the given file name (or null to generate a time-stamped name).
     * Args: gameDirectory, fileName, requestedWidthInPixels, requestedHeightInPixels, frameBuffer
     */
    public static void saveScreenshot(File gameDirectory, String screenshotName, int width, int height, Framebuffer buffer) {
        try {
            final File file1 = new File(gameDirectory, "screenshots");
            file1.mkdir();

            if (OpenGlHelper.isFramebufferEnabled()) {
                width = buffer.framebufferTextureWidth;
                height = buffer.framebufferTextureHeight;
            }

            final int i = width * height;

            if (pixelBuffer == null || pixelBuffer.capacity() < i) {
                pixelBuffer = BufferUtils.createIntBuffer(i);
                pixelValues = new int[i];
            }

            GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            pixelBuffer.clear();

            if (OpenGlHelper.isFramebufferEnabled()) {
                GlStateManager.bindTexture(buffer.framebufferTexture);
                GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
            } else {
                GL11.glReadPixels(0, 0, buffer.framebufferWidth, buffer.framebufferHeight, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
            }

            new Thread("ss") {

                @Override
                public void run() {
                    pixelBuffer.get(pixelValues);
                    TextureUtil.processPixelValues(pixelValues, buffer.framebufferWidth, buffer.framebufferHeight);

                    BufferedImage bufferedimage = null;

                    if (OpenGlHelper.isFramebufferEnabled()) {
                        bufferedimage = new BufferedImage(buffer.framebufferWidth, buffer.framebufferHeight, 1);
                        final int j = buffer.framebufferTextureHeight - buffer.framebufferHeight;

                        for (int k = j; k < buffer.framebufferTextureHeight; ++k) {
                            for (int l = 0; l < buffer.framebufferWidth; ++l) {
                                bufferedimage.setRGB(l, k - j, pixelValues[k * buffer.framebufferTextureWidth + l]);
                            }
                        }
                    } else {
                        bufferedimage = new BufferedImage(buffer.framebufferWidth, buffer.framebufferHeight, 1);
                        bufferedimage.setRGB(0, 0, buffer.framebufferWidth, buffer.framebufferHeight, pixelValues, 0, buffer.framebufferWidth);
                    }


                    final File file2;

                    if (screenshotName == null) {
                        file2 = getTimestampedPNGFileForDirectory(file1);
                    } else {
                        file2 = new File(file1, screenshotName);
                    }

                    try {
                        ImageIO.write(bufferedimage, "png", file2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    super.run();
                }
            }.start();
        } catch (Exception exception) {
            LOGGER.warn("Couldn't save screenshot", exception);
        }
    }

    /**
     * Creates a unique PNG file in the given directory named by a timestamp.  Handles cases where the timestamp alone
     * is not enough to create a uniquely named file, though it still might suffer from an unlikely race condition where
     * the filename was unique when this method was called, but another process or thread created a file at the same
     * path immediately after this method returned.
     */
    private static File getTimestampedPNGFileForDirectory(File gameDirectory) {
        final String s = dateFormat.format(new Date());
        int i = 1;

        while (true) {
            final File file1 = new File(gameDirectory, s + (i == 1 ? "" : "_" + i) + ".png");

            if (!file1.exists()) {
                return file1;
            }

            ++i;
        }
    }

}
