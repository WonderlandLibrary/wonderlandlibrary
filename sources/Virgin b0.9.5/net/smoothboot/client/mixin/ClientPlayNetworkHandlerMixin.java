package net.smoothboot.client.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.smoothboot.client.Virginclient;
import net.smoothboot.client.events.Event;
import net.smoothboot.client.events.impl.SendPacketEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(at = @At("HEAD"), method= "sendPacket(Lnet/minecraft/network/packet/Packet;)V", cancellable = true)
    public void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        Virginclient.getInstance().emitter.triggerEvent(new Event(new SendPacketEvent(packet, ci)));
    }
}