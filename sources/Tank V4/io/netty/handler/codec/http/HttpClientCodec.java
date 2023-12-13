package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.CombinedChannelDuplexHandler;
import io.netty.handler.codec.PrematureChannelClosureException;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

public final class HttpClientCodec extends CombinedChannelDuplexHandler {
   private final Queue queue;
   private boolean done;
   private final AtomicLong requestResponseCounter;
   private final boolean failOnMissingResponse;

   public HttpClientCodec() {
      this(4096, 8192, 8192, false);
   }

   public void setSingleDecode(boolean var1) {
      ((HttpResponseDecoder)this.inboundHandler()).setSingleDecode(var1);
   }

   public boolean isSingleDecode() {
      return ((HttpResponseDecoder)this.inboundHandler()).isSingleDecode();
   }

   public HttpClientCodec(int var1, int var2, int var3) {
      this(var1, var2, var3, false);
   }

   public HttpClientCodec(int var1, int var2, int var3, boolean var4) {
      this(var1, var2, var3, var4, true);
   }

   public HttpClientCodec(int var1, int var2, int var3, boolean var4, boolean var5) {
      this.queue = new ArrayDeque();
      this.requestResponseCounter = new AtomicLong();
      this.init(new HttpClientCodec.Decoder(this, var1, var2, var3, var5), new HttpClientCodec.Encoder(this));
      this.failOnMissingResponse = var4;
   }

   static boolean access$100(HttpClientCodec var0) {
      return var0.done;
   }

   static Queue access$200(HttpClientCodec var0) {
      return var0.queue;
   }

   static boolean access$300(HttpClientCodec var0) {
      return var0.failOnMissingResponse;
   }

   static AtomicLong access$400(HttpClientCodec var0) {
      return var0.requestResponseCounter;
   }

   static boolean access$102(HttpClientCodec var0, boolean var1) {
      return var0.done = var1;
   }

   private final class Decoder extends HttpResponseDecoder {
      final HttpClientCodec this$0;

      Decoder(HttpClientCodec var1, int var2, int var3, int var4, boolean var5) {
         super(var2, var3, var4, var5);
         this.this$0 = var1;
      }

      protected void decode(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
         int var4;
         if (HttpClientCodec.access$100(this.this$0)) {
            var4 = this.actualReadableBytes();
            if (var4 == 0) {
               return;
            }

            var3.add(var2.readBytes(var4));
         } else {
            var4 = var3.size();
            super.decode(var1, var2, var3);
            if (HttpClientCodec.access$300(this.this$0)) {
               int var5 = var3.size();

               for(int var6 = var4; var6 < var5; ++var6) {
                  this.decrement(var3.get(var6));
               }
            }
         }

      }

      private void decrement(Object var1) {
         if (var1 != null) {
            if (var1 instanceof LastHttpContent) {
               HttpClientCodec.access$400(this.this$0).decrementAndGet();
            }

         }
      }

      protected boolean isContentAlwaysEmpty(HttpMessage var1) {
         int var2 = ((HttpResponse)var1).getStatus().code();
         if (var2 == 100) {
            return true;
         } else {
            HttpMethod var3 = (HttpMethod)HttpClientCodec.access$200(this.this$0).poll();
            char var4 = var3.name().charAt(0);
            switch(var4) {
            case 'C':
               if (var2 == 200 && HttpMethod.CONNECT.equals(var3)) {
                  HttpClientCodec.access$102(this.this$0, true);
                  HttpClientCodec.access$200(this.this$0).clear();
                  return true;
               }
               break;
            case 'H':
               if (HttpMethod.HEAD.equals(var3)) {
                  return true;
               }
            }

            return super.isContentAlwaysEmpty(var1);
         }
      }

      public void channelInactive(ChannelHandlerContext var1) throws Exception {
         super.channelInactive(var1);
         if (HttpClientCodec.access$300(this.this$0)) {
            long var2 = HttpClientCodec.access$400(this.this$0).get();
            if (var2 > 0L) {
               var1.fireExceptionCaught(new PrematureChannelClosureException("channel gone inactive with " + var2 + " missing response(s)"));
            }
         }

      }
   }

   private final class Encoder extends HttpRequestEncoder {
      final HttpClientCodec this$0;

      private Encoder(HttpClientCodec var1) {
         this.this$0 = var1;
      }

      protected void encode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
         if (var2 instanceof HttpRequest && !HttpClientCodec.access$100(this.this$0)) {
            HttpClientCodec.access$200(this.this$0).offer(((HttpRequest)var2).getMethod());
         }

         super.encode(var1, var2, var3);
         if (HttpClientCodec.access$300(this.this$0) && var2 instanceof LastHttpContent) {
            HttpClientCodec.access$400(this.this$0).incrementAndGet();
         }

      }

      Encoder(HttpClientCodec var1, Object var2) {
         this(var1);
      }
   }
}
