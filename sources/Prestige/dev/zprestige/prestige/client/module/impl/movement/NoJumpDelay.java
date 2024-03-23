package dev.zprestige.prestige.client.module.impl.movement;

import dev.zprestige.prestige.api.mixin.ILivingEntity;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.TickEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;

public class NoJumpDelay extends Module {

    public NoJumpDelay() {
        super("No Jump Delay", Category.Movement, "Removes the delay between jumps");
    }

    @EventListener
    public void event(TickEvent event) {
        ((ILivingEntity)getMc().player).setJumpingCooldown(0);
    }
}
