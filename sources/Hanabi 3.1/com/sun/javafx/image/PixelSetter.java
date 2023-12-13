package com.sun.javafx.image;

import java.nio.Buffer;

public interface PixelSetter {
   AlphaType getAlphaType();

   int getNumElements();

   void setArgb(Buffer var1, int var2, int var3);

   void setArgbPre(Buffer var1, int var2, int var3);
}
