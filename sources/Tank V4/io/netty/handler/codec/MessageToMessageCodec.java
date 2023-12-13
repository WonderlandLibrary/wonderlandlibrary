package io.netty.handler.codec;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.internal.TypeParameterMatcher;
import java.util.List;

public abstract class MessageToMessageCodec extends ChannelDuplexHandler {
   private final MessageToMessageEncoder encoder = new MessageToMessageEncoder(this) {
      final MessageToMessageCodec this$0;

      {
         this.this$0 = var1;
      }

      public boolean acceptOutboundMessage(Object var1) throws Exception {
         return this.this$0.acceptOutboundMessage(var1);
      }

      protected void encode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
         this.this$0.encode(var1, var2, var3);
      }
   };
   private final MessageToMessageDecoder decoder = new MessageToMessageDecoder(this) {
      final MessageToMessageCodec this$0;

      {
         this.this$0 = var1;
      }

      public boolean acceptInboundMessage(Object var1) throws Exception {
         return this.this$0.acceptInboundMessage(var1);
      }

      protected void decode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
         this.this$0.decode(var1, var2, var3);
      }
   };
   private final TypeParameterMatcher inboundMsgMatcher;
   private final TypeParameterMatcher outboundMsgMatcher;

   protected MessageToMessageCodec() {
      this.inboundMsgMatcher = TypeParameterMatcher.find(this, MessageToMessageCodec.class, "INBOUND_IN");
      this.outboundMsgMatcher = TypeParameterMatcher.find(this, MessageToMessageCodec.class, "OUTBOUND_IN");
   }

   protected MessageToMessageCodec(Class var1, Class var2) {
      this.inboundMsgMatcher = TypeParameterMatcher.get(var1);
      this.outboundMsgMatcher = TypeParameterMatcher.get(var2);
   }

   public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
      this.decoder.channelRead(var1, var2);
   }

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) throws Exception {
      this.encoder.write(var1, var2, var3);
   }

   public boolean acceptInboundMessage(Object var1) throws Exception {
      return this.inboundMsgMatcher.match(var1);
   }

   public boolean acceptOutboundMessage(Object var1) throws Exception {
      return this.outboundMsgMatcher.match(var1);
   }

   protected abstract void encode(ChannelHandlerContext var1, Object var2, List var3) throws Exception;

   protected abstract void decode(ChannelHandlerContext var1, Object var2, List var3) throws Exception;
}
