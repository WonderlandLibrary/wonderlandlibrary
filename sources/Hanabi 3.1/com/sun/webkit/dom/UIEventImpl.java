package com.sun.webkit.dom;

import org.w3c.dom.events.UIEvent;
import org.w3c.dom.views.AbstractView;

public class UIEventImpl extends EventImpl implements UIEvent {
   UIEventImpl(long var1) {
      super(var1);
   }

   static UIEvent getImpl(long var0) {
      return (UIEvent)create(var0);
   }

   public AbstractView getView() {
      return DOMWindowImpl.getImpl(getViewImpl(this.getPeer()));
   }

   static native long getViewImpl(long var0);

   public int getDetail() {
      return getDetailImpl(this.getPeer());
   }

   static native int getDetailImpl(long var0);

   public int getKeyCode() {
      return getKeyCodeImpl(this.getPeer());
   }

   static native int getKeyCodeImpl(long var0);

   public int getCharCode() {
      return getCharCodeImpl(this.getPeer());
   }

   static native int getCharCodeImpl(long var0);

   public int getLayerX() {
      return getLayerXImpl(this.getPeer());
   }

   static native int getLayerXImpl(long var0);

   public int getLayerY() {
      return getLayerYImpl(this.getPeer());
   }

   static native int getLayerYImpl(long var0);

   public int getPageX() {
      return getPageXImpl(this.getPeer());
   }

   static native int getPageXImpl(long var0);

   public int getPageY() {
      return getPageYImpl(this.getPeer());
   }

   static native int getPageYImpl(long var0);

   public int getWhich() {
      return getWhichImpl(this.getPeer());
   }

   static native int getWhichImpl(long var0);

   public void initUIEvent(String var1, boolean var2, boolean var3, AbstractView var4, int var5) {
      initUIEventImpl(this.getPeer(), var1, var2, var3, DOMWindowImpl.getPeer(var4), var5);
   }

   static native void initUIEventImpl(long var0, String var2, boolean var3, boolean var4, long var5, int var7);
}
