package client.module.impl.movement.speed;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.MoveEvent;
import client.event.impl.other.UpdateEvent;
import client.module.impl.movement.Speed;
import client.util.MoveUtil;
import client.value.Mode;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;

public class MushSpeed extends Mode<Speed> {

    public MushSpeed(final String name, final Speed parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<MoveEvent> onMove = event -> {
    };

    @EventLink
    public final Listener<UpdateEvent> onUpdate = event -> {
        if (MoveUtil.isMoving() && mc.thePlayer.onGround) {

            mc.thePlayer.jump();
            MoveUtil.strafe(MoveUtil.getBaseSpeed() + 0.18F);
        } else MoveUtil.strafe(MoveUtil.getBaseSpeed() + 0.18F);
    };

}
