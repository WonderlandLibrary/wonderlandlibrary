package io.netty.channel.sctp.nio;

import com.sun.nio.sctp.SctpServerChannel;
import io.netty.channel.sctp.DefaultSctpServerChannelConfig;

final class NioSctpServerChannel$NioSctpServerChannelConfig extends DefaultSctpServerChannelConfig {
   final NioSctpServerChannel this$0;

   private NioSctpServerChannel$NioSctpServerChannelConfig(NioSctpServerChannel var1, NioSctpServerChannel var2, SctpServerChannel var3) {
      super(var2, var3);
      this.this$0 = var1;
   }

   protected void autoReadCleared() {
      NioSctpServerChannel.access$100(this.this$0, false);
   }

   NioSctpServerChannel$NioSctpServerChannelConfig(NioSctpServerChannel var1, NioSctpServerChannel var2, SctpServerChannel var3, Object var4) {
      this(var1, var2, var3);
   }
}
