package me.offeex.bloomware.mixin.network;

import me.offeex.bloomware.event.events.EventPacket;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class MixinClientConnection {
    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V", at = @At("HEAD"), cancellable = true)
    private void sendPre(Packet<?> packet, @Nullable PacketCallbacks callbacks, CallbackInfo ci) {
        new EventPacket.Send(packet).post(ci);
    }

    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V", at = @At(value = "TAIL"))
    private void sendPost(Packet<?> packet, @Nullable PacketCallbacks callbacks, CallbackInfo ci) {
        new EventPacket.Send(packet).shift().post();
    }
}