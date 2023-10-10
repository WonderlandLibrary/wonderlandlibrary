package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.Mipmaps;
import optifine.Reflector;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class TextureUtil {
   public static final int[] missingTextureData;
   public static final DynamicTexture missingTexture = new DynamicTexture(16, 16);
   private static final Logger logger = LogManager.getLogger();
   private static final String __OBFID = "CL_00001067";
   private static final IntBuffer dataBuffer = GLAllocation.createDirectIntBuffer(4194304);
   private static final int[] mipmapBuffer;

   public static int[] readImageData(IResourceManager var0, ResourceLocation var1) throws IOException {
      BufferedImage var2 = readBufferedImage(var0.getResource(var1).getInputStream());
      if (var2 == null) {
         return null;
      } else {
         int var3 = var2.getWidth();
         int var4 = var2.getHeight();
         int[] var5 = new int[var3 * var4];
         var2.getRGB(0, 0, var3, var4, var5, 0, var3);
         return var5;
      }
   }

   private static int blendColors(int var0, int var1, int var2, int var3, boolean var4) {
      return Mipmaps.alphaBlend(var0, var1, var2, var3);
   }

   public static int[] updateAnaglyph(int[] var0) {
      int[] var1 = new int[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = anaglyphColor(var0[var2]);
      }

      return var1;
   }

   private static void setTextureBlurred(boolean var0) {
      setTextureBlurMipmap(var0, false);
   }

   static {
      missingTextureData = missingTexture.getTextureData();
      int var0 = -16777216;
      int var1 = -524040;
      int[] var2 = new int[]{-524040, -524040, -524040, -524040, -524040, -524040, -524040, -524040};
      int[] var3 = new int[]{-16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216};
      int var4 = var2.length;

      for(int var5 = 0; var5 < 16; ++var5) {
         System.arraycopy(var5 < var4 ? var2 : var3, 0, missingTextureData, 16 * var5, var4);
         System.arraycopy(var5 < var4 ? var3 : var2, 0, missingTextureData, 16 * var5 + var4, var4);
      }

      missingTexture.updateDynamicTexture();
      mipmapBuffer = new int[4];
   }

   public static void setTextureClamped(boolean var0) {
      if (var0) {
         GL11.glTexParameteri(3553, 10242, 33071);
         GL11.glTexParameteri(3553, 10243, 33071);
      } else {
         GL11.glTexParameteri(3553, 10242, 10497);
         GL11.glTexParameteri(3553, 10243, 10497);
      }

   }

   private static void copyToBufferPos(int[] var0, int var1, int var2) {
      int[] var3 = var0;
      if (Minecraft.getMinecraft().gameSettings.anaglyph) {
         var3 = updateAnaglyph(var0);
      }

      dataBuffer.clear();
      dataBuffer.put(var3, var1, var2);
      dataBuffer.position(0).limit(var2);
   }

   public static void uploadTexture(int var0, int[] var1, int var2, int var3) {
      bindTexture(var0);
      uploadTextureSub(0, var1, var2, var3, 0, 0, false, false, false);
   }

   public static void deleteTexture(int var0) {
      GlStateManager.deleteTexture(var0);
   }

   public static void setTextureBlurMipmap(boolean var0, boolean var1) {
      if (var0) {
         GL11.glTexParameteri(3553, 10241, var1 ? 9987 : 9729);
         GL11.glTexParameteri(3553, 10240, 9729);
      } else {
         int var2 = Config.getMipmapType();
         GL11.glTexParameteri(3553, 10241, var1 ? var2 : 9728);
         GL11.glTexParameteri(3553, 10240, 9728);
      }

   }

   private static void uploadTextureImageSubImpl(BufferedImage var0, int var1, int var2, boolean var3, boolean var4) {
      int var5 = var0.getWidth();
      int var6 = var0.getHeight();
      int var7 = 4194304 / var5;
      int[] var8 = new int[var7 * var5];
      setTextureBlurred(var3);
      setTextureClamped(var4);

      for(int var9 = 0; var9 < var5 * var6; var9 += var5 * var7) {
         int var10 = var9 / var5;
         int var11 = Math.min(var7, var6 - var10);
         int var12 = var5 * var11;
         var0.getRGB(0, var10, var5, var11, var8, 0, var5);
         copyToBuffer(var8, var12);
         GL11.glTexSubImage2D(3553, 0, var1, var2 + var10, var5, var11, 32993, 33639, (IntBuffer)dataBuffer);
      }

   }

   public static int uploadTextureImage(int var0, BufferedImage var1) {
      return uploadTextureImageAllocate(var0, var1, false, false);
   }

   public static int[][] generateMipmapData(int var0, int var1, int[][] var2) {
      int[][] var3 = new int[var0 + 1][];
      var3[0] = var2[0];
      if (var0 > 0) {
         boolean var4 = false;

         int var5;
         for(var5 = 0; var5 < var2.length; ++var5) {
            if (var2[0][var5] >> 24 == 0) {
               var4 = true;
               break;
            }
         }

         for(var5 = 1; var5 <= var0; ++var5) {
            if (var2[var5] != null) {
               var3[var5] = var2[var5];
            } else {
               int[] var6 = var3[var5 - 1];
               int[] var7 = new int[var6.length >> 2];
               int var8 = var1 >> var5;
               int var9 = var7.length / var8;
               int var10 = var8 << 1;

               for(int var11 = 0; var11 < var8; ++var11) {
                  for(int var12 = 0; var12 < var9; ++var12) {
                     int var13 = 2 * (var11 + var12 * var10);
                     var7[var11 + var12 * var8] = blendColors(var6[var13 + 0], var6[var13 + 1], var6[var13 + 0 + var10], var6[var13 + 1 + var10], var4);
                  }
               }

               var3[var5] = var7;
            }
         }
      }

      return var3;
   }

   public static void uploadTextureMipmap(int[][] var0, int var1, int var2, int var3, int var4, boolean var5, boolean var6) {
      for(int var7 = 0; var7 < var0.length; ++var7) {
         int[] var8 = var0[var7];
         uploadTextureSub(var7, var8, var1 >> var7, var2 >> var7, var3 >> var7, var4 >> var7, var5, var6, var0.length > 1);
      }

   }

   public static int uploadTextureImageSub(int var0, BufferedImage var1, int var2, int var3, boolean var4, boolean var5) {
      bindTexture(var0);
      uploadTextureImageSubImpl(var1, var2, var3, var4, var5);
      return var0;
   }

   private static void uploadTextureSub(int var0, int[] var1, int var2, int var3, int var4, int var5, boolean var6, boolean var7, boolean var8) {
      int var9 = 4194304 / var2;
      setTextureBlurMipmap(var6, var8);
      setTextureClamped(var7);

      int var10;
      for(int var11 = 0; var11 < var2 * var3; var11 += var2 * var10) {
         int var12 = var11 / var2;
         var10 = Math.min(var9, var3 - var12);
         int var13 = var2 * var10;
         copyToBufferPos(var1, var11, var13);
         GL11.glTexSubImage2D(3553, var0, var4, var5 + var12, var2, var10, 32993, 33639, (IntBuffer)dataBuffer);
      }

   }

   static void bindTexture(int var0) {
      GlStateManager.bindTexture(var0);
   }

   public static void allocateTextureImpl(int var0, int var1, int var2, int var3) {
      Class var4 = TextureUtil.class;
      if (Reflector.SplashScreen.exists()) {
         var4 = Reflector.SplashScreen.getTargetClass();
      }

      synchronized(var4){}
      deleteTexture(var0);
      bindTexture(var0);
      if (var1 >= 0) {
         GL11.glTexParameteri(3553, 33085, var1);
         GL11.glTexParameterf(3553, 33082, 0.0F);
         GL11.glTexParameterf(3553, 33083, (float)var1);
         GL11.glTexParameterf(3553, 34049, 0.0F);
      }

      for(int var5 = 0; var5 <= var1; ++var5) {
         GL11.glTexImage2D(3553, var5, 6408, var2 >> var5, var3 >> var5, 0, 32993, 33639, (IntBuffer)null);
      }

   }

   public static int anaglyphColor(int var0) {
      int var1 = var0 >> 24 & 255;
      int var2 = var0 >> 16 & 255;
      int var3 = var0 >> 8 & 255;
      int var4 = var0 & 255;
      int var5 = (var2 * 30 + var3 * 59 + var4 * 11) / 100;
      int var6 = (var2 * 30 + var3 * 70) / 100;
      int var7 = (var2 * 30 + var4 * 70) / 100;
      return var1 << 24 | var5 << 16 | var6 << 8 | var7;
   }

   private static void copyToBuffer(int[] var0, int var1) {
      copyToBufferPos(var0, 0, var1);
   }

   public static void saveGlTexture(String var0, int var1, int var2, int var3, int var4) {
      bindTexture(var1);
      GL11.glPixelStorei(3333, 1);
      GL11.glPixelStorei(3317, 1);

      for(int var5 = 0; var5 <= var2; ++var5) {
         File var6 = new File(var0 + "_" + var5 + ".png");
         int var7 = var3 >> var5;
         int var8 = var4 >> var5;
         int var9 = var7 * var8;
         IntBuffer var10 = BufferUtils.createIntBuffer(var9);
         int[] var11 = new int[var9];
         GL11.glGetTexImage(3553, var5, 32993, 33639, (IntBuffer)var10);
         var10.get(var11);
         BufferedImage var12 = new BufferedImage(var7, var8, 2);
         var12.setRGB(0, 0, var7, var8, var11, 0, var7);

         try {
            ImageIO.write(var12, "png", var6);
            logger.debug("Exported png to: {}", var6.getAbsolutePath());
         } catch (Exception var14) {
            logger.debug((String)"Unable to write: ", (Throwable)var14);
         }
      }

   }

   public static int glGenTextures() {
      return GlStateManager.generateTexture();
   }

   public static void allocateTexture(int var0, int var1, int var2) {
      allocateTextureImpl(var0, 0, var1, var2);
   }

   private static int blendColorComponent(int var0, int var1, int var2, int var3, int var4) {
      float var5 = (float)Math.pow((double)((float)(var0 >> var4 & 255) / 255.0F), 2.2D);
      float var6 = (float)Math.pow((double)((float)(var1 >> var4 & 255) / 255.0F), 2.2D);
      float var7 = (float)Math.pow((double)((float)(var2 >> var4 & 255) / 255.0F), 2.2D);
      float var8 = (float)Math.pow((double)((float)(var3 >> var4 & 255) / 255.0F), 2.2D);
      float var9 = (float)Math.pow((double)(var5 + var6 + var7 + var8) * 0.25D, 0.45454545454545453D);
      return (int)((double)var9 * 255.0D);
   }

   public static BufferedImage readBufferedImage(InputStream var0) throws IOException {
      if (var0 == null) {
         return null;
      } else {
         BufferedImage var1 = ImageIO.read(var0);
         IOUtils.closeQuietly(var0);
         return var1;
      }
   }

   public static int uploadTextureImageAllocate(int var0, BufferedImage var1, boolean var2, boolean var3) {
      allocateTexture(var0, var1.getWidth(), var1.getHeight());
      return uploadTextureImageSub(var0, var1, 0, 0, var2, var3);
   }

   public static void processPixelValues(int[] var0, int var1, int var2) {
      int[] var3 = new int[var1];
      int var4 = var2 / 2;

      for(int var5 = 0; var5 < var4; ++var5) {
         System.arraycopy(var0, var5 * var1, var3, 0, var1);
         System.arraycopy(var0, (var2 - 1 - var5) * var1, var0, var5 * var1, var1);
         System.arraycopy(var3, 0, var0, (var2 - 1 - var5) * var1, var1);
      }

   }
}
