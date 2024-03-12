// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import java.util.Date;
import net.minecraft.event.ClickEvent;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.src.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import java.io.File;
import java.nio.IntBuffer;
import java.text.DateFormat;
import org.apache.logging.log4j.Logger;

public class ScreenShotHelper
{
    private static final Logger logger;
    private static final DateFormat dateFormat;
    private static IntBuffer pixelBuffer;
    private static int[] pixelValues;
    
    public static IChatComponent saveScreenshot(final File gameDirectory, final int width, final int height, final Framebuffer buffer) {
        return saveScreenshot(gameDirectory, null, width, height, buffer);
    }
    
    public static IChatComponent saveScreenshot(final File gameDirectory, final String screenshotName, int width, int height, final Framebuffer buffer) {
        try {
            final File file1 = new File(gameDirectory, "screenshots");
            file1.mkdir();
            final Minecraft minecraft = Minecraft.getMinecraft();
            final int i = Config.getGameSettings().guiScale;
            final ScaledResolution scaledresolution = new ScaledResolution(minecraft);
            final int j = scaledresolution.getScaleFactor();
            final int k = Config.getScreenshotSize();
            final boolean flag = OpenGlHelper.isFramebufferEnabled() && k > 1;
            if (flag) {
                Config.getGameSettings().guiScale = j * k;
                resize(width * k, height * k);
                GlStateManager.pushMatrix();
                GlStateManager.clear(16640);
                minecraft.getFramebuffer().bindFramebuffer(true);
                minecraft.entityRenderer.updateCameraAndRender(Config.renderPartialTicks, System.nanoTime());
            }
            if (OpenGlHelper.isFramebufferEnabled()) {
                width = buffer.framebufferTextureWidth;
                height = buffer.framebufferTextureHeight;
            }
            final int l = width * height;
            if (ScreenShotHelper.pixelBuffer == null || ScreenShotHelper.pixelBuffer.capacity() < l) {
                ScreenShotHelper.pixelBuffer = BufferUtils.createIntBuffer(l);
                ScreenShotHelper.pixelValues = new int[l];
            }
            GL11.glPixelStorei(3333, 1);
            GL11.glPixelStorei(3317, 1);
            ScreenShotHelper.pixelBuffer.clear();
            if (OpenGlHelper.isFramebufferEnabled()) {
                GlStateManager.bindTexture(buffer.framebufferTexture);
                GL11.glGetTexImage(3553, 0, 32993, 33639, ScreenShotHelper.pixelBuffer);
            }
            else {
                GL11.glReadPixels(0, 0, width, height, 32993, 33639, ScreenShotHelper.pixelBuffer);
            }
            ScreenShotHelper.pixelBuffer.get(ScreenShotHelper.pixelValues);
            TextureUtil.processPixelValues(ScreenShotHelper.pixelValues, width, height);
            BufferedImage bufferedimage = null;
            if (OpenGlHelper.isFramebufferEnabled()) {
                bufferedimage = new BufferedImage(buffer.framebufferWidth, buffer.framebufferHeight, 1);
                int j2;
                for (int i2 = j2 = buffer.framebufferTextureHeight - buffer.framebufferHeight; j2 < buffer.framebufferTextureHeight; ++j2) {
                    for (int k2 = 0; k2 < buffer.framebufferWidth; ++k2) {
                        bufferedimage.setRGB(k2, j2 - i2, ScreenShotHelper.pixelValues[j2 * buffer.framebufferTextureWidth + k2]);
                    }
                }
            }
            else {
                bufferedimage = new BufferedImage(width, height, 1);
                bufferedimage.setRGB(0, 0, width, height, ScreenShotHelper.pixelValues, 0, width);
            }
            if (flag) {
                minecraft.getFramebuffer().unbindFramebuffer();
                GlStateManager.popMatrix();
                Config.getGameSettings().guiScale = i;
                resize(width, height);
            }
            File file2;
            if (screenshotName == null) {
                file2 = getTimestampedPNGFileForDirectory(file1);
            }
            else {
                file2 = new File(file1, screenshotName);
            }
            file2 = file2.getCanonicalFile();
            ImageIO.write(bufferedimage, "png", file2);
            final IChatComponent ichatcomponent = new ChatComponentText(file2.getName());
            ichatcomponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file2.getAbsolutePath()));
            ichatcomponent.getChatStyle().setUnderlined(true);
            return new ChatComponentTranslation("screenshot.success", new Object[] { ichatcomponent });
        }
        catch (Exception exception) {
            ScreenShotHelper.logger.warn("Couldn't save screenshot", (Throwable)exception);
            return new ChatComponentTranslation("screenshot.failure", new Object[] { exception.getMessage() });
        }
    }
    
    private static File getTimestampedPNGFileForDirectory(final File gameDirectory) {
        final String s = ScreenShotHelper.dateFormat.format(new Date()).toString();
        int i = 1;
        File file1;
        while (true) {
            file1 = new File(gameDirectory, s + ((i == 1) ? "" : ("_" + i)) + ".png");
            if (!file1.exists()) {
                break;
            }
            ++i;
        }
        return file1;
    }
    
    private static void resize(final int p_resize_0_, final int p_resize_1_) {
        final Minecraft minecraft = Minecraft.getMinecraft();
        minecraft.displayWidth = Math.max(1, p_resize_0_);
        minecraft.displayHeight = Math.max(1, p_resize_1_);
        if (minecraft.currentScreen != null) {
            final ScaledResolution scaledresolution = new ScaledResolution(minecraft);
            minecraft.currentScreen.onResize(minecraft, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight());
        }
        updateFramebufferSize();
    }
    
    private static void updateFramebufferSize() {
        final Minecraft minecraft = Minecraft.getMinecraft();
        minecraft.getFramebuffer().createBindFramebuffer(minecraft.displayWidth, minecraft.displayHeight);
        if (minecraft.entityRenderer != null) {
            minecraft.entityRenderer.updateShaderGroupSize(minecraft.displayWidth, minecraft.displayHeight);
        }
    }
    
    static {
        logger = LogManager.getLogger();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    }
}
