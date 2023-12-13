package com.sun.glass.ui.win;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Robot;
import java.nio.IntBuffer;

final class WinRobot extends Robot {
   protected void _create() {
   }

   protected void _destroy() {
   }

   protected native void _keyPress(int var1);

   protected native void _keyRelease(int var1);

   protected native void _mouseMove(int var1, int var2);

   protected native void _mousePress(int var1);

   protected native void _mouseRelease(int var1);

   protected native void _mouseWheel(int var1);

   protected native int _getMouseX();

   protected native int _getMouseY();

   protected native int _getPixelColor(int var1, int var2);

   private native void _getScreenCapture(int var1, int var2, int var3, int var4, int[] var5);

   protected Pixels _getScreenCapture(int var1, int var2, int var3, int var4, boolean var5) {
      int[] var6 = new int[var3 * var4];
      this._getScreenCapture(var1, var2, var3, var4, var6);
      return Application.GetApplication().createPixels(var3, var4, IntBuffer.wrap(var6));
   }
}
