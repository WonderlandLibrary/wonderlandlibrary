package io.netty.bootstrap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public final class ServerBootstrap extends AbstractBootstrap {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ServerBootstrap.class);
   private final Map childOptions = new LinkedHashMap();
   private final Map childAttrs = new LinkedHashMap();
   private volatile EventLoopGroup childGroup;
   private volatile ChannelHandler childHandler;

   public ServerBootstrap() {
   }

   private ServerBootstrap(ServerBootstrap var1) {
      super(var1);
      this.childGroup = var1.childGroup;
      this.childHandler = var1.childHandler;
      Map var2;
      synchronized(var2 = var1.childOptions){}
      this.childOptions.putAll(var1.childOptions);
      synchronized(var2 = var1.childAttrs){}
      this.childAttrs.putAll(var1.childAttrs);
   }

   public ServerBootstrap group(EventLoopGroup var1) {
      return this.group(var1, var1);
   }

   public ServerBootstrap group(EventLoopGroup var1, EventLoopGroup var2) {
      super.group(var1);
      if (var2 == null) {
         throw new NullPointerException("childGroup");
      } else if (this.childGroup != null) {
         throw new IllegalStateException("childGroup set already");
      } else {
         this.childGroup = var2;
         return this;
      }
   }

   public ServerBootstrap childOption(ChannelOption var1, Object var2) {
      if (var1 == null) {
         throw new NullPointerException("childOption");
      } else {
         Map var3;
         if (var2 == null) {
            synchronized(var3 = this.childOptions){}
            this.childOptions.remove(var1);
         } else {
            synchronized(var3 = this.childOptions){}
            this.childOptions.put(var1, var2);
         }

         return this;
      }
   }

   public ServerBootstrap childAttr(AttributeKey var1, Object var2) {
      if (var1 == null) {
         throw new NullPointerException("childKey");
      } else {
         if (var2 == null) {
            this.childAttrs.remove(var1);
         } else {
            this.childAttrs.put(var1, var2);
         }

         return this;
      }
   }

   public ServerBootstrap childHandler(ChannelHandler var1) {
      if (var1 == null) {
         throw new NullPointerException("childHandler");
      } else {
         this.childHandler = var1;
         return this;
      }
   }

   public EventLoopGroup childGroup() {
      return this.childGroup;
   }

   void init(Channel var1) throws Exception {
      Map var2 = this.options();
      synchronized(var2){}
      var1.config().setOptions(var2);
      Map var3 = this.attrs();
      synchronized(var3){}
      Iterator var5 = var3.entrySet().iterator();

      while(var5.hasNext()) {
         Entry var6 = (Entry)var5.next();
         AttributeKey var7 = (AttributeKey)var6.getKey();
         var1.attr(var7).set(var6.getValue());
      }

      ChannelPipeline var4 = var1.pipeline();
      if (this.handler() != null) {
         var4.addLast(this.handler());
      }

      EventLoopGroup var12 = this.childGroup;
      ChannelHandler var13 = this.childHandler;
      Map var9;
      synchronized(var9 = this.childOptions){}
      Entry[] var14 = (Entry[])this.childOptions.entrySet().toArray(newOptionArray(this.childOptions.size()));
      synchronized(var9 = this.childAttrs){}
      Entry[] var8 = (Entry[])this.childAttrs.entrySet().toArray(newAttrArray(this.childAttrs.size()));
      var4.addLast(new ChannelInitializer(this, var12, var13, var14, var8) {
         final EventLoopGroup val$currentChildGroup;
         final ChannelHandler val$currentChildHandler;
         final Entry[] val$currentChildOptions;
         final Entry[] val$currentChildAttrs;
         final ServerBootstrap this$0;

         {
            this.this$0 = var1;
            this.val$currentChildGroup = var2;
            this.val$currentChildHandler = var3;
            this.val$currentChildOptions = var4;
            this.val$currentChildAttrs = var5;
         }

         public void initChannel(Channel var1) throws Exception {
            var1.pipeline().addLast(new ServerBootstrap.ServerBootstrapAcceptor(this.val$currentChildGroup, this.val$currentChildHandler, this.val$currentChildOptions, this.val$currentChildAttrs));
         }
      });
   }

   public ServerBootstrap validate() {
      super.validate();
      if (this.childHandler == null) {
         throw new IllegalStateException("childHandler not set");
      } else {
         if (this.childGroup == null) {
            logger.warn("childGroup is not set. Using parentGroup instead.");
            this.childGroup = this.group();
         }

         return this;
      }
   }

   private static Entry[] newOptionArray(int var0) {
      return new Entry[var0];
   }

   private static Entry[] newAttrArray(int var0) {
      return new Entry[var0];
   }

   public ServerBootstrap clone() {
      return new ServerBootstrap(this);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(super.toString());
      var1.setLength(var1.length() - 1);
      var1.append(", ");
      if (this.childGroup != null) {
         var1.append("childGroup: ");
         var1.append(StringUtil.simpleClassName((Object)this.childGroup));
         var1.append(", ");
      }

      Map var2;
      synchronized(var2 = this.childOptions){}
      if (!this.childOptions.isEmpty()) {
         var1.append("childOptions: ");
         var1.append(this.childOptions);
         var1.append(", ");
      }

      synchronized(var2 = this.childAttrs){}
      if (!this.childAttrs.isEmpty()) {
         var1.append("childAttrs: ");
         var1.append(this.childAttrs);
         var1.append(", ");
      }

      if (this.childHandler != null) {
         var1.append("childHandler: ");
         var1.append(this.childHandler);
         var1.append(", ");
      }

      if (var1.charAt(var1.length() - 1) == '(') {
         var1.append(')');
      } else {
         var1.setCharAt(var1.length() - 2, ')');
         var1.setLength(var1.length() - 1);
      }

      return var1.toString();
   }

   public AbstractBootstrap clone() {
      return this.clone();
   }

   public AbstractBootstrap validate() {
      return this.validate();
   }

   public AbstractBootstrap group(EventLoopGroup var1) {
      return this.group(var1);
   }

   public Object clone() throws CloneNotSupportedException {
      return this.clone();
   }

   static InternalLogger access$000() {
      return logger;
   }

   private static class ServerBootstrapAcceptor extends ChannelInboundHandlerAdapter {
      private final EventLoopGroup childGroup;
      private final ChannelHandler childHandler;
      private final Entry[] childOptions;
      private final Entry[] childAttrs;

      ServerBootstrapAcceptor(EventLoopGroup var1, ChannelHandler var2, Entry[] var3, Entry[] var4) {
         this.childGroup = var1;
         this.childHandler = var2;
         this.childOptions = var3;
         this.childAttrs = var4;
      }

      public void channelRead(ChannelHandlerContext var1, Object var2) {
         Channel var3 = (Channel)var2;
         var3.pipeline().addLast(this.childHandler);
         Entry[] var4 = this.childOptions;
         int var5 = var4.length;

         int var6;
         Entry var7;
         for(var6 = 0; var6 < var5; ++var6) {
            var7 = var4[var6];

            try {
               if (!var3.config().setOption((ChannelOption)var7.getKey(), var7.getValue())) {
                  ServerBootstrap.access$000().warn("Unknown channel option: " + var7);
               }
            } catch (Throwable var10) {
               ServerBootstrap.access$000().warn("Failed to set a channel option: " + var3, var10);
            }
         }

         var4 = this.childAttrs;
         var5 = var4.length;

         for(var6 = 0; var6 < var5; ++var6) {
            var7 = var4[var6];
            var3.attr((AttributeKey)var7.getKey()).set(var7.getValue());
         }

         try {
            this.childGroup.register(var3).addListener(new ChannelFutureListener(this, var3) {
               final Channel val$child;
               final ServerBootstrap.ServerBootstrapAcceptor this$0;

               {
                  this.this$0 = var1;
                  this.val$child = var2;
               }

               public void operationComplete(ChannelFuture var1) throws Exception {
                  if (!var1.isSuccess()) {
                     ServerBootstrap.ServerBootstrapAcceptor.access$100(this.val$child, var1.cause());
                  }

               }

               public void operationComplete(Future var1) throws Exception {
                  this.operationComplete((ChannelFuture)var1);
               }
            });
         } catch (Throwable var9) {
            forceClose(var3, var9);
         }

      }

      private static void forceClose(Channel var0, Throwable var1) {
         var0.unsafe().closeForcibly();
         ServerBootstrap.access$000().warn("Failed to register an accepted channel: " + var0, var1);
      }

      public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception {
         ChannelConfig var3 = var1.channel().config();
         if (var3.isAutoRead()) {
            var3.setAutoRead(false);
            var1.channel().eventLoop().schedule(new Runnable(this, var3) {
               final ChannelConfig val$config;
               final ServerBootstrap.ServerBootstrapAcceptor this$0;

               {
                  this.this$0 = var1;
                  this.val$config = var2;
               }

               public void run() {
                  this.val$config.setAutoRead(true);
               }
            }, 1L, TimeUnit.SECONDS);
         }

         var1.fireExceptionCaught(var2);
      }

      static void access$100(Channel var0, Throwable var1) {
         forceClose(var0, var1);
      }
   }
}
