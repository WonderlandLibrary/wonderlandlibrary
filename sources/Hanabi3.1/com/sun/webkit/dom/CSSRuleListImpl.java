package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;

public class CSSRuleListImpl implements CSSRuleList {
   private final long peer;

   CSSRuleListImpl(long var1) {
      this.peer = var1;
      Disposer.addRecord(this, new SelfDisposer(var1));
   }

   static CSSRuleList create(long var0) {
      return var0 == 0L ? null : new CSSRuleListImpl(var0);
   }

   long getPeer() {
      return this.peer;
   }

   public boolean equals(Object var1) {
      return var1 instanceof CSSRuleListImpl && this.peer == ((CSSRuleListImpl)var1).peer;
   }

   public int hashCode() {
      long var1 = this.peer;
      return (int)(var1 ^ var1 >> 17);
   }

   static long getPeer(CSSRuleList var0) {
      return var0 == null ? 0L : ((CSSRuleListImpl)var0).getPeer();
   }

   private static native void dispose(long var0);

   static CSSRuleList getImpl(long var0) {
      return create(var0);
   }

   public int getLength() {
      return getLengthImpl(this.getPeer());
   }

   static native int getLengthImpl(long var0);

   public CSSRule item(int var1) {
      return CSSRuleImpl.getImpl(itemImpl(this.getPeer(), var1));
   }

   static native long itemImpl(long var0, int var2);

   private static class SelfDisposer implements DisposerRecord {
      private final long peer;

      SelfDisposer(long var1) {
         this.peer = var1;
      }

      public void dispose() {
         CSSRuleListImpl.dispose(this.peer);
      }
   }
}
