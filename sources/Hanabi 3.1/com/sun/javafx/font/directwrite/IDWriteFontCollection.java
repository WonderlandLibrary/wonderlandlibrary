package com.sun.javafx.font.directwrite;

class IDWriteFontCollection extends IUnknown {
   IDWriteFontCollection(long var1) {
      super(var1);
   }

   int GetFontFamilyCount() {
      return OS.GetFontFamilyCount(this.ptr);
   }

   IDWriteFontFamily GetFontFamily(int var1) {
      long var2 = OS.GetFontFamily(this.ptr, var1);
      return var2 != 0L ? new IDWriteFontFamily(var2) : null;
   }

   int FindFamilyName(String var1) {
      return OS.FindFamilyName(this.ptr, (var1 + '\u0000').toCharArray());
   }

   IDWriteFont GetFontFromFontFace(IDWriteFontFace var1) {
      long var2 = OS.GetFontFromFontFace(this.ptr, var1.ptr);
      return var2 != 0L ? new IDWriteFont(var2) : null;
   }
}
