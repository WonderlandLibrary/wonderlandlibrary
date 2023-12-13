package io.netty.handler.codec.spdy;

import io.netty.channel.CombinedChannelDuplexHandler;

public final class SpdyFrameCodec extends CombinedChannelDuplexHandler {
   public SpdyFrameCodec(SpdyVersion var1) {
      this(var1, 8192, 16384, 6, 15, 8);
   }

   public SpdyFrameCodec(SpdyVersion var1, int var2, int var3, int var4, int var5, int var6) {
      super(new SpdyFrameDecoder(var1, var2, var3), new SpdyFrameEncoder(var1, var4, var5, var6));
   }
}
