package net.smoothboot.client.events.impl;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.smoothboot.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.smoothboot.client.events.impl.SendPacketEvent.packet;

public class OnAttackEntityEvent extends Event {
    public PlayerEntity player;
    public Entity target;
    public CallbackInfo ci;

    public OnAttackEntityEvent(PlayerEntity player, Entity target, CallbackInfo ci) {
        super(new SendPacketEvent(packet, ci));
        this.player = player;
        this.target = target;
        this.ci = ci;
    }
}