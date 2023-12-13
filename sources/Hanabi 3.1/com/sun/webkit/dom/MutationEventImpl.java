package com.sun.webkit.dom;

import org.w3c.dom.Node;
import org.w3c.dom.events.MutationEvent;

public class MutationEventImpl extends EventImpl implements MutationEvent {
   public static final int MODIFICATION = 1;
   public static final int ADDITION = 2;
   public static final int REMOVAL = 3;

   MutationEventImpl(long var1) {
      super(var1);
   }

   static MutationEvent getImpl(long var0) {
      return (MutationEvent)create(var0);
   }

   public Node getRelatedNode() {
      return NodeImpl.getImpl(getRelatedNodeImpl(this.getPeer()));
   }

   static native long getRelatedNodeImpl(long var0);

   public String getPrevValue() {
      return getPrevValueImpl(this.getPeer());
   }

   static native String getPrevValueImpl(long var0);

   public String getNewValue() {
      return getNewValueImpl(this.getPeer());
   }

   static native String getNewValueImpl(long var0);

   public String getAttrName() {
      return getAttrNameImpl(this.getPeer());
   }

   static native String getAttrNameImpl(long var0);

   public short getAttrChange() {
      return getAttrChangeImpl(this.getPeer());
   }

   static native short getAttrChangeImpl(long var0);

   public void initMutationEvent(String var1, boolean var2, boolean var3, Node var4, String var5, String var6, String var7, short var8) {
      initMutationEventImpl(this.getPeer(), var1, var2, var3, NodeImpl.getPeer(var4), var5, var6, var7, var8);
   }

   static native void initMutationEventImpl(long var0, String var2, boolean var3, boolean var4, long var5, String var7, String var8, String var9, short var10);
}
