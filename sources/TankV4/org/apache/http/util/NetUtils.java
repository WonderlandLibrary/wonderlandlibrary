package org.apache.http.util;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public final class NetUtils {
   public static void formatAddress(StringBuilder var0, SocketAddress var1) {
      Args.notNull(var0, "Buffer");
      Args.notNull(var1, "Socket address");
      if (var1 instanceof InetSocketAddress) {
         InetSocketAddress var2 = (InetSocketAddress)var1;
         InetAddress var3 = var2.getAddress();
         var0.append(var3 != null ? var3.getHostAddress() : var3).append(':').append(var2.getPort());
      } else {
         var0.append(var1);
      }

   }
}
