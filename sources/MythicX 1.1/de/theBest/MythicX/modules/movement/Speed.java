package de.theBest.MythicX.modules.movement;

import de.theBest.MythicX.events.EventUpdate;
import de.theBest.MythicX.modules.Category;
import de.theBest.MythicX.modules.Module;
import eventapi.EventTarget;

import java.awt.*;

public class Speed extends Module {
    private boolean wasOnGround;

    public Speed() {
        super("Speed", Type.Movement, 0, Category.MOVEMENT, Color.red, "Increases your speed");
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {

        if (mc.thePlayer.onGround && !wasOnGround)
            mc.thePlayer.jump();

        if (mc.thePlayer.motionX != 0.0 || mc.thePlayer.motionZ != 0.0) {
            if (!mc.thePlayer.onGround) {
                mc.timer.timerSpeed = 1.2F;
            }
        } else
            mc.timer.timerSpeed = 1.0F;

        wasOnGround = mc.thePlayer.onGround;
    }

    @Override
    public void onEnable() {
        wasOnGround = false;
    }

    public void onDisable(){
        mc.timer.timerSpeed = 1.0F;
    }
}