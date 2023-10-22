package com.sun.javafx.font.directwrite;

class IDWriteFontFamily extends IDWriteFontList {
   IDWriteFontFamily(long var1) {
      super(var1);
   }

   IDWriteLocalizedStrings GetFamilyNames() {
      long var1 = OS.GetFamilyNames(this.ptr);
      return var1 != 0L ? new IDWriteLocalizedStrings(var1) : null;
   }

   IDWriteFont GetFirstMatchingFont(int var1, int var2, int var3) {
      long var4 = OS.GetFirstMatchingFont(this.ptr, var1, var2, var3);
      return var4 != 0L ? new IDWriteFont(var4) : null;
   }
}
