package my.NewSnake.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public class FontUtils {
   private int maxFontHeight = -1;
   private boolean antiAliasing;
   private DynamicTexture loadedTexture;
   private HashMap glyphCharacterMap = new HashMap();
   private Font font;
   private int imgSize;
   private boolean fractionalMetrics;
   private BufferedImage bufferedImage;

   public boolean isAntiAliasingEnabled() {
      return this.antiAliasing;
   }

   public int getMaxFontHeight() {
      return this.maxFontHeight;
   }

   public float drawChar(char var1, float var2, float var3) {
      FontUtils.Glyph var4 = (FontUtils.Glyph)this.glyphCharacterMap.get(var1);
      if (var4 == null) {
         throw new IllegalArgumentException("'" + var1 + "' wasn't found");
      } else {
         float var5 = (float)FontUtils.Glyph.access$6(var4) / (float)this.imgSize;
         float var6 = (float)FontUtils.Glyph.access$7(var4) / (float)this.imgSize;
         float var7 = (float)FontUtils.Glyph.access$3(var4) / (float)this.imgSize;
         float var8 = (float)FontUtils.Glyph.access$2(var4) / (float)this.imgSize;
         float var9 = (float)FontUtils.Glyph.access$3(var4);
         float var10 = (float)FontUtils.Glyph.access$2(var4);
         GL11.glBegin(4);
         GL11.glTexCoord2f(var5 + var7, var6);
         GL11.glVertex2f(var2 + var9, var3);
         GL11.glTexCoord2f(var5, var6);
         GL11.glVertex2f(var2, var3);
         GL11.glTexCoord2f(var5, var6 + var8);
         GL11.glVertex2f(var2, var3 + var10);
         GL11.glTexCoord2f(var5, var6 + var8);
         GL11.glVertex2f(var2, var3 + var10);
         GL11.glTexCoord2f(var5 + var7, var6 + var8);
         GL11.glVertex2f(var2 + var9, var3 + var10);
         GL11.glTexCoord2f(var5 + var7, var6);
         GL11.glVertex2f(var2 + var9, var3);
         GL11.glEnd();
         return var9 - 8.0F;
      }
   }

   public float getWidth(char var1) {
      return (float)FontUtils.Glyph.access$3((FontUtils.Glyph)this.glyphCharacterMap.get(var1));
   }

   public boolean isFractionalMetricsEnabled() {
      return this.fractionalMetrics;
   }

   public FontUtils(Font var1, boolean var2, boolean var3) {
      this.font = var1;
      this.antiAliasing = var2;
      this.fractionalMetrics = var3;
   }

   public void bindTexture() {
      GlStateManager.bindTexture(this.loadedTexture.getGlTextureId());
   }

   public void setupTexture() {
      this.loadedTexture = new DynamicTexture(this.bufferedImage);
   }

   public void generateGlyphPage(char[] var1) {
      double var2 = -1.0D;
      double var4 = -1.0D;
      AffineTransform var6 = new AffineTransform();
      FontRenderContext var7 = new FontRenderContext(var6, this.antiAliasing, this.fractionalMetrics);
      char[] var11 = var1;
      int var10 = var1.length;

      for(int var9 = 0; var9 < var10; ++var9) {
         char var8 = var11[var9];
         Rectangle2D var12 = this.font.getStringBounds(Character.toString(var8), var7);
         if (var2 < var12.getWidth()) {
            var2 = var12.getWidth();
         }

         if (var4 < var12.getHeight()) {
            var4 = var12.getHeight();
         }
      }

      var2 += 2.0D;
      var4 += 2.0D;
      this.imgSize = (int)Math.ceil(Math.max(Math.ceil(Math.sqrt(var2 * var2 * (double)var1.length) / var2), Math.ceil(Math.sqrt(var4 * var4 * (double)var1.length) / var4)) * Math.max(var2, var4)) + 1;
      this.bufferedImage = new BufferedImage(this.imgSize, this.imgSize, 2);
      Graphics2D var19 = (Graphics2D)this.bufferedImage.getGraphics();
      var19.setFont(this.font);
      var19.setColor(new Color(255, 255, 255, 0));
      var19.fillRect(0, 0, this.imgSize, this.imgSize);
      var19.setColor(Color.white);
      var19.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, this.fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
      var19.setRenderingHint(RenderingHints.KEY_ANTIALIASING, this.antiAliasing ? RenderingHints.VALUE_ANTIALIAS_OFF : RenderingHints.VALUE_ANTIALIAS_ON);
      var19.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, this.antiAliasing ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
      FontMetrics var20 = var19.getFontMetrics();
      var10 = 0;
      int var21 = 0;
      int var22 = 1;
      char[] var16 = var1;
      int var15 = var1.length;

      for(int var14 = 0; var14 < var15; ++var14) {
         char var13 = var16[var14];
         FontUtils.Glyph var17 = new FontUtils.Glyph();
         Rectangle2D var18 = var20.getStringBounds(Character.toString(var13), var19);
         FontUtils.Glyph.access$0(var17, var18.getBounds().width + 8);
         FontUtils.Glyph.access$1(var17, var18.getBounds().height);
         if (var22 + FontUtils.Glyph.access$2(var17) >= this.imgSize) {
            throw new IllegalStateException("Not all characters will fit");
         }

         if (var21 + FontUtils.Glyph.access$3(var17) >= this.imgSize) {
            var21 = 0;
            var22 += var10;
            var10 = 0;
         }

         FontUtils.Glyph.access$4(var17, var21);
         FontUtils.Glyph.access$5(var17, var22);
         if (FontUtils.Glyph.access$2(var17) > this.maxFontHeight) {
            this.maxFontHeight = FontUtils.Glyph.access$2(var17);
         }

         if (FontUtils.Glyph.access$2(var17) > var10) {
            var10 = FontUtils.Glyph.access$2(var17);
         }

         var19.drawString(Character.toString(var13), var21 + 2, var22 + var20.getAscent());
         var21 += FontUtils.Glyph.access$3(var17);
         this.glyphCharacterMap.put(var13, var17);
      }

   }

   public void unbindTexture() {
      GlStateManager.bindTexture(0);
   }

   static class Glyph {
      private int y;
      private int height;
      private int x;
      private int width;

      static void access$4(FontUtils.Glyph var0, int var1) {
         var0.x = var1;
      }

      public int getHeight() {
         return this.height;
      }

      static void access$1(FontUtils.Glyph var0, int var1) {
         var0.height = var1;
      }

      public int getWidth() {
         return this.width;
      }

      public int getY() {
         return this.y;
      }

      static void access$5(FontUtils.Glyph var0, int var1) {
         var0.y = var1;
      }

      Glyph(int var1, int var2, int var3, int var4) {
         this.x = var1;
         this.y = var2;
         this.width = var3;
         this.height = var4;
      }

      static int access$2(FontUtils.Glyph var0) {
         return var0.height;
      }

      static int access$7(FontUtils.Glyph var0) {
         return var0.y;
      }

      static int access$3(FontUtils.Glyph var0) {
         return var0.width;
      }

      static int access$6(FontUtils.Glyph var0) {
         return var0.x;
      }

      Glyph() {
      }

      public int getX() {
         return this.x;
      }

      static void access$0(FontUtils.Glyph var0, int var1) {
         var0.width = var1;
      }
   }
}
