package io.netty.handler.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.TypeParameterMatcher;
import java.util.List;

public abstract class MessageToMessageEncoder extends ChannelOutboundHandlerAdapter {
   private final TypeParameterMatcher matcher;

   protected MessageToMessageEncoder() {
      this.matcher = TypeParameterMatcher.find(this, MessageToMessageEncoder.class, "I");
   }

   protected MessageToMessageEncoder(Class var1) {
      this.matcher = TypeParameterMatcher.get(var1);
   }

   public boolean acceptOutboundMessage(Object var1) throws Exception {
      return this.matcher.match(var1);
   }

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) throws Exception {
      RecyclableArrayList var4 = null;

      try {
         if (this.acceptOutboundMessage(var2)) {
            var4 = RecyclableArrayList.newInstance();
            this.encode(var1, var2, var4);
            ReferenceCountUtil.release(var2);
            if (var4.isEmpty()) {
               var4.recycle();
               var4 = null;
               throw new EncoderException(StringUtil.simpleClassName((Object)this) + " must produce at least one message.");
            }
         } else {
            var1.write(var2, var3);
         }
      } catch (EncoderException var10) {
         throw var10;
      } catch (Throwable var11) {
         throw new EncoderException(var11);
      }

      if (var4 != null) {
         int var5 = var4.size() - 1;
         if (var5 >= 0) {
            for(int var6 = 0; var6 < var5; ++var6) {
               var1.write(var4.get(var6));
            }

            var1.write(var4.get(var5), var3);
         }

         var4.recycle();
      }

   }

   protected abstract void encode(ChannelHandlerContext var1, Object var2, List var3) throws Exception;
}
