package io.netty.channel.socket.nio;

import io.netty.channel.socket.DefaultSocketChannelConfig;
import java.net.Socket;

final class NioSocketChannel$NioSocketChannelConfig extends DefaultSocketChannelConfig {
   final NioSocketChannel this$0;

   private NioSocketChannel$NioSocketChannelConfig(NioSocketChannel var1, NioSocketChannel var2, Socket var3) {
      super(var2, var3);
      this.this$0 = var1;
   }

   protected void autoReadCleared() {
      NioSocketChannel.access$100(this.this$0, false);
   }

   NioSocketChannel$NioSocketChannelConfig(NioSocketChannel var1, NioSocketChannel var2, Socket var3, Object var4) {
      this(var1, var2, var3);
   }
}
