package io.netty.handler.codec.http;

import io.netty.channel.CombinedChannelDuplexHandler;

public final class HttpServerCodec extends CombinedChannelDuplexHandler {
   public HttpServerCodec() {
      this(4096, 8192, 8192);
   }

   public HttpServerCodec(int var1, int var2, int var3) {
      super(new HttpRequestDecoder(var1, var2, var3), new HttpResponseEncoder());
   }

   public HttpServerCodec(int var1, int var2, int var3, boolean var4) {
      super(new HttpRequestDecoder(var1, var2, var3, var4), new HttpResponseEncoder());
   }
}
