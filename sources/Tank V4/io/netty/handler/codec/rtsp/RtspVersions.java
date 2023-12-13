package io.netty.handler.codec.rtsp;

import io.netty.handler.codec.http.HttpVersion;

public final class RtspVersions {
   public static final HttpVersion RTSP_1_0 = new HttpVersion("RTSP", 1, 0, true);

   public static HttpVersion valueOf(String var0) {
      if (var0 == null) {
         throw new NullPointerException("text");
      } else {
         var0 = var0.trim().toUpperCase();
         return "RTSP/1.0".equals(var0) ? RTSP_1_0 : new HttpVersion(var0, true);
      }
   }

   private RtspVersions() {
   }
}
