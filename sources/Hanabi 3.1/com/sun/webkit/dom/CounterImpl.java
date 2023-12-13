package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.css.Counter;

public class CounterImpl implements Counter {
   private final long peer;

   CounterImpl(long var1) {
      this.peer = var1;
      Disposer.addRecord(this, new SelfDisposer(var1));
   }

   static Counter create(long var0) {
      return var0 == 0L ? null : new CounterImpl(var0);
   }

   long getPeer() {
      return this.peer;
   }

   public boolean equals(Object var1) {
      return var1 instanceof CounterImpl && this.peer == ((CounterImpl)var1).peer;
   }

   public int hashCode() {
      long var1 = this.peer;
      return (int)(var1 ^ var1 >> 17);
   }

   static long getPeer(Counter var0) {
      return var0 == null ? 0L : ((CounterImpl)var0).getPeer();
   }

   private static native void dispose(long var0);

   static Counter getImpl(long var0) {
      return create(var0);
   }

   public String getIdentifier() {
      return getIdentifierImpl(this.getPeer());
   }

   static native String getIdentifierImpl(long var0);

   public String getListStyle() {
      return getListStyleImpl(this.getPeer());
   }

   static native String getListStyleImpl(long var0);

   public String getSeparator() {
      return getSeparatorImpl(this.getPeer());
   }

   static native String getSeparatorImpl(long var0);

   private static class SelfDisposer implements DisposerRecord {
      private final long peer;

      SelfDisposer(long var1) {
         this.peer = var1;
      }

      public void dispose() {
         CounterImpl.dispose(this.peer);
      }
   }
}
