package com.sun.webkit.dom;

import org.w3c.dom.Node;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.views.AbstractView;

public class MouseEventImpl extends UIEventImpl implements MouseEvent {
   MouseEventImpl(long var1) {
      super(var1);
   }

   static MouseEvent getImpl(long var0) {
      return (MouseEvent)create(var0);
   }

   public int getScreenX() {
      return getScreenXImpl(this.getPeer());
   }

   static native int getScreenXImpl(long var0);

   public int getScreenY() {
      return getScreenYImpl(this.getPeer());
   }

   static native int getScreenYImpl(long var0);

   public int getClientX() {
      return getClientXImpl(this.getPeer());
   }

   static native int getClientXImpl(long var0);

   public int getClientY() {
      return getClientYImpl(this.getPeer());
   }

   static native int getClientYImpl(long var0);

   public boolean getCtrlKey() {
      return getCtrlKeyImpl(this.getPeer());
   }

   static native boolean getCtrlKeyImpl(long var0);

   public boolean getShiftKey() {
      return getShiftKeyImpl(this.getPeer());
   }

   static native boolean getShiftKeyImpl(long var0);

   public boolean getAltKey() {
      return getAltKeyImpl(this.getPeer());
   }

   static native boolean getAltKeyImpl(long var0);

   public boolean getMetaKey() {
      return getMetaKeyImpl(this.getPeer());
   }

   static native boolean getMetaKeyImpl(long var0);

   public short getButton() {
      return getButtonImpl(this.getPeer());
   }

   static native short getButtonImpl(long var0);

   public EventTarget getRelatedTarget() {
      return (EventTarget)NodeImpl.getImpl(getRelatedTargetImpl(this.getPeer()));
   }

   static native long getRelatedTargetImpl(long var0);

   public int getOffsetX() {
      return getOffsetXImpl(this.getPeer());
   }

   static native int getOffsetXImpl(long var0);

   public int getOffsetY() {
      return getOffsetYImpl(this.getPeer());
   }

   static native int getOffsetYImpl(long var0);

   public int getX() {
      return getXImpl(this.getPeer());
   }

   static native int getXImpl(long var0);

   public int getY() {
      return getYImpl(this.getPeer());
   }

   static native int getYImpl(long var0);

   public Node getFromElement() {
      return NodeImpl.getImpl(getFromElementImpl(this.getPeer()));
   }

   static native long getFromElementImpl(long var0);

   public Node getToElement() {
      return NodeImpl.getImpl(getToElementImpl(this.getPeer()));
   }

   static native long getToElementImpl(long var0);

   public void initMouseEvent(String var1, boolean var2, boolean var3, AbstractView var4, int var5, int var6, int var7, int var8, int var9, boolean var10, boolean var11, boolean var12, boolean var13, short var14, EventTarget var15) {
      initMouseEventImpl(this.getPeer(), var1, var2, var3, DOMWindowImpl.getPeer(var4), var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, NodeImpl.getPeer((NodeImpl)var15));
   }

   static native void initMouseEventImpl(long var0, String var2, boolean var3, boolean var4, long var5, int var7, int var8, int var9, int var10, int var11, boolean var12, boolean var13, boolean var14, boolean var15, short var16, long var17);
}
