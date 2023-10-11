package io.github.raze.modules.collection.movement.flight.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.flight.ModeFlight;
import io.github.raze.utilities.collection.world.MoveUtil;
import org.lwjgl.input.Keyboard;

public class VanillaFlight extends ModeFlight {

    public VanillaFlight() { super("Vanilla"); }

    @Listen
    public void onMotionEvent(EventMotion event) {
        if(event.getState() == Event.State.PRE) {
            mc.thePlayer.motionY = 0;

            if (Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode())) {
                mc.thePlayer.motionY = parent.speed.get().doubleValue();
            }

            if (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
                mc.thePlayer.motionY = -parent.speed.get().doubleValue();
            }

            MoveUtil.setSpeed(parent.speed.get().floatValue());
        }
    }
}
