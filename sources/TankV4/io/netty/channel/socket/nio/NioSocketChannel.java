package io.netty.channel.socket.nio;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.FileRegion;
import io.netty.channel.nio.AbstractNioByteChannel;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.socket.DefaultSocketChannelConfig;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.SocketChannelConfig;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.channels.SelectableChannel;

public class NioSocketChannel extends AbstractNioByteChannel implements SocketChannel {
   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
   private final SocketChannelConfig config;

   private static java.nio.channels.SocketChannel newSocket() {
      try {
         return java.nio.channels.SocketChannel.open();
      } catch (IOException var1) {
         throw new ChannelException("Failed to open a socket.", var1);
      }
   }

   public NioSocketChannel() {
      this(newSocket());
   }

   public NioSocketChannel(java.nio.channels.SocketChannel var1) {
      this((Channel)null, var1);
   }

   public NioSocketChannel(Channel var1, java.nio.channels.SocketChannel var2) {
      super(var1, var2);
      this.config = new DefaultSocketChannelConfig(this, var2.socket());
   }

   public ServerSocketChannel parent() {
      return (ServerSocketChannel)super.parent();
   }

   public ChannelMetadata metadata() {
      return METADATA;
   }

   public SocketChannelConfig config() {
      return this.config;
   }

   protected java.nio.channels.SocketChannel javaChannel() {
      return (java.nio.channels.SocketChannel)super.javaChannel();
   }

   public boolean isInputShutdown() {
      return super.isInputShutdown();
   }

   public InetSocketAddress localAddress() {
      return (InetSocketAddress)super.localAddress();
   }

   public InetSocketAddress remoteAddress() {
      return (InetSocketAddress)super.remoteAddress();
   }

   public boolean isOutputShutdown() {
      return this.javaChannel().socket().isOutputShutdown() || this != false;
   }

   public ChannelFuture shutdownOutput() {
      return this.shutdownOutput(this.newPromise());
   }

   public ChannelFuture shutdownOutput(ChannelPromise var1) {
      NioEventLoop var2 = this.eventLoop();
      if (var2.inEventLoop()) {
         try {
            this.javaChannel().socket().shutdownOutput();
            var1.setSuccess();
         } catch (Throwable var4) {
            var1.setFailure(var4);
         }
      } else {
         var2.execute(new Runnable(this, var1) {
            final ChannelPromise val$promise;
            final NioSocketChannel this$0;

            {
               this.this$0 = var1;
               this.val$promise = var2;
            }

            public void run() {
               this.this$0.shutdownOutput(this.val$promise);
            }
         });
      }

      return var1;
   }

   protected SocketAddress localAddress0() {
      return this.javaChannel().socket().getLocalSocketAddress();
   }

   protected SocketAddress remoteAddress0() {
      return this.javaChannel().socket().getRemoteSocketAddress();
   }

   protected void doBind(SocketAddress var1) throws Exception {
      this.javaChannel().socket().bind(var1);
   }

   protected boolean doConnect(SocketAddress var1, SocketAddress var2) throws Exception {
      if (var2 != null) {
         this.javaChannel().socket().bind(var2);
      }

      boolean var3 = false;
      boolean var4 = this.javaChannel().connect(var1);
      if (!var4) {
         this.selectionKey().interestOps(8);
      }

      var3 = true;
      if (!var3) {
         this.doClose();
      }

      return var4;
   }

   protected void doFinishConnect() throws Exception {
      if (!this.javaChannel().finishConnect()) {
         throw new Error();
      }
   }

   protected void doDisconnect() throws Exception {
      this.doClose();
   }

   protected void doClose() throws Exception {
      this.javaChannel().close();
   }

   protected int doReadBytes(ByteBuf var1) throws Exception {
      return var1.writeBytes((ScatteringByteChannel)this.javaChannel(), var1.writableBytes());
   }

   protected int doWriteBytes(ByteBuf var1) throws Exception {
      int var2 = var1.readableBytes();
      int var3 = var1.readBytes((GatheringByteChannel)this.javaChannel(), var2);
      return var3;
   }

   protected long doWriteFileRegion(FileRegion var1) throws Exception {
      long var2 = var1.transfered();
      long var4 = var1.transferTo(this.javaChannel(), var2);
      return var4;
   }

   protected void doWrite(ChannelOutboundBuffer var1) throws Exception {
      while(true) {
         int var2 = var1.size();
         if (var2 <= 1) {
            super.doWrite(var1);
            return;
         }

         ByteBuffer[] var3 = var1.nioBuffers();
         if (var3 == null) {
            super.doWrite(var1);
            return;
         }

         int var4 = var1.nioBufferCount();
         long var5 = var1.nioBufferSize();
         java.nio.channels.SocketChannel var7 = this.javaChannel();
         long var8 = 0L;
         boolean var10 = false;
         boolean var11 = false;

         int var12;
         for(var12 = this.config().getWriteSpinCount() - 1; var12 >= 0; --var12) {
            long var13 = var7.write(var3, 0, var4);
            if (var13 == 0L) {
               var11 = true;
               break;
            }

            var5 -= var13;
            var8 += var13;
            if (var5 == 0L) {
               var10 = true;
               break;
            }
         }

         if (var10) {
            for(var12 = var2; var12 > 0; --var12) {
               var1.remove();
            }

            if (!var1.isEmpty()) {
               continue;
            }

            this.clearOpWrite();
         } else {
            for(var12 = var2; var12 > 0; --var12) {
               ByteBuf var16 = (ByteBuf)var1.current();
               int var14 = var16.readerIndex();
               int var15 = var16.writerIndex() - var14;
               if ((long)var15 >= var8) {
                  if ((long)var15 > var8) {
                     var16.readerIndex(var14 + (int)var8);
                     var1.progress(var8);
                  } else {
                     var1.progress((long)var15);
                     var1.remove();
                  }
                  break;
               }

               var1.progress((long)var15);
               var1.remove();
               var8 -= (long)var15;
            }

            this.incompleteWrite(var11);
         }

         return;
      }
   }

   protected SelectableChannel javaChannel() {
      return this.javaChannel();
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
}
