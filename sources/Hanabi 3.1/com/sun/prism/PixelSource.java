package com.sun.prism;

import com.sun.glass.ui.Pixels;

public interface PixelSource {
   Pixels getLatestPixels();

   void doneWithPixels(Pixels var1);

   void skipLatestPixels();
}
