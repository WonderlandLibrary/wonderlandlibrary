package com.sun.javafx.font.directwrite;

class IWICBitmapLock extends IUnknown {
   IWICBitmapLock(long var1) {
      super(var1);
   }

   byte[] GetDataPointer() {
      return OS.GetDataPointer(this.ptr);
   }

   int GetStride() {
      return OS.GetStride(this.ptr);
   }
}
