package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class NamedNodeMapImpl implements NamedNodeMap {
   private final long peer;

   NamedNodeMapImpl(long var1) {
      this.peer = var1;
      Disposer.addRecord(this, new SelfDisposer(var1));
   }

   static NamedNodeMap create(long var0) {
      return var0 == 0L ? null : new NamedNodeMapImpl(var0);
   }

   long getPeer() {
      return this.peer;
   }

   public boolean equals(Object var1) {
      return var1 instanceof NamedNodeMapImpl && this.peer == ((NamedNodeMapImpl)var1).peer;
   }

   public int hashCode() {
      long var1 = this.peer;
      return (int)(var1 ^ var1 >> 17);
   }

   static long getPeer(NamedNodeMap var0) {
      return var0 == null ? 0L : ((NamedNodeMapImpl)var0).getPeer();
   }

   private static native void dispose(long var0);

   static NamedNodeMap getImpl(long var0) {
      return create(var0);
   }

   public int getLength() {
      return getLengthImpl(this.getPeer());
   }

   static native int getLengthImpl(long var0);

   public Node getNamedItem(String var1) {
      return NodeImpl.getImpl(getNamedItemImpl(this.getPeer(), var1));
   }

   static native long getNamedItemImpl(long var0, String var2);

   public Node setNamedItem(Node var1) throws DOMException {
      return NodeImpl.getImpl(setNamedItemImpl(this.getPeer(), NodeImpl.getPeer(var1)));
   }

   static native long setNamedItemImpl(long var0, long var2);

   public Node removeNamedItem(String var1) throws DOMException {
      return NodeImpl.getImpl(removeNamedItemImpl(this.getPeer(), var1));
   }

   static native long removeNamedItemImpl(long var0, String var2);

   public Node item(int var1) {
      return NodeImpl.getImpl(itemImpl(this.getPeer(), var1));
   }

   static native long itemImpl(long var0, int var2);

   public Node getNamedItemNS(String var1, String var2) {
      return NodeImpl.getImpl(getNamedItemNSImpl(this.getPeer(), var1, var2));
   }

   static native long getNamedItemNSImpl(long var0, String var2, String var3);

   public Node setNamedItemNS(Node var1) throws DOMException {
      return NodeImpl.getImpl(setNamedItemNSImpl(this.getPeer(), NodeImpl.getPeer(var1)));
   }

   static native long setNamedItemNSImpl(long var0, long var2);

   public Node removeNamedItemNS(String var1, String var2) throws DOMException {
      return NodeImpl.getImpl(removeNamedItemNSImpl(this.getPeer(), var1, var2));
   }

   static native long removeNamedItemNSImpl(long var0, String var2, String var3);

   private static class SelfDisposer implements DisposerRecord {
      private final long peer;

      SelfDisposer(long var1) {
         this.peer = var1;
      }

      public void dispose() {
         NamedNodeMapImpl.dispose(this.peer);
      }
   }
}
