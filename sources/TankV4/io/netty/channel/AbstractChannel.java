package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import io.netty.util.DefaultAttributeMap;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.ThreadLocalRandom;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NotYetConnectedException;
import java.util.concurrent.RejectedExecutionException;

public abstract class AbstractChannel extends DefaultAttributeMap implements Channel {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractChannel.class);
   static final ClosedChannelException CLOSED_CHANNEL_EXCEPTION = new ClosedChannelException();
   static final NotYetConnectedException NOT_YET_CONNECTED_EXCEPTION = new NotYetConnectedException();
   private MessageSizeEstimator.Handle estimatorHandle;
   private final Channel parent;
   private final long hashCode = ThreadLocalRandom.current().nextLong();
   private final Channel.Unsafe unsafe;
   private final DefaultChannelPipeline pipeline;
   private final ChannelFuture succeededFuture = new SucceededChannelFuture(this, (EventExecutor)null);
   private final VoidChannelPromise voidPromise = new VoidChannelPromise(this, true);
   private final VoidChannelPromise unsafeVoidPromise = new VoidChannelPromise(this, false);
   private final AbstractChannel.CloseFuture closeFuture = new AbstractChannel.CloseFuture(this);
   private volatile SocketAddress localAddress;
   private volatile SocketAddress remoteAddress;
   private volatile EventLoop eventLoop;
   private volatile boolean registered;
   private boolean strValActive;
   private String strVal;

   protected AbstractChannel(Channel var1) {
      this.parent = var1;
      this.unsafe = this.newUnsafe();
      this.pipeline = new DefaultChannelPipeline(this);
   }

   public boolean isWritable() {
      ChannelOutboundBuffer var1 = this.unsafe.outboundBuffer();
      return var1 != null && var1.getWritable();
   }

   public Channel parent() {
      return this.parent;
   }

   public ChannelPipeline pipeline() {
      return this.pipeline;
   }

   public ByteBufAllocator alloc() {
      return this.config().getAllocator();
   }

   public EventLoop eventLoop() {
      EventLoop var1 = this.eventLoop;
      if (var1 == null) {
         throw new IllegalStateException("channel not registered to an event loop");
      } else {
         return var1;
      }
   }

   public SocketAddress localAddress() {
      SocketAddress var1 = this.localAddress;
      if (var1 == null) {
         try {
            this.localAddress = var1 = this.unsafe().localAddress();
         } catch (Throwable var3) {
            return null;
         }
      }

      return var1;
   }

   protected void invalidateLocalAddress() {
      this.localAddress = null;
   }

   public SocketAddress remoteAddress() {
      SocketAddress var1 = this.remoteAddress;
      if (var1 == null) {
         try {
            this.remoteAddress = var1 = this.unsafe().remoteAddress();
         } catch (Throwable var3) {
            return null;
         }
      }

      return var1;
   }

   protected void invalidateRemoteAddress() {
      this.remoteAddress = null;
   }

   public boolean isRegistered() {
      return this.registered;
   }

   public ChannelFuture bind(SocketAddress var1) {
      return this.pipeline.bind(var1);
   }

   public ChannelFuture connect(SocketAddress var1) {
      return this.pipeline.connect(var1);
   }

   public ChannelFuture connect(SocketAddress var1, SocketAddress var2) {
      return this.pipeline.connect(var1, var2);
   }

   public ChannelFuture disconnect() {
      return this.pipeline.disconnect();
   }

   public ChannelFuture close() {
      return this.pipeline.close();
   }

   public ChannelFuture deregister() {
      return this.pipeline.deregister();
   }

   public Channel flush() {
      this.pipeline.flush();
      return this;
   }

   public ChannelFuture bind(SocketAddress var1, ChannelPromise var2) {
      return this.pipeline.bind(var1, var2);
   }

   public ChannelFuture connect(SocketAddress var1, ChannelPromise var2) {
      return this.pipeline.connect(var1, var2);
   }

   public ChannelFuture connect(SocketAddress var1, SocketAddress var2, ChannelPromise var3) {
      return this.pipeline.connect(var1, var2, var3);
   }

   public ChannelFuture disconnect(ChannelPromise var1) {
      return this.pipeline.disconnect(var1);
   }

   public ChannelFuture close(ChannelPromise var1) {
      return this.pipeline.close(var1);
   }

   public ChannelFuture deregister(ChannelPromise var1) {
      return this.pipeline.deregister(var1);
   }

   public Channel read() {
      this.pipeline.read();
      return this;
   }

   public ChannelFuture write(Object var1) {
      return this.pipeline.write(var1);
   }

   public ChannelFuture write(Object var1, ChannelPromise var2) {
      return this.pipeline.write(var1, var2);
   }

   public ChannelFuture writeAndFlush(Object var1) {
      return this.pipeline.writeAndFlush(var1);
   }

   public ChannelFuture writeAndFlush(Object var1, ChannelPromise var2) {
      return this.pipeline.writeAndFlush(var1, var2);
   }

   public ChannelPromise newPromise() {
      return new DefaultChannelPromise(this);
   }

   public ChannelProgressivePromise newProgressivePromise() {
      return new DefaultChannelProgressivePromise(this);
   }

   public ChannelFuture newSucceededFuture() {
      return this.succeededFuture;
   }

   public ChannelFuture newFailedFuture(Throwable var1) {
      return new FailedChannelFuture(this, (EventExecutor)null, var1);
   }

   public ChannelFuture closeFuture() {
      return this.closeFuture;
   }

   public Channel.Unsafe unsafe() {
      return this.unsafe;
   }

   protected abstract AbstractChannel.AbstractUnsafe newUnsafe();

   public final int hashCode() {
      return (int)this.hashCode;
   }

   public final boolean equals(Object var1) {
      return this == var1;
   }

   public final int compareTo(Channel var1) {
      if (this == var1) {
         return 0;
      } else {
         long var2 = this.hashCode - (long)var1.hashCode();
         if (var2 > 0L) {
            return 1;
         } else if (var2 < 0L) {
            return -1;
         } else {
            var2 = (long)(System.identityHashCode(this) - System.identityHashCode(var1));
            if (var2 != 0L) {
               return (int)var2;
            } else {
               throw new Error();
            }
         }
      }
   }

   public String toString() {
      boolean var1 = this.isActive();
      if (this.strValActive == var1 && this.strVal != null) {
         return this.strVal;
      } else {
         SocketAddress var2 = this.remoteAddress();
         SocketAddress var3 = this.localAddress();
         if (var2 != null) {
            SocketAddress var4;
            SocketAddress var5;
            if (this.parent == null) {
               var4 = var3;
               var5 = var2;
            } else {
               var4 = var2;
               var5 = var3;
            }

            this.strVal = String.format("[id: 0x%08x, %s %s %s]", (int)this.hashCode, var4, var1 ? "=>" : ":>", var5);
         } else if (var3 != null) {
            this.strVal = String.format("[id: 0x%08x, %s]", (int)this.hashCode, var3);
         } else {
            this.strVal = String.format("[id: 0x%08x]", (int)this.hashCode);
         }

         this.strValActive = var1;
         return this.strVal;
      }
   }

   public final ChannelPromise voidPromise() {
      return this.voidPromise;
   }

   final MessageSizeEstimator.Handle estimatorHandle() {
      if (this.estimatorHandle == null) {
         this.estimatorHandle = this.config().getMessageSizeEstimator().newHandle();
      }

      return this.estimatorHandle;
   }

   protected abstract boolean isCompatible(EventLoop var1);

   protected abstract SocketAddress localAddress0();

   protected abstract SocketAddress remoteAddress0();

   protected void doRegister() throws Exception {
   }

   protected abstract void doBind(SocketAddress var1) throws Exception;

   protected abstract void doDisconnect() throws Exception;

   protected abstract void doClose() throws Exception;

   protected void doDeregister() throws Exception {
   }

   protected abstract void doBeginRead() throws Exception;

   protected abstract void doWrite(ChannelOutboundBuffer var1) throws Exception;

   protected static void checkEOF(FileRegion var0) throws IOException {
      if (var0.transfered() < var0.count()) {
         throw new EOFException("Expected to be able to write " + var0.count() + " bytes, but only wrote " + var0.transfered());
      }
   }

   public int compareTo(Object var1) {
      return this.compareTo((Channel)var1);
   }

   static EventLoop access$002(AbstractChannel var0, EventLoop var1) {
      return var0.eventLoop = var1;
   }

   static InternalLogger access$200() {
      return logger;
   }

   static AbstractChannel.CloseFuture access$300(AbstractChannel var0) {
      return var0.closeFuture;
   }

   static boolean access$402(AbstractChannel var0, boolean var1) {
      return var0.registered = var1;
   }

   static DefaultChannelPipeline access$500(AbstractChannel var0) {
      return var0.pipeline;
   }

   static boolean access$400(AbstractChannel var0) {
      return var0.registered;
   }

   static VoidChannelPromise access$600(AbstractChannel var0) {
      return var0.unsafeVoidPromise;
   }

   static {
      CLOSED_CHANNEL_EXCEPTION.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
      NOT_YET_CONNECTED_EXCEPTION.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
   }

   static final class CloseFuture extends DefaultChannelPromise {
      CloseFuture(AbstractChannel var1) {
         super(var1);
      }

      public ChannelPromise setSuccess() {
         throw new IllegalStateException();
      }

      public ChannelPromise setFailure(Throwable var1) {
         throw new IllegalStateException();
      }

      public boolean trySuccess() {
         throw new IllegalStateException();
      }

      public boolean tryFailure(Throwable var1) {
         throw new IllegalStateException();
      }

      boolean setClosed() {
         return super.trySuccess();
      }

      public Promise setFailure(Throwable var1) {
         return this.setFailure(var1);
      }
   }

   protected abstract class AbstractUnsafe implements Channel.Unsafe {
      private ChannelOutboundBuffer outboundBuffer;
      private boolean inFlush0;
      final AbstractChannel this$0;

      protected AbstractUnsafe(AbstractChannel var1) {
         this.this$0 = var1;
         this.outboundBuffer = ChannelOutboundBuffer.newInstance(this.this$0);
      }

      public final ChannelOutboundBuffer outboundBuffer() {
         return this.outboundBuffer;
      }

      public final SocketAddress localAddress() {
         return this.this$0.localAddress0();
      }

      public final SocketAddress remoteAddress() {
         return this.this$0.remoteAddress0();
      }

      public final void register(EventLoop var1, ChannelPromise var2) {
         if (var1 == null) {
            throw new NullPointerException("eventLoop");
         } else if (this.this$0.isRegistered()) {
            var2.setFailure(new IllegalStateException("registered to an event loop already"));
         } else if (!this.this$0.isCompatible(var1)) {
            var2.setFailure(new IllegalStateException("incompatible event loop type: " + var1.getClass().getName()));
         } else {
            AbstractChannel.access$002(this.this$0, var1);
            if (var1.inEventLoop()) {
               this.register0(var2);
            } else {
               try {
                  var1.execute(new Runnable(this, var2) {
                     final ChannelPromise val$promise;
                     final AbstractChannel.AbstractUnsafe this$1;

                     {
                        this.this$1 = var1;
                        this.val$promise = var2;
                     }

                     public void run() {
                        AbstractChannel.AbstractUnsafe.access$100(this.this$1, this.val$promise);
                     }
                  });
               } catch (Throwable var4) {
                  AbstractChannel.access$200().warn("Force-closing a channel whose registration task was not accepted by an event loop: {}", this.this$0, var4);
                  this.closeForcibly();
                  AbstractChannel.access$300(this.this$0).setClosed();
                  var2.setFailure(var4);
               }
            }

         }
      }

      private void register0(ChannelPromise var1) {
         try {
            if (var1 != false) {
               return;
            }

            this.this$0.doRegister();
            AbstractChannel.access$402(this.this$0, true);
            var1.setSuccess();
            AbstractChannel.access$500(this.this$0).fireChannelRegistered();
            if (this.this$0.isActive()) {
               AbstractChannel.access$500(this.this$0).fireChannelActive();
            }
         } catch (Throwable var3) {
            this.closeForcibly();
            AbstractChannel.access$300(this.this$0).setClosed();
            if (!var1.tryFailure(var3)) {
               AbstractChannel.access$200().warn("Tried to fail the registration promise, but it is complete already. Swallowing the cause of the registration failure:", var3);
            }
         }

      }

      public final void bind(SocketAddress var1, ChannelPromise var2) {
         if (var2 == false) {
            if (!PlatformDependent.isWindows() && !PlatformDependent.isRoot() && Boolean.TRUE.equals(this.this$0.config().getOption(ChannelOption.SO_BROADCAST)) && var1 instanceof InetSocketAddress && !((InetSocketAddress)var1).getAddress().isAnyLocalAddress()) {
               AbstractChannel.access$200().warn("A non-root user can't receive a broadcast packet if the socket is not bound to a wildcard address; binding to a non-wildcard address (" + var1 + ") anyway as requested.");
            }

            boolean var3 = this.this$0.isActive();

            try {
               this.this$0.doBind(var1);
            } catch (Throwable var5) {
               var2.setFailure(var5);
               this.closeIfClosed();
               return;
            }

            var2.setSuccess();
            if (!var3 && this.this$0.isActive()) {
               this.invokeLater(new Runnable(this) {
                  final AbstractChannel.AbstractUnsafe this$1;

                  {
                     this.this$1 = var1;
                  }

                  public void run() {
                     AbstractChannel.access$500(this.this$1.this$0).fireChannelActive();
                  }
               });
            }

         }
      }

      public final void disconnect(ChannelPromise var1) {
         boolean var2 = this.this$0.isActive();

         try {
            this.this$0.doDisconnect();
         } catch (Throwable var4) {
            var1.setFailure(var4);
            this.closeIfClosed();
            return;
         }

         var1.setSuccess();
         if (var2 && !this.this$0.isActive()) {
            this.invokeLater(new Runnable(this) {
               final AbstractChannel.AbstractUnsafe this$1;

               {
                  this.this$1 = var1;
               }

               public void run() {
                  AbstractChannel.access$500(this.this$1.this$0).fireChannelInactive();
               }
            });
         }

         this.closeIfClosed();
      }

      public final void close(ChannelPromise var1) {
         if (this.inFlush0) {
            this.invokeLater(new Runnable(this, var1) {
               final ChannelPromise val$promise;
               final AbstractChannel.AbstractUnsafe this$1;

               {
                  this.this$1 = var1;
                  this.val$promise = var2;
               }

               public void run() {
                  this.this$1.close(this.val$promise);
               }
            });
         } else if (AbstractChannel.access$300(this.this$0).isDone()) {
            var1.setSuccess();
         } else {
            boolean var2 = this.this$0.isActive();
            ChannelOutboundBuffer var3 = this.outboundBuffer;
            this.outboundBuffer = null;

            try {
               this.this$0.doClose();
               AbstractChannel.access$300(this.this$0).setClosed();
               var1.setSuccess();
            } catch (Throwable var6) {
               AbstractChannel.access$300(this.this$0).setClosed();
               var1.setFailure(var6);
            }

            var3.failFlushed(AbstractChannel.CLOSED_CHANNEL_EXCEPTION);
            var3.close(AbstractChannel.CLOSED_CHANNEL_EXCEPTION);
            if (var2 && !this.this$0.isActive()) {
               this.invokeLater(new Runnable(this) {
                  final AbstractChannel.AbstractUnsafe this$1;

                  {
                     this.this$1 = var1;
                  }

                  public void run() {
                     AbstractChannel.access$500(this.this$1.this$0).fireChannelInactive();
                  }
               });
            }

            this.deregister(this.voidPromise());
         }
      }

      public final void closeForcibly() {
         try {
            this.this$0.doClose();
         } catch (Exception var2) {
            AbstractChannel.access$200().warn("Failed to close a channel.", (Throwable)var2);
         }

      }

      public final void deregister(ChannelPromise var1) {
         if (!AbstractChannel.access$400(this.this$0)) {
            var1.setSuccess();
         } else {
            try {
               this.this$0.doDeregister();
            } catch (Throwable var4) {
               AbstractChannel.access$200().warn("Unexpected exception occurred while deregistering a channel.", var4);
               if (AbstractChannel.access$400(this.this$0)) {
                  AbstractChannel.access$402(this.this$0, false);
                  var1.setSuccess();
                  this.invokeLater(new Runnable(this) {
                     final AbstractChannel.AbstractUnsafe this$1;

                     {
                        this.this$1 = var1;
                     }

                     public void run() {
                        AbstractChannel.access$500(this.this$1.this$0).fireChannelUnregistered();
                     }
                  });
               } else {
                  var1.setSuccess();
               }

               return;
            }

            if (AbstractChannel.access$400(this.this$0)) {
               AbstractChannel.access$402(this.this$0, false);
               var1.setSuccess();
               this.invokeLater(new Runnable(this) {
                  final AbstractChannel.AbstractUnsafe this$1;

                  {
                     this.this$1 = var1;
                  }

                  public void run() {
                     AbstractChannel.access$500(this.this$1.this$0).fireChannelUnregistered();
                  }
               });
            } else {
               var1.setSuccess();
            }

         }
      }

      public void beginRead() {
         if (this.this$0.isActive()) {
            try {
               this.this$0.doBeginRead();
            } catch (Exception var2) {
               this.invokeLater(new Runnable(this, var2) {
                  final Exception val$e;
                  final AbstractChannel.AbstractUnsafe this$1;

                  {
                     this.this$1 = var1;
                     this.val$e = var2;
                  }

                  public void run() {
                     AbstractChannel.access$500(this.this$1.this$0).fireExceptionCaught(this.val$e);
                  }
               });
               this.close(this.voidPromise());
            }

         }
      }

      public void write(Object var1, ChannelPromise var2) {
         if (!this.this$0.isActive()) {
            if (this.this$0.isOpen()) {
               var2.tryFailure(AbstractChannel.NOT_YET_CONNECTED_EXCEPTION);
            } else {
               var2.tryFailure(AbstractChannel.CLOSED_CHANNEL_EXCEPTION);
            }

            ReferenceCountUtil.release(var1);
         } else {
            this.outboundBuffer.addMessage(var1, var2);
         }

      }

      public void flush() {
         ChannelOutboundBuffer var1 = this.outboundBuffer;
         if (var1 != null) {
            var1.addFlush();
            this.flush0();
         }
      }

      protected void flush0() {
         if (!this.inFlush0) {
            ChannelOutboundBuffer var1 = this.outboundBuffer;
            if (var1 != null && !var1.isEmpty()) {
               this.inFlush0 = true;
               if (!this.this$0.isActive()) {
                  if (this.this$0.isOpen()) {
                     var1.failFlushed(AbstractChannel.NOT_YET_CONNECTED_EXCEPTION);
                  } else {
                     var1.failFlushed(AbstractChannel.CLOSED_CHANNEL_EXCEPTION);
                  }

                  this.inFlush0 = false;
               } else {
                  try {
                     this.this$0.doWrite(var1);
                  } catch (Throwable var4) {
                     var1.failFlushed(var4);
                     if (var4 instanceof IOException && this.this$0.config().isAutoClose()) {
                        this.close(this.voidPromise());
                     }

                     this.inFlush0 = false;
                     return;
                  }

                  this.inFlush0 = false;
               }
            }
         }
      }

      public ChannelPromise voidPromise() {
         return AbstractChannel.access$600(this.this$0);
      }

      protected final void closeIfClosed() {
         if (!this.this$0.isOpen()) {
            this.close(this.voidPromise());
         }
      }

      private void invokeLater(Runnable var1) {
         try {
            this.this$0.eventLoop().execute(var1);
         } catch (RejectedExecutionException var3) {
            AbstractChannel.access$200().warn("Can't invoke task later as EventLoop rejected it", (Throwable)var3);
         }

      }

      static void access$100(AbstractChannel.AbstractUnsafe var0, ChannelPromise var1) {
         var0.register0(var1);
      }
   }
}
