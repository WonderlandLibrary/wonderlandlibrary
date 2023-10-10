package io.netty.channel.sctp.oio;

import com.sun.nio.sctp.SctpChannel;
import io.netty.channel.sctp.DefaultSctpChannelConfig;

final class OioSctpChannel$OioSctpChannelConfig extends DefaultSctpChannelConfig {
   final OioSctpChannel this$0;

   private OioSctpChannel$OioSctpChannelConfig(OioSctpChannel var1, OioSctpChannel var2, SctpChannel var3) {
      super(var2, var3);
      this.this$0 = var1;
   }

   protected void autoReadCleared() {
      OioSctpChannel.access$100(this.this$0, false);
   }

   OioSctpChannel$OioSctpChannelConfig(OioSctpChannel var1, OioSctpChannel var2, SctpChannel var3, Object var4) {
      this(var1, var2, var3);
   }
}
