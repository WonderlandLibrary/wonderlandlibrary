package com.sun.webkit.graphics;

public abstract class WCFont extends Ref {
   public abstract Object getPlatformFont();

   public abstract WCFont deriveFont(float var1);

   public abstract WCTextRun[] getTextRuns(String var1);

   public abstract int[] getGlyphCodes(char[] var1);

   public abstract float getXHeight();

   public abstract double getGlyphWidth(int var1);

   public abstract float[] getGlyphBoundingBox(int var1);

   public int hashCode() {
      Object var1 = this.getPlatformFont();
      return var1 != null ? var1.hashCode() : 0;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof WCFont) {
         Object var2 = this.getPlatformFont();
         Object var3 = ((WCFont)var1).getPlatformFont();
         return var2 == null ? var3 == null : var2.equals(var3);
      } else {
         return false;
      }
   }

   public abstract float getAscent();

   public abstract float getDescent();

   public abstract float getLineSpacing();

   public abstract float getLineGap();

   public abstract boolean hasUniformLineMetrics();

   public abstract float getCapHeight();
}
