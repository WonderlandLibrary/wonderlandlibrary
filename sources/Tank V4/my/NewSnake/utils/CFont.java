package my.NewSnake.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public class CFont {
   protected boolean antiAlias;
   protected int fontHeight = -1;
   protected CFont.CharData[] charData = new CFont.CharData[256];
   protected Font font;
   protected int charOffset = 0;
   protected DynamicTexture tex;
   private float imgSize = 512.0F;
   protected boolean fractionalMetrics;

   public int getStringWidth(String var1) {
      int var2 = 0;
      char[] var6;
      int var5 = (var6 = var1.toCharArray()).length;

      for(int var4 = 0; var4 < var5; ++var4) {
         char var3 = var6[var4];
         if (var3 < this.charData.length) {
            var2 += this.charData[var3].width - 8 + this.charOffset;
         }
      }

      return var2 / 2;
   }

   public int getHeight() {
      return (this.fontHeight - 8) / 2;
   }

   public boolean isFractionalMetrics() {
      return this.fractionalMetrics;
   }

   public void setAntiAlias(boolean var1) {
      if (this.antiAlias != var1) {
         this.antiAlias = var1;
         this.tex = this.setupTexture(this.font, var1, this.fractionalMetrics, this.charData);
      }

   }

   public CFont(Font var1, boolean var2, boolean var3) {
      this.font = var1;
      this.antiAlias = var2;
      this.fractionalMetrics = var3;
      this.tex = this.setupTexture(var1, var2, var3, this.charData);
   }

   protected DynamicTexture setupTexture(Font var1, boolean var2, boolean var3, CFont.CharData[] var4) {
      BufferedImage var5 = this.generateFontImage(var1, var2, var3, var4);

      try {
         return new DynamicTexture(var5);
      } catch (Exception var7) {
         var7.printStackTrace();
         return null;
      }
   }

   public Font getFont() {
      return this.font;
   }

   public boolean isAntiAlias() {
      return this.antiAlias;
   }

   protected BufferedImage generateFontImage(Font var1, boolean var2, boolean var3, CFont.CharData[] var4) {
      int var5 = (int)this.imgSize;
      BufferedImage var6 = new BufferedImage(var5, var5, 2);
      Graphics2D var7 = (Graphics2D)var6.getGraphics();
      var7.setFont(var1);
      var7.setColor(new Color(255, 255, 255, 0));
      var7.fillRect(0, 0, var5, var5);
      var7.setColor(Color.WHITE);
      var7.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, var3 ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
      var7.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, var2 ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
      var7.setRenderingHint(RenderingHints.KEY_ANTIALIASING, var2 ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
      FontMetrics var8 = var7.getFontMetrics();
      int var9 = 0;
      int var10 = 0;
      int var11 = 1;

      for(int var12 = 0; var12 < var4.length; ++var12) {
         char var13 = (char)var12;
         CFont.CharData var14 = new CFont.CharData();
         Rectangle2D var15 = var8.getStringBounds(String.valueOf(var13), var7);
         var14.width = var15.getBounds().width + 8;
         var14.height = var15.getBounds().height;
         if (var10 + var14.width >= var5) {
            var10 = 0;
            var11 += var9;
            var9 = 0;
         }

         if (var14.height > var9) {
            var9 = var14.height;
         }

         var14.storedX = var10;
         var14.storedY = var11;
         if (var14.height > this.fontHeight) {
            this.fontHeight = var14.height;
         }

         var4[var12] = var14;
         var7.drawString(String.valueOf(var13), var10 + 2, var11 + var8.getAscent());
         var10 += var14.width;
      }

      return var6;
   }

   public void setFont(Font var1) {
      this.font = var1;
      this.tex = this.setupTexture(var1, this.antiAlias, this.fractionalMetrics, this.charData);
   }

   public int getStringHeight(String var1) {
      return this.getHeight();
   }

   public void drawChar(CFont.CharData[] var1, char var2, float var3, float var4) throws ArrayIndexOutOfBoundsException {
      try {
         this.drawQuad(var3, var4, (float)var1[var2].width, (float)var1[var2].height, (float)var1[var2].storedX, (float)var1[var2].storedY, (float)var1[var2].width, (float)var1[var2].height);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   protected void drawQuad(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float var9 = var5 / this.imgSize;
      float var10 = var6 / this.imgSize;
      float var11 = var7 / this.imgSize;
      float var12 = var8 / this.imgSize;
      GL11.glTexCoord2f(var9 + var11, var10);
      GL11.glVertex2d((double)(var1 + var3), (double)var2);
      GL11.glTexCoord2f(var9, var10);
      GL11.glVertex2d((double)var1, (double)var2);
      GL11.glTexCoord2f(var9, var10 + var12);
      GL11.glVertex2d((double)var1, (double)(var2 + var4));
      GL11.glTexCoord2f(var9, var10 + var12);
      GL11.glVertex2d((double)var1, (double)(var2 + var4));
      GL11.glTexCoord2f(var9 + var11, var10 + var12);
      GL11.glVertex2d((double)(var1 + var3), (double)(var2 + var4));
      GL11.glTexCoord2f(var9 + var11, var10);
      GL11.glVertex2d((double)(var1 + var3), (double)var2);
   }

   public void setFractionalMetrics(boolean var1) {
      if (this.fractionalMetrics != var1) {
         this.fractionalMetrics = var1;
         this.tex = this.setupTexture(this.font, this.antiAlias, var1, this.charData);
      }

   }

   protected static class CharData {
      public int height;
      public int storedX;
      public int storedY;
      public int width;
   }
}
