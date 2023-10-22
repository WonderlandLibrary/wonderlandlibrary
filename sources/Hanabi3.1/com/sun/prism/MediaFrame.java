package com.sun.prism;

import java.nio.ByteBuffer;

public interface MediaFrame {
   ByteBuffer getBufferForPlane(int var1);

   PixelFormat getPixelFormat();

   int getWidth();

   int getHeight();

   int getEncodedWidth();

   int getEncodedHeight();

   int planeCount();

   int[] planeStrides();

   int strideForPlane(int var1);

   MediaFrame convertToFormat(PixelFormat var1);

   void holdFrame();

   void releaseFrame();
}
