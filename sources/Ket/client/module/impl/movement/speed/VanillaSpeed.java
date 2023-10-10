package client.module.impl.movement.speed;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.UpdateEvent;
import client.module.impl.movement.Speed;
import client.util.MoveUtil;
import client.value.Mode;

public class VanillaSpeed extends Mode<Speed> {

    public VanillaSpeed(final String name, final Speed parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<UpdateEvent> onUpdate = event -> {
        if (MoveUtil.isMoving() && mc.thePlayer.onGround) mc.thePlayer.jump();
        MoveUtil.strafe(1);
    };
}
