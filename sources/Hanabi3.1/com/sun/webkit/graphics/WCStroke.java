package com.sun.webkit.graphics;

import java.util.Arrays;

public abstract class WCStroke {
   public static final int NO_STROKE = 0;
   public static final int SOLID_STROKE = 1;
   public static final int DOTTED_STROKE = 2;
   public static final int DASHED_STROKE = 3;
   public static final int BUTT_CAP = 0;
   public static final int ROUND_CAP = 1;
   public static final int SQUARE_CAP = 2;
   public static final int MITER_JOIN = 0;
   public static final int ROUND_JOIN = 1;
   public static final int BEVEL_JOIN = 2;
   private int style = 1;
   private int lineCap = 0;
   private int lineJoin = 0;
   private float miterLimit = 10.0F;
   private float thickness = 1.0F;
   private float offset;
   private float[] sizes;
   private Object paint;

   protected abstract void invalidate();

   public abstract Object getPlatformStroke();

   public void copyFrom(WCStroke var1) {
      this.style = var1.style;
      this.lineCap = var1.lineCap;
      this.lineJoin = var1.lineJoin;
      this.miterLimit = var1.miterLimit;
      this.thickness = var1.thickness;
      this.offset = var1.offset;
      this.sizes = var1.sizes;
      this.paint = var1.paint;
   }

   public void setStyle(int var1) {
      if (var1 != 1 && var1 != 2 && var1 != 3) {
         var1 = 0;
      }

      if (this.style != var1) {
         this.style = var1;
         this.invalidate();
      }

   }

   public void setLineCap(int var1) {
      if (var1 != 1 && var1 != 2) {
         var1 = 0;
      }

      if (this.lineCap != var1) {
         this.lineCap = var1;
         this.invalidate();
      }

   }

   public void setLineJoin(int var1) {
      if (var1 != 1 && var1 != 2) {
         var1 = 0;
      }

      if (this.lineJoin != var1) {
         this.lineJoin = var1;
         this.invalidate();
      }

   }

   public void setMiterLimit(float var1) {
      if (var1 < 1.0F) {
         var1 = 1.0F;
      }

      if (this.miterLimit != var1) {
         this.miterLimit = var1;
         this.invalidate();
      }

   }

   public void setThickness(float var1) {
      if (var1 < 0.0F) {
         var1 = 1.0F;
      }

      if (this.thickness != var1) {
         this.thickness = var1;
         this.invalidate();
      }

   }

   public void setDashOffset(float var1) {
      if (this.offset != var1) {
         this.offset = var1;
         this.invalidate();
      }

   }

   public void setDashSizes(float... var1) {
      if (var1 != null && var1.length != 0) {
         if (!Arrays.equals(this.sizes, var1)) {
            this.sizes = (float[])var1.clone();
            this.invalidate();
         }
      } else if (this.sizes != null) {
         this.sizes = null;
         this.invalidate();
      }

   }

   public void setPaint(Object var1) {
      this.paint = var1;
   }

   public int getStyle() {
      return this.style;
   }

   public int getLineCap() {
      return this.lineCap;
   }

   public int getLineJoin() {
      return this.lineJoin;
   }

   public float getMiterLimit() {
      return this.miterLimit;
   }

   public float getThickness() {
      return this.thickness;
   }

   public float getDashOffset() {
      return this.offset;
   }

   public float[] getDashSizes() {
      return this.sizes != null ? (float[])this.sizes.clone() : null;
   }

   public Object getPaint() {
      return this.paint;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(this.getClass().getSimpleName());
      var1.append("[style=").append(this.style);
      var1.append(", lineCap=").append(this.lineCap);
      var1.append(", lineJoin=").append(this.lineJoin);
      var1.append(", miterLimit=").append(this.miterLimit);
      var1.append(", thickness=").append(this.thickness);
      var1.append(", offset=").append(this.offset);
      var1.append(", sizes=").append(Arrays.toString(this.sizes));
      var1.append(", paint=").append(this.paint);
      return var1.append("]").toString();
   }
}
