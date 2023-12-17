// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import java.util.Date;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.BufferUtils;
import java.awt.image.BufferedImage;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.TextComponentString;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;
import net.optifine.reflect.Reflector;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.src.Config;
import net.minecraft.client.Minecraft;
import javax.annotation.Nullable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.shader.Framebuffer;
import java.io.File;
import java.nio.IntBuffer;
import java.text.DateFormat;
import org.apache.logging.log4j.Logger;

public class ScreenShotHelper
{
    private static final Logger LOGGER;
    private static final DateFormat DATE_FORMAT;
    private static IntBuffer pixelBuffer;
    private static int[] pixelValues;
    
    public static ITextComponent saveScreenshot(final File gameDirectory, final int width, final int height, final Framebuffer buffer) {
        return saveScreenshot(gameDirectory, null, width, height, buffer);
    }
    
    public static ITextComponent saveScreenshot(final File gameDirectory, @Nullable final String screenshotName, final int width, final int height, final Framebuffer buffer) {
        try {
            final File file1 = new File(gameDirectory, "screenshots");
            file1.mkdir();
            final Minecraft minecraft = Minecraft.getMinecraft();
            final int i = Config.getGameSettings().guiScale;
            final ScaledResolution scaledresolution = new ScaledResolution(minecraft);
            final int j = ScaledResolution.getScaleFactor();
            final int k = Config.getScreenshotSize();
            final boolean flag = OpenGlHelper.isFramebufferEnabled() && k > 1;
            if (flag) {
                Config.getGameSettings().guiScale = j * k;
                resize(width * k, height * k);
                GlStateManager.pushMatrix();
                GlStateManager.clear(16640);
                minecraft.getFramebuffer().bindFramebuffer(true);
                minecraft.entityRenderer.updateCameraAndRender(minecraft.getRenderPartialTicks(), System.nanoTime());
            }
            final BufferedImage bufferedimage = createScreenshot(width, height, buffer);
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
            Object object = null;
            if (Reflector.ForgeHooksClient_onScreenshot.exists()) {
                object = Reflector.call(Reflector.ForgeHooksClient_onScreenshot, bufferedimage, file2);
                if (Reflector.callBoolean(object, Reflector.Event_isCanceled, new Object[0])) {
                    return (ITextComponent)Reflector.call(object, Reflector.ScreenshotEvent_getCancelMessage, new Object[0]);
                }
                file2 = (File)Reflector.call(object, Reflector.ScreenshotEvent_getScreenshotFile, new Object[0]);
            }
            ImageIO.write(bufferedimage, "png", file2);
            final ITextComponent itextcomponent = new TextComponentString(file2.getName());
            itextcomponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file2.getAbsolutePath()));
            itextcomponent.getStyle().setUnderlined(true);
            if (object != null) {
                final ITextComponent itextcomponent2 = (ITextComponent)Reflector.call(object, Reflector.ScreenshotEvent_getResultMessage, new Object[0]);
                if (itextcomponent2 != null) {
                    return itextcomponent2;
                }
            }
            return new TextComponentTranslation("screenshot.success", new Object[] { itextcomponent });
        }
        catch (Exception exception1) {
            ScreenShotHelper.LOGGER.warn("Couldn't save screenshot", (Throwable)exception1);
            return new TextComponentTranslation("screenshot.failure", new Object[] { exception1.getMessage() });
        }
    }
    
    public static BufferedImage createScreenshot(int width, int height, final Framebuffer framebufferIn) {
        if (OpenGlHelper.isFramebufferEnabled()) {
            width = framebufferIn.framebufferTextureWidth;
            height = framebufferIn.framebufferTextureHeight;
        }
        final int i = width * height;
        if (ScreenShotHelper.pixelBuffer == null || ScreenShotHelper.pixelBuffer.capacity() < i) {
            ScreenShotHelper.pixelBuffer = BufferUtils.createIntBuffer(i);
            ScreenShotHelper.pixelValues = new int[i];
        }
        GlStateManager.glPixelStorei(3333, 1);
        GlStateManager.glPixelStorei(3317, 1);
        ScreenShotHelper.pixelBuffer.clear();
        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.bindTexture(framebufferIn.framebufferTexture);
            GlStateManager.glGetTexImage(3553, 0, 32993, 33639, ScreenShotHelper.pixelBuffer);
        }
        else {
            GlStateManager.glReadPixels(0, 0, width, height, 32993, 33639, ScreenShotHelper.pixelBuffer);
        }
        ScreenShotHelper.pixelBuffer.get(ScreenShotHelper.pixelValues);
        TextureUtil.processPixelValues(ScreenShotHelper.pixelValues, width, height);
        final BufferedImage bufferedimage = new BufferedImage(width, height, 1);
        bufferedimage.setRGB(0, 0, width, height, ScreenShotHelper.pixelValues, 0, width);
        return bufferedimage;
    }
    
    private static File getTimestampedPNGFileForDirectory(final File gameDirectory) {
        final String s = ScreenShotHelper.DATE_FORMAT.format(new Date()).toString();
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
        LOGGER = LogManager.getLogger();
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    }
}
