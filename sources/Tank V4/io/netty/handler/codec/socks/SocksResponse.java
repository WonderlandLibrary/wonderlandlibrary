package io.netty.handler.codec.socks;

public abstract class SocksResponse extends SocksMessage {
   private final SocksResponseType responseType;

   protected SocksResponse(SocksResponseType var1) {
      super(SocksMessageType.RESPONSE);
      if (var1 == null) {
         throw new NullPointerException("responseType");
      } else {
         this.responseType = var1;
      }
   }

   public SocksResponseType responseType() {
      return this.responseType;
   }
}
