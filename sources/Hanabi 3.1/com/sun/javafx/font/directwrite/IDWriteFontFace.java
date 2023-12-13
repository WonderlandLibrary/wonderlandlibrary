package com.sun.javafx.font.directwrite;

import com.sun.javafx.geom.Path2D;

class IDWriteFontFace extends IUnknown {
   IDWriteFontFace(long var1) {
      super(var1);
   }

   DWRITE_GLYPH_METRICS GetDesignGlyphMetrics(short var1, boolean var2) {
      return OS.GetDesignGlyphMetrics(this.ptr, var1, var2);
   }

   Path2D GetGlyphRunOutline(float var1, short var2, boolean var3) {
      return OS.GetGlyphRunOutline(this.ptr, var1, var2, var3);
   }
}
