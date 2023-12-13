package io.netty.channel.sctp.oio;

import com.sun.nio.sctp.SctpServerChannel;
import io.netty.channel.sctp.DefaultSctpServerChannelConfig;

final class OioSctpServerChannel$OioSctpServerChannelConfig extends DefaultSctpServerChannelConfig {
   final OioSctpServerChannel this$0;

   private OioSctpServerChannel$OioSctpServerChannelConfig(OioSctpServerChannel var1, OioSctpServerChannel var2, SctpServerChannel var3) {
      super(var2, var3);
      this.this$0 = var1;
   }

   protected void autoReadCleared() {
      OioSctpServerChannel.access$100(this.this$0, false);
   }

   OioSctpServerChannel$OioSctpServerChannelConfig(OioSctpServerChannel var1, OioSctpServerChannel var2, SctpServerChannel var3, Object var4) {
      this(var1, var2, var3);
   }
}
