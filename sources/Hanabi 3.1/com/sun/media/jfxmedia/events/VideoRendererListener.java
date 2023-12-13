package com.sun.media.jfxmedia.events;

public interface VideoRendererListener {
   void videoFrameUpdated(NewFrameEvent var1);

   void releaseVideoFrames();
}
