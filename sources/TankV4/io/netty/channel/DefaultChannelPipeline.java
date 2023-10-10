package io.netty.channel;

import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutionException;

final class DefaultChannelPipeline implements ChannelPipeline {
   static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultChannelPipeline.class);
   private static final WeakHashMap[] nameCaches = new WeakHashMap[Runtime.getRuntime().availableProcessors()];
   final AbstractChannel channel;
   final DefaultChannelHandlerContext head;
   final DefaultChannelHandlerContext tail;
   private final Map name2ctx = new HashMap(4);
   final Map childExecutors = new IdentityHashMap();
   static final boolean $assertionsDisabled = !DefaultChannelPipeline.class.desiredAssertionStatus();

   public DefaultChannelPipeline(AbstractChannel var1) {
      if (var1 == null) {
         throw new NullPointerException("channel");
      } else {
         this.channel = var1;
         DefaultChannelPipeline.TailHandler var2 = new DefaultChannelPipeline.TailHandler();
         this.tail = new DefaultChannelHandlerContext(this, (EventExecutorGroup)null, this.generateName(var2), var2);
         DefaultChannelPipeline.HeadHandler var3 = new DefaultChannelPipeline.HeadHandler(var1.unsafe());
         this.head = new DefaultChannelHandlerContext(this, (EventExecutorGroup)null, this.generateName(var3), var3);
         this.head.next = this.tail;
         this.tail.prev = this.head;
      }
   }

   public Channel channel() {
      return this.channel;
   }

   public ChannelPipeline addFirst(String var1, ChannelHandler var2) {
      return this.addFirst((EventExecutorGroup)null, var1, var2);
   }

   public ChannelPipeline addFirst(EventExecutorGroup var1, String var2, ChannelHandler var3) {
      synchronized(this){}
      this.checkDuplicateName(var2);
      DefaultChannelHandlerContext var5 = new DefaultChannelHandlerContext(this, var1, var2, var3);
      this.addFirst0(var2, var5);
      return this;
   }

   private void addFirst0(String var1, DefaultChannelHandlerContext var2) {
      checkMultiplicity(var2);
      DefaultChannelHandlerContext var3 = this.head.next;
      var2.prev = this.head;
      var2.next = var3;
      this.head.next = var2;
      var3.prev = var2;
      this.name2ctx.put(var1, var2);
      this.callHandlerAdded(var2);
   }

   public ChannelPipeline addLast(String var1, ChannelHandler var2) {
      return this.addLast((EventExecutorGroup)null, var1, var2);
   }

   public ChannelPipeline addLast(EventExecutorGroup var1, String var2, ChannelHandler var3) {
      synchronized(this){}
      this.checkDuplicateName(var2);
      DefaultChannelHandlerContext var5 = new DefaultChannelHandlerContext(this, var1, var2, var3);
      this.addLast0(var2, var5);
      return this;
   }

   private void addLast0(String var1, DefaultChannelHandlerContext var2) {
      checkMultiplicity(var2);
      DefaultChannelHandlerContext var3 = this.tail.prev;
      var2.prev = var3;
      var2.next = this.tail;
      var3.next = var2;
      this.tail.prev = var2;
      this.name2ctx.put(var1, var2);
      this.callHandlerAdded(var2);
   }

   public ChannelPipeline addBefore(String var1, String var2, ChannelHandler var3) {
      return this.addBefore((EventExecutorGroup)null, var1, var2, var3);
   }

   public ChannelPipeline addBefore(EventExecutorGroup var1, String var2, String var3, ChannelHandler var4) {
      synchronized(this){}
      DefaultChannelHandlerContext var6 = this.getContextOrDie(var2);
      this.checkDuplicateName(var3);
      DefaultChannelHandlerContext var7 = new DefaultChannelHandlerContext(this, var1, var3, var4);
      this.addBefore0(var3, var6, var7);
      return this;
   }

   private void addBefore0(String var1, DefaultChannelHandlerContext var2, DefaultChannelHandlerContext var3) {
      checkMultiplicity(var3);
      var3.prev = var2.prev;
      var3.next = var2;
      var2.prev.next = var3;
      var2.prev = var3;
      this.name2ctx.put(var1, var3);
      this.callHandlerAdded(var3);
   }

   public ChannelPipeline addAfter(String var1, String var2, ChannelHandler var3) {
      return this.addAfter((EventExecutorGroup)null, var1, var2, var3);
   }

   public ChannelPipeline addAfter(EventExecutorGroup var1, String var2, String var3, ChannelHandler var4) {
      synchronized(this){}
      DefaultChannelHandlerContext var6 = this.getContextOrDie(var2);
      this.checkDuplicateName(var3);
      DefaultChannelHandlerContext var7 = new DefaultChannelHandlerContext(this, var1, var3, var4);
      this.addAfter0(var3, var6, var7);
      return this;
   }

   private void addAfter0(String var1, DefaultChannelHandlerContext var2, DefaultChannelHandlerContext var3) {
      this.checkDuplicateName(var1);
      checkMultiplicity(var3);
      var3.prev = var2;
      var3.next = var2.next;
      var2.next.prev = var3;
      var2.next = var3;
      this.name2ctx.put(var1, var3);
      this.callHandlerAdded(var3);
   }

   public ChannelPipeline addFirst(ChannelHandler... var1) {
      return this.addFirst((EventExecutorGroup)null, (ChannelHandler[])var1);
   }

   public ChannelPipeline addFirst(EventExecutorGroup var1, ChannelHandler... var2) {
      if (var2 == null) {
         throw new NullPointerException("handlers");
      } else if (var2.length != 0 && var2[0] != null) {
         int var3;
         for(var3 = 1; var3 < var2.length && var2[var3] != null; ++var3) {
         }

         for(int var4 = var3 - 1; var4 >= 0; --var4) {
            ChannelHandler var5 = var2[var4];
            this.addFirst(var1, this.generateName(var5), var5);
         }

         return this;
      } else {
         return this;
      }
   }

   public ChannelPipeline addLast(ChannelHandler... var1) {
      return this.addLast((EventExecutorGroup)null, (ChannelHandler[])var1);
   }

   public ChannelPipeline addLast(EventExecutorGroup var1, ChannelHandler... var2) {
      if (var2 == null) {
         throw new NullPointerException("handlers");
      } else {
         ChannelHandler[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            ChannelHandler var6 = var3[var5];
            if (var6 == null) {
               break;
            }

            this.addLast(var1, this.generateName(var6), var6);
         }

         return this;
      }
   }

   private String generateName(ChannelHandler var1) {
      WeakHashMap var2 = nameCaches[(int)(Thread.currentThread().getId() % (long)nameCaches.length)];
      Class var3 = var1.getClass();
      synchronized(var2){}
      String var4 = (String)var2.get(var3);
      if (var4 == null) {
         var4 = StringUtil.simpleClassName(var3) + "#0";
         var2.put(var3, var4);
      }

      synchronized(this){}
      if (this.name2ctx.containsKey(var4)) {
         String var6 = var4.substring(0, var4.length() - 1);
         int var7 = 1;

         while(true) {
            String var8 = var6 + var7;
            if (!this.name2ctx.containsKey(var8)) {
               var4 = var8;
               break;
            }

            ++var7;
         }
      }

      return var4;
   }

   public ChannelPipeline remove(ChannelHandler var1) {
      this.remove(this.getContextOrDie(var1));
      return this;
   }

   public ChannelHandler remove(String var1) {
      return this.remove(this.getContextOrDie(var1)).handler();
   }

   public ChannelHandler remove(Class var1) {
      return this.remove(this.getContextOrDie(var1)).handler();
   }

   private DefaultChannelHandlerContext remove(DefaultChannelHandlerContext var1) {
      if ($assertionsDisabled || var1 != this.head && var1 != this.tail) {
         synchronized(this){}
         if (var1.channel().isRegistered() && !var1.executor().inEventLoop()) {
            Future var3 = var1.executor().submit(new Runnable(this, var1) {
               final DefaultChannelHandlerContext val$ctx;
               final DefaultChannelPipeline this$0;

               {
                  this.this$0 = var1;
                  this.val$ctx = var2;
               }

               public void run() {
                  DefaultChannelPipeline var1;
                  synchronized(var1 = this.this$0){}
                  this.this$0.remove0(this.val$ctx);
               }
            });
            waitForFuture(var3);
            return var1;
         } else {
            this.remove0(var1);
            return var1;
         }
      } else {
         throw new AssertionError();
      }
   }

   void remove0(DefaultChannelHandlerContext var1) {
      DefaultChannelHandlerContext var2 = var1.prev;
      DefaultChannelHandlerContext var3 = var1.next;
      var2.next = var3;
      var3.prev = var2;
      this.name2ctx.remove(var1.name());
      this.callHandlerRemoved(var1);
   }

   public ChannelHandler removeFirst() {
      if (this.head.next == this.tail) {
         throw new NoSuchElementException();
      } else {
         return this.remove(this.head.next).handler();
      }
   }

   public ChannelHandler removeLast() {
      if (this.head.next == this.tail) {
         throw new NoSuchElementException();
      } else {
         return this.remove(this.tail.prev).handler();
      }
   }

   public ChannelPipeline replace(ChannelHandler var1, String var2, ChannelHandler var3) {
      this.replace(this.getContextOrDie(var1), var2, var3);
      return this;
   }

   public ChannelHandler replace(String var1, String var2, ChannelHandler var3) {
      return this.replace(this.getContextOrDie(var1), var2, var3);
   }

   public ChannelHandler replace(Class var1, String var2, ChannelHandler var3) {
      return this.replace(this.getContextOrDie(var1), var2, var3);
   }

   private ChannelHandler replace(DefaultChannelHandlerContext var1, String var2, ChannelHandler var3) {
      if (!$assertionsDisabled && (var1 == this.head || var1 == this.tail)) {
         throw new AssertionError();
      } else {
         synchronized(this){}
         boolean var6 = var1.name().equals(var2);
         if (!var6) {
            this.checkDuplicateName(var2);
         }

         DefaultChannelHandlerContext var7 = new DefaultChannelHandlerContext(this, var1.executor, var2, var3);
         if (var7.channel().isRegistered() && !var7.executor().inEventLoop()) {
            Future var4 = var7.executor().submit(new Runnable(this, var1, var2, var7) {
               final DefaultChannelHandlerContext val$ctx;
               final String val$newName;
               final DefaultChannelHandlerContext val$newCtx;
               final DefaultChannelPipeline this$0;

               {
                  this.this$0 = var1;
                  this.val$ctx = var2;
                  this.val$newName = var3;
                  this.val$newCtx = var4;
               }

               public void run() {
                  DefaultChannelPipeline var1;
                  synchronized(var1 = this.this$0){}
                  DefaultChannelPipeline.access$000(this.this$0, this.val$ctx, this.val$newName, this.val$newCtx);
               }
            });
            waitForFuture(var4);
            return var1.handler();
         } else {
            this.replace0(var1, var2, var7);
            return var1.handler();
         }
      }
   }

   private void replace0(DefaultChannelHandlerContext var1, String var2, DefaultChannelHandlerContext var3) {
      checkMultiplicity(var3);
      DefaultChannelHandlerContext var4 = var1.prev;
      DefaultChannelHandlerContext var5 = var1.next;
      var3.prev = var4;
      var3.next = var5;
      var4.next = var3;
      var5.prev = var3;
      if (!var1.name().equals(var2)) {
         this.name2ctx.remove(var1.name());
      }

      this.name2ctx.put(var2, var3);
      var1.prev = var3;
      var1.next = var3;
      this.callHandlerAdded(var3);
      this.callHandlerRemoved(var1);
   }

   private static void checkMultiplicity(ChannelHandlerContext var0) {
      ChannelHandler var1 = var0.handler();
      if (var1 instanceof ChannelHandlerAdapter) {
         ChannelHandlerAdapter var2 = (ChannelHandlerAdapter)var1;
         if (!var2.isSharable() && var2.added) {
            throw new ChannelPipelineException(var2.getClass().getName() + " is not a @Sharable handler, so can't be added or removed multiple times.");
         }

         var2.added = true;
      }

   }

   private void callHandlerAdded(ChannelHandlerContext var1) {
      if (var1.channel().isRegistered() && !var1.executor().inEventLoop()) {
         var1.executor().execute(new Runnable(this, var1) {
            final ChannelHandlerContext val$ctx;
            final DefaultChannelPipeline this$0;

            {
               this.this$0 = var1;
               this.val$ctx = var2;
            }

            public void run() {
               DefaultChannelPipeline.access$100(this.this$0, this.val$ctx);
            }
         });
      } else {
         this.callHandlerAdded0(var1);
      }
   }

   private void callHandlerAdded0(ChannelHandlerContext var1) {
      try {
         var1.handler().handlerAdded(var1);
      } catch (Throwable var6) {
         boolean var3 = false;

         try {
            this.remove((DefaultChannelHandlerContext)var1);
            var3 = true;
         } catch (Throwable var5) {
            if (logger.isWarnEnabled()) {
               logger.warn("Failed to remove a handler: " + var1.name(), var5);
            }
         }

         if (var3) {
            this.fireExceptionCaught(new ChannelPipelineException(var1.handler().getClass().getName() + ".handlerAdded() has thrown an exception; removed.", var6));
         } else {
            this.fireExceptionCaught(new ChannelPipelineException(var1.handler().getClass().getName() + ".handlerAdded() has thrown an exception; also failed to remove.", var6));
         }
      }

   }

   private void callHandlerRemoved(DefaultChannelHandlerContext var1) {
      if (var1.channel().isRegistered() && !var1.executor().inEventLoop()) {
         var1.executor().execute(new Runnable(this, var1) {
            final DefaultChannelHandlerContext val$ctx;
            final DefaultChannelPipeline this$0;

            {
               this.this$0 = var1;
               this.val$ctx = var2;
            }

            public void run() {
               DefaultChannelPipeline.access$200(this.this$0, this.val$ctx);
            }
         });
      } else {
         this.callHandlerRemoved0(var1);
      }
   }

   private void callHandlerRemoved0(DefaultChannelHandlerContext var1) {
      try {
         var1.handler().handlerRemoved(var1);
         var1.setRemoved();
      } catch (Throwable var3) {
         this.fireExceptionCaught(new ChannelPipelineException(var1.handler().getClass().getName() + ".handlerRemoved() has thrown an exception.", var3));
      }

   }

   private static void waitForFuture(java.util.concurrent.Future var0) {
      try {
         var0.get();
      } catch (ExecutionException var2) {
         PlatformDependent.throwException(var2.getCause());
      } catch (InterruptedException var3) {
         Thread.currentThread().interrupt();
      }

   }

   public ChannelHandler first() {
      ChannelHandlerContext var1 = this.firstContext();
      return var1 == null ? null : var1.handler();
   }

   public ChannelHandlerContext firstContext() {
      DefaultChannelHandlerContext var1 = this.head.next;
      return var1 == this.head ? null : this.head.next;
   }

   public ChannelHandler last() {
      DefaultChannelHandlerContext var1 = this.tail.prev;
      return var1 == this.head ? null : var1.handler();
   }

   public ChannelHandlerContext lastContext() {
      DefaultChannelHandlerContext var1 = this.tail.prev;
      return var1 == this.head ? null : var1;
   }

   public ChannelHandler get(String var1) {
      ChannelHandlerContext var2 = this.context(var1);
      return var2 == null ? null : var2.handler();
   }

   public ChannelHandler get(Class var1) {
      ChannelHandlerContext var2 = this.context(var1);
      return var2 == null ? null : var2.handler();
   }

   public ChannelHandlerContext context(String var1) {
      if (var1 == null) {
         throw new NullPointerException("name");
      } else {
         synchronized(this){}
         return (ChannelHandlerContext)this.name2ctx.get(var1);
      }
   }

   public ChannelHandlerContext context(ChannelHandler var1) {
      if (var1 == null) {
         throw new NullPointerException("handler");
      } else {
         for(DefaultChannelHandlerContext var2 = this.head.next; var2 != null; var2 = var2.next) {
            if (var2.handler() == var1) {
               return var2;
            }
         }

         return null;
      }
   }

   public ChannelHandlerContext context(Class var1) {
      if (var1 == null) {
         throw new NullPointerException("handlerType");
      } else {
         for(DefaultChannelHandlerContext var2 = this.head.next; var2 != null; var2 = var2.next) {
            if (var1.isAssignableFrom(var2.handler().getClass())) {
               return var2;
            }
         }

         return null;
      }
   }

   public List names() {
      ArrayList var1 = new ArrayList();

      for(DefaultChannelHandlerContext var2 = this.head.next; var2 != null; var2 = var2.next) {
         var1.add(var2.name());
      }

      return var1;
   }

   public Map toMap() {
      LinkedHashMap var1 = new LinkedHashMap();

      for(DefaultChannelHandlerContext var2 = this.head.next; var2 != this.tail; var2 = var2.next) {
         var1.put(var2.name(), var2.handler());
      }

      return var1;
   }

   public Iterator iterator() {
      return this.toMap().entrySet().iterator();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(StringUtil.simpleClassName((Object)this));
      var1.append('{');
      DefaultChannelHandlerContext var2 = this.head.next;

      while(var2 != this.tail) {
         var1.append('(');
         var1.append(var2.name());
         var1.append(" = ");
         var1.append(var2.handler().getClass().getName());
         var1.append(')');
         var2 = var2.next;
         if (var2 == this.tail) {
            break;
         }

         var1.append(", ");
      }

      var1.append('}');
      return var1.toString();
   }

   public ChannelPipeline fireChannelRegistered() {
      this.head.fireChannelRegistered();
      return this;
   }

   public ChannelPipeline fireChannelUnregistered() {
      this.head.fireChannelUnregistered();
      if (!this.channel.isOpen()) {
         this.teardownAll();
      }

      return this;
   }

   private void teardownAll() {
      this.tail.prev.teardown();
   }

   public ChannelPipeline fireChannelActive() {
      this.head.fireChannelActive();
      if (this.channel.config().isAutoRead()) {
         this.channel.read();
      }

      return this;
   }

   public ChannelPipeline fireChannelInactive() {
      this.head.fireChannelInactive();
      return this;
   }

   public ChannelPipeline fireExceptionCaught(Throwable var1) {
      this.head.fireExceptionCaught(var1);
      return this;
   }

   public ChannelPipeline fireUserEventTriggered(Object var1) {
      this.head.fireUserEventTriggered(var1);
      return this;
   }

   public ChannelPipeline fireChannelRead(Object var1) {
      this.head.fireChannelRead(var1);
      return this;
   }

   public ChannelPipeline fireChannelReadComplete() {
      this.head.fireChannelReadComplete();
      if (this.channel.config().isAutoRead()) {
         this.read();
      }

      return this;
   }

   public ChannelPipeline fireChannelWritabilityChanged() {
      this.head.fireChannelWritabilityChanged();
      return this;
   }

   public ChannelFuture bind(SocketAddress var1) {
      return this.tail.bind(var1);
   }

   public ChannelFuture connect(SocketAddress var1) {
      return this.tail.connect(var1);
   }

   public ChannelFuture connect(SocketAddress var1, SocketAddress var2) {
      return this.tail.connect(var1, var2);
   }

   public ChannelFuture disconnect() {
      return this.tail.disconnect();
   }

   public ChannelFuture close() {
      return this.tail.close();
   }

   public ChannelFuture deregister() {
      return this.tail.deregister();
   }

   public ChannelPipeline flush() {
      this.tail.flush();
      return this;
   }

   public ChannelFuture bind(SocketAddress var1, ChannelPromise var2) {
      return this.tail.bind(var1, var2);
   }

   public ChannelFuture connect(SocketAddress var1, ChannelPromise var2) {
      return this.tail.connect(var1, var2);
   }

   public ChannelFuture connect(SocketAddress var1, SocketAddress var2, ChannelPromise var3) {
      return this.tail.connect(var1, var2, var3);
   }

   public ChannelFuture disconnect(ChannelPromise var1) {
      return this.tail.disconnect(var1);
   }

   public ChannelFuture close(ChannelPromise var1) {
      return this.tail.close(var1);
   }

   public ChannelFuture deregister(ChannelPromise var1) {
      return this.tail.deregister(var1);
   }

   public ChannelPipeline read() {
      this.tail.read();
      return this;
   }

   public ChannelFuture write(Object var1) {
      return this.tail.write(var1);
   }

   public ChannelFuture write(Object var1, ChannelPromise var2) {
      return this.tail.write(var1, var2);
   }

   public ChannelFuture writeAndFlush(Object var1, ChannelPromise var2) {
      return this.tail.writeAndFlush(var1, var2);
   }

   public ChannelFuture writeAndFlush(Object var1) {
      return this.tail.writeAndFlush(var1);
   }

   private void checkDuplicateName(String var1) {
      if (this.name2ctx.containsKey(var1)) {
         throw new IllegalArgumentException("Duplicate handler name: " + var1);
      }
   }

   private DefaultChannelHandlerContext getContextOrDie(String var1) {
      DefaultChannelHandlerContext var2 = (DefaultChannelHandlerContext)this.context(var1);
      if (var2 == null) {
         throw new NoSuchElementException(var1);
      } else {
         return var2;
      }
   }

   private DefaultChannelHandlerContext getContextOrDie(ChannelHandler var1) {
      DefaultChannelHandlerContext var2 = (DefaultChannelHandlerContext)this.context(var1);
      if (var2 == null) {
         throw new NoSuchElementException(var1.getClass().getName());
      } else {
         return var2;
      }
   }

   private DefaultChannelHandlerContext getContextOrDie(Class var1) {
      DefaultChannelHandlerContext var2 = (DefaultChannelHandlerContext)this.context(var1);
      if (var2 == null) {
         throw new NoSuchElementException(var1.getName());
      } else {
         return var2;
      }
   }

   static void access$000(DefaultChannelPipeline var0, DefaultChannelHandlerContext var1, String var2, DefaultChannelHandlerContext var3) {
      var0.replace0(var1, var2, var3);
   }

   static void access$100(DefaultChannelPipeline var0, ChannelHandlerContext var1) {
      var0.callHandlerAdded0(var1);
   }

   static void access$200(DefaultChannelPipeline var0, DefaultChannelHandlerContext var1) {
      var0.callHandlerRemoved0(var1);
   }

   static {
      for(int var0 = 0; var0 < nameCaches.length; ++var0) {
         nameCaches[var0] = new WeakHashMap();
      }

   }

   static final class HeadHandler implements ChannelOutboundHandler {
      protected final Channel.Unsafe unsafe;

      protected HeadHandler(Channel.Unsafe var1) {
         this.unsafe = var1;
      }

      public void handlerAdded(ChannelHandlerContext var1) throws Exception {
      }

      public void handlerRemoved(ChannelHandlerContext var1) throws Exception {
      }

      public void bind(ChannelHandlerContext var1, SocketAddress var2, ChannelPromise var3) throws Exception {
         this.unsafe.bind(var2, var3);
      }

      public void connect(ChannelHandlerContext var1, SocketAddress var2, SocketAddress var3, ChannelPromise var4) throws Exception {
         this.unsafe.connect(var2, var3, var4);
      }

      public void disconnect(ChannelHandlerContext var1, ChannelPromise var2) throws Exception {
         this.unsafe.disconnect(var2);
      }

      public void close(ChannelHandlerContext var1, ChannelPromise var2) throws Exception {
         this.unsafe.close(var2);
      }

      public void deregister(ChannelHandlerContext var1, ChannelPromise var2) throws Exception {
         this.unsafe.deregister(var2);
      }

      public void read(ChannelHandlerContext var1) {
         this.unsafe.beginRead();
      }

      public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) throws Exception {
         this.unsafe.write(var2, var3);
      }

      public void flush(ChannelHandlerContext var1) throws Exception {
         this.unsafe.flush();
      }

      public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception {
         var1.fireExceptionCaught(var2);
      }
   }

   static final class TailHandler implements ChannelInboundHandler {
      public void channelRegistered(ChannelHandlerContext var1) throws Exception {
      }

      public void channelUnregistered(ChannelHandlerContext var1) throws Exception {
      }

      public void channelActive(ChannelHandlerContext var1) throws Exception {
      }

      public void channelInactive(ChannelHandlerContext var1) throws Exception {
      }

      public void channelWritabilityChanged(ChannelHandlerContext var1) throws Exception {
      }

      public void handlerAdded(ChannelHandlerContext var1) throws Exception {
      }

      public void handlerRemoved(ChannelHandlerContext var1) throws Exception {
      }

      public void userEventTriggered(ChannelHandlerContext var1, Object var2) throws Exception {
      }

      public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception {
         DefaultChannelPipeline.logger.warn("An exceptionCaught() event was fired, and it reached at the tail of the pipeline. It usually means the last handler in the pipeline did not handle the exception.", var2);
      }

      public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
         DefaultChannelPipeline.logger.debug("Discarded inbound message {} that reached at the tail of the pipeline. Please check your pipeline configuration.", var2);
         ReferenceCountUtil.release(var2);
      }

      public void channelReadComplete(ChannelHandlerContext var1) throws Exception {
      }
   }
}
