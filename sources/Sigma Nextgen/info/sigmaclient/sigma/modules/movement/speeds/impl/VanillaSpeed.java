package info.sigmaclient.sigma.modules.movement.speeds.impl;

import info.sigmaclient.sigma.event.player.MoveEvent;
import info.sigmaclient.sigma.modules.movement.Speed;
import info.sigmaclient.sigma.modules.movement.speeds.SpeedModule;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class VanillaSpeed extends SpeedModule {
    public VanillaSpeed(Speed speed) {
        super("Vanilla", "Speed for Vanilla", speed);
    }

    @Override
    public void onMoveEvent(MoveEvent event) {
        MovementUtils.strafing(event, parent.speed.getValue().floatValue() * 0.4f);
        super.onMoveEvent(event);
    }
}
