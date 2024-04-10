package info.sigmaclient.sigma.modules.movement.flys.impl;

import info.sigmaclient.sigma.event.player.MoveEvent;
import info.sigmaclient.sigma.modules.movement.Fly;
import info.sigmaclient.sigma.modules.movement.flys.FlyModule;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import top.fl0wowp4rty.phantomshield.annotations.Native;

@Native
public class MadcraftFly extends FlyModule {
    public MadcraftFly(Fly fly) {
        super("Madcraft", "Fly for Madcraft", fly);
    }

    @Override
    public void onDisable() {
        mc.player.getMotion().y = 0;
        mc.player.getMotion().x = 0;
        mc.player.getMotion().z = 0;
        super.onDisable();
    }

    @Override
    public void onMoveEvent(MoveEvent event) {
        mc.timer.setTimerSpeed(parent.madCraftTimer.getValue().floatValue());
        MovementUtils.strafing(event, parent.madCraftSpeed.getValue().floatValue());
        event.setY(mc.player.getMotion().y = 0);
        super.onMoveEvent(event);
    }
}
