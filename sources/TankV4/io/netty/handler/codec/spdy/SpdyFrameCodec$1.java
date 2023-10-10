package io.netty.handler.codec.spdy;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.concurrent.Future;

class SpdyFrameCodec$1 implements ChannelFutureListener {
   final SpdyFrameCodec this$0;

   SpdyFrameCodec$1(SpdyFrameCodec var1) {
      this.this$0 = var1;
   }

   public void operationComplete(ChannelFuture var1) throws Exception {
      SpdyFrameCodec.access$000(this.this$0).end();
      SpdyFrameCodec.access$100(this.this$0).end();
   }

   public void operationComplete(Future var1) throws Exception {
      this.operationComplete((ChannelFuture)var1);
   }
}
