package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLCollection;

public class HTMLCollectionImpl implements HTMLCollection {
   private final long peer;
   private static final int TYPE_HTMLOptionsCollection = 1;

   HTMLCollectionImpl(long var1) {
      this.peer = var1;
      Disposer.addRecord(this, new SelfDisposer(var1));
   }

   static HTMLCollection create(long var0) {
      // $FF: Couldn't be decompiled
   }

   long getPeer() {
      return this.peer;
   }

   public boolean equals(Object var1) {
      return var1 instanceof HTMLCollectionImpl && this.peer == ((HTMLCollectionImpl)var1).peer;
   }

   public int hashCode() {
      long var1 = this.peer;
      return (int)(var1 ^ var1 >> 17);
   }

   static long getPeer(HTMLCollection var0) {
      return var0 == null ? 0L : ((HTMLCollectionImpl)var0).getPeer();
   }

   private static native void dispose(long var0);

   private static native int getCPPTypeImpl(long var0);

   static HTMLCollection getImpl(long var0) {
      return create(var0);
   }

   public int getLength() {
      return getLengthImpl(this.getPeer());
   }

   static native int getLengthImpl(long var0);

   public Node item(int var1) {
      return NodeImpl.getImpl(itemImpl(this.getPeer(), var1));
   }

   static native long itemImpl(long var0, int var2);

   public Node namedItem(String var1) {
      return NodeImpl.getImpl(namedItemImpl(this.getPeer(), var1));
   }

   static native long namedItemImpl(long var0, String var2);

   private static class SelfDisposer implements DisposerRecord {
      private final long peer;

      SelfDisposer(long var1) {
         this.peer = var1;
      }

      public void dispose() {
         HTMLCollectionImpl.dispose(this.peer);
      }
   }
}
