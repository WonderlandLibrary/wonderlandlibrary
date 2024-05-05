/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.spectre;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public final class SpectreOnGround
extends SpeedMode {
    private int speedUp;

    @Override
    public void onMotion() {
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
        block17: {
            block16: {
                if (!MovementUtils.isMoving()) break block16;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (!iEntityPlayerSP.getMovementInput().getJump()) break block17;
            }
            return;
        }
        if (this.speedUp >= 10) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.getOnGround()) {
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP2.setMotionX(0.0);
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP3.setMotionZ(0.0);
                this.speedUp = 0;
            }
            return;
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getOnGround() && MinecraftInstance.mc.getGameSettings().getKeyBindForward().isKeyDown()) {
            IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP4 == null) {
                Intrinsics.throwNpe();
            }
            float f = iEntityPlayerSP4.getRotationYaw() * ((float)Math.PI / 180);
            IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP5 == null) {
                Intrinsics.throwNpe();
            }
            double d = iEntityPlayerSP5.getMotionX();
            IEntityPlayerSP iEntityPlayerSP6 = iEntityPlayerSP5;
            int n = 0;
            float f2 = (float)Math.sin(f);
            iEntityPlayerSP6.setMotionX(d - (double)(f2 * 0.145f));
            IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP7 == null) {
                Intrinsics.throwNpe();
            }
            d = iEntityPlayerSP7.getMotionZ();
            iEntityPlayerSP6 = iEntityPlayerSP7;
            n = 0;
            f2 = (float)Math.cos(f);
            iEntityPlayerSP6.setMotionZ(d + (double)(f2 * 0.145f));
            IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP8 == null) {
                Intrinsics.throwNpe();
            }
            event.setX(iEntityPlayerSP8.getMotionX());
            event.setY(0.005);
            IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP9 == null) {
                Intrinsics.throwNpe();
            }
            event.setZ(iEntityPlayerSP9.getMotionZ());
            n = this.speedUp;
            this.speedUp = n + 1;
        }
    }

    public SpectreOnGround() {
        super("SpectreOnGround");
    }
}

