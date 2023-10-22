package com.sun.glass.ui.win;

import com.sun.glass.ui.Pixels;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

final class WinPixels extends Pixels {
   private static final int nativeFormat = _initIDs();

   private static native int _initIDs();

   protected WinPixels(int var1, int var2, ByteBuffer var3) {
      super(var1, var2, var3);
   }

   protected WinPixels(int var1, int var2, IntBuffer var3) {
      super(var1, var2, var3);
   }

   protected WinPixels(int var1, int var2, IntBuffer var3, float var4, float var5) {
      super(var1, var2, var3, var4, var5);
   }

   static int getNativeFormat_impl() {
      return nativeFormat;
   }

   protected native void _fillDirectByteBuffer(ByteBuffer var1);

   protected native void _attachInt(long var1, int var3, int var4, IntBuffer var5, int[] var6, int var7);

   protected native void _attachByte(long var1, int var3, int var4, ByteBuffer var5, byte[] var6, int var7);
}
