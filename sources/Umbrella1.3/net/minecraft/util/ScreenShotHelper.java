/*
 * Decompiled with CFR 0.150.
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
    private static final Logger logger = LogManager.getLogger();
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    private static IntBuffer pixelBuffer;
    private static int[] pixelValues;
    private static final String __OBFID = "CL_00000656";

    public static IChatComponent saveScreenshot(File p_148260_0_, int p_148260_1_, int p_148260_2_, Framebuffer p_148260_3_) {
        return ScreenShotHelper.saveScreenshot(p_148260_0_, null, p_148260_1_, p_148260_2_, p_148260_3_);
    }

    public static IChatComponent saveScreenshot(File p_148259_0_, String p_148259_1_, int p_148259_2_, int p_148259_3_, Framebuffer p_148259_4_) {
        try {
            File var5 = new File(p_148259_0_, "screenshots");
            var5.mkdir();
            if (OpenGlHelper.isFramebufferEnabled()) {
                p_148259_2_ = p_148259_4_.framebufferTextureWidth;
                p_148259_3_ = p_148259_4_.framebufferTextureHeight;
            }
            int var6 = p_148259_2_ * p_148259_3_;
            if (pixelBuffer == null || pixelBuffer.capacity() < var6) {
                pixelBuffer = BufferUtils.createIntBuffer((int)var6);
                pixelValues = new int[var6];
            }
            GL11.glPixelStorei((int)3333, (int)1);
            GL11.glPixelStorei((int)3317, (int)1);
            pixelBuffer.clear();
            if (OpenGlHelper.isFramebufferEnabled()) {
                GlStateManager.func_179144_i(p_148259_4_.framebufferTexture);
                GL11.glGetTexImage((int)3553, (int)0, (int)32993, (int)33639, (IntBuffer)pixelBuffer);
            } else {
                GL11.glReadPixels((int)0, (int)0, (int)p_148259_2_, (int)p_148259_3_, (int)32993, (int)33639, (IntBuffer)pixelBuffer);
            }
            pixelBuffer.get(pixelValues);
            TextureUtil.func_147953_a(pixelValues, p_148259_2_, p_148259_3_);
            BufferedImage var7 = null;
            if (OpenGlHelper.isFramebufferEnabled()) {
                int var8;
                var7 = new BufferedImage(p_148259_4_.framebufferWidth, p_148259_4_.framebufferHeight, 1);
                for (int var9 = var8 = p_148259_4_.framebufferTextureHeight - p_148259_4_.framebufferHeight; var9 < p_148259_4_.framebufferTextureHeight; ++var9) {
                    for (int var10 = 0; var10 < p_148259_4_.framebufferWidth; ++var10) {
                        var7.setRGB(var10, var9 - var8, pixelValues[var9 * p_148259_4_.framebufferTextureWidth + var10]);
                    }
                }
            } else {
                var7 = new BufferedImage(p_148259_2_, p_148259_3_, 1);
                var7.setRGB(0, 0, p_148259_2_, p_148259_3_, pixelValues, 0, p_148259_2_);
            }
            File var12 = p_148259_1_ == null ? ScreenShotHelper.getTimestampedPNGFileForDirectory(var5) : new File(var5, p_148259_1_);
            ImageIO.write((RenderedImage)var7, "png", var12);
            ChatComponentText var13 = new ChatComponentText(var12.getName());
            var13.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, var12.getAbsolutePath()));
            var13.getChatStyle().setUnderlined(true);
            return new ChatComponentTranslation("screenshot.success", var13);
        }
        catch (Exception var11) {
            logger.warn("Couldn't save screenshot", (Throwable)var11);
            return new ChatComponentTranslation("screenshot.failure", var11.getMessage());
        }
    }

    private static File getTimestampedPNGFileForDirectory(File p_74290_0_) {
        String var2 = dateFormat.format(new Date()).toString();
        int var3 = 1;
        File var1;
        while ((var1 = new File(p_74290_0_, String.valueOf(var2) + (var3 == 1 ? "" : "_" + var3) + ".png")).exists()) {
            ++var3;
        }
        return var1;
    }
}

