package client.module.impl.movement.longjump;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.UpdateEvent;
import client.module.impl.movement.LongJump;
import client.module.impl.movement.Speed;
import client.util.MoveUtil;
import client.value.Mode;

public class MushLongJump extends Mode<LongJump> {


    public MushLongJump(final String name, final LongJump parent) {
        super(name, parent);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.timer.timerSpeed = 1.0f;
        MoveUtil.strafe(0 );
    }

    @EventLink
    public final Listener<UpdateEvent> onUpdate = event -> {
        if (MoveUtil.isMoving()) mc.thePlayer.jump();
        mc.timer.timerSpeed = 1.04f;
        MoveUtil.strafe(7.5);
    };
}
