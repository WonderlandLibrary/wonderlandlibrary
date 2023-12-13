package com.sun.prism.impl;

import com.sun.prism.PixelFormat;

public interface TextureResourcePool extends ResourcePool {
   long estimateTextureSize(int var1, int var2, PixelFormat var3);

   long estimateRTTextureSize(int var1, int var2, boolean var3);
}
