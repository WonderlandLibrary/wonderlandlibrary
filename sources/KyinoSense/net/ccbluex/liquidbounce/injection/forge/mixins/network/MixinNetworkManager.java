/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.channel.ChannelHandlerContext
 *  net.minecraft.network.NetworkManager
 *  net.minecraft.network.Packet
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.network;

import io.netty.channel.ChannelHandlerContext;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.utils.PacketUtils;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={NetworkManager.class})
public abstract class MixinNetworkManager {
    @Inject(method={"channelRead0"}, at={@At(value="HEAD")}, cancellable=true)
    private void read(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callback) {
        if (PacketUtils.getPacketType(packet) != PacketUtils.PacketType.SERVERSIDE) {
            return;
        }
        PacketEvent event = new PacketEvent(packet);
        LiquidBounce.eventManager.callEvent(event);
        if (event.isCancelled()) {
            callback.cancel();
        }
    }

    @Inject(method={"sendPacket(Lnet/minecraft/network/Packet;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void send(Packet<?> packet, CallbackInfo callback) {
        if (PacketUtils.getPacketType(packet) != PacketUtils.PacketType.CLIENTSIDE) {
            return;
        }
        if (!PacketUtils.handleSendPacket(packet)) {
            PacketEvent event = new PacketEvent(packet);
            LiquidBounce.eventManager.callEvent(event);
            if (event.isCancelled()) {
                callback.cancel();
            }
        }
    }
}

