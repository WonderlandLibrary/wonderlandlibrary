package com.sun.prism;

import java.nio.Buffer;

public interface RTTexture extends Texture, RenderTarget {
   int[] getPixels();

   boolean readPixels(Buffer var1);

   boolean readPixels(Buffer var1, int var2, int var3, int var4, int var5);

   boolean isVolatile();
}
