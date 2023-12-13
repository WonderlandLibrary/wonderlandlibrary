package cn.hanabi.injection.mixins;

import cn.hanabi.Hanabi;
import cn.hanabi.events.EventPacket;
import cn.hanabi.injection.interfaces.INetworkManager;
import cn.hanabi.utils.PacketHelper;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.types.EventType;
import com.google.common.collect.Queues;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.GenericFutureListener;
import java.lang.reflect.Field;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({NetworkManager.class})
public abstract class MixinNetworkManager implements INetworkManager {
   private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
   @Shadow
   private Channel channel;
   @Final
   @Shadow
   private final Queue outboundPacketsQueue = Queues.newConcurrentLinkedQueue();

   @Inject(
      method = {"channelRead0*"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/network/Packet;processPacket(Lnet/minecraft/network/INetHandler;)V",
   shift = At.Shift.BEFORE
)},
      cancellable = true
   )
   private void packetReceived(ChannelHandlerContext p_channelRead0_1_, Packet packet, CallbackInfo ci) {
      EventPacket event = new EventPacket(EventType.RECIEVE, packet);
      EventManager.call(event);
      PacketHelper.onPacketReceive(packet);
      if (event.isCancelled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"sendPacket(Lnet/minecraft/network/Packet;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void sendPacket(Packet packetIn, CallbackInfo ci) {
      try {
         if (packetIn instanceof C00Handshake) {
            C00Handshake handshakePacket = (C00Handshake)packetIn;
            Class clazz = handshakePacket.getClass();
            Field[] var5 = clazz.getDeclaredFields();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               Field field = var5[var7];
               if (field.getType() == String.class) {
                  field.setAccessible(true);
                  String targetIP = field.get(handshakePacket).toString();
                  if (Hanabi.INSTANCE.hypixelBypass) {
                     Hanabi.INSTANCE.println("Redirect to Hypixel");
                     field.set(handshakePacket, "mc.hypixel.net");
                  }
               }
            }
         }
      } catch (Throwable var10) {
         var10.printStackTrace();
      }

      EventPacket event = new EventPacket(EventType.SEND, packetIn);
      EventManager.call(event);
      if (event.isCancelled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"closeChannel(Lnet/minecraft/util/IChatComponent;)V"},
      at = {@At("RETURN")}
   )
   private void onClose(IChatComponent chatComponent, CallbackInfo ci) {
      Logger.getLogger("Closed");
   }

   @Shadow
   public abstract boolean isChannelOpen();

   public void sendPacketNoEvent(Packet packet) {
      if (this.channel != null && this.channel.isOpen()) {
         this.flushOutboundQueue();
         this.dispatchPacket(packet, (GenericFutureListener[])null);
      } else {
         this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packet, (GenericFutureListener[])null));
      }

   }

   @Shadow
   protected abstract void dispatchPacket(Packet var1, GenericFutureListener[] var2);

   @Shadow
   protected abstract void flushOutboundQueue();
}
