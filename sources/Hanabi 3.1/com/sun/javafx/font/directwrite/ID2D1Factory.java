package com.sun.javafx.font.directwrite;

class ID2D1Factory extends IUnknown {
   ID2D1Factory(long var1) {
      super(var1);
   }

   ID2D1RenderTarget CreateWicBitmapRenderTarget(IWICBitmap var1, D2D1_RENDER_TARGET_PROPERTIES var2) {
      long var3 = OS.CreateWicBitmapRenderTarget(this.ptr, var1.ptr, var2);
      return var3 != 0L ? new ID2D1RenderTarget(var3) : null;
   }
}
