package info.sigmaclient.sigma.modules.movement.speeds.impl;

import info.sigmaclient.sigma.event.player.MoveEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.movement.Speed;
import info.sigmaclient.sigma.modules.movement.speeds.SpeedModule;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class BlocksMCSpeed extends SpeedModule {
    boolean wasSlow = false;
    int airTick = 0;
    public BlocksMCSpeed(Speed speed) {
        super("BlocksMC", "Speed for BlocksMC", speed);
    }

    @Override
    public void onEnable() {
        airTick = 0;
        wasSlow = false;
        super.onEnable();
    }

    @Override
    public void onMoveEvent(MoveEvent event) {
        MovementUtils.strafing(event,
                MovementUtils.getBaseMoveSpeed() *
                Math.min(1.2f,
                    Math.max(
                            1.2 - Math.sqrt(airTick / 300f),
                            0.5f
                    )
                )
        );
        if(wasSlow){
            if(parent.timer.isEnable()) {
                mc.timer.setTimerSpeed(1f);
            }
            MovementUtils.strafing(event, MovementUtils.getBaseMoveSpeed());
            wasSlow = false;
        }
        if(airTick == 5){
            if(parent.lowHop.isEnable()) {
                event.setY(mc.player.getMotion().y = event.getY() - 0.15f);
            }
        }
        if (mc.player.onGround) {
            airTick = 0;
        }else{
            airTick ++;
        }
        if(MovementUtils.isMoving()) {
            if (mc.player.onGround) {
                event.setY(mc.player.getMotion().y = 0.41999998688697815);
                MovementUtils.strafing(event, MovementUtils.getBaseMoveSpeed() * 1.5);
                wasSlow = true;
                if(parent.timer.isEnable()) {
                    mc.timer.setTimerSpeed(1.2f);
                }
            }
        }else MovementUtils.strafing(0);
        super.onMoveEvent(event);
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPost()) return;
        super.onUpdateEvent(event);
    }
}
