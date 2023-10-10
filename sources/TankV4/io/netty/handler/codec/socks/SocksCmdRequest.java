package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import io.netty.util.NetUtil;
import java.net.IDN;

public final class SocksCmdRequest extends SocksRequest {
   private final SocksCmdType cmdType;
   private final SocksAddressType addressType;
   private final String host;
   private final int port;

   public SocksCmdRequest(SocksCmdType var1, SocksAddressType var2, String var3, int var4) {
      super(SocksRequestType.CMD);
      if (var1 == null) {
         throw new NullPointerException("cmdType");
      } else if (var2 == null) {
         throw new NullPointerException("addressType");
      } else if (var3 == null) {
         throw new NullPointerException("host");
      } else {
         switch(var2) {
         case IPv4:
            if (!NetUtil.isValidIpV4Address(var3)) {
               throw new IllegalArgumentException(var3 + " is not a valid IPv4 address");
            }
            break;
         case DOMAIN:
            if (IDN.toASCII(var3).length() > 255) {
               throw new IllegalArgumentException(var3 + " IDN: " + IDN.toASCII(var3) + " exceeds 255 char limit");
            }
            break;
         case IPv6:
            if (!NetUtil.isValidIpV6Address(var3)) {
               throw new IllegalArgumentException(var3 + " is not a valid IPv6 address");
            }
         case UNKNOWN:
         }

         if (var4 < 0 && var4 >= 65535) {
            throw new IllegalArgumentException(var4 + " is not in bounds 0 < x < 65536");
         } else {
            this.cmdType = var1;
            this.addressType = var2;
            this.host = IDN.toASCII(var3);
            this.port = var4;
         }
      }
   }

   public SocksCmdType cmdType() {
      return this.cmdType;
   }

   public SocksAddressType addressType() {
      return this.addressType;
   }

   public String host() {
      return IDN.toUnicode(this.host);
   }

   public int port() {
      return this.port;
   }

   public void encodeAsByteBuf(ByteBuf var1) {
      var1.writeByte(this.protocolVersion().byteValue());
      var1.writeByte(this.cmdType.byteValue());
      var1.writeByte(0);
      var1.writeByte(this.addressType.byteValue());
      switch(this.addressType) {
      case IPv4:
         var1.writeBytes(NetUtil.createByteArrayFromIpAddressString(this.host));
         var1.writeShort(this.port);
         break;
      case DOMAIN:
         var1.writeByte(this.host.length());
         var1.writeBytes(this.host.getBytes(CharsetUtil.US_ASCII));
         var1.writeShort(this.port);
         break;
      case IPv6:
         var1.writeBytes(NetUtil.createByteArrayFromIpAddressString(this.host));
         var1.writeShort(this.port);
      }

   }
}
