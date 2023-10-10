package io.netty.channel.socket.oio;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ConnectTimeoutException;
import io.netty.channel.EventLoop;
import io.netty.channel.oio.OioByteStreamChannel;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

public class OioSocketChannel extends OioByteStreamChannel implements SocketChannel {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(OioSocketChannel.class);
   private final Socket socket;
   private final OioSocketChannelConfig config;

   public OioSocketChannel() {
      this(new Socket());
   }

   public OioSocketChannel(Socket var1) {
      this((Channel)null, var1);
   }

   public OioSocketChannel(Channel var1, Socket var2) {
      super(var1);
      this.socket = var2;
      this.config = new DefaultOioSocketChannelConfig(this, var2);
      boolean var3 = false;

      try {
         if (var2.isConnected()) {
            this.activate(var2.getInputStream(), var2.getOutputStream());
         }

         var2.setSoTimeout(1000);
         var3 = true;
      } catch (Exception var8) {
         throw new ChannelException("failed to initialize a socket", var8);
      }

      if (!var3) {
         try {
            var2.close();
         } catch (IOException var7) {
            logger.warn("Failed to close a socket.", (Throwable)var7);
         }
      }

   }

   public ServerSocketChannel parent() {
      return (ServerSocketChannel)super.parent();
   }

   public OioSocketChannelConfig config() {
      return this.config;
   }

   public boolean isOpen() {
      return !this.socket.isClosed();
   }

   public boolean isInputShutdown() {
      return super.isInputShutdown();
   }

   public boolean isOutputShutdown() {
      return this.socket.isOutputShutdown() || this == false;
   }

   public ChannelFuture shutdownOutput() {
      return this.shutdownOutput(this.newPromise());
   }

   protected int doReadBytes(ByteBuf var1) throws Exception {
      if (this.socket.isClosed()) {
         return -1;
      } else {
         try {
            return super.doReadBytes(var1);
         } catch (SocketTimeoutException var3) {
            return 0;
         }
      }
   }

   public ChannelFuture shutdownOutput(ChannelPromise var1) {
      EventLoop var2 = this.eventLoop();
      if (var2.inEventLoop()) {
         try {
            this.socket.shutdownOutput();
            var1.setSuccess();
         } catch (Throwable var4) {
            var1.setFailure(var4);
         }
      } else {
         var2.execute(new Runnable(this, var1) {
            final ChannelPromise val$future;
            final OioSocketChannel this$0;

            {
               this.this$0 = var1;
               this.val$future = var2;
            }

            public void run() {
               this.this$0.shutdownOutput(this.val$future);
            }
         });
      }

      return var1;
   }

   public InetSocketAddress localAddress() {
      return (InetSocketAddress)super.localAddress();
   }

   public InetSocketAddress remoteAddress() {
      return (InetSocketAddress)super.remoteAddress();
   }

   protected SocketAddress localAddress0() {
      return this.socket.getLocalSocketAddress();
   }

   protected SocketAddress remoteAddress0() {
      return this.socket.getRemoteSocketAddress();
   }

   protected void doBind(SocketAddress var1) throws Exception {
      this.socket.bind(var1);
   }

   protected void doConnect(SocketAddress var1, SocketAddress var2) throws Exception {
      if (var2 != null) {
         this.socket.bind(var2);
      }

      boolean var3 = false;

      try {
         this.socket.connect(var1, this.config().getConnectTimeoutMillis());
         this.activate(this.socket.getInputStream(), this.socket.getOutputStream());
         var3 = true;
      } catch (SocketTimeoutException var7) {
         ConnectTimeoutException var5 = new ConnectTimeoutException("connection timed out: " + var1);
         var5.setStackTrace(var7.getStackTrace());
         throw var5;
      }

      if (!var3) {
         this.doClose();
      }

   }

   protected void doDisconnect() throws Exception {
      this.doClose();
   }

   protected void doClose() throws Exception {
      this.socket.close();
   }

   protected boolean checkInputShutdown() {
      if (this.isInputShutdown()) {
         try {
            Thread.sleep((long)this.config().getSoTimeout());
         } catch (Throwable var2) {
         }

         return true;
      } else {
         return false;
      }
   }

   public SocketAddress remoteAddress() {
      return this.remoteAddress();
   }

   public SocketAddress localAddress() {
      return this.localAddress();
   }

   public Channel parent() {
      return this.parent();
   }

   public ChannelConfig config() {
      return this.config();
   }

   public SocketChannelConfig config() {
      return this.config();
   }
}
