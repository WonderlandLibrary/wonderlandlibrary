package client.module.impl.player.nofall;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.MotionEvent;
import client.module.impl.player.NoFall;
import client.value.Mode;

public class CollisionNoFall extends Mode<NoFall> {

    public CollisionNoFall(final String name, final NoFall parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<MotionEvent> onMotion = event -> {
        if (mc.thePlayer.fallDistance > 3.0F) {
            mc.thePlayer.motionY = -(mc.thePlayer.posY - (mc.thePlayer.posY - (mc.thePlayer.posY % 0.015625)));
            event.setOnGround(true);
            mc.thePlayer.fallDistance = 0.0F;
        }
    };
}
