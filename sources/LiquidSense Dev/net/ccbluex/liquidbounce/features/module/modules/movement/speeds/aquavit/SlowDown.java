/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aquavit;

import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

import java.util.Random;

public class SlowDown extends SpeedMode {
    static Minecraft mc  = Minecraft.getMinecraft();

    public SlowDown() {
        super("SlowDown");
    }
    private int level = 1;
    private double moveSpeed = 0.2873;
    private double lastDist;
    private Random random = new Random();
    private double speed = 0.07999999821186066D;

    @Override
    public void onEnable() {
        mc.timer.timerSpeed = 1F;
        level = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, mc.thePlayer.motionY, 0.0)).size() > 0 || mc.thePlayer.isCollidedVertically ? 1 : 4;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1F;
        moveSpeed = getBaseMoveSpeed();
        level = 0;
    }

    @Override
    public void onJump(JumpEvent event) {
    }

    @Override
    public void onMotion(MotionEvent event) {
        double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
        double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
        lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    }

    @Override
    public void onUpdate() {
        if (this.mc.thePlayer.isInWater()
                || mc.thePlayer.isSneaking()
                || mc.thePlayer.getHealth() < 0.0F) {
            return;
        }
        if(mc.thePlayer.onGround && canZoom()) {
            mc.thePlayer.jump();
        }
    }

    @Override
    public void onMove(MoveEvent event) {
        if (this.mc.thePlayer.isInWater() || mc.thePlayer.isSneaking() || mc.thePlayer.getHealth() < 0.0F)
        { return; }
        switch (level) {
            case -1: {
                if (canZoom()) {
                    lastDist = 0.0f;
                    level = 0;
                    break;
                }
            }
        }
        int Take;
        for (int i = 0; i < 4; i++) {
            if (i == 1) {
                i = 1;
                if (level == i && canZoom()) {
                    ++level;
                    this.moveSpeed = 1.0353000025 *  getBaseMoveSpeed() - 0.011;
                }
            } else if (i == 2) {
                i = 2;
                if (level == i && canZoom()) {
                    this.moveSpeed *= 1.599991;
                } else if (level == 3) {
                    for(float s = 0.6666f; s > 0.66; s-=0.0001) {
                        final double difference = (mc.thePlayer.isCollidedVertically ? 1.0E-4-Math.random() : 1.0E-5 + Math.random()) + s * (this.lastDist - getBaseMoveSpeed());
                        moveSpeed = lastDist - difference;
                    }
                } else {
                    if (mc.thePlayer.isCollidedVertically || level > 0)
                        level =(mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) ? 1 : 0 ;
                    moveSpeed = lastDist - lastDist / 159;
                }
            }

        }
        this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
        if (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) {
            if (level > 0) {
                setMotion(event, moveSpeed,RandomUtils.nextInt(89 /2 , 90 / 2) , 90);
            }
            ++level;
        }
    }

    private double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if(mc.thePlayer.isPotionActive(Potion.moveSpeed))
            baseSpeed *= 1.0 + 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        return baseSpeed;
    }

    private boolean canZoom() {
        if (((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F)) && this.mc.thePlayer.onGround) {
            return true;
        }
        return false;
    }

    private void setMotion(MoveEvent em, double speed, float tick, float tick3) {
        double forward = Minecraft.getMinecraft().thePlayer.movementInput.moveForward;
        double strafe = Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe;
        float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            em.setX(0.0);
            em.setZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float) (forward > 0.0 ? -tick : tick);
                } else if (strafe < 0.0) {
                    yaw += (float) (forward > 0.0 ? tick : -tick);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0f;
                } else if (forward < 0.0) {
                    forward = -1.0f;
                }
            }
            em.setX(forward * speed * Math.cos(Math.toRadians(yaw + tick3))
                    + strafe * speed * Math.sin(Math.toRadians(yaw + tick3)));
            em.setZ(forward * speed * Math.sin(Math.toRadians(yaw + tick3))
                    - strafe * speed * Math.cos(Math.toRadians(yaw + tick3)));
        }
    }

}
