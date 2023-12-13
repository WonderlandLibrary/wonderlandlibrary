package io.netty.channel.socket.nio;

import io.netty.channel.socket.InternetProtocolFamily;
import java.net.ProtocolFamily;
import java.net.StandardProtocolFamily;

final class ProtocolFamilyConverter {
   private ProtocolFamilyConverter() {
   }

   public static ProtocolFamily convert(InternetProtocolFamily var0) {
      switch(var0) {
      case IPv4:
         return StandardProtocolFamily.INET;
      case IPv6:
         return StandardProtocolFamily.INET6;
      default:
         throw new IllegalArgumentException();
      }
   }
}
