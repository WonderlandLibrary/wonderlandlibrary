package org.newdawn.slick;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.newdawn.slick.opengl.GLUtils;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.util.BufferedImageUtil;

/** @deprecated */
public class TrueTypeFont implements Font {
   private static final SGL GL = Renderer.get();
   private TrueTypeFont.IntObject[] charArray;
   private Map customChars;
   private boolean antiAlias;
   private int fontSize;
   private int fontHeight;
   private Texture fontTexture;
   private int textureWidth;
   private int textureHeight;
   private java.awt.Font font;
   private FontMetrics fontMetrics;

   public TrueTypeFont(java.awt.Font var1, boolean var2, char[] var3) {
      this.charArray = new TrueTypeFont.IntObject[256];
      this.customChars = new HashMap();
      this.fontSize = 0;
      this.fontHeight = 0;
      this.textureWidth = 512;
      this.textureHeight = 512;
      GLUtils.checkGLContext();
      this.font = var1;
      this.fontSize = var1.getSize();
      this.antiAlias = var2;
      this.createSet(var3);
   }

   public TrueTypeFont(java.awt.Font var1, boolean var2) {
      this(var1, var2, (char[])null);
   }

   private BufferedImage getFontImage(char var1) {
      BufferedImage var2 = new BufferedImage(1, 1, 2);
      Graphics2D var3 = (Graphics2D)var2.getGraphics();
      if (this.antiAlias) {
         var3.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      }

      var3.setFont(this.font);
      this.fontMetrics = var3.getFontMetrics();
      int var4 = this.fontMetrics.charWidth(var1);
      if (var4 <= 0) {
         var4 = 1;
      }

      int var5 = this.fontMetrics.getHeight();
      if (var5 <= 0) {
         var5 = this.fontSize;
      }

      BufferedImage var6 = new BufferedImage(var4, var5, 2);
      Graphics2D var7 = (Graphics2D)var6.getGraphics();
      if (this.antiAlias) {
         var7.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      }

      var7.setFont(this.font);
      var7.setColor(java.awt.Color.WHITE);
      byte var8 = 0;
      byte var9 = 0;
      var7.drawString(String.valueOf(var1), var8, var9 + this.fontMetrics.getAscent());
      return var6;
   }

   private void createSet(char[] var1) {
      if (var1 != null && var1.length > 0) {
         this.textureWidth *= 2;
      }

      try {
         BufferedImage var2 = new BufferedImage(this.textureWidth, this.textureHeight, 2);
         Graphics2D var3 = (Graphics2D)var2.getGraphics();
         var3.setColor(new java.awt.Color(255, 255, 255, 1));
         var3.fillRect(0, 0, this.textureWidth, this.textureHeight);
         int var4 = 0;
         int var5 = 0;
         int var6 = 0;
         int var7 = var1 != null ? var1.length : 0;

         for(int var8 = 0; var8 < 256 + var7; ++var8) {
            char var9 = var8 < 256 ? (char)var8 : var1[var8 - 256];
            BufferedImage var10 = this.getFontImage(var9);
            TrueTypeFont.IntObject var11 = new TrueTypeFont.IntObject(this);
            var11.width = var10.getWidth();
            var11.height = var10.getHeight();
            if (var5 + var11.width >= this.textureWidth) {
               var5 = 0;
               var6 += var4;
               var4 = 0;
            }

            var11.storedX = var5;
            var11.storedY = var6;
            if (var11.height > this.fontHeight) {
               this.fontHeight = var11.height;
            }

            if (var11.height > var4) {
               var4 = var11.height;
            }

            var3.drawImage(var10, var5, var6, (ImageObserver)null);
            var5 += var11.width;
            if (var8 < 256) {
               this.charArray[var8] = var11;
            } else {
               this.customChars.put(new Character(var9), var11);
            }

            var10 = null;
         }

         this.fontTexture = BufferedImageUtil.getTexture(this.font.toString(), var2);
      } catch (IOException var12) {
         System.err.println("Failed to create font.");
         var12.printStackTrace();
      }

   }

   private void drawQuad(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float var9 = var3 - var1;
      float var10 = var4 - var2;
      float var11 = var5 / (float)this.textureWidth;
      float var12 = var6 / (float)this.textureHeight;
      float var13 = var7 - var5;
      float var14 = var8 - var6;
      float var15 = var13 / (float)this.textureWidth;
      float var16 = var14 / (float)this.textureHeight;
      GL.glTexCoord2f(var11, var12);
      GL.glVertex2f(var1, var2);
      GL.glTexCoord2f(var11, var12 + var16);
      GL.glVertex2f(var1, var2 + var10);
      GL.glTexCoord2f(var11 + var15, var12 + var16);
      GL.glVertex2f(var1 + var9, var2 + var10);
      GL.glTexCoord2f(var11 + var15, var12);
      GL.glVertex2f(var1 + var9, var2);
   }

   public int getWidth(String var1) {
      int var2 = 0;
      TrueTypeFont.IntObject var3 = null;
      boolean var4 = false;

      for(int var5 = 0; var5 < var1.length(); ++var5) {
         char var6 = var1.charAt(var5);
         if (var6 < 256) {
            var3 = this.charArray[var6];
         } else {
            var3 = (TrueTypeFont.IntObject)this.customChars.get(new Character((char)var6));
         }

         if (var3 != null) {
            var2 += var3.width;
         }
      }

      return var2;
   }

   public int getHeight() {
      return this.fontHeight;
   }

   public int getHeight(String var1) {
      return this.fontHeight;
   }

   public int getLineHeight() {
      return this.fontHeight;
   }

   public void drawString(float var1, float var2, String var3, Color var4) {
      this.drawString(var1, var2, var3, var4, 0, var3.length() - 1);
   }

   public void drawString(float var1, float var2, String var3, Color var4, int var5, int var6) {
      var4.bind();
      this.fontTexture.bind();
      TrueTypeFont.IntObject var7 = null;
      GL.glBegin(7);
      int var9 = 0;

      for(int var10 = 0; var10 < var3.length(); ++var10) {
         char var8 = var3.charAt(var10);
         if (var8 < 256) {
            var7 = this.charArray[var8];
         } else {
            var7 = (TrueTypeFont.IntObject)this.customChars.get(new Character((char)var8));
         }

         if (var7 != null) {
            if (var10 >= var5 || var10 <= var6) {
               this.drawQuad(var1 + (float)var9, var2, var1 + (float)var9 + (float)var7.width, var2 + (float)var7.height, (float)var7.storedX, (float)var7.storedY, (float)(var7.storedX + var7.width), (float)(var7.storedY + var7.height));
            }

            var9 += var7.width;
         }
      }

      GL.glEnd();
   }

   public void drawString(float var1, float var2, String var3) {
      this.drawString(var1, var2, var3, Color.white);
   }

   private class IntObject {
      public int width;
      public int height;
      public int storedX;
      public int storedY;
      private final TrueTypeFont this$0;

      private IntObject(TrueTypeFont var1) {
         this.this$0 = var1;
      }

      IntObject(TrueTypeFont var1, Object var2) {
         this(var1);
      }
   }
}
