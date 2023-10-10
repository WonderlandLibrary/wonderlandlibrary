package io.netty.channel;

import io.netty.util.Recycler;

final class ChannelOutboundBuffer$Entry$1 extends Recycler {
   protected ChannelOutboundBuffer.Entry newObject(Recycler.Handle var1) {
      return new ChannelOutboundBuffer.Entry(var1);
   }

   protected Object newObject(Recycler.Handle var1) {
      return this.newObject(var1);
   }
}
