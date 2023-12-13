package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;

public final class SocksCmdResponse extends SocksResponse {
   private final SocksCmdStatus cmdStatus;
   private final SocksAddressType addressType;
   private static final byte[] IPv4_HOSTNAME_ZEROED = new byte[]{0, 0, 0, 0};
   private static final byte[] IPv6_HOSTNAME_ZEROED = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

   public SocksCmdResponse(SocksCmdStatus var1, SocksAddressType var2) {
      super(SocksResponseType.CMD);
      if (var1 == null) {
         throw new NullPointerException("cmdStatus");
      } else if (var2 == null) {
         throw new NullPointerException("addressType");
      } else {
         this.cmdStatus = var1;
         this.addressType = var2;
      }
   }

   public SocksCmdStatus cmdStatus() {
      return this.cmdStatus;
   }

   public SocksAddressType addressType() {
      return this.addressType;
   }

   public void encodeAsByteBuf(ByteBuf var1) {
      var1.writeByte(this.protocolVersion().byteValue());
      var1.writeByte(this.cmdStatus.byteValue());
      var1.writeByte(0);
      var1.writeByte(this.addressType.byteValue());
      switch(this.addressType) {
      case IPv4:
         var1.writeBytes(IPv4_HOSTNAME_ZEROED);
         var1.writeShort(0);
         break;
      case DOMAIN:
         var1.writeByte(1);
         var1.writeByte(0);
         var1.writeShort(0);
         break;
      case IPv6:
         var1.writeBytes(IPv6_HOSTNAME_ZEROED);
         var1.writeShort(0);
      }

   }
}
