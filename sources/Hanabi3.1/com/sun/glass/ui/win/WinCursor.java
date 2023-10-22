package com.sun.glass.ui.win;

import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Size;

final class WinCursor extends Cursor {
   private static native void _initIDs();

   protected WinCursor(int var1) {
      super(var1);
   }

   protected WinCursor(int var1, int var2, Pixels var3) {
      super(var1, var2, var3);
   }

   protected native long _createCursor(int var1, int var2, Pixels var3);

   private static native void _setVisible(boolean var0);

   private static native Size _getBestSize(int var0, int var1);

   static void setVisible_impl(boolean var0) {
      _setVisible(var0);
   }

   static Size getBestSize_impl(int var0, int var1) {
      return _getBestSize(var0, var1);
   }

   static {
      _initIDs();
   }
}
