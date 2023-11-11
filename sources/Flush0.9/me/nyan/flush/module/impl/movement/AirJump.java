package me.nyan.flush.module.impl.movement;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventUpdate;
import me.nyan.flush.module.Module;

public class AirJump extends Module {
    public AirJump() {
        super("AirJump", Category.MOVEMENT);
    }

    @SubscribeEvent
    public void onUpdate(EventUpdate e) {
        if (!mc.thePlayer.movementInput.jump || mc.thePlayer.jumpTicks != 0) {
            return;
        }
        mc.thePlayer.jump();
        mc.thePlayer.jumpTicks = 10;
    }
}
