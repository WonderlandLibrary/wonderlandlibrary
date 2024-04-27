package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aquavit;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.*;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;

import java.util.List;

public class NewHypixelHop extends SpeedMode implements Listenable {
    public NewHypixelHop() {
        super("NewHypixelHop");
        LiquidBounce.eventManager.registerListener(this);
    }

    @Override
    public void onJump(JumpEvent event) {
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onMove(MoveEvent event) {

    }

    int stage;
    double speed, lastDist;

    @Override
    public void onEnable() {
        MovementUtils.SpeedSetMotion(MovementUtils.getBaseMoveSpeed() * 0.8);
        mc.timer.timerSpeed = 1;
    }


    @Override
    public void onMotion(MotionEvent event){
        switch (event.getEventState()) {
            case PRE:
                if (mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround) {
                    mc.thePlayer.motionY = 1.0E-14;
                }
                break;
            case POST:
                double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
                double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
                lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
                break;
        }
    }

    @EventTarget
    public void onMoveEvent(MoveEvent event) {
        boolean canTimerBoost = true;
        Speed speedModule = (Speed) LiquidBounce.moduleManager.getModule(Speed.class);

        if (speedModule.stopTicks > 0)
            return;

        if (!MovementUtils.isMoving() || mc.thePlayer.onGround || stage == 1 && mc.thePlayer.isCollidedVertically) {
            speed = MovementUtils.getBaseMoveSpeed();
        }

        if (mc.thePlayer.onGround && MovementUtils.isMoving() && stage == 2) {
            if (canTimerBoost) mc.timer.timerSpeed = 1.0F;
            if (speedModule.newHypixelHopMode.get().toLowerCase().equals("slowhop") && !mc.thePlayer.isCollidedHorizontally) {
                event.setY(0.4);
            } else {
                mc.thePlayer.jump();
                event.setY(mc.thePlayer.motionY = 0.4074196 + MovementUtils.getJumpEffect() * 0.1);
            }
            speed *= speedModule.newHypixelHopMode.get().toLowerCase().equals("slowhop") ? 1.529 : 1.95;
            if (canTimerBoost) mc.timer.timerSpeed = 1.35f;
        } else if (stage == 3) {
            double diff = 0.66 * (lastDist - MovementUtils.getBaseMoveSpeed());
            speed = lastDist - diff;
            if (canTimerBoost) mc.timer.timerSpeed = 1.25f;
        } else {
            if(stage <= 6 && (!speedModule.newHypixelHopMode.get().toLowerCase().equals("slowhop") || event.getY() > 0.4)) {
                event.setY(getMotion(event, stage - 4));
            }
            if (canTimerBoost) mc.timer.timerSpeed = 1.0F;
            List collidingList = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, mc.thePlayer.motionY, 0.0));
            if ((collidingList.size() > 0 || mc.thePlayer.isCollidedVertically || mc.thePlayer.isCollidedHorizontally) && stage > 0) {
                stage = MovementUtils.isMoving() ? 1 : 0;
            }
            speed = lastDist - lastDist / 159.0D;
        }
        speed = Math.max(speed, MovementUtils.getBaseMoveSpeed());

        if (stage > 0 && MovementUtils.isMoving()) {
            MovementUtils.setMotion(event, speed * (1 + mc.thePlayer.hurtTime * speedModule.newHypixelHopDamageBoostValue.get() / 20));
        }

        ++stage;
    }

    public double getMotion(MoveEvent event, int stage) {
        if (stage == 0) {
            return event.getY() * 0.99;
        } else if(stage == 1) {
            return event.getY() * 0.985;
        } else if(stage == 2) {
            return event.getY() * 0.98;
        }
        return event.getY();
    }

    @Override
    public boolean handleEvents() {
        Speed speedModule = (Speed) LiquidBounce.moduleManager.getModule(Speed.class);
        return speedModule.getState() && speedModule.modeValue.get().equals(modeName);
    }
}
