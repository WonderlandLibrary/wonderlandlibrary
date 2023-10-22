package com.sun.media.jfxmedia.control;

import java.nio.ByteBuffer;

public interface VideoDataBuffer {
   int PACKED_FORMAT_PLANE = 0;
   int YCBCR_PLANE_LUMA = 0;
   int YCBCR_PLANE_CR = 1;
   int YCBCR_PLANE_CB = 2;
   int YCBCR_PLANE_ALPHA = 3;

   ByteBuffer getBufferForPlane(int var1);

   double getTimestamp();

   int getWidth();

   int getHeight();

   int getEncodedWidth();

   int getEncodedHeight();

   VideoFormat getFormat();

   boolean hasAlpha();

   int getPlaneCount();

   int getStrideForPlane(int var1);

   int[] getPlaneStrides();

   VideoDataBuffer convertToFormat(VideoFormat var1);

   void setDirty();

   void holdFrame();

   void releaseFrame();
}
