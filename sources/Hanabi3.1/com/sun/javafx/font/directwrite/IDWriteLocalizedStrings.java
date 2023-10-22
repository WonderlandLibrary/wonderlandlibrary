package com.sun.javafx.font.directwrite;

class IDWriteLocalizedStrings extends IUnknown {
   IDWriteLocalizedStrings(long var1) {
      super(var1);
   }

   int FindLocaleName(String var1) {
      return OS.FindLocaleName(this.ptr, (var1 + '\u0000').toCharArray());
   }

   int GetStringLength(int var1) {
      return OS.GetStringLength(this.ptr, var1);
   }

   String GetString(int var1, int var2) {
      char[] var3 = OS.GetString(this.ptr, var1, var2 + 1);
      return var3 != null ? new String(var3, 0, var2) : null;
   }
}
