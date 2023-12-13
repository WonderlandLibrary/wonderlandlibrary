package io.netty.channel.socket.oio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.oio.AbstractOioMessageChannel;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramChannelConfig;
import io.netty.channel.socket.DefaultDatagramChannelConfig;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Locale;

public class OioDatagramChannel extends AbstractOioMessageChannel implements DatagramChannel {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(OioDatagramChannel.class);
   private static final ChannelMetadata METADATA = new ChannelMetadata(true);
   private final MulticastSocket socket;
   private final DatagramChannelConfig config;
   private final DatagramPacket tmpPacket;
   private RecvByteBufAllocator.Handle allocHandle;

   private static MulticastSocket newSocket() {
      try {
         return new MulticastSocket((SocketAddress)null);
      } catch (Exception var1) {
         throw new ChannelException("failed to create a new socket", var1);
      }
   }

   public OioDatagramChannel() {
      this(newSocket());
   }

   public OioDatagramChannel(MulticastSocket var1) {
      super((Channel)null);
      this.tmpPacket = new DatagramPacket(EmptyArrays.EMPTY_BYTES, 0);
      boolean var2 = false;

      try {
         var1.setSoTimeout(1000);
         var1.setBroadcast(false);
         var2 = true;
      } catch (SocketException var5) {
         throw new ChannelException("Failed to configure the datagram socket timeout.", var5);
      }

      if (!var2) {
         var1.close();
      }

      this.socket = var1;
      this.config = new DefaultDatagramChannelConfig(this, var1);
   }

   public ChannelMetadata metadata() {
      return METADATA;
   }

   public DatagramChannelConfig config() {
      return this.config;
   }

   public boolean isConnected() {
      return this.socket.isConnected();
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

   public InetSocketAddress localAddress() {
      return (InetSocketAddress)super.localAddress();
   }

   public InetSocketAddress remoteAddress() {
      return (InetSocketAddress)super.remoteAddress();
   }

   protected void doConnect(SocketAddress var1, SocketAddress var2) throws Exception {
      if (var2 != null) {
         this.socket.bind(var2);
      }

      boolean var3 = false;
      this.socket.connect(var1);
      var3 = true;
      if (!var3) {
         try {
            this.socket.close();
         } catch (Throwable var7) {
            logger.warn("Failed to close a socket.", var7);
         }
      }

   }

   protected void doDisconnect() throws Exception {
      this.socket.disconnect();
   }

   protected void doClose() throws Exception {
      this.socket.close();
   }

   protected int doReadMessages(List var1) throws Exception {
      DatagramChannelConfig var2 = this.config();
      RecvByteBufAllocator.Handle var3 = this.allocHandle;
      if (var3 == null) {
         this.allocHandle = var3 = var2.getRecvByteBufAllocator().newHandle();
      }

      ByteBuf var4 = var2.getAllocator().heapBuffer(var3.guess());
      boolean var5 = true;

      byte var7;
      byte var8;
      try {
         this.tmpPacket.setData(var4.array(), var4.arrayOffset(), var4.capacity());
         this.socket.receive(this.tmpPacket);
         InetSocketAddress var6 = (InetSocketAddress)this.tmpPacket.getSocketAddress();
         if (var6 == null) {
            var6 = this.remoteAddress();
         }

         int var14 = this.tmpPacket.getLength();
         var3.record(var14);
         var1.add(new io.netty.channel.socket.DatagramPacket(var4.writerIndex(var14), this.localAddress(), var6));
         var5 = false;
         var8 = 1;
      } catch (SocketTimeoutException var10) {
         byte var13 = 0;
         if (var5) {
            var4.release();
         }

         return var13;
      } catch (SocketException var11) {
         if (!var11.getMessage().toLowerCase(Locale.US).contains("socket closed")) {
            throw var11;
         }

         var7 = -1;
         if (var5) {
            var4.release();
         }

         return var7;
      } catch (Throwable var12) {
         PlatformDependent.throwException(var12);
         var7 = -1;
         if (var5) {
            var4.release();
         }

         return var7;
      }

      if (var5) {
         var4.release();
      }

      return var8;
   }

   protected void doWrite(ChannelOutboundBuffer var1) throws Exception {
      while(true) {
         Object var2 = var1.current();
         if (var2 == null) {
            return;
         }

         Object var3;
         SocketAddress var5;
         if (var2 instanceof AddressedEnvelope) {
            AddressedEnvelope var6 = (AddressedEnvelope)var2;
            var5 = var6.recipient();
            var3 = var6.content();
         } else {
            var3 = var2;
            var5 = null;
         }

         ByteBuf var4;
         if (var3 instanceof ByteBufHolder) {
            var4 = ((ByteBufHolder)var3).content();
         } else {
            if (!(var3 instanceof ByteBuf)) {
               throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(var2));
            }

            var4 = (ByteBuf)var3;
         }

         int var8 = var4.readableBytes();
         if (var5 != null) {
            this.tmpPacket.setSocketAddress(var5);
         }

         if (var4.hasArray()) {
            this.tmpPacket.setData(var4.array(), var4.arrayOffset() + var4.readerIndex(), var8);
         } else {
            byte[] var7 = new byte[var8];
            var4.getBytes(var4.readerIndex(), var7);
            this.tmpPacket.setData(var7);
         }

         this.socket.send(this.tmpPacket);
         var1.remove();
      }
   }

