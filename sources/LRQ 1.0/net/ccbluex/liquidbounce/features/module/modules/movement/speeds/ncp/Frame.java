/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;

public final class Frame
extends SpeedMode {
    private int motionTicks;
    private boolean move;
    private final TickTimer tickTimer = new TickTimer();

    @Override
    public void onMotion() {
        block20: {
            block19: {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP.getMovementInput().getMoveForward() > 0.0f) break block19;
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                if (!(iEntityPlayerSP2.getMovementInput().getMoveStrafe() > 0.0f)) break block20;
            }
            double speed = 4.25;
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.getOnGround()) {
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP3.jump();
                if (this.motionTicks == 1) {
                    this.tickTimer.reset();
                    if (this.move) {
                        IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP4 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP4.setMotionX(0.0);
                        IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP5 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP5.setMotionZ(0.0);
                        this.move = false;
                    }
                    this.motionTicks = 0;
                } else {
                    this.motionTicks = 1;
                }
            } else if (!this.move && this.motionTicks == 1 && this.tickTimer.hasTimePassed(5)) {
                IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP6 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP6.setMotionX(iEntityPlayerSP6.getMotionX() * speed);
                IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP7 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP7.setMotionZ(iEntityPlayerSP7.getMotionZ() * speed);
                this.move = true;
            }
            IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP8 == null) {
                Intrinsics.throwNpe();
            }
            if (!iEntityPlayerSP8.getOnGround()) {
                MovementUtils.strafe$default(0.0f, 1, null);
            }
            this.tickTimer.update();
        }
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
    }

    public Frame() {
        super("Frame");
    }
}

