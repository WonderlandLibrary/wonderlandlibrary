package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.Rect;

public class RectImpl implements Rect {
   private final long peer;

   RectImpl(long var1) {
      this.peer = var1;
      Disposer.addRecord(this, new SelfDisposer(var1));
   }

   static Rect create(long var0) {
      return var0 == 0L ? null : new RectImpl(var0);
   }

   long getPeer() {
      return this.peer;
   }

   public boolean equals(Object var1) {
      return var1 instanceof RectImpl && this.peer == ((RectImpl)var1).peer;
   }

   public int hashCode() {
      long var1 = this.peer;
      return (int)(var1 ^ var1 >> 17);
   }

   static long getPeer(Rect var0) {
      return var0 == null ? 0L : ((RectImpl)var0).getPeer();
   }

   private static native void dispose(long var0);

   static Rect getImpl(long var0) {
      return create(var0);
   }

   public CSSPrimitiveValue getTop() {
      return CSSPrimitiveValueImpl.getImpl(getTopImpl(this.getPeer()));
   }

   static native long getTopImpl(long var0);

   public CSSPrimitiveValue getRight() {
      return CSSPrimitiveValueImpl.getImpl(getRightImpl(this.getPeer()));
   }

   static native long getRightImpl(long var0);

   public CSSPrimitiveValue getBottom() {
      return CSSPrimitiveValueImpl.getImpl(getBottomImpl(this.getPeer()));
   }

   static native long getBottomImpl(long var0);

   public CSSPrimitiveValue getLeft() {
      return CSSPrimitiveValueImpl.getImpl(getLeftImpl(this.getPeer()));
   }

   static native long getLeftImpl(long var0);

   private static class SelfDisposer implements DisposerRecord {
      private final long peer;

      SelfDisposer(long var1) {
         this.peer = var1;
      }

      public void dispose() {
         RectImpl.dispose(this.peer);
      }
   }
}
