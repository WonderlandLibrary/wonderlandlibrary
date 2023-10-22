package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.ranges.Range;

public class DOMSelectionImpl {
   private final long peer;

   DOMSelectionImpl(long var1) {
      this.peer = var1;
      Disposer.addRecord(this, new SelfDisposer(var1));
   }

   static DOMSelectionImpl create(long var0) {
      return var0 == 0L ? null : new DOMSelectionImpl(var0);
   }

   long getPeer() {
      return this.peer;
   }

   public boolean equals(Object var1) {
      return var1 instanceof DOMSelectionImpl && this.peer == ((DOMSelectionImpl)var1).peer;
   }

   public int hashCode() {
      long var1 = this.peer;
      return (int)(var1 ^ var1 >> 17);
   }

   static long getPeer(DOMSelectionImpl var0) {
      return var0 == null ? 0L : var0.getPeer();
   }

   private static native void dispose(long var0);

   static DOMSelectionImpl getImpl(long var0) {
      return create(var0);
   }

   public Node getAnchorNode() {
      return NodeImpl.getImpl(getAnchorNodeImpl(this.getPeer()));
   }

   static native long getAnchorNodeImpl(long var0);

   public int getAnchorOffset() {
      return getAnchorOffsetImpl(this.getPeer());
   }

   static native int getAnchorOffsetImpl(long var0);

   public Node getFocusNode() {
      return NodeImpl.getImpl(getFocusNodeImpl(this.getPeer()));
   }

   static native long getFocusNodeImpl(long var0);

   public int getFocusOffset() {
      return getFocusOffsetImpl(this.getPeer());
   }

   static native int getFocusOffsetImpl(long var0);

   public boolean getIsCollapsed() {
      return getIsCollapsedImpl(this.getPeer());
   }

   static native boolean getIsCollapsedImpl(long var0);

   public int getRangeCount() {
      return getRangeCountImpl(this.getPeer());
   }

   static native int getRangeCountImpl(long var0);

   public Node getBaseNode() {
      return NodeImpl.getImpl(getBaseNodeImpl(this.getPeer()));
   }

   static native long getBaseNodeImpl(long var0);

   public int getBaseOffset() {
      return getBaseOffsetImpl(this.getPeer());
   }

   static native int getBaseOffsetImpl(long var0);

   public Node getExtentNode() {
      return NodeImpl.getImpl(getExtentNodeImpl(this.getPeer()));
   }

   static native long getExtentNodeImpl(long var0);

   public int getExtentOffset() {
      return getExtentOffsetImpl(this.getPeer());
   }

   static native int getExtentOffsetImpl(long var0);

   public String getType() {
      return getTypeImpl(this.getPeer());
   }

   static native String getTypeImpl(long var0);

   public void collapse(Node var1, int var2) throws DOMException {
      collapseImpl(this.getPeer(), NodeImpl.getPeer(var1), var2);
   }

   static native void collapseImpl(long var0, long var2, int var4);

   public void collapseToEnd() throws DOMException {
      collapseToEndImpl(this.getPeer());
   }

   static native void collapseToEndImpl(long var0);

   public void collapseToStart() throws DOMException {
      collapseToStartImpl(this.getPeer());
   }

   static native void collapseToStartImpl(long var0);

   public void deleteFromDocument() {
      deleteFromDocumentImpl(this.getPeer());
   }

   static native void deleteFromDocumentImpl(long var0);

   public boolean containsNode(Node var1, boolean var2) {
      return containsNodeImpl(this.getPeer(), NodeImpl.getPeer(var1), var2);
   }

   static native boolean containsNodeImpl(long var0, long var2, boolean var4);

   public void selectAllChildren(Node var1) throws DOMException {
      selectAllChildrenImpl(this.getPeer(), NodeImpl.getPeer(var1));
   }

   static native void selectAllChildrenImpl(long var0, long var2);

   public void extend(Node var1, int var2) throws DOMException {
      extendImpl(this.getPeer(), NodeImpl.getPeer(var1), var2);
   }

   static native void extendImpl(long var0, long var2, int var4);

   public Range getRangeAt(int var1) throws DOMException {
      return RangeImpl.getImpl(getRangeAtImpl(this.getPeer(), var1));
   }

   static native long getRangeAtImpl(long var0, int var2);

   public void removeAllRanges() {
      removeAllRangesImpl(this.getPeer());
   }

   static native void removeAllRangesImpl(long var0);

   public void addRange(Range var1) {
      addRangeImpl(this.getPeer(), RangeImpl.getPeer(var1));
   }

   static native void addRangeImpl(long var0, long var2);

   public void modify(String var1, String var2, String var3) {
      modifyImpl(this.getPeer(), var1, var2, var3);
   }

   static native void modifyImpl(long var0, String var2, String var3, String var4);

   public void setBaseAndExtent(Node var1, int var2, Node var3, int var4) throws DOMException {
      setBaseAndExtentImpl(this.getPeer(), NodeImpl.getPeer(var1), var2, NodeImpl.getPeer(var3), var4);
   }

   static native void setBaseAndExtentImpl(long var0, long var2, int var4, long var5, int var7);

   public void setPosition(Node var1, int var2) throws DOMException {
      setPositionImpl(this.getPeer(), NodeImpl.getPeer(var1), var2);
   }

   static native void setPositionImpl(long var0, long var2, int var4);

   public void empty() {
      emptyImpl(this.getPeer());
   }

   static native void emptyImpl(long var0);

   private static class SelfDisposer implements DisposerRecord {
      private final long peer;

      SelfDisposer(long var1) {
         this.peer = var1;
      }

      public void dispose() {
         DOMSelectionImpl.dispose(this.peer);
      }
   }
}
