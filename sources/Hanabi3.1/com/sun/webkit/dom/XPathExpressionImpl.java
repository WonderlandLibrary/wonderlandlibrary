package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.xpath.XPathExpression;
import org.w3c.dom.xpath.XPathResult;

public class XPathExpressionImpl implements XPathExpression {
   private final long peer;

   XPathExpressionImpl(long var1) {
      this.peer = var1;
      Disposer.addRecord(this, new SelfDisposer(var1));
   }

   static XPathExpression create(long var0) {
      return var0 == 0L ? null : new XPathExpressionImpl(var0);
   }

   long getPeer() {
      return this.peer;
   }

   public boolean equals(Object var1) {
      return var1 instanceof XPathExpressionImpl && this.peer == ((XPathExpressionImpl)var1).peer;
   }

   public int hashCode() {
      long var1 = this.peer;
      return (int)(var1 ^ var1 >> 17);
   }

   static long getPeer(XPathExpression var0) {
      return var0 == null ? 0L : ((XPathExpressionImpl)var0).getPeer();
   }

   private static native void dispose(long var0);

   static XPathExpression getImpl(long var0) {
      return create(var0);
   }

   public Object evaluate(Node var1, short var2, Object var3) throws DOMException {
      return this.evaluate(var1, var2, (XPathResult)var3);
   }

   public XPathResult evaluate(Node var1, short var2, XPathResult var3) throws DOMException {
      return XPathResultImpl.getImpl(evaluateImpl(this.getPeer(), NodeImpl.getPeer(var1), var2, XPathResultImpl.getPeer(var3)));
   }

   static native long evaluateImpl(long var0, long var2, short var4, long var5);

   private static class SelfDisposer implements DisposerRecord {
      private final long peer;

      SelfDisposer(long var1) {
         this.peer = var1;
      }

      public void dispose() {
         XPathExpressionImpl.dispose(this.peer);
      }
   }
}
