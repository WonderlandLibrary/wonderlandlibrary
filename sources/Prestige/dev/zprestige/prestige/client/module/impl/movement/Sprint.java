package dev.zprestige.prestige.client.module.impl.movement;

import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.TickEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;

public class Sprint extends Module {

    public Sprint() {
        super("Sprint", Category.Movement, "Automatically sprints for you");
    }

    @EventListener
    public void event(TickEvent event) {
        if (getMc().player != null) {
            if (getMc().player.forwardSpeed > 0) {
                getMc().options.sprintKey.setPressed(true);
            }
        }
    }
}
