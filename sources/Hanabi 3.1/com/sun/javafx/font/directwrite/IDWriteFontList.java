package com.sun.javafx.font.directwrite;

class IDWriteFontList extends IUnknown {
   IDWriteFontList(long var1) {
      super(var1);
   }

   int GetFontCount() {
      return OS.GetFontCount(this.ptr);
   }

   IDWriteFont GetFont(int var1) {
      long var2 = OS.GetFont(this.ptr, var1);
      return var2 != 0L ? new IDWriteFont(var2) : null;
   }
}
