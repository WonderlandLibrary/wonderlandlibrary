package io.netty.handler.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.util.internal.TypeParameterMatcher;
import java.util.List;

public abstract class MessageToMessageDecoder extends ChannelInboundHandlerAdapter {
   private final TypeParameterMatcher matcher;

   protected MessageToMessageDecoder() {
      this.matcher = TypeParameterMatcher.find(this, MessageToMessageDecoder.class, "I");
   }

   protected MessageToMessageDecoder(Class var1) {
      this.matcher = TypeParameterMatcher.get(var1);
   }

   public boolean acceptInboundMessage(Object var1) throws Exception {
      return this.matcher.match(var1);
   }

   public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
      RecyclableArrayList var3 = RecyclableArrayList.newInstance();

      try {
         if (this.acceptInboundMessage(var2)) {
            this.decode(var1, var2, var3);
            ReferenceCountUtil.release(var2);
         } else {
            var3.add(var2);
         }
      } catch (DecoderException var9) {
         throw var9;
      } catch (Exception var10) {
         throw new DecoderException(var10);
      }

      int var4 = var3.size();

      for(int var5 = 0; var5 < var4; ++var5) {
         var1.fireChannelRead(var3.get(var5));
      }

      var3.recycle();
   }

   protected abstract void decode(ChannelHandlerContext var1, Object var2, List var3) throws Exception;
}
