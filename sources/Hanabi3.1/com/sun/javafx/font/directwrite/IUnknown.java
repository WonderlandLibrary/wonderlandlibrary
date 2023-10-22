package com.sun.javafx.font.directwrite;

class IUnknown {
   long ptr;

   IUnknown(long var1) {
      this.ptr = var1;
   }

   int AddRef() {
      return OS.AddRef(this.ptr);
   }

   int Release() {
      int var1 = 0;
      if (this.ptr != 0L) {
         var1 = OS.Release(this.ptr);
         this.ptr = 0L;
      }

      return var1;
   }
}
