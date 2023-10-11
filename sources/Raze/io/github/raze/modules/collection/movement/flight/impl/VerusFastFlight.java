package io.github.raze.modules.collection.movement.flight.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.flight.ModeFlight;
import io.github.raze.utilities.collection.visual.ChatUtil;
import io.github.raze.utilities.collection.world.MoveUtil;

public class VerusFastFlight extends ModeFlight {

    public VerusFastFlight() { super("Verus Fast"); }

    private int dmgTicks = 1;

    @Listen
    public void onMotionEvent(EventMotion event) {
        if(event.getState() == Event.State.PRE) {
            if(mc.thePlayer.hurtTime == 1) {
                dmgTicks = 1;
            } else {
                dmgTicks++;
            }

            if(16 > dmgTicks) {
                MoveUtil.strafe(6);
            }

            if(dmgTicks != 0 && dmgTicks != 1) {
                mc.thePlayer.motionY = 0;
                mc.thePlayer.onGround = true;
                event.setOnGround(true);
                MoveUtil.strafe((float) MoveUtil.getSpeed());
                mc.thePlayer.speedInAir = (float) (0.02 + Math.random() / 100);
            }

        }
    }

    @Override
    public void onDisable() {
        mc.thePlayer.speedInAir = 0.02F;
        dmgTicks = 0;
        super.onDisable();
    }

    @Override
    public void onEnable() {
        ChatUtil.addChatMessage("NOTE: You have to take damage!");
        mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + 3.5 + Math.random(), mc.thePlayer.posZ);
        super.onEnable();
    }

}
