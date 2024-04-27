package me.aquavit.liquidsense.module.modules.movement.speeds.aquavit;

import me.aquavit.liquidsense.event.events.JumpEvent;
import me.aquavit.liquidsense.event.events.MotionEvent;
import me.aquavit.liquidsense.event.events.MoveEvent;
import me.aquavit.liquidsense.utils.entity.MovementUtils;
import me.aquavit.liquidsense.utils.module.Particles;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.modules.movement.Speed;
import me.aquavit.liquidsense.module.modules.movement.speeds.SpeedMode;

public class Hypixel extends SpeedMode {

    private int stage;
    private double speed;
    private double lastDist;

    public Hypixel() {
        super("Hypixel");
    }

    @Override
    public void onEnable() {
        this.stage = 0;
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onJump(JumpEvent event) {
        if (mc.thePlayer != null && !mc.thePlayer.isSneaking())
            event.cancelEvent();
    }

    @Override
    public void onMotion(MotionEvent event) {
        switch (event.getEventState()) {
            case PRE:
                double xDist = mc.thePlayer.posX - mc.thePlayer.lastTickPosX;
                double zDist = mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ;
                lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
                break;
            case POST:
                if (MovementUtils.isMoving() && MovementUtils.isOnGround(0.01)) {
                    mc.thePlayer.motionY -= 2.4e-6;
                }
                break;
        }
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
        Speed speedModule = (Speed) LiquidSense.moduleManager.getModule(Speed.class);

        if (speedModule.oldHypixel.get()) {
            switch (stage) {
                case 1:
                    if (MovementUtils.isMoving() && mc.thePlayer.onGround) {
                        speed = MovementUtils.getBaseMoveSpeed();
                    }
                    break;
                case 2:
                    if (MovementUtils.isMoving() && mc.thePlayer.onGround) {
                        event.y = MovementUtils.getJumpBoostModifier(speedModule.motionYValue.get().doubleValue(), true);
                        mc.thePlayer.motionY = event.y;
                    }
                    speed = speedModule.movementSpeedValue.get() * MovementUtils.getBaseMoveSpeed();
                    break;
                case 3:
                    speed = lastDist - 0.66 * (lastDist - MovementUtils.getBaseMoveSpeed());
                    break;
                default:
                    if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, mc.thePlayer.motionY, 0.0)).size() > 0
                            || mc.thePlayer.isCollidedVertically && stage > 0) {
                        stage = MovementUtils.isMoving() ? 1 : 0;
                    }
                    speed = lastDist - lastDist / 99;
                    break;
            }
            speed = Math.max(speed, MovementUtils.getBaseMoveSpeed());
            MovementUtils.setMotion(event, speed);
            if (MovementUtils.isMoving()) {
                stage++;
            }
        } else {
            if (MovementUtils.isMoving()) {
                if (mc.thePlayer.onGround && !mc.thePlayer.isInWater()) {
                    stage = 0;
                    event.y = MovementUtils.getJumpBoostModifier(speedModule.motionYValue.get().doubleValue(), true);
                    mc.thePlayer.motionY = event.y;
                }
                stage++;
            } else {
                stage = 0;
            }
            double speed = (getHypixelSpeed(stage) + 0.0331) * Particles.roundToPlace(speedModule.movementSpeedValue.get(), 2) * 0.98;
            MovementUtils.setMotion(event, speed);
        }
    }

    private double getHypixelSpeed(int stage) {
        double speed = 0.64 + MovementUtils.getSpeedEffect() * 0.125;
        double firstValue = 0.4145 + MovementUtils.getSpeedEffect() / 12.5; // speed = 0.417
        double decr = stage / 150.0;
        if (stage == 1) {
            speed = firstValue;
        } else if (stage >= 2) {
            speed = firstValue - decr;
        }
        return Math.max(speed, MovementUtils.getBaseMoveSpeed() + MovementUtils.getSpeedEffect() * 0.028);
    }

}
