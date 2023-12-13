package com.sun.javafx.image;

import java.nio.Buffer;

public interface PixelConverter {
   void convert(Buffer var1, int var2, int var3, Buffer var4, int var5, int var6, int var7, int var8);

   PixelGetter getGetter();

   PixelSetter getSetter();
}
