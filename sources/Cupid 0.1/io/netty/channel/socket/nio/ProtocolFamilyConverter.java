package io.netty.channel.socket.nio;

import io.netty.channel.socket.InternetProtocolFamily;
import java.net.ProtocolFamily;
import java.net.StandardProtocolFamily;

final class ProtocolFamilyConverter {
  public static ProtocolFamily convert(InternetProtocolFamily family) {
    switch (family) {
      case IPv4:
        return StandardProtocolFamily.INET;
      case IPv6:
        return StandardProtocolFamily.INET6;
    } 
    throw new IllegalArgumentException();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\socket\nio\ProtocolFamilyConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */