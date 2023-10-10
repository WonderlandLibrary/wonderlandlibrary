package io.netty.bootstrap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.internal.StringUtil;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractBootstrap implements Cloneable {
   private volatile EventLoopGroup group;
   private volatile ChannelFactory channelFactory;
   private volatile SocketAddress localAddress;
   private final Map options = new LinkedHashMap();
   private final Map attrs = new LinkedHashMap();
   private volatile ChannelHandler handler;

   AbstractBootstrap() {
   }

   AbstractBootstrap(AbstractBootstrap var1) {
      this.group = var1.group;
      this.channelFactory = var1.channelFactory;
      this.handler = var1.handler;
      this.localAddress = var1.localAddress;
      Map var2;
      synchronized(var2 = var1.options){}
      this.options.putAll(var1.options);
      synchronized(var2 = var1.attrs){}
      this.attrs.putAll(var1.attrs);
   }

   public AbstractBootstrap group(EventLoopGroup var1) {
      if (var1 == null) {
         throw new NullPointerException("group");
      } else if (this.group != null) {
         throw new IllegalStateException("group set already");
      } else {
         this.group = var1;
         return this;
      }
   }

   public AbstractBootstrap channel(Class var1) {
      if (var1 == null) {
         throw new NullPointerException("channelClass");
      } else {
         return this.channelFactory(new AbstractBootstrap.BootstrapChannelFactory(var1));
      }
   }

   public AbstractBootstrap channelFactory(ChannelFactory var1) {
      if (var1 == null) {
         throw new NullPointerException("channelFactory");
      } else if (this.channelFactory != null) {
         throw new IllegalStateException("channelFactory set already");
      } else {
         this.channelFactory = var1;
         return this;
      }
   }

   public AbstractBootstrap localAddress(SocketAddress var1) {
      this.localAddress = var1;
      return this;
   }

   public AbstractBootstrap localAddress(int var1) {
      return this.localAddress(new InetSocketAddress(var1));
   }

   public AbstractBootstrap localAddress(String var1, int var2) {
      return this.localAddress(new InetSocketAddress(var1, var2));
   }

   public AbstractBootstrap localAddress(InetAddress var1, int var2) {
      return this.localAddress(new InetSocketAddress(var1, var2));
   }

   public AbstractBootstrap option(ChannelOption var1, Object var2) {
      if (var1 == null) {
         throw new NullPointerException("option");
      } else {
         Map var3;
         if (var2 == null) {
            synchronized(var3 = this.options){}
            this.options.remove(var1);
         } else {
            synchronized(var3 = this.options){}
            this.options.put(var1, var2);
         }

         return this;
      }
   }

   public AbstractBootstrap attr(AttributeKey var1, Object var2) {
      if (var1 == null) {
         throw new NullPointerException("key");
      } else {
         Map var3;
         if (var2 == null) {
            synchronized(var3 = this.attrs){}
            this.attrs.remove(var1);
         } else {
            synchronized(var3 = this.attrs){}
            this.attrs.put(var1, var2);
         }

         return this;
      }
   }

   public AbstractBootstrap validate() {
      if (this.group == null) {
         throw new IllegalStateException("group not set");
      } else if (this.channelFactory == null) {
         throw new IllegalStateException("channel or channelFactory not set");
      } else {
         return this;
      }
   }

   public abstract AbstractBootstrap clone();

   public ChannelFuture register() {
      this.validate();
      return this.initAndRegister();
   }

   public ChannelFuture bind() {
      this.validate();
      SocketAddress var1 = this.localAddress;
      if (var1 == null) {
         throw new IllegalStateException("localAddress not set");
      } else {
         return this.doBind(var1);
      }
   }

   public ChannelFuture bind(int var1) {
      return this.bind(new InetSocketAddress(var1));
   }

   public ChannelFuture bind(String var1, int var2) {
      return this.bind(new InetSocketAddress(var1, var2));
   }

   public ChannelFuture bind(InetAddress var1, int var2) {
      return this.bind(new InetSocketAddress(var1, var2));
   }

   public ChannelFuture bind(SocketAddress var1) {
      this.validate();
      if (var1 == null) {
         throw new NullPointerException("localAddress");
      } else {
         return this.doBind(var1);
      }
   }

   private ChannelFuture doBind(SocketAddress var1) {
      ChannelFuture var2 = this.initAndRegister();
      Channel var3 = var2.channel();
      if (var2.cause() != null) {
         return var2;
      } else {
         Object var4;
         if (var2.isDone()) {
            var4 = var3.newPromise();
            doBind0(var2, var3, var1, (ChannelPromise)var4);
         } else {
            var4 = new DefaultChannelPromise(var3, GlobalEventExecutor.INSTANCE);
            var2.addListener(new ChannelFutureListener(this, var2, var3, var1, (ChannelPromise)var4) {
               final ChannelFuture val$regFuture;
               final Channel val$channel;
               final SocketAddress val$localAddress;
               final ChannelPromise val$promise;
               final AbstractBootstrap this$0;

               {
                  this.this$0 = var1;
                  this.val$regFuture = var2;
                  this.val$channel = var3;
                  this.val$localAddress = var4;
                  this.val$promise = var5;
               }

               public void operationComplete(ChannelFuture var1) throws Exception {
                  AbstractBootstrap.access$000(this.val$regFuture, this.val$channel, this.val$localAddress, this.val$promise);
               }

               public void operationComplete(Future var1) throws Exception {
                  this.operationComplete((ChannelFuture)var1);
               }
            });
         }

         return (ChannelFuture)var4;
      }
   }

   final ChannelFuture initAndRegister() {
      Channel var1 = this.channelFactory().newChannel();

      try {
         this.init(var1);
      } catch (Throwable var3) {
         var1.unsafe().closeForcibly();
         return var1.newFailedFuture(var3);
      }

      ChannelFuture var2 = this.group().register(var1);
      if (var2.cause() != null) {
         if (var1.isRegistered()) {
            var1.close();
         } else {
            var1.unsafe().closeForcibly();
         }
      }

      return var2;
   }

   abstract void init(Channel var1) throws Exception;

   private static void doBind0(ChannelFuture var0, Channel var1, SocketAddress var2, ChannelPromise var3) {
      var1.eventLoop().execute(new Runnable(var0, var1, var2, var3) {
         final ChannelFuture val$regFuture;
         final Channel val$channel;
         final SocketAddress val$localAddress;
         final ChannelPromise val$promise;

         {
            this.val$regFuture = var1;
            this.val$channel = var2;
            this.val$localAddress = var3;
            this.val$promise = var4;
         }

         public void run() {
            if (this.val$regFuture.isSuccess()) {
               this.val$channel.bind(this.val$localAddress, this.val$promise).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } else {
               this.val$promise.setFailure(this.val$regFuture.cause());
            }

         }
      });
   }

   public AbstractBootstrap handler(ChannelHandler var1) {
      if (var1 == null) {
         throw new NullPointerException("handler");
      } else {
         this.handler = var1;
         return this;
      }
   }

   final SocketAddress localAddress() {
      return this.localAddress;
   }

   final ChannelFactory channelFactory() {
      return this.channelFactory;
   }

   final ChannelHandler handler() {
      return this.handler;
   }

   public final EventLoopGroup group() {
      return this.group;
   }

   final Map options() {
      return this.options;
   }

   final Map attrs() {
      return this.attrs;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(StringUtil.simpleClassName((Object)this));
      var1.append('(');
      if (this.group != null) {
         var1.append("group: ");
         var1.append(StringUtil.simpleClassName((Object)this.group));
         var1.append(", ");
      }

      if (this.channelFactory != null) {
         var1.append("channelFactory: ");
         var1.append(this.channelFactory);
         var1.append(", ");
      }

      if (this.localAddress != null) {
         var1.append("localAddress: ");
         var1.append(this.localAddress);
         var1.append(", ");
      }

      Map var2;
      synchronized(var2 = this.options){}
      if (!this.options.isEmpty()) {
         var1.append("options: ");
         var1.append(this.options);
         var1.append(", ");
      }

      synchronized(var2 = this.attrs){}
      if (!this.attrs.isEmpty()) {
         var1.append("attrs: ");
         var1.append(this.attrs);
         var1.append(", ");
      }

      if (this.handler != null) {
         var1.append("handler: ");
         var1.append(this.handler);
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

   public Object clone() throws CloneNotSupportedException {
      return this.clone();
   }

   static void access$000(ChannelFuture var0, Channel var1, SocketAddress var2, ChannelPromise var3) {
      doBind0(var0, var1, var2, var3);
   }

   private static final class BootstrapChannelFactory implements ChannelFactory {
      private final Class clazz;

      BootstrapChannelFactory(Class var1) {
         this.clazz = var1;
      }

      public Channel newChannel() {
         try {
            return (Channel)this.clazz.newInstance();
         } catch (Throwable var2) {
            throw new ChannelException("Unable to create Channel from class " + this.clazz, var2);
         }
      }

      public String toString() {
         return StringUtil.simpleClassName(this.clazz) + ".class";
      }
   }
}
