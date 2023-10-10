package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import java.util.List;

public class WebSocketFrameAggregator extends MessageToMessageDecoder {
   private final int maxFrameSize;
   private WebSocketFrame currentFrame;
   private boolean tooLongFrameFound;

   public WebSocketFrameAggregator(int var1) {
      if (var1 < 1) {
         throw new IllegalArgumentException("maxFrameSize must be > 0");
      } else {
         this.maxFrameSize = var1;
      }
   }

   protected void decode(ChannelHandlerContext var1, WebSocketFrame var2, List var3) throws Exception {
      CompositeByteBuf var4;
      if (this.currentFrame == null) {
         this.tooLongFrameFound = false;
         if (var2.isFinalFragment()) {
            var3.add(var2.retain());
         } else {
            var4 = var1.alloc().compositeBuffer().addComponent(var2.content().retain());
            var4.writerIndex(var4.writerIndex() + var2.content().readableBytes());
            if (var2 instanceof TextWebSocketFrame) {
               this.currentFrame = new TextWebSocketFrame(true, var2.rsv(), var4);
            } else {
               if (!(var2 instanceof BinaryWebSocketFrame)) {
                  var4.release();
                  throw new IllegalStateException("WebSocket frame was not of type TextWebSocketFrame or BinaryWebSocketFrame");
               }

               this.currentFrame = new BinaryWebSocketFrame(true, var2.rsv(), var4);
            }

         }
      } else if (var2 instanceof ContinuationWebSocketFrame) {
         if (this.tooLongFrameFound) {
            if (var2.isFinalFragment()) {
               this.currentFrame = null;
            }

         } else {
            var4 = (CompositeByteBuf)this.currentFrame.content();
            if (var4.readableBytes() > this.maxFrameSize - var2.content().readableBytes()) {
               this.currentFrame.release();
               this.tooLongFrameFound = true;
               throw new TooLongFrameException("WebSocketFrame length exceeded " + var4 + " bytes.");
            } else {
               var4.addComponent(var2.content().retain());
               var4.writerIndex(var4.writerIndex() + var2.content().readableBytes());
               if (var2.isFinalFragment()) {
                  WebSocketFrame var5 = this.currentFrame;
                  this.currentFrame = null;
                  var3.add(var5);
               }
            }
         }
      } else {
         var3.add(var2.retain());
      }
   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      super.channelInactive(var1);
      if (this.currentFrame != null) {
         this.currentFrame.release();
         this.currentFrame = null;
      }

   }

   public void handlerRemoved(ChannelHandlerContext var1) throws Exception {
      super.handlerRemoved(var1);
      if (this.currentFrame != null) {
         this.currentFrame.release();
         this.currentFrame = null;
      }

   }

   protected void decode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
      this.decode(var1, (WebSocketFrame)var2, var3);
   }
}
