package io.github.raze.modules.collection.movement.flight.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.flight.ModeFlight;
import io.github.raze.utilities.collection.math.TimeUtil;

public class VulcanGlideFlight extends ModeFlight {

    public VulcanGlideFlight() { super("Vulcan Glide"); }

    private final TimeUtil timer = new TimeUtil();

    @Listen
    public void onMotionEvent(EventMotion event) {
        if(event.getState() == Event.State.PRE) {
            if (timer.elapsed(100, true)) {
                mc.thePlayer.motionY = -0.155f;
            } else {
                mc.thePlayer.motionY = -0.1f;
            }
        }
    }
}
