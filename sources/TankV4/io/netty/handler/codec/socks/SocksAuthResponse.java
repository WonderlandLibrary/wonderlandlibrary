package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;

public final class SocksAuthResponse extends SocksResponse {
   private static final SocksSubnegotiationVersion SUBNEGOTIATION_VERSION;
   private final SocksAuthStatus authStatus;

   public SocksAuthResponse(SocksAuthStatus var1) {
      super(SocksResponseType.AUTH);
      if (var1 == null) {
         throw new NullPointerException("authStatus");
      } else {
         this.authStatus = var1;
      }
   }

   public SocksAuthStatus authStatus() {
      return this.authStatus;
   }

   public void encodeAsByteBuf(ByteBuf var1) {
      var1.writeByte(SUBNEGOTIATION_VERSION.byteValue());
      var1.writeByte(this.authStatus.byteValue());
   }

   static {
      SUBNEGOTIATION_VERSION = SocksSubnegotiationVersion.AUTH_PASSWORD;
   }
}
