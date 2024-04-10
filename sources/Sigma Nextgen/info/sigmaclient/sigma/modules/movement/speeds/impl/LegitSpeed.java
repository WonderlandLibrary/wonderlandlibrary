package info.sigmaclient.sigma.modules.movement.speeds.impl;

import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.movement.Speed;
import info.sigmaclient.sigma.modules.movement.speeds.SpeedModule;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class LegitSpeed extends SpeedModule {
    public LegitSpeed(Speed speed) {
        super("Legit", "Legit speed", speed);
    }

    @Override
    public void onDisable() {
        mc.gameSettings.keyBindJump.pressed = false;
        super.onDisable();
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPost()) return;
        mc.gameSettings.keyBindJump.pressed = MovementUtils.isMoving();
        super.onUpdateEvent(event);
    }
}
