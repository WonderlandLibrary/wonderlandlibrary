package koks.module.movement;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.utils.MovementUtil;
import koks.api.manager.value.annotation.Value;
import koks.event.UpdateEvent;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */
@Module.Info(name = "Step", category = Module.Category.MOVEMENT, description = "Goes up blocks automatically")
public class Step extends Module {

    @Value(name = "Mode", modes = {"Vanilla", "Intave13", "Intave13-2", "Mineplex", "AAC3.6.4", "Spartan424"})
    String mode = "Vanilla";

    @Value(name = "StepHeight", maximum = 10, minimum = 1)
    double stepHeight = 4;

    int ticksAAC4;

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        switch (name) {
            case "StepHeight":
                return mode.equalsIgnoreCase("Vanilla");
        }
        return super.isVisible(value, name);
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    @Event.Info
    public void onEvent(Event event) {
        final MovementUtil movementUtil = MovementUtil.getInstance();

        if (event instanceof UpdateEvent) {
            switch (mode) {
                case "Vanilla":
                    getPlayer().stepHeight = (float) stepHeight;
                    break;
                case "Spartan424":
                    if(getPlayer().fallDistance != 0) return;
                    if (getPlayer().isCollidedHorizontally && !getPlayer().isOnLadder() && getPlayer().onGround && isMoving() && getPlayer().stepHeight != 1) {
                        getPlayer().jump();
                        getPlayer().onGround = false;
                        getPlayer().stepHeight = 2;
                    } else {
                        getPlayer().stepHeight = 0.5F;
                        if (getPlayer().isCollidedHorizontally && !getPlayer().isOnLadder() && isMoving()) {
                            getPlayer().onGround = true;
                            getPlayer().motionY *= 0.9;
                            movementUtil.setSpeed(0.25);
                        }
                    }
                    break;
                case "Intave13":
                    if (getPlayer().isCollidedHorizontally && !getPlayer().isOnLadder() && getPlayer().onGround && isMoving() && getPlayer().stepHeight != 1) {
                        getPlayer().motionY = 0.408;
                        getPlayer().onGround = false;
                        getPlayer().stepHeight = 1;
                    } else {
                        getPlayer().stepHeight = 0.5F;
                        if (getPlayer().isCollidedHorizontally && !getPlayer().isOnLadder() && isMoving())
                            getPlayer().onGround = true;
                    }
                    break;
                case "Intave13-2":
                    getPlayer().stepHeight = 0.5F;
                    if (getPlayer().isCollidedHorizontally) {
                        if (getPlayer().onGround) getPlayer().motionY = 0.55F;
                        if (isMoving())
                            movementUtil.setSpeed(0.55F);
                    }
                    break;
                case "Mineplex":
                    if (getPlayer().isCollidedHorizontally && !getPlayer().isOnLadder() && getPlayer().onGround && isMoving() && getPlayer().stepHeight != 1) {
                        getPlayer().motionY = 0.408;
                        getPlayer().stepHeight = 1;
                    } else {
                        getPlayer().stepHeight = 0.5F;
                        if (getPlayer().isCollidedHorizontally && !getPlayer().isOnLadder() && isMoving())
                            getPlayer().onGround = true;
                        if (getPlayer().isCollidedHorizontally)
                            getGameSettings().keyBindJump.pressed = true;
                    }
                    break;
                case "AAC3.6.4":
                    if(getPlayer().isCollidedHorizontally) {
                        switch(ticksAAC4) {
                            case 0:
                                if(getPlayer().onGround)
                                    getPlayer().jump();
                                break;
                            case 7:
                                getPlayer().motionY = 0;
                                break;
                            case 8:
                                if(!getPlayer().onGround)
                                    setPosition(getX(), getY() + 1, getZ());
                                break;
                        }
                        ticksAAC4++;
                    }
                    else
                        ticksAAC4 = 0;
                    break;
            }
        }
    }

    @Override
    public void onEnable() {
        ticksAAC4 = 0;
    }

    @Override
    public void onDisable() {
        if(mode.equalsIgnoreCase("Vanilla"))
            getPlayer().stepHeight = 0.5F;
    }
}
