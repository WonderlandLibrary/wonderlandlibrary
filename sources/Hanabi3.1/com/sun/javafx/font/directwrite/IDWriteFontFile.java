package com.sun.javafx.font.directwrite;

class IDWriteFontFile extends IUnknown {
   IDWriteFontFile(long var1) {
      super(var1);
   }

   int Analyze(boolean[] var1, int[] var2, int[] var3, int[] var4) {
      return OS.Analyze(this.ptr, var1, var2, var3, var4);
   }
}
