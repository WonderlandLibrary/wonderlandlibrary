package com.sun.javafx.text;

import com.sun.javafx.geom.RectBounds;

public class TextLine implements com.sun.javafx.scene.text.TextLine {
   TextRun[] runs;
   RectBounds bounds;
   float lsb;
   float rsb;
   float leading;
   int start;
   int length;

   public TextLine(int var1, int var2, TextRun[] var3, float var4, float var5, float var6, float var7) {
      this.start = var1;
      this.length = var2;
      this.bounds = new RectBounds(0.0F, var5, var4, var6 + var7);
      this.leading = var7;
      this.runs = var3;
   }

   public RectBounds getBounds() {
      return this.bounds;
   }

   public float getLeading() {
      return this.leading;
   }

   public TextRun[] getRuns() {
      return this.runs;
   }

   public int getStart() {
      return this.start;
   }

   public int getLength() {
      return this.length;
   }

   public void setSideBearings(float var1, float var2) {
      this.lsb = var1;
      this.rsb = var2;
   }

   public float getLeftSideBearing() {
      return this.lsb;
   }

   public float getRightSideBearing() {
      return this.rsb;
   }

   public void setAlignment(float var1) {
      this.bounds.setMinX(var1);
      this.bounds.setMaxX(var1 + this.bounds.getMaxX());
   }

   public void setWidth(float var1) {
      this.bounds.setMaxX(this.bounds.getMinX() + var1);
   }
}
