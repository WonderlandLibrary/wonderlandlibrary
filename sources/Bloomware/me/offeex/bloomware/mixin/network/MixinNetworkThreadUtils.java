package me.offeex.bloomware.mixin.network;

import me.offeex.bloomware.api.manager.managers.SessionManager;
import me.offeex.bloomware.event.events.EventPacket;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.listener.PacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkThreadUtils.class)
public class MixinNetworkThreadUtils {
    @Inject(method = "method_11072", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/Packet;apply(Lnet/minecraft/network/listener/PacketListener;)V"), cancellable = true)
    private static void receivePre(PacketListener packetListener, Packet<?> packet, CallbackInfo ci) {
        new EventPacket.Receive(packet).post(ci);
    }

    @Inject(method = "method_11072", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/Packet;apply(Lnet/minecraft/network/listener/PacketListener;)V", shift = At.Shift.AFTER))
    private static void receivePost(PacketListener packetListener, Packet<?> packet, CallbackInfo ci) {
        new EventPacket.Receive(packet).shift().post();
    }
}
