package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;

public final class UnknownSocksResponse extends SocksResponse {
   public UnknownSocksResponse() {
      super(SocksResponseType.UNKNOWN);
   }

   public void encodeAsByteBuf(ByteBuf var1) {
   }
}
