package koks.module.movement;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.api.utils.MovementUtil;
import koks.event.NoClipEvent;
import koks.event.UpdateEvent;

@Module.Info(name = "Dolphin", description = "you can jump like a dolphine", category = Module.Category.MOVEMENT)
public class Dolphin extends Module {

    public boolean wasInWater;
    public double posY;

    @Value(name = "Mode", modes = {"Intave13", "Karhu2.1.9 167", "Matrix6.6.1"})
    public String mode = "Intave13";

    @Value(name = "Intave13-Height", displayName = "Height", minimum = 0, maximum = 5)
    public double intave13Height = 3;

    @Value(name = "Matrix6.6.1-Boost", displayName = "Boost", minimum = 0, maximum = 3.9)
    public double matrix661Boost = 1.9;

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        if(name.contains("-")) {
            final String[] split = name.split("-");
            return split[0].equalsIgnoreCase(mode);
        }
        return super.isVisible(value, name);
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    @Event.Info
    public void onEvent(Event event) {
        final MovementUtil movementUtil = MovementUtil.getInstance();
        switch (mode) {
            case "Matrix6.6.1":
                if(event instanceof UpdateEvent) {
                    if (getPlayer().isInWater()) {
                        if (!wasInWater && getPlayer().onGround) {
                            getPlayer().motionY = 1;
                            movementUtil.setSpeed(matrix661Boost, getYaw(), false);
                            wasInWater = true;
                        }
                    } else if (getPlayer().onGround) {
                        wasInWater = false;
                    }
                }
                break;
            case "Karhu2.1.9 167":
                if(event instanceof UpdateEvent) {
                    if (getPlayer().isInWater()) {
                        getPlayer().motionY = 0.8;
                        if (isMoving())
                            movementUtil.setSpeed(0.4);
                    }
                }
                break;
            case "Intave13":
                if(event instanceof final NoClipEvent noClipEvent) {
                    if(wasInWater) {
                        if (Math.abs(getY() - posY) <= intave13Height) {
                            noClipEvent.setNoClip(true);
                        }
                    }
                }
                if (event instanceof UpdateEvent) {
                    if(getPlayer().isInWater()) {
                        wasInWater = true;
                        posY = getY();
                    }

                    if(wasInWater) {
                        if(Math.abs(getY() - posY) <= intave13Height) {
                            getPlayer().motionY = 0.25;
                            getPlayer().onGround = true;
                        } else {
                            wasInWater = false;
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onEnable() {
        wasInWater = false;
    }

    @Override
    public void onDisable() {

    }

}
