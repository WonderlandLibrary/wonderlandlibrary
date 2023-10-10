package io.netty.channel.sctp.oio;

import com.sun.nio.sctp.Association;
import com.sun.nio.sctp.MessageInfo;
import com.sun.nio.sctp.NotificationHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.oio.AbstractOioMessageChannel;
import io.netty.channel.sctp.DefaultSctpChannelConfig;
import io.netty.channel.sctp.SctpChannel;
import io.netty.channel.sctp.SctpChannelConfig;
import io.netty.channel.sctp.SctpMessage;
import io.netty.channel.sctp.SctpNotificationHandler;
import io.netty.channel.sctp.SctpServerChannel;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class OioSctpChannel extends AbstractOioMessageChannel implements SctpChannel {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(OioSctpChannel.class);
   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
   private final com.sun.nio.sctp.SctpChannel ch;
   private final SctpChannelConfig config;
   private final Selector readSelector;
   private final Selector writeSelector;
   private final Selector connectSelector;
   private final NotificationHandler notificationHandler;
   private RecvByteBufAllocator.Handle allocHandle;

   private static com.sun.nio.sctp.SctpChannel openChannel() {
      try {
         return com.sun.nio.sctp.SctpChannel.open();
      } catch (IOException var1) {
         throw new ChannelException("Failed to open a sctp channel.", var1);
      }
   }

   public OioSctpChannel() {
      this(openChannel());
   }

   public OioSctpChannel(com.sun.nio.sctp.SctpChannel var1) {
      this((Channel)null, var1);
   }

   public OioSctpChannel(Channel var1, com.sun.nio.sctp.SctpChannel var2) {
      super(var1);
      this.ch = var2;
      boolean var3 = false;

      try {
         var2.configureBlocking(false);
         this.readSelector = Selector.open();
         this.writeSelector = Selector.open();
         this.connectSelector = Selector.open();
         var2.register(this.readSelector, 1);
         var2.register(this.writeSelector, 4);
         var2.register(this.connectSelector, 8);
         this.config = new DefaultSctpChannelConfig(this, var2);
         this.notificationHandler = new SctpNotificationHandler(this);
         var3 = true;
      } catch (Exception var8) {
         throw new ChannelException("failed to initialize a sctp channel", var8);
      }

      if (!var3) {
         try {
            var2.close();
         } catch (IOException var7) {
            logger.warn("Failed to close a sctp channel.", (Throwable)var7);
         }
      }

   }

   public InetSocketAddress localAddress() {
      return (InetSocketAddress)super.localAddress();
   }

   public InetSocketAddress remoteAddress() {
      return (InetSocketAddress)super.remoteAddress();
   }

   public SctpServerChannel parent() {
      return (SctpServerChannel)super.parent();
   }

   public ChannelMetadata metadata() {
      return METADATA;
   }

   public SctpChannelConfig config() {
      return this.config;
   }

   public boolean isOpen() {
      return this.ch.isOpen();
   }

   protected int doReadMessages(List var1) throws Exception {
      if (!this.readSelector.isOpen()) {
         return 0;
      } else {
         int var2 = 0;
         int var3 = this.readSelector.select(1000L);
         boolean var4 = var3 > 0;
         if (!var4) {
            return var2;
         } else {
            Set var5 = this.readSelector.selectedKeys();
            Iterator var6 = var5.iterator();

            RecvByteBufAllocator.Handle var8;
            ByteBuf var9;
            boolean var10;
            int var13;
            while(true) {
               if (!var6.hasNext()) {
                  var5.clear();
                  return var2;
               }

               SelectionKey var7 = (SelectionKey)var6.next();
               var8 = this.allocHandle;
               if (var8 == null) {
                  this.allocHandle = var8 = this.config().getRecvByteBufAllocator().newHandle();
               }

               var9 = var8.allocate(this.config().getAllocator());
               var10 = true;

               int var11;
               try {
                  ByteBuffer var19 = var9.nioBuffer(var9.writerIndex(), var9.writableBytes());
                  MessageInfo var12 = this.ch.receive(var19, (Object)null, this.notificationHandler);
                  if (var12 == null) {
                     var13 = var2;
                     break;
                  }

                  var19.flip();
                  var1.add(new SctpMessage(var12, var9.writerIndex(var9.writerIndex() + var19.remaining())));
                  var10 = false;
                  ++var2;
               } catch (Throwable var18) {
                  PlatformDependent.throwException(var18);
                  var11 = var9.readableBytes();
                  var8.record(var11);
                  if (var10) {
                     var9.release();
                  }
                  continue;
               }

               var11 = var9.readableBytes();
               var8.record(var11);
               if (var10) {
                  var9.release();
               }
            }

            int var14 = var9.readableBytes();
            var8.record(var14);
            if (var10) {
               var9.release();
            }

            var5.clear();
            return var13;
         }
      }
   }

   protected void doWrite(ChannelOutboundBuffer var1) throws Exception {
      if (this.writeSelector.isOpen()) {
         int var2 = var1.size();
         int var3 = this.writeSelector.select(1000L);
         if (var3 > 0) {
            Set var4 = this.writeSelector.selectedKeys();
            if (!var4.isEmpty()) {
               Iterator var5 = var4.iterator();
               int var6 = 0;

               while(var6 != var2) {
                  var5.next();
                  var5.remove();
                  SctpMessage var7 = (SctpMessage)var1.current();
                  if (var7 == null) {
                     return;
                  }

                  ByteBuf var8 = var7.content();
                  int var9 = var8.readableBytes();
                  ByteBuffer var10;
                  if (var8.nioBufferCount() != -1) {
                     var10 = var8.nioBuffer();
                  } else {
                     var10 = ByteBuffer.allocate(var9);
                     var8.getBytes(var8.readerIndex(), var10);
                     var10.flip();
                  }

                  MessageInfo var11 = MessageInfo.createOutgoing(this.association(), (SocketAddress)null, var7.streamIdentifier());
                  var11.payloadProtocolID(var7.protocolIdentifier());
                  var11.streamNumber(var7.streamIdentifier());
                  this.ch.send(var10, var11);
                  ++var6;
                  var1.remove();
                  if (!var5.hasNext()) {
                     return;
                  }
               }

            }
         }
      }
   }

   public Association association() {
      try {
         return this.ch.association();
      } catch (IOException var2) {
         return null;
      }
   }

   public boolean isActive() {
      return this.isOpen() && this.association() != null;
   }

   protected SocketAddress localAddress0() {
      try {
         Iterator var1 = this.ch.getAllLocalAddresses().iterator();
         if (var1.hasNext()) {
            return (SocketAddress)var1.next();
         }
      } catch (IOException var2) {
      }

      return null;
   }

   public Set allLocalAddresses() {
      try {
         Set var1 = this.ch.getAllLocalAddresses();
         LinkedHashSet var2 = new LinkedHashSet(var1.size());
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            SocketAddress var4 = (SocketAddress)var3.next();
            var2.add((InetSocketAddress)var4);
         }

         return var2;
      } catch (Throwable var5) {
         return Collections.emptySet();
      }
   }

   protected SocketAddress remoteAddress0() {
      try {
         Iterator var1 = this.ch.getRemoteAddresses().iterator();
         if (var1.hasNext()) {
            return (SocketAddress)var1.next();
         }
      } catch (IOException var2) {
      }

      return null;
   }

   public Set allRemoteAddresses() {
      try {
         Set var1 = this.ch.getRemoteAddresses();
         LinkedHashSet var2 = new LinkedHashSet(var1.size());
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            SocketAddress var4 = (SocketAddress)var3.next();
            var2.add((InetSocketAddress)var4);
         }

         return var2;
      } catch (Throwable var5) {
         return Collections.emptySet();
      }
   }

   protected void doBind(SocketAddress var1) throws Exception {
      this.ch.bind(var1);
   }

   protected void doConnect(SocketAddress var1, SocketAddress var2) throws Exception {
      if (var2 != null) {
         this.ch.bind(var2);
      }

      boolean var3 = false;
      this.ch.connect(var1);
      boolean var4 = false;

      while(true) {
         do {
            if (var4) {
               var3 = this.ch.finishConnect();
               if (!var3) {
                  this.doClose();
               }

               return;
            }
         } while(this.connectSelector.select(1000L) < 0);

         Set var5 = this.connectSelector.selectedKeys();
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            SelectionKey var7 = (SelectionKey)var6.next();
            if (var7.isConnectable()) {
               var5.clear();
               var4 = true;
               break;
            }
         }

         var5.clear();
      }
   }

   protected void doDisconnect() throws Exception {
      this.doClose();
   }

   protected void doClose() throws Exception {
      closeSelector("read", this.readSelector);
      closeSelector("write", this.writeSelector);
      closeSelector("connect", this.connectSelector);
      this.ch.close();
   }

   private static void closeSelector(String var0, Selector var1) {
      try {
         var1.close();
      } catch (IOException var3) {
         logger.warn("Failed to close a " + var0 + " selector.", (Throwable)var3);
      }

   }

   public ChannelFuture bindAddress(InetAddress var1) {
      return this.bindAddress(var1, this.newPromise());
   }

   public ChannelFuture bindAddress(InetAddress var1, ChannelPromise var2) {
      if (this.eventLoop().inEventLoop()) {
         try {
            this.ch.bindAddress(var1);
            var2.setSuccess();
         } catch (Throwable var4) {
            var2.setFailure(var4);
         }
      } else {
         this.eventLoop().execute(new Runnable(this, var1, var2) {
            final InetAddress val$localAddress;
            final ChannelPromise val$promise;
            final OioSctpChannel this$0;

            {
               this.this$0 = var1;
               this.val$localAddress = var2;
               this.val$promise = var3;
            }

            public void run() {
               this.this$0.bindAddress(this.val$localAddress, this.val$promise);
            }
         });
      }

      return var2;
   }

   public ChannelFuture unbindAddress(InetAddress var1) {
      return this.unbindAddress(var1, this.newPromise());
   }

   public ChannelFuture unbindAddress(InetAddress var1, ChannelPromise var2) {
      if (this.eventLoop().inEventLoop()) {
         try {
            this.ch.unbindAddress(var1);
            var2.setSuccess();
         } catch (Throwable var4) {
            var2.setFailure(var4);
         }
      } else {
         this.eventLoop().execute(new Runnable(this, var1, var2) {
            final InetAddress val$localAddress;
            final ChannelPromise val$promise;
            final OioSctpChannel this$0;

            {
               this.this$0 = var1;
               this.val$localAddress = var2;
               this.val$promise = var3;
            }

            public void run() {
               this.this$0.unbindAddress(this.val$localAddress, this.val$promise);
            }
         });
      }

      return var2;
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
