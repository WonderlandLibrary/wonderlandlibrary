package io.netty.channel.local;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.net.SocketAddress;
import java.util.concurrent.ConcurrentMap;

final class LocalChannelRegistry {
   private static final ConcurrentMap boundChannels = PlatformDependent.newConcurrentHashMap();

   static LocalAddress register(Channel var0, LocalAddress var1, SocketAddress var2) {
      if (var1 != null) {
         throw new ChannelException("already bound");
      } else if (!(var2 instanceof LocalAddress)) {
         throw new ChannelException("unsupported address type: " + StringUtil.simpleClassName((Object)var2));
      } else {
         LocalAddress var3 = (LocalAddress)var2;
         if (LocalAddress.ANY.equals(var3)) {
            var3 = new LocalAddress(var0);
         }

         Channel var4 = (Channel)boundChannels.putIfAbsent(var3, var0);
         if (var4 != null) {
            throw new ChannelException("address already in use by: " + var4);
         } else {
            return var3;
         }
      }
   }

   static Channel get(SocketAddress var0) {
      return (Channel)boundChannels.get(var0);
   }

   static void unregister(LocalAddress var0) {
      boundChannels.remove(var0);
   }

   private LocalChannelRegistry() {
   }
}
