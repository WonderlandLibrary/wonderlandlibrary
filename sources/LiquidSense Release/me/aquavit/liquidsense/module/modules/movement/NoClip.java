package me.aquavit.liquidsense.module.modules.movement;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;

@ModuleInfo(name = "NoClip", description = "Allows you to freely move through walls (A sandblock has to fall on your head).", category = ModuleCategory.MOVEMENT)
public class NoClip extends Module {

    @Override
    public void onDisable() {
        mc.thePlayer.noClip = false;
    }

    @EventTarget
    public void onUpdate(UpdateEvent event){
        mc.thePlayer.noClip = true;
        mc.thePlayer.fallDistance = 0f;
        mc.thePlayer.onGround = false;

        mc.thePlayer.capabilities.isFlying = false;
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionY = 0;
        mc.thePlayer.motionZ = 0;

        float speed = 0.32f;
        mc.thePlayer.jumpMovementFactor = speed;
        if (mc.gameSettings.keyBindJump.isKeyDown())
            mc.thePlayer.motionY += speed;
        if (mc.gameSettings.keyBindSneak.isKeyDown())
            mc.thePlayer.motionY -= speed;
    }

}
