package com.sun.prism;

import com.sun.javafx.geom.Rectangle;

public interface ReadbackGraphics extends Graphics {
   boolean canReadBack();

   RTTexture readBack(Rectangle var1);

   void releaseReadBackBuffer(RTTexture var1);
}
