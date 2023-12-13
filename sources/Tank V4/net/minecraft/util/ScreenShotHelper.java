package net.minecraft.util;

import java.awt.image.BufferedImage;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class ScreenShotHelper {
   private static int[] pixelValues;
   private static IntBuffer pixelBuffer;
   private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
   private static final Logger logger = LogManager.getLogger();

   public static IChatComponent saveScreenshot(File var0, int var1, int var2, Framebuffer var3) {
      return saveScreenshot(var0, (String)null, var1, var2, var3);
   }

   public static IChatComponent saveScreenshot(File var0, String var1, int var2, int var3, Framebuffer var4) {
      try {
         File var5 = new File(var0, "screenshots");
         var5.mkdir();
         if (OpenGlHelper.isFramebufferEnabled()) {
            var2 = var4.framebufferTextureWidth;
            var3 = var4.framebufferTextureHeight;
         }

         int var6 = var2 * var3;
         if (pixelBuffer == null || pixelBuffer.capacity() < var6) {
            pixelBuffer = BufferUtils.createIntBuffer(var6);
            pixelValues = new int[var6];
         }

         GL11.glPixelStorei(3333, 1);
         GL11.glPixelStorei(3317, 1);
         pixelBuffer.clear();
         if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.bindTexture(var4.framebufferTexture);
            GL11.glGetTexImage(3553, 0, 32993, 33639, (IntBuffer)pixelBuffer);
         } else {
            GL11.glReadPixels(0, 0, var2, var3, 32993, 33639, (IntBuffer)pixelBuffer);
         }

         pixelBuffer.get(pixelValues);
         TextureUtil.processPixelValues(pixelValues, var2, var3);
         BufferedImage var7 = null;
         if (OpenGlHelper.isFramebufferEnabled()) {
            var7 = new BufferedImage(var4.framebufferWidth, var4.framebufferHeight, 1);
            int var8 = var4.framebufferTextureHeight - var4.framebufferHeight;

            for(int var9 = var8; var9 < var4.framebufferTextureHeight; ++var9) {
               for(int var10 = 0; var10 < var4.framebufferWidth; ++var10) {
                  var7.setRGB(var10, var9 - var8, pixelValues[var9 * var4.framebufferTextureWidth + var10]);
               }
            }
         } else {
            var7 = new BufferedImage(var2, var3, 1);
            var7.setRGB(0, 0, var2, var3, pixelValues, 0, var2);
         }

         File var12;
         if (var1 == null) {
            var12 = getTimestampedPNGFileForDirectory(var5);
         } else {
            var12 = new File(var5, var1);
         }

         ImageIO.write(var7, "png", var12);
         ChatComponentText var13 = new ChatComponentText(var12.getName());
         var13.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, var12.getAbsolutePath()));
         var13.getChatStyle().setUnderlined(true);
         return new ChatComponentTranslation("screenshot.success", new Object[]{var13});
      } catch (Exception var11) {
         logger.warn((String)"Couldn't save screenshot", (Throwable)var11);
         return new ChatComponentTranslation("screenshot.failure", new Object[]{var11.getMessage()});
      }
   }

   private static File getTimestampedPNGFileForDirectory(File var0) {
      String var1 = dateFormat.format(new Date()).toString();
      byte var2 = 1;
      File var3 = new File(var0, var1 + (var2 == 1 ? "" : "_" + var2) + ".png");
      if (!var3.exists()) {
         return var3;
      } else {
         int var4 = var2 + 1;
         return null;
      }
   }
}
