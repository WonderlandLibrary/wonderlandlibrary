package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.TooLongFrameException;
import java.util.List;

public class WebSocket00FrameDecoder extends ReplayingDecoder implements WebSocketFrameDecoder {
   static final int DEFAULT_MAX_FRAME_SIZE = 16384;
   private final long maxFrameSize;
   private boolean receivedClosingHandshake;

   public WebSocket00FrameDecoder() {
      this(16384);
   }

   public WebSocket00FrameDecoder(int var1) {
      this.maxFrameSize = (long)var1;
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
      if (this.receivedClosingHandshake) {
         var2.skipBytes(this.actualReadableBytes());
      } else {
         byte var4 = var2.readByte();
         if ((var4 & 128) == 128) {
            var3.add(this.decodeBinaryFrame(var1, var4, var2));
         } else {
            var3.add(this.decodeTextFrame(var1, var2));
         }

      }
   }

   private WebSocketFrame decodeBinaryFrame(ChannelHandlerContext var1, byte var2, ByteBuf var3) {
      long var4 = 0L;
      int var6 = 0;

      byte var7;
      do {
         var7 = var3.readByte();
         var4 <<= 7;
         var4 |= (long)(var7 & 127);
         if (var4 > this.maxFrameSize) {
            throw new TooLongFrameException();
         }

         ++var6;
         if (var6 > 8) {
            throw new TooLongFrameException();
         }
      } while((var7 & 128) == 128);

      if (var2 == -1 && var4 == 0L) {
         this.receivedClosingHandshake = true;
         return new CloseWebSocketFrame();
      } else {
         ByteBuf var8 = var1.alloc().buffer((int)var4);
         var3.readBytes(var8);
         return new BinaryWebSocketFrame(var8);
      }
   }

   private WebSocketFrame decodeTextFrame(ChannelHandlerContext var1, ByteBuf var2) {
      int var3 = var2.readerIndex();
      int var4 = this.actualReadableBytes();
      int var5 = var2.indexOf(var3, var3 + var4, (byte)-1);
      if (var5 == -1) {
         if ((long)var4 > this.maxFrameSize) {
            throw new TooLongFrameException();
         } else {
            return null;
         }
      } else {
         int var6 = var5 - var3;
         if ((long)var6 > this.maxFrameSize) {
            throw new TooLongFrameException();
         } else {
            ByteBuf var7 = var1.alloc().buffer(var6);
            var2.readBytes(var7);
            var2.skipBytes(1);
            int var8 = var7.indexOf(var7.readerIndex(), var7.writerIndex(), (byte)-1);
            if (var8 >= 0) {
               throw new IllegalArgumentException("a text frame should not contain 0xFF.");
            } else {
               return new TextWebSocketFrame(var7);
            }
         }
      }
   }
}
