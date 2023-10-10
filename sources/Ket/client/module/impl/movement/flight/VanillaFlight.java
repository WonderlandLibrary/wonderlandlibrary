package client.module.impl.movement.flight;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.MotionEvent;
import client.event.impl.other.UpdateEvent;
import client.module.impl.movement.Flight;
import client.util.MoveUtil;
import client.value.Mode;
import client.value.impl.NumberValue;

public class VanillaFlight extends Mode<Flight> {

    private boolean up;

    public VanillaFlight(final String name, final Flight parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<MotionEvent> onUpdate = event -> {

        final double speed = getParent().getSpeed().getValue().doubleValue();
        if (mc.thePlayer.onGround) {
            mc.thePlayer.jump();
            mc.thePlayer.motionY = 0.42f;

        } else {
            mc.thePlayer.motionY = (mc.gameSettings.keyBindJump.isKeyDown() ? 1 : 0) - (mc.gameSettings.keyBindSneak.isKeyDown() ? 1 : 0);
            if (mc.thePlayer.motionY == 0) {
                mc.thePlayer.motionY = up ? 0.0626 : -0.0626;
                up = !up;
            }
            MoveUtil.stop();
            MoveUtil.strafe(speed);
        }
    };
}
