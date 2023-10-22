package com.sun.javafx.font.directwrite;

class JFXTextRenderer extends IUnknown {
   JFXTextRenderer(long var1) {
      super(var1);
   }

   boolean Next() {
      return OS.JFXTextRendererNext(this.ptr);
   }

   int GetStart() {
      return OS.JFXTextRendererGetStart(this.ptr);
   }

   int GetLength() {
      return OS.JFXTextRendererGetLength(this.ptr);
   }

   int GetGlyphCount() {
      return OS.JFXTextRendererGetGlyphCount(this.ptr);
   }

   int GetTotalGlyphCount() {
      return OS.JFXTextRendererGetTotalGlyphCount(this.ptr);
   }

   IDWriteFontFace GetFontFace() {
      long var1 = OS.JFXTextRendererGetFontFace(this.ptr);
      return var1 != 0L ? new IDWriteFontFace(var1) : null;
   }

   int GetGlyphIndices(int[] var1, int var2, int var3) {
      return OS.JFXTextRendererGetGlyphIndices(this.ptr, var1, var2, var3);
   }

   int GetGlyphAdvances(float[] var1, int var2) {
      return OS.JFXTextRendererGetGlyphAdvances(this.ptr, var1, var2);
   }

   int GetGlyphOffsets(float[] var1, int var2) {
      return OS.JFXTextRendererGetGlyphOffsets(this.ptr, var1, var2);
   }

   int GetClusterMap(short[] var1, int var2, int var3) {
      return OS.JFXTextRendererGetClusterMap(this.ptr, var1, var2, var3);
   }
}
