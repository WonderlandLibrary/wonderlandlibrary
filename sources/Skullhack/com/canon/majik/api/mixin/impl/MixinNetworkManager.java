package com.canon.majik.api.mixin.impl;

import com.canon.majik.api.core.Initializer;
import com.canon.majik.api.event.events.PacketEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void sendPacket(Packet<?> packet, CallbackInfo ci) {
        PacketEvent.Send event = new PacketEvent.Send(packet);
        if (Initializer.eventBus.invoke(event)) {
            ci.cancel();
        }
    }

    @Inject(method = "channelRead0*", at = @At("HEAD"), cancellable = true)
    private void receivePacket(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        PacketEvent.Receive event = new PacketEvent.Receive(packet);
        if (Initializer.eventBus.invoke(event)) {
            ci.cancel();
        }
    }
}
