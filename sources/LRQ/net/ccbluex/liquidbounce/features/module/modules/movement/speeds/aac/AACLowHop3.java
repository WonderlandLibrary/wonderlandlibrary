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
    public void onEnable() {
        this.firstJump = true;
    }

    @Override
    public void onMotion() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (MovementUtils.isMoving()) {
            if (thePlayer.getHurtTime() <= 0) {
                if (thePlayer.getOnGround()) {
                    this.waitForGround = false;
                    if (!this.firstJump) {
                        this.firstJump = true;
                    }
                    thePlayer.jump();
                    thePlayer.setMotionY(0.41);
                } else {
                    if (this.waitForGround) {
                        return;
                    }
                    if (thePlayer.isCollidedHorizontally()) {
                        return;
                    }
                    this.firstJump = false;
                    IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
                    iEntityPlayerSP2.setMotionY(iEntityPlayerSP2.getMotionY() - 0.0149);
                }
                if (!thePlayer.isCollidedHorizontally()) {
                    MovementUtils.forward(this.firstJump ? 0.0016 : 0.001799);
                }
            } else {
                this.firstJump = true;
                this.waitForGround = true;
            }
        } else {
            thePlayer.setMotionZ(0.0);
            thePlayer.setMotionX(0.0);
        }
        double speed = MovementUtils.INSTANCE.getSpeed();
        double d = MovementUtils.getDirection();
        IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
        boolean bl = false;
        double d2 = Math.sin(d);
        iEntityPlayerSP3.setMotionX(-(d2 * speed));
        d = MovementUtils.getDirection();
        iEntityPlayerSP3 = thePlayer;
        bl = false;
        d2 = Math.cos(d);
        iEntityPlayerSP3.setMotionZ(d2 * speed);
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
    }

    public AACLowHop3() {
        super("AACLowHop3");
    }
}

