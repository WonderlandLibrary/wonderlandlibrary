package io.netty.channel.socket.nio;

import io.netty.channel.socket.DefaultServerSocketChannelConfig;
import java.net.ServerSocket;

final class NioServerSocketChannel$NioServerSocketChannelConfig extends DefaultServerSocketChannelConfig {
   final NioServerSocketChannel this$0;

   private NioServerSocketChannel$NioServerSocketChannelConfig(NioServerSocketChannel var1, NioServerSocketChannel var2, ServerSocket var3) {
      super(var2, var3);
      this.this$0 = var1;
   }

   protected void autoReadCleared() {
      NioServerSocketChannel.access$100(this.this$0, false);
   }

   NioServerSocketChannel$NioServerSocketChannelConfig(NioServerSocketChannel var1, NioServerSocketChannel var2, ServerSocket var3, NioServerSocketChannel$1 var4) {
      this(var1, var2, var3);
   }
}
