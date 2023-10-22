package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

public class NodeIteratorImpl implements NodeIterator {
   private final long peer;

   NodeIteratorImpl(long var1) {
      this.peer = var1;
      Disposer.addRecord(this, new SelfDisposer(var1));
   }

   static NodeIterator create(long var0) {
      return var0 == 0L ? null : new NodeIteratorImpl(var0);
   }

   long getPeer() {
      return this.peer;
   }

   public boolean equals(Object var1) {
      return var1 instanceof NodeIteratorImpl && this.peer == ((NodeIteratorImpl)var1).peer;
   }

   public int hashCode() {
      long var1 = this.peer;
      return (int)(var1 ^ var1 >> 17);
   }

   static long getPeer(NodeIterator var0) {
      return var0 == null ? 0L : ((NodeIteratorImpl)var0).getPeer();
   }

   private static native void dispose(long var0);

   static NodeIterator getImpl(long var0) {
      return create(var0);
   }

   public Node getRoot() {
      return NodeImpl.getImpl(getRootImpl(this.getPeer()));
   }

   static native long getRootImpl(long var0);

   public int getWhatToShow() {
      return getWhatToShowImpl(this.getPeer());
   }

   static native int getWhatToShowImpl(long var0);

   public NodeFilter getFilter() {
      return NodeFilterImpl.getImpl(getFilterImpl(this.getPeer()));
   }

   static native long getFilterImpl(long var0);

   public boolean getExpandEntityReferences() {
      return getExpandEntityReferencesImpl(this.getPeer());
   }

   static native boolean getExpandEntityReferencesImpl(long var0);

   public Node getReferenceNode() {
      return NodeImpl.getImpl(getReferenceNodeImpl(this.getPeer()));
   }

   static native long getReferenceNodeImpl(long var0);

   public boolean getPointerBeforeReferenceNode() {
      return getPointerBeforeReferenceNodeImpl(this.getPeer());
   }

   static native boolean getPointerBeforeReferenceNodeImpl(long var0);

   public Node nextNode() {
      return NodeImpl.getImpl(nextNodeImpl(this.getPeer()));
   }

   static native long nextNodeImpl(long var0);

   public Node previousNode() {
      return NodeImpl.getImpl(previousNodeImpl(this.getPeer()));
   }

   static native long previousNodeImpl(long var0);

   public void detach() {
      detachImpl(this.getPeer());
   }

   static native void detachImpl(long var0);

   private static class SelfDisposer implements DisposerRecord {
      private final long peer;

      SelfDisposer(long var1) {
         this.peer = var1;
      }

      public void dispose() {
         NodeIteratorImpl.dispose(this.peer);
      }
   }
}
