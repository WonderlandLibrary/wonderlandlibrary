package koks.module.movement;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.ModuleRegistry;
import koks.event.UpdateEvent;
import koks.event.DirectionSprintCheckEvent;
import koks.module.world.Scaffold;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */
@Module.Info(name = "Sprint", description = "You sprint always", category = Module.Category.MOVEMENT)
public class Sprint extends Module implements Module.NotUnToggle {

    @Value(name = "Legit")
    boolean legit = true;

    @Value(name = "All Direction")
    boolean allDirection = false;

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if(event instanceof final DirectionSprintCheckEvent sprintCheckEvent) {
            if(allDirection)
                sprintCheckEvent.setSprintCheck(false);
        }
        if(event instanceof UpdateEvent) {
            if(ModuleRegistry.getModule(Scaffold.class).isToggled())
                return;
            if(legit)
                getGameSettings().keyBindSprint.pressed = true;
            else
                getPlayer().setSprinting(true);
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
