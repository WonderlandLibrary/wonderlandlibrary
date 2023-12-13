package io.netty.channel.socket.nio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultAddressedEnvelope;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.nio.AbstractNioMessageChannel;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramChannelConfig;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.MembershipKey;
import java.nio.channels.SelectableChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class NioDatagramChannel extends AbstractNioMessageChannel implements DatagramChannel {
   private static final ChannelMetadata METADATA = new ChannelMetadata(true);
   private final DatagramChannelConfig config;
   private final Map memberships;
   private RecvByteBufAllocator.Handle allocHandle;

   private static java.nio.channels.DatagramChannel newSocket() {
      try {
         return java.nio.channels.DatagramChannel.open();
      } catch (IOException var1) {
         throw new ChannelException("Failed to open a socket.", var1);
      }
   }

   private static java.nio.channels.DatagramChannel newSocket(InternetProtocolFamily var0) {
      if (var0 == null) {
         return newSocket();
      } else if (PlatformDependent.javaVersion() < 7) {
         throw new UnsupportedOperationException();
      } else {
         try {
            return java.nio.channels.DatagramChannel.open(ProtocolFamilyConverter.convert(var0));
         } catch (IOException var2) {
            throw new ChannelException("Failed to open a socket.", var2);
         }
      }
   }

   public NioDatagramChannel() {
      this(newSocket());
   }

   public NioDatagramChannel(InternetProtocolFamily var1) {
      this(newSocket(var1));
   }

   public NioDatagramChannel(java.nio.channels.DatagramChannel var1) {
      super((Channel)null, var1, 1);
      this.memberships = new HashMap();
      this.config = new NioDatagramChannelConfig(this, var1);
   }

   public ChannelMetadata metadata() {
      return METADATA;
   }

   public DatagramChannelConfig config() {
      return this.config;
   }

   public boolean isActive() {
      java.nio.channels.DatagramChannel var1 = this.javaChannel();
      return var1.isOpen() && ((Boolean)this.config.getOption(ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION) && this.isRegistered() || var1.socket().isBound());
   }

   public boolean isConnected() {
      return this.javaChannel().isConnected();
   }

   protected java.nio.channels.DatagramChannel javaChannel() {
      return (java.nio.channels.DatagramChannel)super.javaChannel();
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
      this.javaChannel().connect(var1);
      var3 = true;
      boolean var4 = true;
      if (!var3) {
         this.doClose();
      }

      return var4;
   }

   protected void doFinishConnect() throws Exception {
      throw new Error();
   }

   protected void doDisconnect() throws Exception {
      this.javaChannel().disconnect();
   }

   protected void doClose() throws Exception {
      this.javaChannel().close();
   }

   protected int doReadMessages(List var1) throws Exception {
      java.nio.channels.DatagramChannel var2 = this.javaChannel();
      DatagramChannelConfig var3 = this.config();
      RecvByteBufAllocator.Handle var4 = this.allocHandle;
      if (var4 == null) {
         this.allocHandle = var4 = var3.getRecvByteBufAllocator().newHandle();
      }

      ByteBuf var5 = var4.allocate(var3.getAllocator());
      boolean var6 = true;

      byte var14;
      label46: {
         byte var11;
         try {
            ByteBuffer var7 = var5.internalNioBuffer(var5.writerIndex(), var5.writableBytes());
            int var15 = var7.position();
            InetSocketAddress var9 = (InetSocketAddress)var2.receive(var7);
            if (var9 == null) {
               var14 = 0;
               break label46;
            }

            int var10 = var7.position() - var15;
            var5.writerIndex(var5.writerIndex() + var10);
            var4.record(var10);
            var1.add(new DatagramPacket(var5, this.localAddress(), var9));
            var6 = false;
            var11 = 1;
         } catch (Throwable var13) {
            PlatformDependent.throwException(var13);
            byte var8 = -1;
            if (var6) {
               var5.release();
            }

            return var8;
         }

         if (var6) {
            var5.release();
         }

         return var11;
      }

      if (var6) {
         var5.release();
      }

      return var14;
   }

   protected boolean doWriteMessage(Object var1, ChannelOutboundBuffer var2) throws Exception {
      Object var3;
      SocketAddress var4;
      if (var1 instanceof AddressedEnvelope) {
         AddressedEnvelope var6 = (AddressedEnvelope)var1;
         var4 = var6.recipient();
         var3 = var6.content();
      } else {
         var3 = var1;
         var4 = null;
      }

      ByteBuf var5;
      if (var3 instanceof ByteBufHolder) {
         var5 = ((ByteBufHolder)var3).content();
      } else {
         if (!(var3 instanceof ByteBuf)) {
            throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(var1));
         }

         var5 = (ByteBuf)var3;
      }

      int var12 = var5.readableBytes();
      if (var12 == 0) {
         return true;
      } else {
         ByteBufAllocator var7 = this.alloc();
         boolean var8 = var5.nioBufferCount() != 1;
         if (!var8 && !var5.isDirect() && var7.isDirectBufferPooled()) {
            var8 = true;
         }

         ByteBuffer var9;
         if (!var8) {
            var9 = var5.nioBuffer();
         } else {
            var5 = var7.directBuffer(var12).writeBytes(var5);
            var9 = var5.nioBuffer();
         }

         int var10;
         if (var4 != null) {
            var10 = this.javaChannel().send(var9, var4);
         } else {
            var10 = this.javaChannel().write(var9);
         }

         boolean var11 = var10 > 0;
         if (var8) {
            if (var4 == null) {
               var2.current(var5);
            } else if (!var11) {
               var2.current(new DefaultAddressedEnvelope(var5, var4));
            } else {
               var2.current(var5);
            }
         }

         return var11;
      }
   }

   public InetSocketAddress localAddress() {
      return (InetSocketAddress)super.localAddress();
   }

   public InetSocketAddress remoteAddress() {
      return (InetSocketAddress)super.remoteAddress();
   }

   public ChannelFuture joinGroup(InetAddress var1) {
      return this.joinGroup(var1, this.newPromise());
   }

   public ChannelFuture joinGroup(InetAddress var1, ChannelPromise var2) {
      try {
         return this.joinGroup(var1, NetworkInterface.getByInetAddress(this.localAddress().getAddress()), (InetAddress)null, var2);
      } catch (SocketException var4) {
         var2.setFailure(var4);
         return var2;
      }
   }

   public ChannelFuture joinGroup(InetSocketAddress var1, NetworkInterface var2) {
      return this.joinGroup(var1, var2, this.newPromise());
   }

   public ChannelFuture joinGroup(InetSocketAddress var1, NetworkInterface var2, ChannelPromise var3) {
      return this.joinGroup(var1.getAddress(), var2, (InetAddress)null, var3);
   }

   public ChannelFuture joinGroup(InetAddress var1, NetworkInterface var2, InetAddress var3) {
      return this.joinGroup(var1, var2, var3, this.newPromise());
   }

   public ChannelFuture joinGroup(InetAddress var1, NetworkInterface var2, InetAddress var3, ChannelPromise var4) {
      if (PlatformDependent.javaVersion() >= 7) {
         if (var1 == null) {
            throw new NullPointerException("multicastAddress");
         } else if (var2 == null) {
            throw new NullPointerException("networkInterface");
         } else {
            try {
               MembershipKey var5;
               if (var3 == null) {
                  var5 = this.javaChannel().join(var1, var2);
               } else {
                  var5 = this.javaChannel().join(var1, var2, var3);
               }

               synchronized(this){}
               Object var7 = (List)this.memberships.get(var1);
               if (var7 == null) {
                  var7 = new ArrayList();
                  this.memberships.put(var1, var7);
               }

               ((List)var7).add(var5);
               var4.setSuccess();
            } catch (Throwable var9) {
               var4.setFailure(var9);
            }

            return var4;
         }
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public ChannelFuture leaveGroup(InetAddress var1) {
      return this.leaveGroup(var1, this.newPromise());
   }

   public ChannelFuture leaveGroup(InetAddress var1, ChannelPromise var2) {
      try {
         return this.leaveGroup(var1, NetworkInterface.getByInetAddress(this.localAddress().getAddress()), (InetAddress)null, var2);
      } catch (SocketException var4) {
         var2.setFailure(var4);
         return var2;
      }
   }

   public ChannelFuture leaveGroup(InetSocketAddress var1, NetworkInterface var2) {
      return this.leaveGroup(var1, var2, this.newPromise());
   }

   public ChannelFuture leaveGroup(InetSocketAddress var1, NetworkInterface var2, ChannelPromise var3) {
      return this.leaveGroup(var1.getAddress(), var2, (InetAddress)null, var3);
   }

   public ChannelFuture leaveGroup(InetAddress var1, NetworkInterface var2, InetAddress var3) {
      return this.leaveGroup(var1, var2, var3, this.newPromise());
   }

   public ChannelFuture leaveGroup(InetAddress var1, NetworkInterface var2, InetAddress var3, ChannelPromise var4) {
      if (PlatformDependent.javaVersion() < 7) {
         throw new UnsupportedOperationException();
      } else if (var1 == null) {
         throw new NullPointerException("multicastAddress");
      } else if (var2 == null) {
         throw new NullPointerException("networkInterface");
      } else {
         synchronized(this){}
         if (this.memberships != null) {
            List var6 = (List)this.memberships.get(var1);
            if (var6 != null) {
               Iterator var7 = var6.iterator();

               label43:
               while(true) {
                  MembershipKey var8;
                  do {
                     do {
                        if (!var7.hasNext()) {
                           if (var6.isEmpty()) {
                              this.memberships.remove(var1);
                           }
                           break label43;
                        }

                        var8 = (MembershipKey)var7.next();
                     } while(!var2.equals(var8.networkInterface()));
                  } while((var3 != null || var8.sourceAddress() != null) && (var3 == null || !var3.equals(var8.sourceAddress())));

                  var8.drop();
                  var7.remove();
               }
            }
         }

         var4.setSuccess();
         return var4;
      }
   }

   public ChannelFuture block(InetAddress var1, NetworkInterface var2, InetAddress var3) {
      return this.block(var1, var2, var3, this.newPromise());
   }

   public ChannelFuture block(InetAddress var1, NetworkInterface var2, InetAddress var3, ChannelPromise var4) {
      if (PlatformDependent.javaVersion() < 7) {
         throw new UnsupportedOperationException();
      } else if (var1 == null) {
         throw new NullPointerException("multicastAddress");
      } else if (var3 == null) {
         throw new NullPointerException("sourceToBlock");
      } else if (var2 == null) {
         throw new NullPointerException("networkInterface");
      } else {
         synchronized(this){}
         if (this.memberships != null) {
            List var6 = (List)this.memberships.get(var1);
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
               MembershipKey var8 = (MembershipKey)var7.next();
               if (var2.equals(var8.networkInterface())) {
                  try {
                     var8.block(var3);
                  } catch (IOException var11) {
                     var4.setFailure(var11);
                  }
               }
            }
         }

         var4.setSuccess();
         return var4;
      }
   }

   public ChannelFuture block(InetAddress var1, InetAddress var2) {
      return this.block(var1, var2, this.newPromise());
   }

   public ChannelFuture block(InetAddress var1, InetAddress var2, ChannelPromise var3) {
      try {
         return this.block(var1, NetworkInterface.getByInetAddress(this.localAddress().getAddress()), var2, var3);
      } catch (SocketException var5) {
         var3.setFailure(var5);
         return var3;
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

   public ChannelConfig config() {
      return this.config();
   }
}
