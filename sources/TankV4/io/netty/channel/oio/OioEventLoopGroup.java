package io.netty.channel.oio;

import io.netty.channel.ThreadPerChannelEventLoopGroup;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class OioEventLoopGroup extends ThreadPerChannelEventLoopGroup {
   public OioEventLoopGroup() {
      this(0);
   }

   public OioEventLoopGroup(int var1) {
      this(var1, Executors.defaultThreadFactory());
   }

   public OioEventLoopGroup(int var1, ThreadFactory var2) {
      super(var1, var2);
   }
}
