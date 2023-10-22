package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;

public class TreeWalkerImpl implements TreeWalker {
   private final long peer;

   TreeWalkerImpl(long var1) {
      this.peer = var1;
      Disposer.addRecord(this, new SelfDisposer(var1));
   }

   static TreeWalker create(long var0) {
      return var0 == 0L ? null : new TreeWalkerImpl(var0);
   }

   long getPeer() {
      return this.peer;
   }

   public boolean equals(Object var1) {
      return var1 instanceof TreeWalkerImpl && this.peer == ((TreeWalkerImpl)var1).peer;
   }

   public int hashCode() {
      long var1 = this.peer;
      return (int)(var1 ^ var1 >> 17);
   }

   static long getPeer(TreeWalker var0) {
      return var0 == null ? 0L : ((TreeWalkerImpl)var0).getPeer();
   }

   private static native void dispose(long var0);

   static TreeWalker getImpl(long var0) {
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

   public Node getCurrentNode() {
      return NodeImpl.getImpl(getCurrentNodeImpl(this.getPeer()));
   }

   static native long getCurrentNodeImpl(long var0);

   public void setCurrentNode(Node var1) throws DOMException {
      setCurrentNodeImpl(this.getPeer(), NodeImpl.getPeer(var1));
   }

   static native void setCurrentNodeImpl(long var0, long var2);

   public Node parentNode() {
      return NodeImpl.getImpl(parentNodeImpl(this.getPeer()));
   }

   static native long parentNodeImpl(long var0);

   public Node firstChild() {
      return NodeImpl.getImpl(firstChildImpl(this.getPeer()));
   }

   static native long firstChildImpl(long var0);

   public Node lastChild() {
      return NodeImpl.getImpl(lastChildImpl(this.getPeer()));
   }

   static native long lastChildImpl(long var0);

   public Node previousSibling() {
      return NodeImpl.getImpl(previousSiblingImpl(this.getPeer()));
   }

   static native long previousSiblingImpl(long var0);

   public Node nextSibling() {
      return NodeImpl.getImpl(nextSiblingImpl(this.getPeer()));
   }

   static native long nextSiblingImpl(long var0);

   public Node previousNode() {
      return NodeImpl.getImpl(previousNodeImpl(this.getPeer()));
   }

   static native long previousNodeImpl(long var0);

   public Node nextNode() {
      return NodeImpl.getImpl(nextNodeImpl(this.getPeer()));
   }

   static native long nextNodeImpl(long var0);

   private static class SelfDisposer implements DisposerRecord {
      private final long peer;

      SelfDisposer(long var1) {
         this.peer = var1;
      }

      public void dispose() {
         TreeWalkerImpl.dispose(this.peer);
      }
   }
}
