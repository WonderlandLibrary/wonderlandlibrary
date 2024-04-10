package info.sigmaclient.sigma.modules.movement.speeds.impl;

import info.sigmaclient.sigma.event.player.MoveEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.movement.Speed;
import info.sigmaclient.sigma.modules.movement.speeds.SpeedModule;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class StrafeSpeed extends SpeedModule {
    public StrafeSpeed(Speed speed) {
        super("Strafe", "Strafe speed", speed);
    }

    @Override
    public void onMoveEvent(MoveEvent event) {
        MovementUtils.strafing(event, MovementUtils.getSpeed());
        super.onMoveEvent(event);
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPost()) return;
        if(MovementUtils.isMoving()) {
            if (mc.player.onGround) {
                mc.player.jump();
            }
        }
        super.onUpdateEvent(event);
    }
}
