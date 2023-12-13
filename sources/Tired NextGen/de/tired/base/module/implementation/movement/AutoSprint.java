package de.tired.base.module.implementation.movement;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.guis.newclickgui.setting.impl.BooleanSetting;
import de.tired.util.hook.PlayerHook;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.UpdateEvent;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;

@ModuleAnnotation(name = "AutoSprint", category = ModuleCategory.MOVEMENT, clickG = "Sprinting automatically")
public class AutoSprint extends Module {

    private final BooleanSetting omniSprint = new BooleanSetting("OmniSprint", this, false);

    private final BooleanSetting checkWalk = new BooleanSetting("CheckWalk", this, false);


    @EventTarget
    public void onUpdate(UpdateEvent e) {
        if (omniSprint.getValue()) {
            if (MC.gameSettings.keyBindRight.pressed ) {
                MC.thePlayer.setSprinting(true);
                PlayerHook.increaseSpeedWithStrafe(PlayerHook.getSpeed(), MC.thePlayer.rotationYaw);
            } else {
                if (checkWalk.getValue() && PlayerHook.isMoving())
                    MC.thePlayer.setSprinting(true);
                else
                    MC.thePlayer.setSprinting(true);
            }
        } else {
            if (checkWalk.getValue() && PlayerHook.isMoving())
                MC.thePlayer.setSprinting(true);
            else if (!checkWalk.getValue())
                MC.thePlayer.setSprinting(true);
        }
    }

    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {

    }
}
