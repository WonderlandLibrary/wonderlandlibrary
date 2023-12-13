package io.netty.channel.nio;

import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ConnectTimeoutException;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketAddress;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class AbstractNioChannel extends AbstractChannel {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractNioChannel.class);
   private final SelectableChannel ch;
   protected final int readInterestOp;
   private volatile SelectionKey selectionKey;
   private volatile boolean inputShutdown;
   private ChannelPromise connectPromise;
   private ScheduledFuture connectTimeoutFuture;
   private SocketAddress requestedRemoteAddress;
   static final boolean $assertionsDisabled = !AbstractNioChannel.class.desiredAssertionStatus();

   protected AbstractNioChannel(Channel var1, SelectableChannel var2, int var3) {
      super(var1);
      this.ch = var2;
      this.readInterestOp = var3;

      try {
         var2.configureBlocking(false);
      } catch (IOException var7) {
         try {
            var2.close();
         } catch (IOException var6) {
            if (logger.isWarnEnabled()) {
               logger.warn("Failed to close a partially initialized socket.", (Throwable)var6);
            }
         }

         throw new ChannelException("Failed to enter non-blocking mode.", var7);
      }
   }

   public boolean isOpen() {
      return this.ch.isOpen();
   }

   public AbstractNioChannel.NioUnsafe unsafe() {
      return (AbstractNioChannel.NioUnsafe)super.unsafe();
   }

   protected SelectableChannel javaChannel() {
      return this.ch;
   }

   public NioEventLoop eventLoop() {
      return (NioEventLoop)super.eventLoop();
   }

   protected SelectionKey selectionKey() {
      if (!$assertionsDisabled && this.selectionKey == null) {
         throw new AssertionError();
      } else {
         return this.selectionKey;
      }
   }

   protected boolean isInputShutdown() {
      return this.inputShutdown;
   }

   void setInputShutdown() {
      this.inputShutdown = true;
   }

   protected boolean isCompatible(EventLoop var1) {
      return var1 instanceof NioEventLoop;
   }

   protected void doRegister() throws Exception {
      boolean var1 = false;

      while(true) {
         try {
            this.selectionKey = this.javaChannel().register(this.eventLoop().selector, 0, this);
            return;
         } catch (CancelledKeyException var3) {
            if (var1) {
               throw var3;
            }

            this.eventLoop().selectNow();
            var1 = true;
         }
      }
   }

   protected void doDeregister() throws Exception {
      this.eventLoop().cancel(this.selectionKey());
   }

   protected void doBeginRead() throws Exception {
      if (!this.inputShutdown) {
         SelectionKey var1 = this.selectionKey;
         if (var1.isValid()) {
            int var2 = var1.interestOps();
            if ((var2 & this.readInterestOp) == 0) {
               var1.interestOps(var2 | this.readInterestOp);
            }

         }
      }
   }

   protected abstract boolean doConnect(SocketAddress var1, SocketAddress var2) throws Exception;

   protected abstract void doFinishConnect() throws Exception;

   public Channel.Unsafe unsafe() {
      return this.unsafe();
   }

   public EventLoop eventLoop() {
      return this.eventLoop();
   }

   static ChannelPromise access$000(AbstractNioChannel var0) {
      return var0.connectPromise;
   }

   static ChannelPromise access$002(AbstractNioChannel var0, ChannelPromise var1) {
      return var0.connectPromise = var1;
   }

   static SocketAddress access$102(AbstractNioChannel var0, SocketAddress var1) {
      return var0.requestedRemoteAddress = var1;
   }

   static ScheduledFuture access$202(AbstractNioChannel var0, ScheduledFuture var1) {
      return var0.connectTimeoutFuture = var1;
   }

   static ScheduledFuture access$200(AbstractNioChannel var0) {
      return var0.connectTimeoutFuture;
   }

   static SocketAddress access$100(AbstractNioChannel var0) {
      return var0.requestedRemoteAddress;
   }

   protected abstract class AbstractNioUnsafe extends AbstractChannel.AbstractUnsafe implements AbstractNioChannel.NioUnsafe {
      static final boolean $assertionsDisabled = !AbstractNioChannel.class.desiredAssertionStatus();
      final AbstractNioChannel this$0;

      protected AbstractNioUnsafe(AbstractNioChannel var1) {
         super();
         this.this$0 = var1;
      }

      public SelectableChannel ch() {
         return this.this$0.javaChannel();
      }

      public void connect(SocketAddress var1, SocketAddress var2, ChannelPromise var3) {
         if (this.ensureOpen(var3)) {
            try {
               if (AbstractNioChannel.access$000(this.this$0) != null) {
                  throw new IllegalStateException("connection attempt already made");
               }

               boolean var7 = this.this$0.isActive();
               if (this.this$0.doConnect(var1, var2)) {
                  this.fulfillConnectPromise(var3, var7);
               } else {
                  AbstractNioChannel.access$002(this.this$0, var3);
                  AbstractNioChannel.access$102(this.this$0, var1);
                  int var8 = this.this$0.config().getConnectTimeoutMillis();
                  if (var8 > 0) {
                     AbstractNioChannel.access$202(this.this$0, this.this$0.eventLoop().schedule(new Runnable(this, var1) {
                        final SocketAddress val$remoteAddress;
                        final AbstractNioChannel.AbstractNioUnsafe this$1;

                        {
                           this.this$1 = var1;
                           this.val$remoteAddress = var2;
                        }

                        public void run() {
                           ChannelPromise var1 = AbstractNioChannel.access$000(this.this$1.this$0);
                           ConnectTimeoutException var2 = new ConnectTimeoutException("connection timed out: " + this.val$remoteAddress);
                           if (var1 != null && var1.tryFailure(var2)) {
                              this.this$1.close(this.this$1.voidPromise());
                           }

                        }
                     }, (long)var8, TimeUnit.MILLISECONDS));
                  }

                  var3.addListener(new ChannelFutureListener(this) {
                     final AbstractNioChannel.AbstractNioUnsafe this$1;

                     {
                        this.this$1 = var1;
                     }

                     public void operationComplete(ChannelFuture var1) throws Exception {
                        if (var1.isCancelled()) {
                           if (AbstractNioChannel.access$200(this.this$1.this$0) != null) {
                              AbstractNioChannel.access$200(this.this$1.this$0).cancel(false);
                           }

                           AbstractNioChannel.access$002(this.this$1.this$0, (ChannelPromise)null);
                           this.this$1.close(this.this$1.voidPromise());
                        }

                     }

                     public void operationComplete(Future var1) throws Exception {
                        this.operationComplete((ChannelFuture)var1);
                     }
                  });
               }
            } catch (Throwable var6) {
               Object var4 = var6;
               if (var6 instanceof ConnectException) {
                  ConnectException var5 = new ConnectException(var6.getMessage() + ": " + var1);
                  var5.setStackTrace(var6.getStackTrace());
                  var4 = var5;
               }

               var3.tryFailure((Throwable)var4);
               this.closeIfClosed();
            }

         }
      }

      private void fulfillConnectPromise(ChannelPromise var1, boolean var2) {
         if (var1 != null) {
            boolean var3 = var1.trySuccess();
            if (!var2 && this.this$0.isActive()) {
               this.this$0.pipeline().fireChannelActive();
            }

            if (!var3) {
               this.close(this.voidPromise());
            }

         }
      }

      private void fulfillConnectPromise(ChannelPromise var1, Throwable var2) {
         if (var1 == null) {
         }

         var1.tryFailure(var2);
         this.closeIfClosed();
      }

      public void finishConnect() {
         if (!$assertionsDisabled && !this.this$0.eventLoop().inEventLoop()) {
            throw new AssertionError();
         } else {
            try {
               boolean var5 = this.this$0.isActive();
               this.this$0.doFinishConnect();
               this.fulfillConnectPromise(AbstractNioChannel.access$000(this.this$0), var5);
            } catch (Throwable var4) {
               Object var1 = var4;
               if (var4 instanceof ConnectException) {
                  ConnectException var2 = new ConnectException(var4.getMessage() + ": " + AbstractNioChannel.access$100(this.this$0));
                  var2.setStackTrace(var4.getStackTrace());
                  var1 = var2;
               }

               this.fulfillConnectPromise(AbstractNioChannel.access$000(this.this$0), (Throwable)var1);
               if (AbstractNioChannel.access$200(this.this$0) != null) {
                  AbstractNioChannel.access$200(this.this$0).cancel(false);
               }

               AbstractNioChannel.access$002(this.this$0, (ChannelPromise)null);
               return;
            }

            if (AbstractNioChannel.access$200(this.this$0) != null) {
               AbstractNioChannel.access$200(this.this$0).cancel(false);
            }

            AbstractNioChannel.access$002(this.this$0, (ChannelPromise)null);
         }
      }

      protected void flush0() {
         if (this == false) {
            super.flush0();
         }
      }

      public void forceFlush() {
         super.flush0();
      }
   }

   public interface NioUnsafe extends Channel.Unsafe {
      SelectableChannel ch();

      void finishConnect();

      void read();

      void forceFlush();
   }
}
