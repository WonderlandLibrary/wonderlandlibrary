/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventPacketReceive;
import net.ccbluex.liquidbounce.event.EventRespawn;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S07PacketRespawn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

    @Shadow
    private Channel channel;

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void read(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callback) {
        final PacketEvent event = new PacketEvent(packet);
        LiquidBounce.eventManager.callEvent(event);

        if(event.isCancelled())
            callback.cancel();

    }

    @Inject(method = "channelRead0", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Packet;processPacket(Lnet/minecraft/network/INetHandler;)V", shift = At.Shift.BEFORE), cancellable = true)
    private void readpacket(ChannelHandlerContext context, Packet packet, CallbackInfo callback) {
        if (this.channel.isOpen()) {
            EventPacketReceive event = new EventPacketReceive(packet);
            LiquidBounce.eventManager.callEvent(event);
            if (packet instanceof S07PacketRespawn) {
                EventRespawn eventRespawn = new EventRespawn();
                LiquidBounce.eventManager.callEvent(eventRespawn);
            }
            if (event.isCancelled()) {
                callback.cancel();
            }
        }

    }

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void send(Packet<?> packet, CallbackInfo callback) {
        final PacketEvent event = new PacketEvent(packet);
        LiquidBounce.eventManager.callEvent(event);

        if(event.isCancelled())
            callback.cancel();
    }
}