package client.module.impl.movement.speed;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.UpdateEvent;
import client.module.impl.movement.Speed;
import client.util.MoveUtil;
import client.value.Mode;

public class VerusSpeed extends Mode<Speed> {

    public VerusSpeed(final String name, final Speed parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<UpdateEvent> onUpdate = event -> {
        if (!MoveUtil.isMoving()) return;
        if (mc.thePlayer.onGround) {
            mc.thePlayer.jump();
            MoveUtil.strafe(mc.thePlayer.isSprinting() ? 0.556 : 0.556 / 1.3F);
        } else {
            MoveUtil.strafe(mc.thePlayer.isSprinting() ? 0.36 : 0.36 / 1.3F);
        }
    };

}
