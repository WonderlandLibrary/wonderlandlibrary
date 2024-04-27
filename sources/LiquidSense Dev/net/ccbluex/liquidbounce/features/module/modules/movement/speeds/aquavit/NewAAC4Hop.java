package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aquavit;

import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public class NewAAC4Hop extends SpeedMode {
    public NewAAC4Hop() {
        super("NewAAC4Hop");
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
                mc.gameSettings.keyBindJump.pressed = false;
                mc.thePlayer.jump();
            }
            if(!mc.thePlayer.onGround && mc.thePlayer.fallDistance <= 0.1) {
                mc.thePlayer.speedInAir = 0.02f;
                mc.timer.timerSpeed = 1.4f;
            }
            if(mc.thePlayer.fallDistance > 0.1 && mc.thePlayer.fallDistance < 1.3) {
                mc.thePlayer.speedInAir = 0.0205f;
                mc.timer.timerSpeed = 0.65f;
            }
            if(mc.thePlayer.fallDistance >= 1.3) {
                mc.timer.timerSpeed = 1;
                mc.thePlayer.speedInAir = 0.02f;
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

