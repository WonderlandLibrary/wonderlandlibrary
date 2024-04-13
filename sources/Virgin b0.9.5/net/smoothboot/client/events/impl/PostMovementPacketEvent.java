package net.smoothboot.client.events.impl;

import net.smoothboot.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.smoothboot.client.events.impl.SendPacketEvent.packet;

public class PostMovementPacketEvent extends Event {
    public CallbackInfo ci;

    public PostMovementPacketEvent(CallbackInfo ci) {
        super(new SendPacketEvent(packet, ci));
        this.ci = ci;
    }
}
