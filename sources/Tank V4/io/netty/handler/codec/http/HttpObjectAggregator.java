package io.netty.handler.codec.http;

import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import java.util.List;

public class HttpObjectAggregator extends MessageToMessageDecoder {
   public static final int DEFAULT_MAX_COMPOSITEBUFFER_COMPONENTS = 1024;
   private static final FullHttpResponse CONTINUE;
   private final int maxContentLength;
   private FullHttpMessage currentMessage;
   private boolean tooLongFrameFound;
   private int maxCumulationBufferComponents = 1024;
   private ChannelHandlerContext ctx;
   static final boolean $assertionsDisabled = !HttpObjectAggregator.class.desiredAssertionStatus();

   public HttpObjectAggregator(int var1) {
      if (var1 <= 0) {
         throw new IllegalArgumentException("maxContentLength must be a positive integer: " + var1);
      } else {
         this.maxContentLength = var1;
      }
   }

   public final int getMaxCumulationBufferComponents() {
      return this.maxCumulationBufferComponents;
   }

   public final void setMaxCumulationBufferComponents(int var1) {
      if (var1 < 2) {
         throw new IllegalArgumentException("maxCumulationBufferComponents: " + var1 + " (expected: >= 2)");
      } else if (this.ctx == null) {
         this.maxCumulationBufferComponents = var1;
      } else {
         throw new IllegalStateException("decoder properties cannot be changed once the decoder is added to a pipeline.");
      }
   }

   protected void decode(ChannelHandlerContext var1, HttpObject var2, List var3) throws Exception {
      FullHttpMessage var4 = this.currentMessage;
      if (var2 instanceof HttpMessage) {
         this.tooLongFrameFound = false;
         if (!$assertionsDisabled && var4 != null) {
            throw new AssertionError();
         }

         HttpMessage var5 = (HttpMessage)var2;
         if (HttpHeaders.is100ContinueExpected(var5)) {
            var1.writeAndFlush(CONTINUE).addListener(new ChannelFutureListener(this, var1) {
               final ChannelHandlerContext val$ctx;
               final HttpObjectAggregator this$0;

               {
                  this.this$0 = var1;
                  this.val$ctx = var2;
               }

               public void operationComplete(ChannelFuture var1) throws Exception {
                  if (!var1.isSuccess()) {
                     this.val$ctx.fireExceptionCaught(var1.cause());
                  }

               }

               public void operationComplete(Future var1) throws Exception {
                  this.operationComplete((ChannelFuture)var1);
               }
            });
         }

         if (!var5.getDecoderResult().isSuccess()) {
            HttpHeaders.removeTransferEncodingChunked(var5);
            this.currentMessage = null;
            var3.add(ReferenceCountUtil.retain(var5));
            return;
         }

         Object var9;
         if (var2 instanceof HttpRequest) {
            HttpRequest var6 = (HttpRequest)var2;
            this.currentMessage = (FullHttpMessage)(var9 = new DefaultFullHttpRequest(var6.getProtocolVersion(), var6.getMethod(), var6.getUri(), Unpooled.compositeBuffer(this.maxCumulationBufferComponents)));
         } else {
            if (!(var2 instanceof HttpResponse)) {
               throw new Error();
            }

            HttpResponse var11 = (HttpResponse)var2;
            this.currentMessage = (FullHttpMessage)(var9 = new DefaultFullHttpResponse(var11.getProtocolVersion(), var11.getStatus(), Unpooled.compositeBuffer(this.maxCumulationBufferComponents)));
         }

         ((FullHttpMessage)var9).headers().set(var5.headers());
         HttpHeaders.removeTransferEncodingChunked((HttpMessage)var9);
      } else {
         if (!(var2 instanceof HttpContent)) {
            throw new Error();
         }

         if (this.tooLongFrameFound) {
            if (var2 instanceof LastHttpContent) {
               this.currentMessage = null;
            }

            return;
         }

         if (!$assertionsDisabled && var4 == null) {
            throw new AssertionError();
         }

         HttpContent var10 = (HttpContent)var2;
         CompositeByteBuf var12 = (CompositeByteBuf)var4.content();
         if (var12.readableBytes() > this.maxContentLength - var10.content().readableBytes()) {
            this.tooLongFrameFound = true;
            var4.release();
            this.currentMessage = null;
            throw new TooLongFrameException("HTTP content length exceeded " + this.maxContentLength + " bytes.");
         }

         if (var10.content().isReadable()) {
            var10.retain();
            var12.addComponent(var10.content());
            var12.writerIndex(var12.writerIndex() + var10.content().readableBytes());
         }

         boolean var7;
         if (!var10.getDecoderResult().isSuccess()) {
            var4.setDecoderResult(DecoderResult.failure(var10.getDecoderResult().cause()));
            var7 = true;
         } else {
            var7 = var10 instanceof LastHttpContent;
         }

         if (var7) {
            this.currentMessage = null;
            if (var10 instanceof LastHttpContent) {
               LastHttpContent var8 = (LastHttpContent)var10;
               var4.headers().add(var8.trailingHeaders());
            }

            var4.headers().set((String)"Content-Length", (Object)String.valueOf(var12.readableBytes()));
            var3.add(var4);
         }
      }

   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      super.channelInactive(var1);
      if (this.currentMessage != null) {
         this.currentMessage.release();
         this.currentMessage = null;
      }

   }

   public void handlerAdded(ChannelHandlerContext var1) throws Exception {
      this.ctx = var1;
   }

   public void handlerRemoved(ChannelHandlerContext var1) throws Exception {
      super.handlerRemoved(var1);
      if (this.currentMessage != null) {
         this.currentMessage.release();
         this.currentMessage = null;
      }

   }

   protected void decode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
      this.decode(var1, (HttpObject)var2, var3);
   }

   static {
      CONTINUE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE, Unpooled.EMPTY_BUFFER);
   }
}
