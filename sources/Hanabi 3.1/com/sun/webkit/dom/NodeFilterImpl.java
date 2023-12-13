package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;

public class NodeFilterImpl implements NodeFilter {
   private final long peer;
   public static final int FILTER_ACCEPT = 1;
   public static final int FILTER_REJECT = 2;
   public static final int FILTER_SKIP = 3;
   public static final int SHOW_ALL = -1;
   public static final int SHOW_ELEMENT = 1;
   public static final int SHOW_ATTRIBUTE = 2;
   public static final int SHOW_TEXT = 4;
   public static final int SHOW_CDATA_SECTION = 8;
   public static final int SHOW_ENTITY_REFERENCE = 16;
   public static final int SHOW_ENTITY = 32;
   public static final int SHOW_PROCESSING_INSTRUCTION = 64;
   public static final int SHOW_COMMENT = 128;
   public static final int SHOW_DOCUMENT = 256;
   public static final int SHOW_DOCUMENT_TYPE = 512;
   public static final int SHOW_DOCUMENT_FRAGMENT = 1024;
   public static final int SHOW_NOTATION = 2048;

   NodeFilterImpl(long var1) {
      this.peer = var1;
      Disposer.addRecord(this, new SelfDisposer(var1));
   }

   static NodeFilter create(long var0) {
      return var0 == 0L ? null : new NodeFilterImpl(var0);
   }

   long getPeer() {
      return this.peer;
   }

   public boolean equals(Object var1) {
      return var1 instanceof NodeFilterImpl && this.peer == ((NodeFilterImpl)var1).peer;
   }

   public int hashCode() {
      long var1 = this.peer;
      return (int)(var1 ^ var1 >> 17);
   }

   static long getPeer(NodeFilter var0) {
      return var0 == null ? 0L : ((NodeFilterImpl)var0).getPeer();
   }

   private static native void dispose(long var0);

   static NodeFilter getImpl(long var0) {
      return create(var0);
   }

   public short acceptNode(Node var1) {
      return acceptNodeImpl(this.getPeer(), NodeImpl.getPeer(var1));
   }

   static native short acceptNodeImpl(long var0, long var2);

   private static class SelfDisposer implements DisposerRecord {
      private final long peer;

      SelfDisposer(long var1) {
         this.peer = var1;
      }

      public void dispose() {
         NodeFilterImpl.dispose(this.peer);
      }
   }
}
