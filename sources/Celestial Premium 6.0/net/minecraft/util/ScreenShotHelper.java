/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.nio.IntBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import optifine.Config;
import optifine.Reflector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;

public class ScreenShotHelper {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    private static IntBuffer pixelBuffer;
    private static int[] pixelValues;

    public static ITextComponent saveScreenshot(File gameDirectory, int width, int height, Framebuffer buffer) {
        return ScreenShotHelper.saveScreenshot(gameDirectory, null, width, height, buffer);
    }

    public static ITextComponent saveScreenshot(File gameDirectory, @Nullable String screenshotName, int width, int height, Framebuffer buffer) {
        try {
            ITextComponent itextcomponent1;
            boolean flag;
            File file1 = new File(gameDirectory, "screenshots");
            file1.mkdir();
            Minecraft minecraft = Minecraft.getMinecraft();
            int i = Config.getGameSettings().guiScale;
            ScaledResolution scaledresolution = new ScaledResolution(minecraft);
            int j = scaledresolution.getScaleFactor();
            int k = Config.getScreenshotSize();
            boolean bl = flag = OpenGlHelper.isFramebufferEnabled() && k > 1;
            if (flag) {
                Config.getGameSettings().guiScale = j * k;
                ScreenShotHelper.resize(width * k, height * k);
                GlStateManager.pushMatrix();
                GlStateManager.clear(16640);
                minecraft.getFramebuffer().bindFramebuffer(true);
                minecraft.entityRenderer.updateCameraAndRender(minecraft.getRenderPartialTicks(), System.nanoTime());
            }
            BufferedImage bufferedimage = ScreenShotHelper.createScreenshot(width, height, buffer);
            if (flag) {
                minecraft.getFramebuffer().unbindFramebuffer();
                GlStateManager.popMatrix();
                Config.getGameSettings().guiScale = i;
                ScreenShotHelper.resize(width, height);
            }
            File file2 = screenshotName == null ? ScreenShotHelper.getTimestampedPNGFileForDirectory(file1) : new File(file1, screenshotName);
            file2 = file2.getCanonicalFile();
            Object object = null;
            if (Reflector.ForgeHooksClient_onScreenshot.exists()) {
                object = Reflector.call(Reflector.ForgeHooksClient_onScreenshot, bufferedimage, file2);
                if (Reflector.callBoolean(object, Reflector.Event_isCanceled, new Object[0])) {
                    return (ITextComponent)Reflector.call(object, Reflector.ScreenshotEvent_getCancelMessage, new Object[0]);
                }
                file2 = (File)Reflector.call(object, Reflector.ScreenshotEvent_getScreenshotFile, new Object[0]);
            }
            ImageIO.write((RenderedImage)bufferedimage, "png", file2);
            TextComponentString itextcomponent = new TextComponentString(file2.getName());
            itextcomponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file2.getAbsolutePath()));
            itextcomponent.getStyle().setUnderlined(true);
            if (object != null && (itextcomponent1 = (ITextComponent)Reflector.call(object, Reflector.ScreenshotEvent_getResultMessage, new Object[0])) != null) {
                return itextcomponent1;
            }
            return new TextComponentTranslation("screenshot.success", itextcomponent);
        }
        catch (Exception exception1) {
            LOGGER.warn("Couldn't save screenshot", (Throwable)exception1);
            return new TextComponentTranslation("screenshot.failure", exception1.getMessage());
        }
    }

    public static BufferedImage createScreenshot(int width, int height, Framebuffer framebufferIn) {
        if (OpenGlHelper.isFramebufferEnabled()) {
            width = framebufferIn.framebufferTextureWidth;
            height = framebufferIn.framebufferTextureHeight;
        }
        int i = width * height;
        if (pixelBuffer == null || pixelBuffer.capacity() < i) {
            pixelBuffer = BufferUtils.createIntBuffer(i);
            pixelValues = new int[i];
        }
        GlStateManager.glPixelStorei(3333, 1);
        GlStateManager.glPixelStorei(3317, 1);
        pixelBuffer.clear();
        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.bindTexture(framebufferIn.framebufferTexture);
            GlStateManager.glGetTexImage(3553, 0, 32993, 33639, pixelBuffer);
        } else {
            GlStateManager.glReadPixels(0, 0, width, height, 32993, 33639, pixelBuffer);
        }
        pixelBuffer.get(pixelValues);
        TextureUtil.processPixelValues(pixelValues, width, height);
        BufferedImage bufferedimage = new BufferedImage(width, height, 1);
        bufferedimage.setRGB(0, 0, width, height, pixelValues, 0, width);
        return bufferedimage;
    }

    private static File getTimestampedPNGFileForDirectory(File gameDirectory) {
        String s = DATE_FORMAT.format(new Date()).toString();
        int i = 1;
        File file1;
        while ((file1 = new File(gameDirectory, s + (i == 1 ? "" : "_" + i) + ".png")).exists()) {
            ++i;
        }
        return file1;
    }

    private static void resize(int p_resize_0_, int p_resize_1_) {
        Minecraft minecraft = Minecraft.getMinecraft();
        minecraft.displayWidth = Math.max(1, p_resize_0_);
        minecraft.displayHeight = Math.max(1, p_resize_1_);
        if (minecraft.currentScreen != null) {
            ScaledResolution scaledresolution = new ScaledResolution(minecraft);
            minecraft.currentScreen.onResize(minecraft, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight());
        }
        ScreenShotHelper.updateFramebufferSize();
    }

    private static void updateFramebufferSize() {
        Minecraft minecraft = Minecraft.getMinecraft();
        minecraft.getFramebuffer().createBindFramebuffer(minecraft.displayWidth, minecraft.displayHeight);
        if (minecraft.entityRenderer != null) {
            minecraft.entityRenderer.updateShaderGroupSize(minecraft.displayWidth, minecraft.displayHeight);
        }
    }
}

