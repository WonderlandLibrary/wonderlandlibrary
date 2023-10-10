package org.newdawn.slick.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.newdawn.slick.opengl.ImageIOImageData;
import org.newdawn.slick.opengl.InternalTextureLoader;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;

public class BufferedImageUtil {
   public static Texture getTexture(String var0, BufferedImage var1) throws IOException {
      Texture var2 = getTexture(var0, var1, 3553, 32856, 9729, 9729);
      return var2;
   }

   public static Texture getTexture(String var0, BufferedImage var1, int var2) throws IOException {
      Texture var3 = getTexture(var0, var1, 3553, 32856, var2, var2);
      return var3;
   }

   public static Texture getTexture(String var0, BufferedImage var1, int var2, int var3, int var4, int var5) throws IOException {
      ImageIOImageData var6 = new ImageIOImageData();
      boolean var7 = false;
      int var8 = InternalTextureLoader.createTextureID();
      TextureImpl var9 = new TextureImpl(var0, var2, var8);
      GL11.glEnable(3553);
      GL11.glBindTexture(var2, var8);
      var9.setWidth(var1.getWidth());
      var9.setHeight(var1.getHeight());
      short var12;
      if (var1.getColorModel().hasAlpha()) {
         var12 = 6408;
      } else {
         var12 = 6407;
      }

      ByteBuffer var11 = var6.imageToByteBuffer(var1, false, false, (int[])null);
      var9.setTextureHeight(var6.getTexHeight());
      var9.setTextureWidth(var6.getTexWidth());
      var9.setAlpha(var6.getDepth() == 32);
      if (var2 == 3553) {
         GL11.glTexParameteri(var2, 10241, var4);
         GL11.glTexParameteri(var2, 10240, var5);
         if (GLContext.getCapabilities().GL_EXT_texture_mirror_clamp) {
            GL11.glTexParameteri(3553, 10242, 34627);
            GL11.glTexParameteri(3553, 10243, 34627);
         } else {
            GL11.glTexParameteri(3553, 10242, 10496);
            GL11.glTexParameteri(3553, 10243, 10496);
         }
      }

      GL11.glTexImage2D(var2, 0, var3, var9.getTextureWidth(), var9.getTextureHeight(), 0, var12, 5121, (ByteBuffer)var11);
      return var9;
   }

   private static void copyArea(BufferedImage var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      Graphics2D var7 = (Graphics2D)var0.getGraphics();
      var7.drawImage(var0.getSubimage(var1, var2, var3, var4), var1 + var5, var2 + var6, (ImageObserver)null);
   }
}
