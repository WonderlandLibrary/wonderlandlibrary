package me.theresa.fontRenderer.font.opengl;

import java.nio.ByteBuffer;

public interface ImageData {
   int getDepth();

   int getWidth();

   int getHeight();

   int getTexWidth();

   int getTexHeight();

   ByteBuffer getImageBufferData();
}
