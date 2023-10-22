package com.sun.javafx.font;

public class PrismMetrics implements Metrics {
   PrismFontFile fontResource;
   float ascent;
   float descent;
   float linegap;
   private float[] styleMetrics;
   float size;
   static final int XHEIGHT = 0;
   static final int CAPHEIGHT = 1;
   static final int TYPO_ASCENT = 2;
   static final int TYPO_DESCENT = 3;
   static final int TYPO_LINEGAP = 4;
   static final int STRIKETHROUGH_THICKNESS = 5;
   static final int STRIKETHROUGH_OFFSET = 6;
   static final int UNDERLINE_THICKESS = 7;
   static final int UNDERLINE_OFFSET = 8;
   static final int METRICS_TOTAL = 9;

   PrismMetrics(float var1, float var2, float var3, PrismFontFile var4, float var5) {
      this.ascent = var1;
      this.descent = var2;
      this.linegap = var3;
      this.fontResource = var4;
      this.size = var5;
   }

   public float getAscent() {
      return this.ascent;
   }

   public float getDescent() {
      return this.descent;
   }

   public float getLineGap() {
      return this.linegap;
   }

   public float getLineHeight() {
      return -this.ascent + this.descent + this.linegap;
   }

   private void checkStyleMetrics() {
      if (this.styleMetrics == null) {
         this.styleMetrics = this.fontResource.getStyleMetrics(this.size);
      }

   }

   public float getTypoAscent() {
      this.checkStyleMetrics();
      return this.styleMetrics[2];
   }

   public float getTypoDescent() {
      this.checkStyleMetrics();
      return this.styleMetrics[3];
   }

   public float getTypoLineGap() {
      this.checkStyleMetrics();
      return this.styleMetrics[4];
   }

   public float getCapHeight() {
      this.checkStyleMetrics();
      return this.styleMetrics[1];
   }

   public float getXHeight() {
      this.checkStyleMetrics();
      return this.styleMetrics[0];
   }

   public float getStrikethroughOffset() {
      this.checkStyleMetrics();
      return this.styleMetrics[6];
   }

   public float getStrikethroughThickness() {
      this.checkStyleMetrics();
      return this.styleMetrics[5];
   }

   public float getUnderLineOffset() {
      this.checkStyleMetrics();
      return this.styleMetrics[8];
   }

   public float getUnderLineThickness() {
      this.checkStyleMetrics();
      return this.styleMetrics[7];
   }

   public String toString() {
      return "ascent = " + this.getAscent() + " descent = " + this.getDescent() + " linegap = " + this.getLineGap() + " lineheight = " + this.getLineHeight();
   }
}
