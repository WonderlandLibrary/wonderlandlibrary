package io.netty.handler.codec.socks;

public abstract class SocksRequest extends SocksMessage {
   private final SocksRequestType requestType;

   protected SocksRequest(SocksRequestType var1) {
      super(SocksMessageType.REQUEST);
      if (var1 == null) {
         throw new NullPointerException("requestType");
      } else {
         this.requestType = var1;
      }
   }

   public SocksRequestType requestType() {
      return this.requestType;
   }
}
