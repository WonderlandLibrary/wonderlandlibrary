package koks.manager.module.impl.movement;

import god.buddy.aot.BCompiler;
import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;

@ModuleInfo(name = "Step", description = "Goes up blocks automatically", category = Module.Category.MOVEMENT)
public class Step extends Module {

    Setting mode = new Setting("Mode", new String[]{"Vanilla", "Intave","Intave2", "Mineplex"}, "Vanilla", this);
    final Setting stepHeight = new Setting("Step Height", 4, 1, 4, false, this);

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventUpdate) {
            String extra = mode.getCurrentMode().equalsIgnoreCase("Vanilla") ? " [" + stepHeight.getCurrentValue() + "]" : "";
            setInfo(mode.getCurrentMode() + extra);
        }

        switch (mode.getCurrentMode()) {
            case "Vanilla":
                if (event instanceof EventUpdate) getPlayer().stepHeight = stepHeight.getCurrentValue();
                break;
            case "Intave":
                if (event instanceof EventUpdate) {
                    if (getPlayer().isCollidedHorizontally && !getPlayer().isOnLadder() && getPlayer().onGround && isMoving() && getPlayer().stepHeight != 1) {
                        getPlayer().motionY = 0.408;
                        getPlayer().onGround = false;
                        getPlayer().stepHeight = 1;
                    } else {
                        getPlayer().stepHeight = 0.5F;
                        if (getPlayer().isCollidedHorizontally && !getPlayer().isOnLadder() && isMoving())
                            getPlayer().onGround = true;
                    }
                }
                break;
            case "Intave2":
                if(event instanceof EventUpdate) {
                    getPlayer().stepHeight = 0.5F;
                    if(getPlayer().isCollidedHorizontally) {
                        if(getPlayer().onGround) getPlayer().motionY = 0.55F;
                        movementUtil.setSpeed(0.55F, true);
                    }
                }
                break;
            case "Mineplex": {
                if (event instanceof EventUpdate) {
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
        switch (mode.getCurrentMode()) {
            case "Vanilla":
                getPlayer().stepHeight = 0.6f;
                break;
        }
    }
}
