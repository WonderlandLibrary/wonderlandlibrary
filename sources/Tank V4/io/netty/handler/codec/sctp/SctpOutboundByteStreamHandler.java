package io.netty.handler.codec.sctp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.sctp.SctpMessage;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;

public class SctpOutboundByteStreamHandler extends MessageToMessageEncoder {
   private final int streamIdentifier;
   private final int protocolIdentifier;

   public SctpOutboundByteStreamHandler(int var1, int var2) {
      this.streamIdentifier = var1;
      this.protocolIdentifier = var2;
   }

   protected void encode(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
      var3.add(new SctpMessage(this.streamIdentifier, this.protocolIdentifier, var2.retain()));
   }

   protected void encode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
      this.encode(var1, (ByteBuf)var2, var3);
   }
}
