/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.channel.ChannelHandlerContext
 *  net.minecraft.network.NetworkManager
 *  net.minecraft.network.Packet
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.network;

import io.netty.channel.ChannelHandlerContext;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.injection.backend.PacketImplKt;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={NetworkManager.class})
public class MixinNetworkManager {
    @Inject(method={"channelRead0"}, at={@At(value="HEAD")}, cancellable=true)
    private void read(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callback) {
        PacketEvent event = new PacketEvent(PacketImplKt.wrap(packet));
        LiquidBounce.eventManager.callEvent(event);
        if (event.isCancelled()) {
            callback.cancel();
        }
    }

    @Inject(method={"sendPacket(Lnet/minecraft/network/Packet;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void send(Packet<?> packet, CallbackInfo callback) {
        PacketEvent event = new PacketEvent(PacketImplKt.wrap(packet));
        LiquidBounce.eventManager.callEvent(event);
        if (event.isCancelled()) {
            callback.cancel();
        }
    }
}

