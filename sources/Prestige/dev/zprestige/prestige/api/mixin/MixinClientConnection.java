package dev.zprestige.prestige.api.mixin;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.impl.PacketReceiveEvent;
import dev.zprestige.prestige.client.event.impl.PacketSendEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ClientConnection.class})
public class MixinClientConnection {
    @Inject(at={@At(value="HEAD")}, method={"handlePacket"}, cancellable=true)
    private static void handlePacket(Packet packet, PacketListener packetListener, CallbackInfo callbackInfo) {
        if (Prestige.Companion.getSelfDestructed()) {
            return;
        }
        PacketReceiveEvent event = new PacketReceiveEvent(packet);
        if (event.invoke()) {
            callbackInfo.cancel();
        }
    }

    @Inject(at={@At(value="HEAD")}, method={"send(Lnet/minecraft/network/packet/Packet;)V"}, cancellable=true)
    void onPacketSend(Packet packet, CallbackInfo callbackInfo) {
        if (Prestige.Companion.getSelfDestructed()) {
            return;
        }
        PacketSendEvent event = new PacketSendEvent(packet);
        if (event.invoke()) {
            callbackInfo.cancel();
        }
    }
}
