package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.RGBColor;

public class RGBColorImpl implements RGBColor {
   private final long peer;

   RGBColorImpl(long var1) {
      this.peer = var1;
      Disposer.addRecord(this, new SelfDisposer(var1));
   }

   static RGBColor create(long var0) {
      return var0 == 0L ? null : new RGBColorImpl(var0);
   }

   long getPeer() {
      return this.peer;
   }

   public boolean equals(Object var1) {
      return var1 instanceof RGBColorImpl && this.peer == ((RGBColorImpl)var1).peer;
   }

   public int hashCode() {
      long var1 = this.peer;
      return (int)(var1 ^ var1 >> 17);
   }

   static long getPeer(RGBColor var0) {
      return var0 == null ? 0L : ((RGBColorImpl)var0).getPeer();
   }

   private static native void dispose(long var0);

   static RGBColor getImpl(long var0) {
      return create(var0);
   }

   public CSSPrimitiveValue getRed() {
      return CSSPrimitiveValueImpl.getImpl(getRedImpl(this.getPeer()));
   }

   static native long getRedImpl(long var0);

   public CSSPrimitiveValue getGreen() {
      return CSSPrimitiveValueImpl.getImpl(getGreenImpl(this.getPeer()));
   }

   static native long getGreenImpl(long var0);

   public CSSPrimitiveValue getBlue() {
      return CSSPrimitiveValueImpl.getImpl(getBlueImpl(this.getPeer()));
   }

   static native long getBlueImpl(long var0);

   public CSSPrimitiveValue getAlpha() {
      return CSSPrimitiveValueImpl.getImpl(getAlphaImpl(this.getPeer()));
   }

   static native long getAlphaImpl(long var0);

   private static class SelfDisposer implements DisposerRecord {
      private final long peer;

      SelfDisposer(long var1) {
         this.peer = var1;
      }

      public void dispose() {
         RGBColorImpl.dispose(this.peer);
      }
   }
}
