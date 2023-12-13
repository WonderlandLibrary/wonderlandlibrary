package com.sun.javafx.font.directwrite;

class IDWriteGlyphRunAnalysis extends IUnknown {
   IDWriteGlyphRunAnalysis(long var1) {
      super(var1);
   }

   byte[] CreateAlphaTexture(int var1, RECT var2) {
      return OS.CreateAlphaTexture(this.ptr, var1, var2);
   }

   RECT GetAlphaTextureBounds(int var1) {
      return OS.GetAlphaTextureBounds(this.ptr, var1);
   }
}
