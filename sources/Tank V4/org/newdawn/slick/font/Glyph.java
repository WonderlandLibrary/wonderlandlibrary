package org.newdawn.slick.font;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.GlyphMetrics;
import java.awt.font.GlyphVector;
import org.newdawn.slick.Image;
import org.newdawn.slick.UnicodeFont;

public class Glyph {
   private int codePoint;
   private short width;
   private short height;
   private short yOffset;
   private boolean isMissing;
   private Shape shape;
   private Image image;

   public Glyph(int var1, Rectangle var2, GlyphVector var3, int var4, UnicodeFont var5) {
      this.codePoint = var1;
      GlyphMetrics var6 = var3.getGlyphMetrics(var4);
      int var7 = (int)var6.getLSB();
      if (var7 > 0) {
         var7 = 0;
      }

      int var8 = (int)var6.getRSB();
      if (var8 > 0) {
         var8 = 0;
      }

      int var9 = var2.width - var7 - var8;
      int var10 = var2.height;
      if (var9 > 0 && var10 > 0) {
         int var11 = var5.getPaddingTop();
         int var12 = var5.getPaddingRight();
         int var13 = var5.getPaddingBottom();
         int var14 = var5.getPaddingLeft();
         byte var15 = 1;
         this.width = (short)(var9 + var14 + var12 + var15);
         this.height = (short)(var10 + var11 + var13 + var15);
         this.yOffset = (short)(var5.getAscent() + var2.y - var11);
      }

      this.shape = var3.getGlyphOutline(var4, (float)(-var2.x + var5.getPaddingLeft()), (float)(-var2.y + var5.getPaddingTop()));
      this.isMissing = !var5.getFont().canDisplay((char)var1);
   }

   public int getCodePoint() {
      return this.codePoint;
   }

   public boolean isMissing() {
      return this.isMissing;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public Shape getShape() {
      return this.shape;
   }

   public void setShape(Shape var1) {
      this.shape = var1;
   }

   public Image getImage() {
      return this.image;
   }

   public void setImage(Image var1) {
      this.image = var1;
   }

   public int getYOffset() {
      return this.yOffset;
   }
}
