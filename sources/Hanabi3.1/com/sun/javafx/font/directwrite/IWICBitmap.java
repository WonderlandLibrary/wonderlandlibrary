package com.sun.javafx.font.directwrite;

class IWICBitmap extends IUnknown {
   IWICBitmap(long var1) {
      super(var1);
   }

   IWICBitmapLock Lock(int var1, int var2, int var3, int var4, int var5) {
      long var6 = OS.Lock(this.ptr, var1, var2, var3, var4, var5);
      return var6 != 0L ? new IWICBitmapLock(var6) : null;
   }
}
