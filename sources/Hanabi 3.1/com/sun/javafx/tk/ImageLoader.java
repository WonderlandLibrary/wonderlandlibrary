package com.sun.javafx.tk;

public interface ImageLoader {
   Exception getException();

   int getFrameCount();

   PlatformImage getFrame(int var1);

   int getFrameDelay(int var1);

   int getLoopCount();

   int getWidth();

   int getHeight();
}
