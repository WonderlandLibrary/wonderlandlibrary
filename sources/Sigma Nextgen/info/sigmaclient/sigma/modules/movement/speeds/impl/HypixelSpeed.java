package info.sigmaclient.sigma.modules.movement.speeds.impl;

import info.sigmaclient.sigma.event.player.MoveEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.movement.Speed;
import info.sigmaclient.sigma.modules.movement.speeds.SpeedModule;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import net.minecraft.client.util.InputMappings;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class HypixelSpeed extends SpeedModule {
    boolean wasSlow = false;
    double stair;
    public double fakeMotionY, fakeY;
    private int counter;
    private boolean prevOnGround;

    public HypixelSpeed(Speed speed) {
        super("Hypixel", "Speed for Hypixel", speed);
    }

    @Override
    public void onEnable() {
        wasSlow = false;
        stair = MovementUtils.getBaseMoveSpeed();
        fakeMotionY = 0;
        fakeY = mc.player.getPositionVector().y;
        super.onEnable();
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if (event.isPost()) return;
        if (this.parent.hypixelMode.is("Ground")) {
            if (event.isPre() && !mc.player.isInWater()) {
                if (wasSlow && MovementUtils.isMoving()) {
                    stair *= 0.88;
                    stair = Math.max(MovementUtils.getBaseMoveSpeed(), stair);
                    wasSlow = false;
                    MovementUtils.strafing(stair);
                }
                if (mc.player.onGround && MovementUtils.isMoving()) {
                    mc.player.jump();
                    stair = MovementUtils.getBaseMoveSpeed() * 1.2D;
                    MovementUtils.strafing(stair);
                    wasSlow = true;
                }
                if (stair > MovementUtils.getBaseMoveSpeed() && !wasSlow)
                    stair -= stair / 156;

                stair = Math.max(MovementUtils.getBaseMoveSpeed(), stair);

                if (MovementUtils.isMoving()) {
                } else {
                    wasSlow = false;
                    mc.player.getMotion().x *= 0.05;
                    mc.player.getMotion().z *= 0.05;
                }
            }
        } else if (this.parent.hypixelMode.is("FakeStrafe")) {
            if (!mc.player.isInWater()) {
                fakeY += fakeMotionY;
                mc.player.getPositionVec().y = fakeY;

                if (fakeMotionY > 0) {
                    fakeMotionY *= 0.8;
                    fakeMotionY -= 0.05;
                } else {
                    fakeMotionY -= 0.07;
                }

                if (fakeY < mc.player.getBoundingBox().minY) {
                    fakeY = mc.player.getBoundingBox().minY;
                    mc.player.getPositionVec().y = fakeY;
                }
                if (fakeY <= mc.player.getBoundingBox().minY) {
                    if (MovementUtils.isMoving())
                        fakeMotionY = 0.42;
                    else fakeMotionY = 0;
                }
            }
        } else if (this.parent.hypixelMode.is("Real")) {
            if (!mc.player.isInWater()) {
                if (mc.player.onGround && MovementUtils.isMoving()) {
                    mc.player.getMotion().y = 0.41999998688697815;
                    stair = MovementUtils.getBaseMoveSpeed() * 1.4D;
                    wasSlow = true;
                }
            }
        } else if (this.parent.hypixelMode.is("EternityF")) {
            if (MovementUtils.isMoving()) {
                if (mc.player.isOnGround()) {
                    mc.player.jump();
                    MovementUtils.strafing(0.485);
                }
                if (mc.player.getMotion().y < 0.1 && mc.player.getMotion().y > 0.01) {
                    mc.player.setMotion(mc.player.getMotion().mul(1.005, 1, 1.005));
                }
                if (mc.player.getMotion().y < 0.005 && mc.player.getMotion().y > 0) {
                    mc.player.setMotion(mc.player.getMotion().mul(1.005, 1, 1.005));
                }
                if (mc.player.getMotion().y < 0.01 && mc.player.getMotion().y > -0.03) {
                    mc.player.setMotion(mc.player.getMotion().mul(1.002, 1, 1.002));
                }
            }
            if (InputMappings.isKeyDown(32) && InputMappings.isKeyDown(30)) {
                if (mc.player.getMotion().y < -0.05 && mc.player.getMotion().y > -0.08 && mc.player.hurtTime <= 1) {
                    MovementUtils.strafing(0.15);
                } else if (mc.player.getMotion().y < -0.05 && mc.player.getMotion().y > -0.08) {
                    MovementUtils.strafing(0.15);
                }
            }
            if (mc.player.hurtTime > 6) {
                mc.player.setMotion(mc.player.getMotion().mul(1.007, 1, 1.007));
            }
        }
        super.onUpdateEvent(event);
    }


    @Override
    public void onMoveEvent(MoveEvent event) {
        if(this.parent.hypixelMode.is("Ground")) {
        }else if(this.parent.hypixelMode.is("FakeStrafe")) {
            if (!mc.player.isInWater()) {
                if (mc.player.onGround && MovementUtils.isMoving()) {
                    MovementUtils.strafing(event, MovementUtils.getBaseMoveSpeed() * 1.0f);
                }
            }
        }else if(this.parent.hypixelMode.is("Real")) {
            if (!mc.player.isInWater()) {
                if (wasSlow && MovementUtils.isMoving()) {
                    stair *= 0.8;
                    stair = Math.max(MovementUtils.getBaseMoveSpeed(), stair);
                    wasSlow = false;
                    MovementUtils.strafing(event, stair);
                }
                if (mc.player.onGround && MovementUtils.isMoving()) {
                    MovementUtils.strafing(event, stair);
                }
                if(mc.player.getMotion().y < -0.05 && mc.player.getMotion().y > -0.08 && mc.player.movementInput.moveStrafe != 0){
                    MovementUtils.strafing(event, stair * 0.5);
                }
                if (stair > MovementUtils.getBaseMoveSpeed() && !wasSlow)
                    stair -= stair / 156;

                stair = Math.max(MovementUtils.getBaseMoveSpeed() * 1.2f, stair);

                if (MovementUtils.isMoving()) {
                } else {
                    MovementUtils.strafing(event, 0);
                    wasSlow = false;
                }
            }
        }
        super.onMoveEvent(event);
    }
}
