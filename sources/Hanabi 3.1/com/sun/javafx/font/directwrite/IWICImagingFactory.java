package com.sun.javafx.font.directwrite;

class IWICImagingFactory extends IUnknown {
   IWICImagingFactory(long var1) {
      super(var1);
   }

   IWICBitmap CreateBitmap(int var1, int var2, int var3, int var4) {
      long var5 = OS.CreateBitmap(this.ptr, var1, var2, var3, var4);
      return var5 != 0L ? new IWICBitmap(var5) : null;
   }
}
