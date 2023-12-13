package io.netty.handler.codec.rtsp;

import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpObjectDecoder;

public abstract class RtspObjectDecoder extends HttpObjectDecoder {
   protected RtspObjectDecoder() {
      this(4096, 8192, 8192);
   }

   protected RtspObjectDecoder(int var1, int var2, int var3) {
      super(var1, var2, var3 * 2, false);
   }

   protected RtspObjectDecoder(int var1, int var2, int var3, boolean var4) {
      super(var1, var2, var3 * 2, false, var4);
   }

   protected boolean isContentAlwaysEmpty(HttpMessage var1) {
      boolean var2 = super.isContentAlwaysEmpty(var1);
      if (var2) {
         return true;
      } else {
         return !var1.headers().contains("Content-Length") ? true : var2;
      }
   }
}
