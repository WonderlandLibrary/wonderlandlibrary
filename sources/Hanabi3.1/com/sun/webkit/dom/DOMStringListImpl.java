package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMStringList;

public class DOMStringListImpl implements DOMStringList {
   private final long peer;

   DOMStringListImpl(long var1) {
      this.peer = var1;
      Disposer.addRecord(this, new SelfDisposer(var1));
   }

   static DOMStringList create(long var0) {
      return var0 == 0L ? null : new DOMStringListImpl(var0);
   }

   long getPeer() {
      return this.peer;
   }

   public boolean equals(Object var1) {
      return var1 instanceof DOMStringListImpl && this.peer == ((DOMStringListImpl)var1).peer;
   }

   public int hashCode() {
      long var1 = this.peer;
      return (int)(var1 ^ var1 >> 17);
   }

   static long getPeer(DOMStringList var0) {
      return var0 == null ? 0L : ((DOMStringListImpl)var0).getPeer();
   }

   private static native void dispose(long var0);

   static DOMStringList getImpl(long var0) {
      return create(var0);
   }

   public int getLength() {
      return getLengthImpl(this.getPeer());
   }

   static native int getLengthImpl(long var0);

   public String item(int var1) {
      return itemImpl(this.getPeer(), var1);
   }

   static native String itemImpl(long var0, int var2);

   public boolean contains(String var1) {
      return containsImpl(this.getPeer(), var1);
   }

   static native boolean containsImpl(long var0, String var2);

   private static class SelfDisposer implements DisposerRecord {
      private final long peer;

      SelfDisposer(long var1) {
         this.peer = var1;
      }

      public void dispose() {
         DOMStringListImpl.dispose(this.peer);
      }
   }
}
