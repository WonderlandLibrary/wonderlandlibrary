/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.spartan;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public final class SpartanYPort
extends SpeedMode {
    private int airMoves;

    @Override
    public void onMotion() {
        if (MinecraftInstance.mc.getGameSettings().getKeyBindForward().isKeyDown() && !MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.getOnGround()) {
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP2.jump();
                this.airMoves = 0;
            } else {
                MinecraftInstance.mc.getTimer().setTimerSpeed(1.08f);
                if (this.airMoves >= 3) {
                    IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP3 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP3.setJumpMovementFactor(0.0275f);
                }
                if (this.airMoves >= 4 && (double)this.airMoves % (double)2 == 0.0) {
                    IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP4 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP4.setMotionY((double)-0.32f - 0.009 * Math.random());
                    IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP5 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP5.setJumpMovementFactor(0.0238f);
                }
                int n = this.airMoves;
                this.airMoves = n + 1;
            }
        }
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
    }

    public SpartanYPort() {
        super("SpartanYPort");
    }
}

