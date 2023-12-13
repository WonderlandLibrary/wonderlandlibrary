package com.sun.javafx.tk;

import javafx.scene.text.Font;

public class FontMetrics {
   private float maxAscent;
   private float ascent;
   private float xheight;
   private int baseline;
   private float descent;
   private float maxDescent;
   private float leading;
   private float lineHeight;
   private Font font;

   public static FontMetrics impl_createFontMetrics(float var0, float var1, float var2, float var3, float var4, float var5, Font var6) {
      return new FontMetrics(var0, var1, var2, var3, var4, var5, var6);
   }

   public final float getMaxAscent() {
      return this.maxAscent;
   }

   public final float getAscent() {
      return this.ascent;
   }

   public final float getXheight() {
      return this.xheight;
   }

   public final int getBaseline() {
      return this.baseline;
   }

   public final float getDescent() {
      return this.descent;
   }

   public final float getMaxDescent() {
      return this.maxDescent;
   }

   public final float getLeading() {
      return this.leading;
   }

   public final float getLineHeight() {
      return this.lineHeight;
   }

   public final Font getFont() {
      if (this.font == null) {
         this.font = Font.getDefault();
      }

      return this.font;
   }

   public FontMetrics(float var1, float var2, float var3, float var4, float var5, float var6, Font var7) {
      this.maxAscent = var1;
      this.ascent = var2;
      this.xheight = var3;
      this.descent = var4;
      this.maxDescent = var5;
      this.leading = var6;
      this.font = var7;
      this.lineHeight = var1 + var5 + var6;
   }

   public float computeStringWidth(String var1) {
      return Toolkit.getToolkit().getFontLoader().computeStringWidth(var1, this.getFont());
   }

   public String toString() {
      return "FontMetrics: [maxAscent=" + this.getMaxAscent() + ", ascent=" + this.getAscent() + ", xheight=" + this.getXheight() + ", baseline=" + this.getBaseline() + ", descent=" + this.getDescent() + ", maxDescent=" + this.getMaxDescent() + ", leading=" + this.getLeading() + ", lineHeight=" + this.getLineHeight() + ", font=" + this.getFont() + "]";
   }
}
