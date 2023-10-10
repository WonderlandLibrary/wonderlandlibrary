package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.TypeParameterMatcher;

public abstract class MessageToByteEncoder extends ChannelOutboundHandlerAdapter {
   private final TypeParameterMatcher matcher;
   private final boolean preferDirect;

   protected MessageToByteEncoder() {
      this(true);
   }

   protected MessageToByteEncoder(Class var1) {
      this(var1, true);
   }

   protected MessageToByteEncoder(boolean var1) {
      this.matcher = TypeParameterMatcher.find(this, MessageToByteEncoder.class, "I");
      this.preferDirect = var1;
   }

   protected MessageToByteEncoder(Class var1, boolean var2) {
      this.matcher = TypeParameterMatcher.get(var1);
      this.preferDirect = var2;
   }

   public boolean acceptOutboundMessage(Object var1) throws Exception {
      return this.matcher.match(var1);
   }

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) throws Exception {
      ByteBuf var4 = null;

      try {
         if (this.acceptOutboundMessage(var2)) {
            if (this.preferDirect) {
               var4 = var1.alloc().ioBuffer();
            } else {
               var4 = var1.alloc().heapBuffer();
            }

            this.encode(var1, var2, var4);
            ReferenceCountUtil.release(var2);
            if (var4.isReadable()) {
               var1.write(var4, var3);
            } else {
               var4.release();
               var1.write(Unpooled.EMPTY_BUFFER, var3);
            }

            var4 = null;
         } else {
            var1.write(var2, var3);
         }
      } catch (EncoderException var8) {
         throw var8;
      } catch (Throwable var9) {
         throw new EncoderException(var9);
      }

      if (var4 != null) {
         var4.release();
      }

   }

   protected abstract void encode(ChannelHandlerContext var1, Object var2, ByteBuf var3) throws Exception;
}
