package net.smoothboot.client.events.impl;

import net.smoothboot.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.smoothboot.client.events.impl.SendPacketEvent.packet;

public class PreMovementPacketEvent extends Event {
    public CallbackInfo ci;

    public PreMovementPacketEvent(CallbackInfo ci) {
        super(new SendPacketEvent(packet, ci));
        this.ci = ci;
    }
}
