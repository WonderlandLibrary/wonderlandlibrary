package koks.module.movement;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.utils.MovementUtil;
import koks.api.utils.RandomUtil;
import koks.api.manager.value.annotation.Value;
import koks.event.UpdateEvent;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "WallSpeed", description = "You are fast on walls", category = Module.Category.MOVEMENT)
public class WallSpeed extends Module {

    @Value(name = "Mode", modes = {"Intave13"})
    String mode = "Intave13";

    float intaveSpeed;

    @Override
    @Event.Info
    public void onEvent(Event event) {
        final MovementUtil movementUtil = MovementUtil.getInstance();
        final RandomUtil randomUtil = RandomUtil.getInstance();
        if (event instanceof UpdateEvent) {
            switch (mode) {
                case "Intave13":
                    if (getPlayer().isCollidedHorizontally) {
                        intaveSpeed += randomUtil.getRandomFloat(20, 23);
                        getPlayer().setSprinting(true);
                        intaveSpeed /= 69;
                        if (isMoving())
                            movementUtil.setSpeed(intaveSpeed);
                    }
                    break;
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        intaveSpeed = 0;
    }
}
