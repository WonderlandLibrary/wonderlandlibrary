package koks.module.movement;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.manager.value.annotation.Value;
import koks.event.UpdateEvent;
import net.minecraft.init.Blocks;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "NoCobweb", description = "You doesn't affect by cobweb", category = Module.Category.MOVEMENT)
public class NoCobweb extends Module {

    @Value(name = "Mode", modes = {"Vanilla", "Intave13"})
    String mode = "Vanilla";

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof UpdateEvent) {
            switch (mode) {
                case "Intave13":
                    if (!getPlayer().isInWeb) {
                        if (getBlockUnderPlayer(0.1F).equals(Blocks.web)) {
                            getPlayer().motionY = 0;
                        }
                    } else {
                        getPlayer().motionY = 0.26;
                    }
                    break;
                case "Vanilla":
                    getPlayer().isInWeb = false;
                    break;
            }


        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
