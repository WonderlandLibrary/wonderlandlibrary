package client.module.impl.movement.speed;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.StrafeEvent;
import client.event.impl.other.UpdateEvent;
import client.module.impl.movement.Speed;
import client.util.MoveUtil;
import client.value.Mode;

public class HypixelSpeed extends Mode<Speed> {

    public HypixelSpeed(final String name, final Speed parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<UpdateEvent> onUpdate = event -> {
        if (!MoveUtil.isMoving()) return;
        if (mc.thePlayer.onGround) {
            mc.thePlayer.jump();
            MoveUtil.setSpeed(MoveUtil.getBaseSpeed() + 0.2F / 1.2F);
        } else if (mc.thePlayer.hurtTime > 0) MoveUtil.strafe(MoveUtil.getSpeed() / 1.2F);
    };

    @EventLink
    public final Listener<StrafeEvent> onStrafe = event -> {
        event.setYaw(MoveUtil.getYaw());
    };

}
