package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.xpath.XPathNSResolver;

public class XPathNSResolverImpl implements XPathNSResolver {
   private final long peer;

   XPathNSResolverImpl(long var1) {
      this.peer = var1;
      Disposer.addRecord(this, new SelfDisposer(var1));
   }

   static XPathNSResolver create(long var0) {
      return var0 == 0L ? null : new XPathNSResolverImpl(var0);
   }

   long getPeer() {
      return this.peer;
   }

   public boolean equals(Object var1) {
      return var1 instanceof XPathNSResolverImpl && this.peer == ((XPathNSResolverImpl)var1).peer;
   }

   public int hashCode() {
      long var1 = this.peer;
      return (int)(var1 ^ var1 >> 17);
   }

   static long getPeer(XPathNSResolver var0) {
      return var0 == null ? 0L : ((XPathNSResolverImpl)var0).getPeer();
   }

   private static native void dispose(long var0);

   static XPathNSResolver getImpl(long var0) {
      return create(var0);
   }

   public String lookupNamespaceURI(String var1) {
      return lookupNamespaceURIImpl(this.getPeer(), var1);
   }

   static native String lookupNamespaceURIImpl(long var0, String var2);

   private static class SelfDisposer implements DisposerRecord {
      private final long peer;

      SelfDisposer(long var1) {
         this.peer = var1;
      }

      public void dispose() {
         XPathNSResolverImpl.dispose(this.peer);
      }
   }
}
