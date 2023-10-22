package com.sun.media.jfxmedia.control;

import com.sun.media.jfxmedia.events.VideoFrameRateListener;
import com.sun.media.jfxmedia.events.VideoRendererListener;

public interface VideoRenderControl {
   void addVideoRendererListener(VideoRendererListener var1);

   void removeVideoRendererListener(VideoRendererListener var1);

   void addVideoFrameRateListener(VideoFrameRateListener var1);

   void removeVideoFrameRateListener(VideoFrameRateListener var1);

   int getFrameWidth();

   int getFrameHeight();
}
