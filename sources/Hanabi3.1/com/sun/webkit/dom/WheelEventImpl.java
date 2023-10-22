package com.sun.webkit.dom;

import org.w3c.dom.views.AbstractView;

public class WheelEventImpl extends MouseEventImpl {
   public static final int DOM_DELTA_PIXEL = 0;
   public static final int DOM_DELTA_LINE = 1;
   public static final int DOM_DELTA_PAGE = 2;

   WheelEventImpl(long var1) {
      super(var1);
   }

   static WheelEventImpl getImpl(long var0) {
      return (WheelEventImpl)create(var0);
   }

   public double getDeltaX() {
      return getDeltaXImpl(this.getPeer());
   }

   static native double getDeltaXImpl(long var0);

   public double getDeltaY() {
      return getDeltaYImpl(this.getPeer());
   }

   static native double getDeltaYImpl(long var0);

   public double getDeltaZ() {
      return getDeltaZImpl(this.getPeer());
   }

   static native double getDeltaZImpl(long var0);

   public int getDeltaMode() {
      return getDeltaModeImpl(this.getPeer());
   }

   static native int getDeltaModeImpl(long var0);

   public int getWheelDeltaX() {
      return getWheelDeltaXImpl(this.getPeer());
   }

   static native int getWheelDeltaXImpl(long var0);

   public int getWheelDeltaY() {
      return getWheelDeltaYImpl(this.getPeer());
   }

   static native int getWheelDeltaYImpl(long var0);

   public int getWheelDelta() {
      return getWheelDeltaImpl(this.getPeer());
   }

   static native int getWheelDeltaImpl(long var0);

   public boolean getWebkitDirectionInvertedFromDevice() {
      return getWebkitDirectionInvertedFromDeviceImpl(this.getPeer());
   }

   static native boolean getWebkitDirectionInvertedFromDeviceImpl(long var0);

   public void initWheelEvent(int var1, int var2, AbstractView var3, int var4, int var5, int var6, int var7, boolean var8, boolean var9, boolean var10, boolean var11) {
      initWheelEventImpl(this.getPeer(), var1, var2, DOMWindowImpl.getPeer(var3), var4, var5, var6, var7, var8, var9, var10, var11);
   }

   static native void initWheelEventImpl(long var0, int var2, int var3, long var4, int var6, int var7, int var8, int var9, boolean var10, boolean var11, boolean var12, boolean var13);
}
