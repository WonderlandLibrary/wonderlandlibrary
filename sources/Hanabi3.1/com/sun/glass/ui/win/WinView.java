package com.sun.glass.ui.win;

import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.View;
import java.util.Map;

final class WinView extends View {
   private static final long multiClickTime;
   private static final int multiClickMaxX;
   private static final int multiClickMaxY;

   private static native void _initIDs();

   protected WinView() {
   }

   private static native long _getMultiClickTime_impl();

   private static native int _getMultiClickMaxX_impl();

   private static native int _getMultiClickMaxY_impl();

   static long getMultiClickTime_impl() {
      return multiClickTime;
   }

   static int getMultiClickMaxX_impl() {
      return multiClickMaxX;
   }

   static int getMultiClickMaxY_impl() {
      return multiClickMaxY;
   }

   protected int _getNativeFrameBuffer(long var1) {
      return 0;
   }

   protected native void _enableInputMethodEvents(long var1, boolean var3);

   protected native void _finishInputMethodComposition(long var1);

   protected native long _create(Map var1);

   protected native long _getNativeView(long var1);

   protected native int _getX(long var1);

   protected native int _getY(long var1);

   protected native void _setParent(long var1, long var3);

   protected native boolean _close(long var1);

   protected native void _scheduleRepaint(long var1);

   protected native void _begin(long var1);

   protected native void _end(long var1);

   protected native void _uploadPixels(long var1, Pixels var3);

   protected native boolean _enterFullscreen(long var1, boolean var3, boolean var4, boolean var5);

   protected native void _exitFullscreen(long var1, boolean var3);

   static {
      _initIDs();
      multiClickTime = _getMultiClickTime_impl();
      multiClickMaxX = _getMultiClickMaxX_impl();
      multiClickMaxY = _getMultiClickMaxY_impl();
   }
}
