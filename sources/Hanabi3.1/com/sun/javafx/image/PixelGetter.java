package com.sun.javafx.image;

import java.nio.Buffer;

public interface PixelGetter {
   AlphaType getAlphaType();

   int getNumElements();

   int getArgb(Buffer var1, int var2);

   int getArgbPre(Buffer var1, int var2);
}
