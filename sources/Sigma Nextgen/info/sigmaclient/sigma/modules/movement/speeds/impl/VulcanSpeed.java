package info.sigmaclient.sigma.modules.movement.speeds.impl;

import info.sigmaclient.sigma.event.player.MoveEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.movement.Speed;
import info.sigmaclient.sigma.modules.movement.speeds.SpeedModule;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class VulcanSpeed extends SpeedModule {
    boolean wasSlow = false;
    int airTick = 0;
    public VulcanSpeed(Speed speed) {
        super("Vulcan", "Speed for Vulcan", speed);
    }

    @Override
    public void onEnable() {
        airTick = 0;
        wasSlow = false;
        super.onEnable();
    }

    @Override
    public void onMoveEvent(MoveEvent event) {
        if(MovementUtils.getSpeed() <= 0.222){
            MovementUtils.strafing(event, 0.222);
        }
        super.onMoveEvent(event);
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPost()) return;
        if(wasSlow){
            if(parent.timer.isEnable()) {
                mc.timer.setTimerSpeed(1f);
            }
            MovementUtils.strafing(MovementUtils.getBaseMoveSpeed());
            wasSlow = false;
        }
        if(airTick == 5){
            if(parent.lowHop.isEnable()) {
                mc.player.getMotion().y -= 0.15;
            }
        }
        if (mc.player.onGround) {
            airTick = 0;
        }else{
            airTick ++;
        }
        if(MovementUtils.isMoving()) {
            if (mc.player.onGround) {
                mc.player.getMotion().y = 0.41999998688697815;
                MovementUtils.strafing(MovementUtils.getBaseMoveSpeed() * 1.5);
                wasSlow = true;
                if(parent.timer.isEnable()) {
                    mc.timer.setTimerSpeed(1.2f);
                }
            }
        }else MovementUtils.strafing(0);
        super.onUpdateEvent(event);
    }
}
