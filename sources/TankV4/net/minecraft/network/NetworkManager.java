package net.minecraft.network;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.crypto.SecretKey;
import my.NewSnake.event.Event;
import my.NewSnake.event.events.PacketReceiveEvent;
import my.NewSnake.event.events.PacketSendEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.CryptManager;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.MessageDeserializer;
import net.minecraft.util.MessageDeserializer2;
import net.minecraft.util.MessageSerializer;
import net.minecraft.util.MessageSerializer2;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class NetworkManager extends SimpleChannelInboundHandler {
   private final ReentrantReadWriteLock field_181680_j = new ReentrantReadWriteLock();
   private SocketAddress socketAddress;
   public static final AttributeKey attrKeyConnectionState;
   public static final LazyLoadBase field_181125_e;
   private static final Logger logger = LogManager.getLogger();
   private INetHandler packetListener;
   private boolean disconnected;
   public static final LazyLoadBase CLIENT_NIO_EVENTLOOP;
   private IChatComponent terminationReason;
   public static final Marker logMarkerPackets;
   private boolean isEncrypted;
   private Channel channel;
   public static final LazyLoadBase CLIENT_LOCAL_EVENTLOOP;
   private final Queue outboundPacketsQueue = Queues.newConcurrentLinkedQueue();
   private final EnumPacketDirection direction;
   public static final Marker logMarkerNetwork = MarkerManager.getMarker("NETWORK");

   public static NetworkManager provideLocalClient(SocketAddress var0) {
      NetworkManager var1 = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
      ((Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap()).group((EventLoopGroup)CLIENT_LOCAL_EVENTLOOP.getValue())).handler(new ChannelInitializer(var1) {
         private final NetworkManager val$networkmanager;

         {
            this.val$networkmanager = var1;
         }

         protected void initChannel(Channel var1) throws Exception {
            var1.pipeline().addLast((String)"packet_handler", (ChannelHandler)this.val$networkmanager);
         }
      })).channel(LocalChannel.class)).connect(var0).syncUninterruptibly();
      return var1;
   }

   public void checkDisconnected() {
      if (this.channel != null && !this.channel.isOpen()) {
         if (!this.disconnected) {
            this.disconnected = true;
            if (this.getExitMessage() != null) {
               this.getNetHandler().onDisconnect(this.getExitMessage());
            } else if (this.getNetHandler() != null) {
               this.getNetHandler().onDisconnect(new ChatComponentText("Disconnected"));
            }
         } else {
            logger.warn("handleDisconnection() called twice");
         }
      }

   }

   public void sendPacket(Packet var1, GenericFutureListener var2, GenericFutureListener... var3) {
      if (this != false) {
         this.flushOutboundQueue();
         this.dispatchPacket(var1, (GenericFutureListener[])ArrayUtils.add(var3, 0, var2));
      } else {
         this.field_181680_j.writeLock().lock();
         this.outboundPacketsQueue.add(new NetworkManager.InboundHandlerTuplePacketListener(var1, (GenericFutureListener[])ArrayUtils.add(var3, 0, var2)));
         this.field_181680_j.writeLock().unlock();
      }

   }

   static Channel access$1(NetworkManager var0) {
      return var0.channel;
   }

   public boolean getIsencrypted() {
      return this.isEncrypted;
   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      this.closeChannel(new ChatComponentTranslation("disconnect.endOfStream", new Object[0]));
   }

   public NetworkManager(EnumPacketDirection var1) {
      this.direction = var1;
   }

   private void flushOutboundQueue() {
      if (this.channel != null && this.channel.isOpen()) {
         this.field_181680_j.readLock().lock();

         while(!this.outboundPacketsQueue.isEmpty()) {
            NetworkManager.InboundHandlerTuplePacketListener var1 = (NetworkManager.InboundHandlerTuplePacketListener)this.outboundPacketsQueue.poll();
            this.dispatchPacket(NetworkManager.InboundHandlerTuplePacketListener.access$0(var1), NetworkManager.InboundHandlerTuplePacketListener.access$1(var1));
         }

         this.field_181680_j.readLock().unlock();
      }

   }

   protected void channelRead0(ChannelHandlerContext var1, Object var2) throws Exception {
      this.channelRead0(var1, (Packet)var2);
   }

   public void enableEncryption(SecretKey var1) {
      this.isEncrypted = true;
      this.channel.pipeline().addBefore("splitter", "decrypt", new NettyEncryptingDecoder(CryptManager.createNetCipherInstance(2, var1)));
      this.channel.pipeline().addBefore("prepender", "encrypt", new NettyEncryptingEncoder(CryptManager.createNetCipherInstance(1, var1)));
   }

   private void dispatchPacket(Packet var1, GenericFutureListener[] var2) {
      PacketSendEvent var3 = new PacketSendEvent(Event.State.PRE, var1);
      var3.call();
      if (!var3.isCancelled()) {
         EnumConnectionState var4 = EnumConnectionState.getFromPacket(var1);
         EnumConnectionState var5 = (EnumConnectionState)this.channel.attr(attrKeyConnectionState).get();
         if (var5 != var4) {
            logger.debug("Disabled auto read");
            this.channel.config().setAutoRead(false);
         }

         if (this.channel.eventLoop().inEventLoop()) {
            if (var4 != var5) {
               this.setConnectionState(var4);
            }

            ChannelFuture var6 = this.channel.writeAndFlush(var1);
            if (var2 != null) {
               var6.addListeners(var2);
            }

            var6.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
         } else {
            this.channel.eventLoop().execute(new Runnable(this, var4, var5, var1, var2) {
               private final EnumConnectionState val$var4;
               private final Packet val$inPacket;
               private static final String __OBFID = "CL_00001243";
               final NetworkManager this$0;
               private final GenericFutureListener[] val$futureListeners;
               private final EnumConnectionState val$var3;

               {
                  this.this$0 = var1;
                  this.val$var3 = var2;
                  this.val$var4 = var3;
                  this.val$inPacket = var4;
                  this.val$futureListeners = var5;
               }

               public void run() {
                  if (this.val$var3 != this.val$var4) {
                     this.this$0.setConnectionState(this.val$var3);
                  }

                  ChannelFuture var1 = NetworkManager.access$1(this.this$0).writeAndFlush(this.val$inPacket);
                  if (this.val$futureListeners != null) {
                     var1.addListeners(this.val$futureListeners);
                  }

                  var1.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
               }
            });
         }

         (new PacketSendEvent(Event.State.POST, var1)).call();
      }
   }

   public void setConnectionState(EnumConnectionState var1) {
      this.channel.attr(attrKeyConnectionState).set(var1);
      this.channel.config().setAutoRead(true);
      logger.debug("Enabled auto read");
   }

   public IChatComponent getExitMessage() {
      return this.terminationReason;
   }

   public void closeChannel(IChatComponent var1) {
      if (this.channel.isOpen()) {
         this.channel.close().awaitUninterruptibly();
         this.terminationReason = var1;
      }

   }

   static {
      logMarkerPackets = MarkerManager.getMarker("NETWORK_PACKETS", logMarkerNetwork);
      attrKeyConnectionState = AttributeKey.valueOf("protocol");
      CLIENT_NIO_EVENTLOOP = new LazyLoadBase() {
         protected NioEventLoopGroup load() {
            return new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Client IO #%d").setDaemon(true).build());
         }

         protected Object load() {
            return this.load();
         }
      };
      field_181125_e = new LazyLoadBase() {
         protected Object load() {
            return this.load();
         }

         protected EpollEventLoopGroup load() {
            return new EpollEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Epoll Client IO #%d").setDaemon(true).build());
         }
      };
      CLIENT_LOCAL_EVENTLOOP = new LazyLoadBase() {
         protected Object load() {
            return this.load();
         }

         protected LocalEventLoopGroup load() {
            return new LocalEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Local Client IO #%d").setDaemon(true).build());
         }
      };
   }

   public void channelActive(ChannelHandlerContext var1) throws Exception {
      super.channelActive(var1);
      this.channel = var1.channel();
      this.socketAddress = this.channel.remoteAddress();

      try {
         this.setConnectionState(EnumConnectionState.HANDSHAKING);
      } catch (Throwable var3) {
         logger.fatal((Object)var3);
      }

   }

   public void disableAutoRead() {
      this.channel.config().setAutoRead(false);
   }

   public void setCompressionTreshold(int var1) {
      if (var1 >= 0) {
         if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
            ((NettyCompressionDecoder)this.channel.pipeline().get("decompress")).setCompressionTreshold(var1);
         } else {
            this.channel.pipeline().addBefore("decoder", "decompress", new NettyCompressionDecoder(var1));
         }

         if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
            ((NettyCompressionEncoder)this.channel.pipeline().get("decompress")).setCompressionTreshold(var1);
         } else {
            this.channel.pipeline().addBefore("encoder", "compress", new NettyCompressionEncoder(var1));
         }
      } else {
         if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
            this.channel.pipeline().remove("decompress");
         }

         if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
            this.channel.pipeline().remove("compress");
         }
      }

   }

   public boolean hasNoChannel() {
      return this.channel == null;
   }

   public static NetworkManager func_181124_a(InetAddress var0, int var1, boolean var2) {
      NetworkManager var3 = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
      Class var4;
      LazyLoadBase var5;
      if (Epoll.isAvailable() && var2) {
         var4 = EpollSocketChannel.class;
         var5 = field_181125_e;
      } else {
         var4 = NioSocketChannel.class;
         var5 = CLIENT_NIO_EVENTLOOP;
      }

      ((Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap()).group((EventLoopGroup)var5.getValue())).handler(new ChannelInitializer(var3) {
         private final NetworkManager val$networkmanager;

         protected void initChannel(Channel var1) throws Exception {
            try {
               var1.config().setOption(ChannelOption.TCP_NODELAY, true);
            } catch (ChannelException var3) {
            }

            var1.pipeline().addLast((String)"timeout", (ChannelHandler)(new ReadTimeoutHandler(30))).addLast((String)"splitter", (ChannelHandler)(new MessageDeserializer2())).addLast((String)"decoder", (ChannelHandler)(new MessageDeserializer(EnumPacketDirection.CLIENTBOUND))).addLast((String)"prepender", (ChannelHandler)(new MessageSerializer2())).addLast((String)"encoder", (ChannelHandler)(new MessageSerializer(EnumPacketDirection.SERVERBOUND))).addLast((String)"packet_handler", (ChannelHandler)this.val$networkmanager);
         }

         {
            this.val$networkmanager = var1;
         }
      })).channel(var4)).connect(var0, var1).syncUninterruptibly();
      return var3;
   }

   public INetHandler getNetHandler() {
      return this.packetListener;
   }

   public boolean isLocalChannel() {
      return this.channel instanceof LocalChannel || this.channel instanceof LocalServerChannel;
   }

   public SocketAddress getRemoteAddress() {
      return this.socketAddress;
   }

   public void setNetHandler(INetHandler var1) {
      Validate.notNull(var1, "packetListener");
      logger.debug("Set listener of {} to {}", this, var1);
      this.packetListener = var1;
   }

   protected void channelRead0(ChannelHandlerContext var1, Packet var2) throws Exception {
      if (this.channel.isOpen()) {
         try {
            PacketReceiveEvent var3 = new PacketReceiveEvent(var2);
            var3.call();
            if (!var3.isCancelled()) {
               var2.processPacket(this.packetListener);
            }
         } catch (ThreadQuickExitException var5) {
         }
      }

   }

   public void sendPacket(Packet var1) {
      if (this != null) {
         this.flushOutboundQueue();
         this.dispatchPacket(var1, (GenericFutureListener[])null);
      } else {
         this.field_181680_j.writeLock().lock();
         this.outboundPacketsQueue.add(new NetworkManager.InboundHandlerTuplePacketListener(var1, (GenericFutureListener[])null));
         this.field_181680_j.writeLock().unlock();
      }

   }

   public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception {
      ChatComponentTranslation var3;
      if (var2 instanceof TimeoutException) {
         var3 = new ChatComponentTranslation("disconnect.timeout", new Object[0]);
      } else {
         var3 = new ChatComponentTranslation("disconnect.genericReason", new Object[]{"Internal Exception: " + var2});
      }

      this.closeChannel(var3);
   }

   public void processReceivedPackets() {
      this.flushOutboundQueue();
      if (this.packetListener instanceof ITickable) {
         ((ITickable)this.packetListener).update();
      }

      this.channel.flush();
   }

   static class InboundHandlerTuplePacketListener {
      private final GenericFutureListener[] futureListeners;
      private final Packet packet;

      static Packet access$0(NetworkManager.InboundHandlerTuplePacketListener var0) {
         return var0.packet;
      }

      public InboundHandlerTuplePacketListener(Packet var1, GenericFutureListener... var2) {
         this.packet = var1;
         this.futureListeners = var2;
      }

      static GenericFutureListener[] access$1(NetworkManager.InboundHandlerTuplePacketListener var0) {
         return var0.futureListeners;
      }
   }
}
