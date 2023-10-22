package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.stylesheets.StyleSheet;
import org.w3c.dom.stylesheets.StyleSheetList;

public class StyleSheetListImpl implements StyleSheetList {
   private final long peer;

   StyleSheetListImpl(long var1) {
      this.peer = var1;
      Disposer.addRecord(this, new SelfDisposer(var1));
   }

   static StyleSheetList create(long var0) {
      return var0 == 0L ? null : new StyleSheetListImpl(var0);
   }

   long getPeer() {
      return this.peer;
   }

   public boolean equals(Object var1) {
      return var1 instanceof StyleSheetListImpl && this.peer == ((StyleSheetListImpl)var1).peer;
   }

   public int hashCode() {
      long var1 = this.peer;
      return (int)(var1 ^ var1 >> 17);
   }

   static long getPeer(StyleSheetList var0) {
      return var0 == null ? 0L : ((StyleSheetListImpl)var0).getPeer();
   }

   private static native void dispose(long var0);

   static StyleSheetList getImpl(long var0) {
      return create(var0);
   }

   public int getLength() {
      return getLengthImpl(this.getPeer());
   }

   static native int getLengthImpl(long var0);

   public StyleSheet item(int var1) {
      return StyleSheetImpl.getImpl(itemImpl(this.getPeer(), var1));
   }

   static native long itemImpl(long var0, int var2);

   private static class SelfDisposer implements DisposerRecord {
      private final long peer;

      SelfDisposer(long var1) {
         this.peer = var1;
      }

      public void dispose() {
         StyleSheetListImpl.dispose(this.peer);
      }
   }
}
