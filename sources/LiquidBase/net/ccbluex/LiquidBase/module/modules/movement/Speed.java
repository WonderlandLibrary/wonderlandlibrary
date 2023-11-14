package net.ccbluex.LiquidBase.module.modules.movement;

import net.ccbluex.LiquidBase.event.EventTarget;
import net.ccbluex.LiquidBase.event.events.MotionUpdateEvent;
import net.ccbluex.LiquidBase.module.Module;
import net.ccbluex.LiquidBase.module.ModuleCategory;
import net.ccbluex.LiquidBase.module.ModuleInfo;

/**
 * Project: LiquidBase
 * -----------------------------------------------------------
 * Copyright © 2017 | CCBlueX | All rights reserved.
 */
@ModuleInfo(moduleName = "Speed", moduleDescription = "Simple YPort Speed", moduleCateogry = ModuleCategory.MOVEMENT)
public class Speed extends Module {

    @EventTarget
    public void onMotion(MotionUpdateEvent event) {
        if(getState() && event.getState() == MotionUpdateEvent.State.POST) {
            if(mc.thePlayer.onGround)
                mc.thePlayer.jump();
            else
                mc.thePlayer.motionY = -1;
        }
    }
}