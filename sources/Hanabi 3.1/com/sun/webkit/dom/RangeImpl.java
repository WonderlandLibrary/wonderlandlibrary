package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.ranges.Range;

public class RangeImpl implements Range {
   private final long peer;
   public static final int START_TO_START = 0;
   public static final int START_TO_END = 1;
   public static final int END_TO_END = 2;
   public static final int END_TO_START = 3;
   public static final int NODE_BEFORE = 0;
   public static final int NODE_AFTER = 1;
   public static final int NODE_BEFORE_AND_AFTER = 2;
   public static final int NODE_INSIDE = 3;

   RangeImpl(long var1) {
      this.peer = var1;
      Disposer.addRecord(this, new SelfDisposer(var1));
   }

   static Range create(long var0) {
      return var0 == 0L ? null : new RangeImpl(var0);
   }

   long getPeer() {
      return this.peer;
   }

   public boolean equals(Object var1) {
      return var1 instanceof RangeImpl && this.peer == ((RangeImpl)var1).peer;
   }

   public int hashCode() {
      long var1 = this.peer;
      return (int)(var1 ^ var1 >> 17);
   }

   static long getPeer(Range var0) {
      return var0 == null ? 0L : ((RangeImpl)var0).getPeer();
   }

   private static native void dispose(long var0);

   static Range getImpl(long var0) {
      return create(var0);
   }

   public Node getStartContainer() {
      return NodeImpl.getImpl(getStartContainerImpl(this.getPeer()));
   }

   static native long getStartContainerImpl(long var0);

   public int getStartOffset() {
      return getStartOffsetImpl(this.getPeer());
   }

   static native int getStartOffsetImpl(long var0);

   public Node getEndContainer() {
      return NodeImpl.getImpl(getEndContainerImpl(this.getPeer()));
   }

   static native long getEndContainerImpl(long var0);

   public int getEndOffset() {
      return getEndOffsetImpl(this.getPeer());
   }

   static native int getEndOffsetImpl(long var0);

   public boolean getCollapsed() {
      return getCollapsedImpl(this.getPeer());
   }

   static native boolean getCollapsedImpl(long var0);

   public Node getCommonAncestorContainer() {
      return NodeImpl.getImpl(getCommonAncestorContainerImpl(this.getPeer()));
   }

   static native long getCommonAncestorContainerImpl(long var0);

   public String getText() {
      return getTextImpl(this.getPeer());
   }

   static native String getTextImpl(long var0);

   public void setStart(Node var1, int var2) throws DOMException {
      setStartImpl(this.getPeer(), NodeImpl.getPeer(var1), var2);
   }

   static native void setStartImpl(long var0, long var2, int var4);

   public void setEnd(Node var1, int var2) throws DOMException {
      setEndImpl(this.getPeer(), NodeImpl.getPeer(var1), var2);
   }

   static native void setEndImpl(long var0, long var2, int var4);

   public void setStartBefore(Node var1) throws DOMException {
      setStartBeforeImpl(this.getPeer(), NodeImpl.getPeer(var1));
   }

   static native void setStartBeforeImpl(long var0, long var2);

   public void setStartAfter(Node var1) throws DOMException {
      setStartAfterImpl(this.getPeer(), NodeImpl.getPeer(var1));
   }

   static native void setStartAfterImpl(long var0, long var2);

   public void setEndBefore(Node var1) throws DOMException {
      setEndBeforeImpl(this.getPeer(), NodeImpl.getPeer(var1));
   }

   static native void setEndBeforeImpl(long var0, long var2);

   public void setEndAfter(Node var1) throws DOMException {
      setEndAfterImpl(this.getPeer(), NodeImpl.getPeer(var1));
   }

   static native void setEndAfterImpl(long var0, long var2);

   public void collapse(boolean var1) {
      collapseImpl(this.getPeer(), var1);
   }

   static native void collapseImpl(long var0, boolean var2);

   public void selectNode(Node var1) throws DOMException {
      selectNodeImpl(this.getPeer(), NodeImpl.getPeer(var1));
   }

   static native void selectNodeImpl(long var0, long var2);

   public void selectNodeContents(Node var1) throws DOMException {
      selectNodeContentsImpl(this.getPeer(), NodeImpl.getPeer(var1));
   }

   static native void selectNodeContentsImpl(long var0, long var2);

   public short compareBoundaryPoints(short var1, Range var2) throws DOMException {
      return compareBoundaryPointsImpl(this.getPeer(), var1, getPeer(var2));
   }

   static native short compareBoundaryPointsImpl(long var0, short var2, long var3);

   public void deleteContents() throws DOMException {
      deleteContentsImpl(this.getPeer());
   }

   static native void deleteContentsImpl(long var0);

   public DocumentFragment extractContents() throws DOMException {
      return DocumentFragmentImpl.getImpl(extractContentsImpl(this.getPeer()));
   }

   static native long extractContentsImpl(long var0);

   public DocumentFragment cloneContents() throws DOMException {
      return DocumentFragmentImpl.getImpl(cloneContentsImpl(this.getPeer()));
   }

   static native long cloneContentsImpl(long var0);

   public void insertNode(Node var1) throws DOMException {
      insertNodeImpl(this.getPeer(), NodeImpl.getPeer(var1));
   }

   static native void insertNodeImpl(long var0, long var2);

   public void surroundContents(Node var1) throws DOMException {
      surroundContentsImpl(this.getPeer(), NodeImpl.getPeer(var1));
   }

   static native void surroundContentsImpl(long var0, long var2);

   public Range cloneRange() {
      return getImpl(cloneRangeImpl(this.getPeer()));
   }

   static native long cloneRangeImpl(long var0);

   public String toString() {
      return toStringImpl(this.getPeer());
   }

   static native String toStringImpl(long var0);

   public void detach() {
      detachImpl(this.getPeer());
   }

   static native void detachImpl(long var0);

   public DocumentFragment createContextualFragment(String var1) throws DOMException {
      return DocumentFragmentImpl.getImpl(createContextualFragmentImpl(this.getPeer(), var1));
   }

   static native long createContextualFragmentImpl(long var0, String var2);

   public short compareNode(Node var1) throws DOMException {
      return compareNodeImpl(this.getPeer(), NodeImpl.getPeer(var1));
   }

   static native short compareNodeImpl(long var0, long var2);

   public short comparePoint(Node var1, int var2) throws DOMException {
      return comparePointImpl(this.getPeer(), NodeImpl.getPeer(var1), var2);
   }

   static native short comparePointImpl(long var0, long var2, int var4);

   public boolean intersectsNode(Node var1) throws DOMException {
      return intersectsNodeImpl(this.getPeer(), NodeImpl.getPeer(var1));
   }

   static native boolean intersectsNodeImpl(long var0, long var2);

   public boolean isPointInRange(Node var1, int var2) throws DOMException {
      return isPointInRangeImpl(this.getPeer(), NodeImpl.getPeer(var1), var2);
   }

   static native boolean isPointInRangeImpl(long var0, long var2, int var4);

   public void expand(String var1) throws DOMException {
      expandImpl(this.getPeer(), var1);
   }

   static native void expandImpl(long var0, String var2);

   private static class SelfDisposer implements DisposerRecord {
      private final long peer;

      SelfDisposer(long var1) {
         this.peer = var1;
      }

      public void dispose() {
         RangeImpl.dispose(this.peer);
      }
   }
}
