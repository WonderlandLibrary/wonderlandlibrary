package me.aquavit.liquidsense.module.modules.movement.speeds.aquavit;

import me.aquavit.liquidsense.event.events.JumpEvent;
import me.aquavit.liquidsense.event.events.MotionEvent;
import me.aquavit.liquidsense.event.events.MoveEvent;
import me.aquavit.liquidsense.module.modules.movement.speeds.SpeedMode;
import me.aquavit.liquidsense.utils.entity.MovementUtils;

public class AAC4Hop extends SpeedMode {
    public AAC4Hop() {
        super("AAC4Hop");
    }

    @Override
    public void onMotion(MotionEvent event) {
    }

    @Override
    public void onJump(JumpEvent event) {
    }

    @Override
    public void onUpdate() {
        if(mc.thePlayer.isInWater())
            return;

        if(MovementUtils.isMoving()) {
            if(mc.thePlayer.onGround) {
                mc.thePlayer.jump();
                mc.thePlayer.speedInAir = 0.0201f;
                mc.timer.timerSpeed = 0.94f;
            }
            if(mc.thePlayer.fallDistance > 0.7 && mc.thePlayer.fallDistance < 1.3) {
                mc.thePlayer.speedInAir = 0.02f;
                mc.timer.timerSpeed = 1.8f;
            }
        }else{
            mc.thePlayer.motionX = 0D;
            mc.thePlayer.motionZ = 0D;
        }
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1;
        mc.thePlayer.speedInAir = 0.02f;
    }
    @Override
    public void onMove(MoveEvent event) {
    }
}

