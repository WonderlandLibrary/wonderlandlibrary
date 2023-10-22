package com.sun.javafx.font.directwrite;

class IDWriteFactory extends IUnknown {
   IDWriteFactory(long var1) {
      super(var1);
   }

   IDWriteFontCollection GetSystemFontCollection(boolean var1) {
      long var2 = OS.GetSystemFontCollection(this.ptr, var1);
      return var2 != 0L ? new IDWriteFontCollection(var2) : null;
   }

   IDWriteTextAnalyzer CreateTextAnalyzer() {
      long var1 = OS.CreateTextAnalyzer(this.ptr);
      return var1 != 0L ? new IDWriteTextAnalyzer(var1) : null;
   }

   IDWriteTextFormat CreateTextFormat(String var1, IDWriteFontCollection var2, int var3, int var4, int var5, float var6, String var7) {
      long var8 = OS.CreateTextFormat(this.ptr, (var1 + '\u0000').toCharArray(), var2.ptr, var3, var4, var5, var6, (var7 + '\u0000').toCharArray());
      return var8 != 0L ? new IDWriteTextFormat(var8) : null;
   }

   IDWriteTextLayout CreateTextLayout(char[] var1, int var2, int var3, IDWriteTextFormat var4, float var5, float var6) {
      long var7 = OS.CreateTextLayout(this.ptr, var1, var2, var3, var4.ptr, var5, var6);
      return var7 != 0L ? new IDWriteTextLayout(var7) : null;
   }

   IDWriteGlyphRunAnalysis CreateGlyphRunAnalysis(DWRITE_GLYPH_RUN var1, float var2, DWRITE_MATRIX var3, int var4, int var5, float var6, float var7) {
      long var8 = OS.CreateGlyphRunAnalysis(this.ptr, var1, var2, var3, var4, var5, var6, var7);
      return var8 != 0L ? new IDWriteGlyphRunAnalysis(var8) : null;
   }

   IDWriteFontFile CreateFontFileReference(String var1) {
      long var2 = OS.CreateFontFileReference(this.ptr, (var1 + '\u0000').toCharArray());
      return var2 != 0L ? new IDWriteFontFile(var2) : null;
   }

   IDWriteFontFace CreateFontFace(int var1, IDWriteFontFile var2, int var3, int var4) {
      long var5 = OS.CreateFontFace(this.ptr, var1, var2.ptr, var3, var4);
      return var5 != 0L ? new IDWriteFontFace(var5) : null;
   }
}
