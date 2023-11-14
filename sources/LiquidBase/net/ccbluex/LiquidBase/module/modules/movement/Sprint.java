package net.ccbluex.LiquidBase.module.modules.movement;

import net.ccbluex.LiquidBase.event.EventTarget;
import net.ccbluex.LiquidBase.module.Module;
import net.ccbluex.LiquidBase.module.ModuleCategory;
import net.ccbluex.LiquidBase.module.ModuleInfo;

@ModuleInfo(moduleName = "Sprint", moduleCateogry = ModuleCategory.MOVEMENT, moduleDescription = "Forward only ")
public class Sprint extends Module {
    @EventTarget
    public void onEnable(){
        if(!getState())
            return;
        mc.thePlayer.setSprinting(true);
    }
    @Override
    public void onDisable(){
        mc.thePlayer.setSprinting(false);
    }
}
