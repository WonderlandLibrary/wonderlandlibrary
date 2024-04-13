package net.smoothboot.client.events.impl;

import jdk.jfr.Event;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class SendPacketEvent extends Event {
    public static CallbackInfo ci;
    public static Packet<?> packet;

    public SendPacketEvent(Packet<?> packet, CallbackInfo ci) {
        this.ci = ci;
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}