   public ChannelFuture joinGroup(InetAddress var1) {
      return this.joinGroup(var1, this.newPromise());
   }

   public ChannelFuture joinGroup(InetAddress var1, ChannelPromise var2) {
      this.ensureBound();

      try {
         this.socket.joinGroup(var1);
         var2.setSuccess();
      } catch (IOException var4) {
         var2.setFailure(var4);
      }

      return var2;
   }

   public ChannelFuture joinGroup(InetSocketAddress var1, NetworkInterface var2) {
      return this.joinGroup(var1, var2, this.newPromise());
   }

   public ChannelFuture joinGroup(InetSocketAddress var1, NetworkInterface var2, ChannelPromise var3) {
      this.ensureBound();

      try {
         this.socket.joinGroup(var1, var2);
         var3.setSuccess();
      } catch (IOException var5) {
         var3.setFailure(var5);
      }

      return var3;
   }

   public ChannelFuture joinGroup(InetAddress var1, NetworkInterface var2, InetAddress var3) {
      return this.newFailedFuture(new UnsupportedOperationException());
   }

   public ChannelFuture joinGroup(InetAddress var1, NetworkInterface var2, InetAddress var3, ChannelPromise var4) {
      var4.setFailure(new UnsupportedOperationException());
      return var4;
   }

   private void ensureBound() {
      if (this == false) {
         throw new IllegalStateException(DatagramChannel.class.getName() + " must be bound to join a group.");
      }
   }

   public ChannelFuture leaveGroup(InetAddress var1) {
      return this.leaveGroup(var1, this.newPromise());
   }

   public ChannelFuture leaveGroup(InetAddress var1, ChannelPromise var2) {
      try {
         this.socket.leaveGroup(var1);
         var2.setSuccess();
      } catch (IOException var4) {
         var2.setFailure(var4);
      }

      return var2;
   }

   public ChannelFuture leaveGroup(InetSocketAddress var1, NetworkInterface var2) {
      return this.leaveGroup(var1, var2, this.newPromise());
   }

   public ChannelFuture leaveGroup(InetSocketAddress var1, NetworkInterface var2, ChannelPromise var3) {
      try {
         this.socket.leaveGroup(var1, var2);
         var3.setSuccess();
      } catch (IOException var5) {
         var3.setFailure(var5);
      }

      return var3;
   }

   public ChannelFuture leaveGroup(InetAddress var1, NetworkInterface var2, InetAddress var3) {
      return this.newFailedFuture(new UnsupportedOperationException());
   }

   public ChannelFuture leaveGroup(InetAddress var1, NetworkInterface var2, InetAddress var3, ChannelPromise var4) {
      var4.setFailure(new UnsupportedOperationException());
      return var4;
   }

   public ChannelFuture block(InetAddress var1, NetworkInterface var2, InetAddress var3) {
      return this.newFailedFuture(new UnsupportedOperationException());
   }

   public ChannelFuture block(InetAddress var1, NetworkInterface var2, InetAddress var3, ChannelPromise var4) {
      var4.setFailure(new UnsupportedOperationException());
      return var4;
   }

   public ChannelFuture block(InetAddress var1, InetAddress var2) {
      return this.newFailedFuture(new UnsupportedOperationException());
   }

   public ChannelFuture block(InetAddress var1, InetAddress var2, ChannelPromise var3) {
      var3.setFailure(new UnsupportedOperationException());
      return var3;
   }

   public SocketAddress remoteAddress() {
      return this.remoteAddress();
   }

   public SocketAddress localAddress() {
      return this.localAddress();
   }

   public ChannelConfig config() {
      return this.config();
   }
}
