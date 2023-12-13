package com.sun.media.jfxmedia.track;

import java.util.Locale;

public class VideoTrack extends Track {
   private VideoResolution frameSize;
   private float encodedFrameRate;
   private boolean hasAlphaChannel;

   public VideoTrack(boolean var1, long var2, String var4, Locale var5, Track.Encoding var6, VideoResolution var7, float var8, boolean var9) {
      super(var1, var2, var4, var5, var6);
      if (var7 == null) {
         throw new IllegalArgumentException("frameSize == null!");
      } else if (var7.width <= 0) {
         throw new IllegalArgumentException("frameSize.width <= 0!");
      } else if (var7.height <= 0) {
         throw new IllegalArgumentException("frameSize.height <= 0!");
      } else if (var8 < 0.0F) {
         throw new IllegalArgumentException("encodedFrameRate < 0.0!");
      } else {
         this.frameSize = var7;
         this.encodedFrameRate = var8;
         this.hasAlphaChannel = var9;
      }
   }

   public boolean hasAlphaChannel() {
      return this.hasAlphaChannel;
   }

   public float getEncodedFrameRate() {
      return this.encodedFrameRate;
   }

   public VideoResolution getFrameSize() {
      return this.frameSize;
   }

   public final String toString() {
      return "VideoTrack {\n    name: " + this.getName() + "\n    encoding: " + this.getEncodingType() + "\n    frameSize: " + this.frameSize + "\n    encodedFrameRate: " + this.encodedFrameRate + "\n    hasAlphaChannel: " + this.hasAlphaChannel + "\n}";
   }
}
