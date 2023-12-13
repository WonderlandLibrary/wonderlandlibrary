package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;

public final class UnknownSocksRequest extends SocksRequest {
   public UnknownSocksRequest() {
      super(SocksRequestType.UNKNOWN);
   }

   public void encodeAsByteBuf(ByteBuf var1) {
   }
}
