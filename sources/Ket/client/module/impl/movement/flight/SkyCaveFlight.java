package client.module.impl.movement.flight;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.MotionEvent;
import client.module.impl.movement.Flight;
import client.util.MoveUtil;
import client.value.Mode;

public class SkyCaveFlight extends Mode<Flight> {

    private int ticks;

    public SkyCaveFlight(final String name, final Flight parent) {
        super(name, parent);
    }

    @Override
    public void onEnable() {
        ticks = 0;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
    }

    @EventLink
    public final Listener<MotionEvent> onMotion = event -> {
        if (mc.thePlayer.onGround) mc.thePlayer.jump(); else {
            mc.thePlayer.motionY = 0.0;
            ticks++;
            switch (ticks) {
                case 1: {
                    MoveUtil.strafe(2.5);
                    break;
                }
                case 7: {
                    mc.timer.timerSpeed = 1.5F;
                    MoveUtil.strafe(1.34);
                    break;
                }
                case 45: {
                    mc.timer.timerSpeed = 1.0F;
                    MoveUtil.strafe(0.24);
                    break;
                }
            }
        }
    };
}
