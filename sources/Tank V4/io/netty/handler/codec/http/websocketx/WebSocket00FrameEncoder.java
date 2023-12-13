package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;

@ChannelHandler.Sharable
public class WebSocket00FrameEncoder extends MessageToMessageEncoder implements WebSocketFrameEncoder {
   private static final ByteBuf _0X00 = Unpooled.unreleasableBuffer(Unpooled.directBuffer(1, 1).writeByte(0));
   private static final ByteBuf _0XFF = Unpooled.unreleasableBuffer(Unpooled.directBuffer(1, 1).writeByte(-1));
   private static final ByteBuf _0XFF_0X00 = Unpooled.unreleasableBuffer(Unpooled.directBuffer(2, 2).writeByte(-1).writeByte(0));

   protected void encode(ChannelHandlerContext var1, WebSocketFrame var2, List var3) throws Exception {
      ByteBuf var4;
      if (var2 instanceof TextWebSocketFrame) {
         var4 = var2.content();
         var3.add(_0X00.duplicate());
         var3.add(var4.retain());
         var3.add(_0XFF.duplicate());
      } else if (var2 instanceof CloseWebSocketFrame) {
         var3.add(_0XFF_0X00);
      } else {
         var4 = var2.content();
         int var5 = var4.readableBytes();
         ByteBuf var6 = var1.alloc().buffer(5);
         boolean var7 = true;
         var6.writeByte(-128);
         int var8 = var5 >>> 28 & 127;
         int var9 = var5 >>> 14 & 127;
         int var10 = var5 >>> 7 & 127;
         int var11 = var5 & 127;
         if (var8 == 0) {
            if (var9 == 0) {
               if (var10 == 0) {
                  var6.writeByte(var11);
               } else {
                  var6.writeByte(var10 | 128);
                  var6.writeByte(var11);
               }
            } else {
               var6.writeByte(var9 | 128);
               var6.writeByte(var10 | 128);
               var6.writeByte(var11);
            }
         } else {
            var6.writeByte(var8 | 128);
            var6.writeByte(var9 | 128);
            var6.writeByte(var10 | 128);
            var6.writeByte(var11);
         }

         var3.add(var6);
         var3.add(var4.retain());
         var7 = false;
         if (var7) {
            var6.release();
         }
      }

   }

   protected void encode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
      this.encode(var1, (WebSocketFrame)var2, var3);
   }
}
