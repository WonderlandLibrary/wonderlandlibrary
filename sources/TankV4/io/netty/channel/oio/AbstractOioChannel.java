package io.netty.channel.oio;

import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.channel.ThreadPerChannelEventLoop;
import java.net.ConnectException;
import java.net.SocketAddress;

public abstract class AbstractOioChannel extends AbstractChannel {
   protected static final int SO_TIMEOUT = 1000;
   private boolean readInProgress;
   private final Runnable readTask = new Runnable(this) {
      final AbstractOioChannel this$0;

      {
         this.this$0 = var1;
      }

      public void run() {
         AbstractOioChannel.access$002(this.this$0, false);
         this.this$0.doRead();
      }
   };

   protected AbstractOioChannel(Channel var1) {
      super(var1);
   }

   protected AbstractChannel.AbstractUnsafe newUnsafe() {
      return new AbstractOioChannel.DefaultOioUnsafe(this);
   }

   protected boolean isCompatible(EventLoop var1) {
      return var1 instanceof ThreadPerChannelEventLoop;
   }

   protected abstract void doConnect(SocketAddress var1, SocketAddress var2) throws Exception;

   protected void doBeginRead() throws Exception {
      if (!this.readInProgress) {
         this.readInProgress = true;
         this.eventLoop().execute(this.readTask);
      }
   }

   protected abstract void doRead();

   static boolean access$002(AbstractOioChannel var0, boolean var1) {
      return var0.readInProgress = var1;
   }

   private final class DefaultOioUnsafe extends AbstractChannel.AbstractUnsafe {
      final AbstractOioChannel this$0;

      private DefaultOioUnsafe(AbstractOioChannel var1) {
         super();
         this.this$0 = var1;
      }

      public void connect(SocketAddress var1, SocketAddress var2, ChannelPromise var3) {
         if (this.ensureOpen(var3)) {
            if (!var3.setUncancellable()) {
               this.close(this.voidPromise());
            } else {
               try {
                  boolean var7 = this.this$0.isActive();
                  this.this$0.doConnect(var1, var2);
                  var3.setSuccess();
                  if (!var7 && this.this$0.isActive()) {
                     this.this$0.pipeline().fireChannelActive();
                  }
               } catch (Throwable var6) {
                  Object var4 = var6;
                  if (var6 instanceof ConnectException) {
                     ConnectException var5 = new ConnectException(var6.getMessage() + ": " + var1);
                     var5.setStackTrace(var6.getStackTrace());
                     var4 = var5;
                  }

                  var3.setFailure((Throwable)var4);
                  this.closeIfClosed();
               }

            }
         }
      }

      DefaultOioUnsafe(AbstractOioChannel var1, Object var2) {
         this(var1);
      }
   }
}
