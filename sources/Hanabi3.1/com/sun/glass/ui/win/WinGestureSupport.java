package com.sun.glass.ui.win;

import com.sun.glass.ui.GestureSupport;
import com.sun.glass.ui.TouchInputSupport;
import com.sun.glass.ui.View;

final class WinGestureSupport {
   private static final double multiplier = 1.0;
   private static final GestureSupport gestures;
   private static final TouchInputSupport touches;
   private static int modifiers;
   private static boolean isDirect;

   private static native void _initIDs();

   public static void notifyBeginTouchEvent(View var0, int var1, boolean var2, int var3) {
      touches.notifyBeginTouchEvent(var0, var1, var2, var3);
   }

   public static void notifyNextTouchEvent(View var0, int var1, long var2, int var4, int var5, int var6, int var7) {
      touches.notifyNextTouchEvent(var0, var1, var2, var4, var5, var6, var7);
   }

   public static void notifyEndTouchEvent(View var0) {
      touches.notifyEndTouchEvent(var0);
      gestureFinished(var0, touches.getTouchCount(), false);
   }

   private static void gestureFinished(View var0, int var1, boolean var2) {
      if (gestures.isScrolling() && var1 == 0) {
         gestures.handleScrollingEnd(var0, modifiers, var1, isDirect, var2, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
      }

      if (gestures.isRotating() && var1 < 2) {
         gestures.handleRotationEnd(var0, modifiers, isDirect, var2, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
      }

      if (gestures.isZooming() && var1 < 2) {
         gestures.handleZoomingEnd(var0, modifiers, isDirect, var2, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
      }

   }

   public static void inertiaGestureFinished(View var0) {
      gestureFinished(var0, 0, true);
   }

   public static void gesturePerformed(View var0, int var1, boolean var2, boolean var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14) {
      modifiers = var1;
      isDirect = var2;
      int var15 = touches.getTouchCount();
      if (var15 >= 2) {
         gestures.handleTotalZooming(var0, var1, var2, var3, var4, var5, var6, var7, (double)var12, (double)var13);
         gestures.handleTotalRotation(var0, var1, var2, var3, var4, var5, var6, var7, Math.toDegrees((double)var14));
      }

      gestures.handleTotalScrolling(var0, var1, var2, var3, var15, var4, var5, var6, var7, (double)var10, (double)var11, 1.0, 1.0);
   }

   static {
      _initIDs();
      gestures = new GestureSupport(true);
      touches = new TouchInputSupport(gestures.createTouchCountListener(), true);
   }
}
