/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public final class AACLowHop3
extends SpeedMode {
    private boolean firstJump;
    private boolean waitForGround;

    @Override
    public void onMove(MoveEvent moveEvent) {
    }

    @Override
    public void onMotion() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (MovementUtils.isMoving()) {
            if (iEntityPlayerSP2.getHurtTime() <= 0) {
                if (iEntityPlayerSP2.getOnGround()) {
                    this.waitForGround = false;
                    if (!this.firstJump) {
                        this.firstJump = true;
                    }
                    iEntityPlayerSP2.jump();
                    iEntityPlayerSP2.setMotionY(0.41);
                } else {
                    if (this.waitForGround) {
                        return;
                    }
                    if (iEntityPlayerSP2.isCollidedHorizontally()) {
                        return;
                    }
                    this.firstJump = false;
                    IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
                    iEntityPlayerSP3.setMotionY(iEntityPlayerSP3.getMotionY() - 0.0149);
                }
                if (!iEntityPlayerSP2.isCollidedHorizontally()) {
                    MovementUtils.forward(this.firstJump ? 0.0016 : 0.001799);
                }
            } else {
                this.firstJump = true;
                this.waitForGround = true;
            }
        } else {
            iEntityPlayerSP2.setMotionZ(0.0);
            iEntityPlayerSP2.setMotionX(0.0);
        }
        double d = MovementUtils.INSTANCE.getSpeed();
        double d2 = MovementUtils.getDirection();
        IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP2;
        boolean bl = false;
        double d3 = Math.sin(d2);
        iEntityPlayerSP4.setMotionX(-(d3 * d));
        d2 = MovementUtils.getDirection();
        iEntityPlayerSP4 = iEntityPlayerSP2;
        bl = false;
        d3 = Math.cos(d2);
        iEntityPlayerSP4.setMotionZ(d3 * d);
    }

    public AACLowHop3() {
        super("AACLowHop3");
    }

    @Override
    public void onEnable() {
        this.firstJump = true;
    }

    @Override
    public void onUpdate() {
    }
}

