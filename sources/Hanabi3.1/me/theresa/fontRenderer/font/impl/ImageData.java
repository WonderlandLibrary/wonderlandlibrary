package me.theresa.fontRenderer.font.impl;

import java.nio.ByteBuffer;

public interface ImageData {
   int getDepth();

   int getWidth();

   int getHeight();

   int getTexWidth();

   int getTexHeight();

   ByteBuffer getImageBufferData();
}
