package io.netty.channel;

public final class ChannelMetadata {
   private final boolean hasDisconnect;

   public ChannelMetadata(boolean var1) {
      this.hasDisconnect = var1;
   }

   public boolean hasDisconnect() {
      return this.hasDisconnect;
   }
}
