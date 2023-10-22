package com.sun.javafx.font.directwrite;

class ID2D1RenderTarget extends IUnknown {
   ID2D1RenderTarget(long var1) {
      super(var1);
   }

   void BeginDraw() {
      OS.BeginDraw(this.ptr);
   }

   int EndDraw() {
      return OS.EndDraw(this.ptr);
   }

   void Clear(D2D1_COLOR_F var1) {
      OS.Clear(this.ptr, var1);
   }

   void SetTransform(D2D1_MATRIX_3X2_F var1) {
      OS.SetTransform(this.ptr, var1);
   }

   void SetTextAntialiasMode(int var1) {
      OS.SetTextAntialiasMode(this.ptr, var1);
   }

   void DrawGlyphRun(D2D1_POINT_2F var1, DWRITE_GLYPH_RUN var2, ID2D1Brush var3, int var4) {
      OS.DrawGlyphRun(this.ptr, var1, var2, var3.ptr, var4);
   }

   ID2D1Brush CreateSolidColorBrush(D2D1_COLOR_F var1) {
      long var2 = OS.CreateSolidColorBrush(this.ptr, var1);
      return var2 != 0L ? new ID2D1Brush(var2) : null;
   }
}
