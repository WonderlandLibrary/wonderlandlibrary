package com.sun.javafx.font.directwrite;

class IDWriteFont extends IUnknown {
   IDWriteFont(long var1) {
      super(var1);
   }

   IDWriteFontFace CreateFontFace() {
      long var1 = OS.CreateFontFace(this.ptr);
      return var1 != 0L ? new IDWriteFontFace(var1) : null;
   }

   IDWriteLocalizedStrings GetFaceNames() {
      long var1 = OS.GetFaceNames(this.ptr);
      return var1 != 0L ? new IDWriteLocalizedStrings(var1) : null;
   }

   IDWriteFontFamily GetFontFamily() {
      long var1 = OS.GetFontFamily(this.ptr);
      return var1 != 0L ? new IDWriteFontFamily(var1) : null;
   }

   IDWriteLocalizedStrings GetInformationalStrings(int var1) {
      long var2 = OS.GetInformationalStrings(this.ptr, var1);
      return var2 != 0L ? new IDWriteLocalizedStrings(var2) : null;
   }

   int GetSimulations() {
      return OS.GetSimulations(this.ptr);
   }

   int GetStretch() {
      return OS.GetStretch(this.ptr);
   }

   int GetStyle() {
      return OS.GetStyle(this.ptr);
   }

   int GetWeight() {
      return OS.GetWeight(this.ptr);
   }
}
