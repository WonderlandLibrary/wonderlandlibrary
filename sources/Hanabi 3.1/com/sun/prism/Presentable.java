package com.sun.prism;

import com.sun.javafx.geom.Rectangle;

public interface Presentable extends RenderTarget {
   boolean lockResources(PresentableState var1);

   boolean prepare(Rectangle var1);

   boolean present();

   float getPixelScaleFactorX();

   float getPixelScaleFactorY();
}
