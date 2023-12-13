package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.ReferenceCountUtil;
import java.util.List;

public abstract class HttpContentDecoder extends MessageToMessageDecoder {
   private EmbeddedChannel decoder;
   private HttpMessage message;
   private boolean decodeStarted;
   private boolean continueResponse;
   static final boolean $assertionsDisabled = !HttpContentDecoder.class.desiredAssertionStatus();

   protected void decode(ChannelHandlerContext var1, HttpObject var2, List var3) throws Exception {
      if (var2 instanceof HttpResponse && ((HttpResponse)var2).getStatus().code() == 100) {
         if (!(var2 instanceof LastHttpContent)) {
            this.continueResponse = true;
         }

         var3.add(ReferenceCountUtil.retain(var2));
      } else if (this.continueResponse) {
         if (var2 instanceof LastHttpContent) {
            this.continueResponse = false;
         }

         var3.add(ReferenceCountUtil.retain(var2));
      } else {
         if (var2 instanceof HttpMessage) {
            if (!$assertionsDisabled && this.message != null) {
               throw new AssertionError();
            }

            this.message = (HttpMessage)var2;
            this.decodeStarted = false;
            this.cleanup();
         }

         if (var2 instanceof HttpContent) {
            HttpContent var4 = (HttpContent)var2;
            if (!this.decodeStarted) {
               this.decodeStarted = true;
               HttpMessage var5 = this.message;
               HttpHeaders var6 = var5.headers();
               this.message = null;
               String var7 = var6.get("Content-Encoding");
               if (var7 != null) {
                  var7 = var7.trim();
               } else {
                  var7 = "identity";
               }

               if ((this.decoder = this.newContentDecoder(var7)) != null) {
                  String var8 = this.getTargetContentEncoding(var7);
                  if ("identity".equals(var8)) {
                     var6.remove("Content-Encoding");
                  } else {
                     var6.set((String)"Content-Encoding", (Object)var8);
                  }

                  var3.add(var5);
                  this.decodeContent(var4, var3);
                  if (var6.contains("Content-Length")) {
                     int var9 = 0;
                     int var10 = var3.size();

                     for(int var11 = 0; var11 < var10; ++var11) {
                        Object var12 = var3.get(var11);
                        if (var12 instanceof HttpContent) {
                           var9 += ((HttpContent)var12).content().readableBytes();
                        }
                     }

                     var6.set((String)"Content-Length", (Object)Integer.toString(var9));
                  }

                  return;
               }

               if (var4 instanceof LastHttpContent) {
                  this.decodeStarted = false;
               }

               var3.add(var5);
               var3.add(var4.retain());
               return;
            }

            if (this.decoder != null) {
               this.decodeContent(var4, var3);
            } else {
               if (var4 instanceof LastHttpContent) {
                  this.decodeStarted = false;
               }

               var3.add(var4.retain());
            }
         }

      }
   }

   private void decodeContent(HttpContent var1, List var2) {
      ByteBuf var3 = var1.content();
      this.decode(var3, var2);
      if (var1 instanceof LastHttpContent) {
         this.finishDecode(var2);
         LastHttpContent var4 = (LastHttpContent)var1;
         HttpHeaders var5 = var4.trailingHeaders();
         if (var5.isEmpty()) {
            var2.add(LastHttpContent.EMPTY_LAST_CONTENT);
         } else {
            var2.add(new ComposedLastHttpContent(var5));
         }
      }

   }

   protected abstract EmbeddedChannel newContentDecoder(String var1) throws Exception;

   protected String getTargetContentEncoding(String var1) throws Exception {
      return "identity";
   }

   public void handlerRemoved(ChannelHandlerContext var1) throws Exception {
      this.cleanup();
      super.handlerRemoved(var1);
   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      this.cleanup();
      super.channelInactive(var1);
   }

   private void cleanup() {
      if (this.decoder != null) {
         if (this.decoder.finish()) {
            while(true) {
               ByteBuf var1 = (ByteBuf)this.decoder.readOutbound();
               if (var1 == null) {
                  break;
               }

               var1.release();
            }
         }

         this.decoder = null;
      }

   }

   private void decode(ByteBuf var1, List var2) {
      this.decoder.writeInbound(var1.retain());
      this.fetchDecoderOutput(var2);
   }

   private void finishDecode(List var1) {
      if (this.decoder.finish()) {
         this.fetchDecoderOutput(var1);
      }

      this.decodeStarted = false;
      this.decoder = null;
   }

   private void fetchDecoderOutput(List var1) {
      while(true) {
         ByteBuf var2 = (ByteBuf)this.decoder.readInbound();
         if (var2 == null) {
            return;
         }

         if (!var2.isReadable()) {
            var2.release();
         } else {
            var1.add(new DefaultHttpContent(var2));
         }
      }
   }

   protected void decode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
      this.decode(var1, (HttpObject)var2, var3);
   }
}
