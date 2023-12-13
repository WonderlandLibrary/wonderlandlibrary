package io.netty.channel.sctp.nio;

import com.sun.nio.sctp.SctpChannel;
import io.netty.channel.sctp.DefaultSctpChannelConfig;

final class NioSctpChannel$NioSctpChannelConfig extends DefaultSctpChannelConfig {
   final NioSctpChannel this$0;

   private NioSctpChannel$NioSctpChannelConfig(NioSctpChannel var1, NioSctpChannel var2, SctpChannel var3) {
      super(var2, var3);
      this.this$0 = var1;
   }

   protected void autoReadCleared() {
      NioSctpChannel.access$100(this.this$0, false);
   }

   NioSctpChannel$NioSctpChannelConfig(NioSctpChannel var1, NioSctpChannel var2, SctpChannel var3, Object var4) {
      this(var1, var2, var3);
   }
}
