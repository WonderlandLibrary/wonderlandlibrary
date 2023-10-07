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

@Module.Info(name = "IceSpeed", category = Module.Category.MOVEMENT, description = "You are fast on ice")
public class IceSpeed extends Module {

    @Value(name = "Slipperiness", minimum = 0.1, maximum = 1)
    double slipperiness = 0.1;

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if(event instanceof UpdateEvent) {
            Blocks.ice.slipperiness = (float) slipperiness;
            Blocks.packed_ice.slipperiness = (float) slipperiness;
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        Blocks.ice.slipperiness = 0.98F;
        Blocks.packed_ice.slipperiness = 0.98F;
    }
}
