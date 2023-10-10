package io.netty.bootstrap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public final class Bootstrap extends AbstractBootstrap {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(Bootstrap.class);
   private volatile SocketAddress remoteAddress;

   public Bootstrap() {
   }

   private Bootstrap(Bootstrap var1) {
      super(var1);
      this.remoteAddress = var1.remoteAddress;
   }

   public Bootstrap remoteAddress(SocketAddress var1) {
      this.remoteAddress = var1;
      return this;
   }

   public Bootstrap remoteAddress(String var1, int var2) {
      this.remoteAddress = new InetSocketAddress(var1, var2);
      return this;
   }

   public Bootstrap remoteAddress(InetAddress var1, int var2) {
      this.remoteAddress = new InetSocketAddress(var1, var2);
      return this;
   }

   public ChannelFuture connect() {
      this.validate();
      SocketAddress var1 = this.remoteAddress;
      if (var1 == null) {
         throw new IllegalStateException("remoteAddress not set");
      } else {
         return this.doConnect(var1, this.localAddress());
      }
   }

   public ChannelFuture connect(String var1, int var2) {
      return this.connect(new InetSocketAddress(var1, var2));
   }

   public ChannelFuture connect(InetAddress var1, int var2) {
      return this.connect(new InetSocketAddress(var1, var2));
   }

   public ChannelFuture connect(SocketAddress var1) {
      if (var1 == null) {
         throw new NullPointerException("remoteAddress");
      } else {
         this.validate();
         return this.doConnect(var1, this.localAddress());
      }
   }

   public ChannelFuture connect(SocketAddress var1, SocketAddress var2) {
      if (var1 == null) {
         throw new NullPointerException("remoteAddress");
      } else {
         this.validate();
         return this.doConnect(var1, var2);
      }
   }

   private ChannelFuture doConnect(SocketAddress var1, SocketAddress var2) {
      ChannelFuture var3 = this.initAndRegister();
      Channel var4 = var3.channel();
      if (var3.cause() != null) {
         return var3;
      } else {
         ChannelPromise var5 = var4.newPromise();
         if (var3.isDone()) {
            doConnect0(var3, var4, var1, var2, var5);
         } else {
            var3.addListener(new ChannelFutureListener(this, var3, var4, var1, var2, var5) {
               final ChannelFuture val$regFuture;
               final Channel val$channel;
               final SocketAddress val$remoteAddress;
               final SocketAddress val$localAddress;
               final ChannelPromise val$promise;
               final Bootstrap this$0;

               {
                  this.this$0 = var1;
                  this.val$regFuture = var2;
                  this.val$channel = var3;
                  this.val$remoteAddress = var4;
                  this.val$localAddress = var5;
                  this.val$promise = var6;
               }

               public void operationComplete(ChannelFuture var1) throws Exception {
                  Bootstrap.access$000(this.val$regFuture, this.val$channel, this.val$remoteAddress, this.val$localAddress, this.val$promise);
               }

               public void operationComplete(Future var1) throws Exception {
                  this.operationComplete((ChannelFuture)var1);
               }
            });
         }

         return var5;
      }
   }

   private static void doConnect0(ChannelFuture var0, Channel var1, SocketAddress var2, SocketAddress var3, ChannelPromise var4) {
      var1.eventLoop().execute(new Runnable(var0, var3, var1, var2, var4) {
         final ChannelFuture val$regFuture;
         final SocketAddress val$localAddress;
         final Channel val$channel;
         final SocketAddress val$remoteAddress;
         final ChannelPromise val$promise;

         {
            this.val$regFuture = var1;
            this.val$localAddress = var2;
            this.val$channel = var3;
            this.val$remoteAddress = var4;
            this.val$promise = var5;
         }

         public void run() {
            if (this.val$regFuture.isSuccess()) {
               if (this.val$localAddress == null) {
                  this.val$channel.connect(this.val$remoteAddress, this.val$promise);
               } else {
                  this.val$channel.connect(this.val$remoteAddress, this.val$localAddress, this.val$promise);
               }

               this.val$promise.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } else {
               this.val$promise.setFailure(this.val$regFuture.cause());
            }

         }
      });
   }

   void init(Channel var1) throws Exception {
      ChannelPipeline var2 = var1.pipeline();
      var2.addLast(this.handler());
      Map var3 = this.options();
      synchronized(var3){}
      Iterator var5 = var3.entrySet().iterator();

      while(var5.hasNext()) {
         Entry var6 = (Entry)var5.next();

         try {
            if (!var1.config().setOption((ChannelOption)var6.getKey(), var6.getValue())) {
               logger.warn("Unknown channel option: " + var6);
            }
         } catch (Throwable var10) {
            logger.warn("Failed to set a channel option: " + var1, var10);
         }
      }

      Map var4 = this.attrs();
      synchronized(var4){}
      Iterator var11 = var4.entrySet().iterator();

      while(var11.hasNext()) {
         Entry var7 = (Entry)var11.next();
         var1.attr((AttributeKey)var7.getKey()).set(var7.getValue());
      }

   }

   public Bootstrap validate() {
      super.validate();
      if (this.handler() == null) {
         throw new IllegalStateException("handler not set");
      } else {
         return this;
      }
   }

   public Bootstrap clone() {
      return new Bootstrap(this);
   }

   public String toString() {
      if (this.remoteAddress == null) {
         return super.toString();
      } else {
         StringBuilder var1 = new StringBuilder(super.toString());
         var1.setLength(var1.length() - 1);
         var1.append(", remoteAddress: ");
         var1.append(this.remoteAddress);
         var1.append(')');
         return var1.toString();
      }
   }

   public AbstractBootstrap clone() {
      return this.clone();
   }

   public AbstractBootstrap validate() {
      return this.validate();
   }

   public Object clone() throws CloneNotSupportedException {
      return this.clone();
   }

   static void access$000(ChannelFuture var0, Channel var1, SocketAddress var2, SocketAddress var3, ChannelPromise var4) {
      doConnect0(var0, var1, var2, var3, var4);
   }
}
