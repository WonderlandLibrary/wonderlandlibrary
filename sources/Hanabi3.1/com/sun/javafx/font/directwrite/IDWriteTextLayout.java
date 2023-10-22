package com.sun.javafx.font.directwrite;

class IDWriteTextLayout extends IUnknown {
   IDWriteTextLayout(long var1) {
      super(var1);
   }

   int Draw(long var1, JFXTextRenderer var3, float var4, float var5) {
      return OS.Draw(this.ptr, var1, var3.ptr, var4, var5);
   }
}
