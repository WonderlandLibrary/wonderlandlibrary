package io.netty.handler.codec.http;

import io.netty.channel.ChannelHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibWrapper;

public class HttpContentDecompressor extends HttpContentDecoder {
   private final boolean strict;

   public HttpContentDecompressor() {
      this(false);
   }

   public HttpContentDecompressor(boolean var1) {
      this.strict = var1;
   }

   protected EmbeddedChannel newContentDecoder(String var1) throws Exception {
      if (!"gzip".equalsIgnoreCase(var1) && !"x-gzip".equalsIgnoreCase(var1)) {
         if (!"deflate".equalsIgnoreCase(var1) && !"x-deflate".equalsIgnoreCase(var1)) {
            return null;
         } else {
            ZlibWrapper var2;
            if (this.strict) {
               var2 = ZlibWrapper.ZLIB;
            } else {
               var2 = ZlibWrapper.ZLIB_OR_NONE;
            }

            return new EmbeddedChannel(new ChannelHandler[]{ZlibCodecFactory.newZlibDecoder(var2)});
         }
      } else {
         return new EmbeddedChannel(new ChannelHandler[]{ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP)});
      }
   }
}
