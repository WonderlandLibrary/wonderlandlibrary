package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import java.nio.charset.CharsetEncoder;

public final class SocksAuthRequest extends SocksRequest {
   private static final CharsetEncoder asciiEncoder;
   private static final SocksSubnegotiationVersion SUBNEGOTIATION_VERSION;
   private final String username;
   private final String password;

   public SocksAuthRequest(String var1, String var2) {
      super(SocksRequestType.AUTH);
      if (var1 == null) {
         throw new NullPointerException("username");
      } else if (var2 == null) {
         throw new NullPointerException("username");
      } else if (asciiEncoder.canEncode(var1) && asciiEncoder.canEncode(var2)) {
         if (var1.length() > 255) {
            throw new IllegalArgumentException(var1 + " exceeds 255 char limit");
         } else if (var2.length() > 255) {
            throw new IllegalArgumentException(var2 + " exceeds 255 char limit");
         } else {
            this.username = var1;
            this.password = var2;
         }
      } else {
         throw new IllegalArgumentException(" username: " + var1 + " or password: " + var2 + " values should be in pure ascii");
      }
   }

   public String username() {
      return this.username;
   }

   public String password() {
      return this.password;
   }

   public void encodeAsByteBuf(ByteBuf var1) {
      var1.writeByte(SUBNEGOTIATION_VERSION.byteValue());
      var1.writeByte(this.username.length());
      var1.writeBytes(this.username.getBytes(CharsetUtil.US_ASCII));
      var1.writeByte(this.password.length());
      var1.writeBytes(this.password.getBytes(CharsetUtil.US_ASCII));
   }

   static {
      asciiEncoder = CharsetUtil.getEncoder(CharsetUtil.US_ASCII);
      SUBNEGOTIATION_VERSION = SocksSubnegotiationVersion.AUTH_PASSWORD;
   }
}
