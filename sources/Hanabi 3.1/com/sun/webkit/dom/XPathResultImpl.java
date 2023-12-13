package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.xpath.XPathResult;

public class XPathResultImpl implements XPathResult {
   private final long peer;
   public static final int ANY_TYPE = 0;
   public static final int NUMBER_TYPE = 1;
   public static final int STRING_TYPE = 2;
   public static final int BOOLEAN_TYPE = 3;
   public static final int UNORDERED_NODE_ITERATOR_TYPE = 4;
   public static final int ORDERED_NODE_ITERATOR_TYPE = 5;
   public static final int UNORDERED_NODE_SNAPSHOT_TYPE = 6;
   public static final int ORDERED_NODE_SNAPSHOT_TYPE = 7;
   public static final int ANY_UNORDERED_NODE_TYPE = 8;
   public static final int FIRST_ORDERED_NODE_TYPE = 9;

   XPathResultImpl(long var1) {
      this.peer = var1;
      Disposer.addRecord(this, new SelfDisposer(var1));
   }

   static XPathResult create(long var0) {
      return var0 == 0L ? null : new XPathResultImpl(var0);
   }

   long getPeer() {
      return this.peer;
   }

   public boolean equals(Object var1) {
      return var1 instanceof XPathResultImpl && this.peer == ((XPathResultImpl)var1).peer;
   }

   public int hashCode() {
      long var1 = this.peer;
      return (int)(var1 ^ var1 >> 17);
   }

   static long getPeer(XPathResult var0) {
      return var0 == null ? 0L : ((XPathResultImpl)var0).getPeer();
   }

   private static native void dispose(long var0);

   static XPathResult getImpl(long var0) {
      return create(var0);
   }

   public short getResultType() {
      return getResultTypeImpl(this.getPeer());
   }

   static native short getResultTypeImpl(long var0);

   public double getNumberValue() throws DOMException {
      return getNumberValueImpl(this.getPeer());
   }

   static native double getNumberValueImpl(long var0);

   public String getStringValue() throws DOMException {
      return getStringValueImpl(this.getPeer());
   }

   static native String getStringValueImpl(long var0);

   public boolean getBooleanValue() throws DOMException {
      return getBooleanValueImpl(this.getPeer());
   }

   static native boolean getBooleanValueImpl(long var0);

   public Node getSingleNodeValue() throws DOMException {
      return NodeImpl.getImpl(getSingleNodeValueImpl(this.getPeer()));
   }

   static native long getSingleNodeValueImpl(long var0);

   public boolean getInvalidIteratorState() {
      return getInvalidIteratorStateImpl(this.getPeer());
   }

   static native boolean getInvalidIteratorStateImpl(long var0);

   public int getSnapshotLength() throws DOMException {
      return getSnapshotLengthImpl(this.getPeer());
   }

   static native int getSnapshotLengthImpl(long var0);

   public Node iterateNext() throws DOMException {
      return NodeImpl.getImpl(iterateNextImpl(this.getPeer()));
   }

   static native long iterateNextImpl(long var0);

   public Node snapshotItem(int var1) throws DOMException {
      return NodeImpl.getImpl(snapshotItemImpl(this.getPeer(), var1));
   }

   static native long snapshotItemImpl(long var0, int var2);

   private static class SelfDisposer implements DisposerRecord {
      private final long peer;

      SelfDisposer(long var1) {
         this.peer = var1;
      }

      public void dispose() {
         XPathResultImpl.dispose(this.peer);
      }
   }
}
