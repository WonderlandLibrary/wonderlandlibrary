package info.sigmaclient.sigma.modules.movement.flys.impl;

import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.movement.Fly;
import info.sigmaclient.sigma.modules.movement.flys.FlyModule;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import top.fl0wowp4rty.phantomshield.annotations.Native;

@Native
public class VeltPVPFly extends FlyModule {
    public VeltPVPFly(Fly fly) {
        super("VeltPVP", "Fly for VeltPVP", fly);
    }
    int timerTick = 0;

    @Override
    public void onEnable() {
        timerTick = 0;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.player.getMotion().y = 0;
        mc.player.getMotion().x = 0;
        mc.player.getMotion().z = 0;
        super.onDisable();
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPost()) return;
        timerTick ++;
        mc.player.getMotion().y = 0;
        if(timerTick > 5) {
            event.y += 0.42;
            MovementUtils.strafing(9.9);
            event.y += 0.5;
            timerTick = 0;
        }else{
            event.cancelable = true;
            MovementUtils.strafing( 0);
        }
        super.onUpdateEvent(event);
    }
}
