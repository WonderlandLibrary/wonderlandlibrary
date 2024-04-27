package me.aquavit.liquidsense.module.modules.movement;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;

@ModuleInfo(name = "Freeze", description = "Allows you to stay stuck in mid air.", category = ModuleCategory.MOVEMENT)
public class Freeze extends Module {

    @EventTarget
    public void onUpdate(UpdateEvent event){
        mc.thePlayer.isDead = true;
        mc.thePlayer.rotationYaw = mc.thePlayer.cameraYaw;
        mc.thePlayer.rotationPitch = mc.thePlayer.cameraPitch;
    }

    @Override
    public void onDisable() {
        mc.thePlayer.isDead = false;
    }
}
