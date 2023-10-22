package com.sun.media.jfxmedia.events;

import com.sun.media.jfxmedia.control.VideoDataBuffer;

public class NewFrameEvent extends PlayerEvent {
   private VideoDataBuffer frameData;

   public NewFrameEvent(VideoDataBuffer var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("buffer == null!");
      } else {
         this.frameData = var1;
      }
   }

   public VideoDataBuffer getFrameData() {
      return this.frameData;
   }
}
