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

public final class NCPYPort
extends SpeedMode {
    private int jumps;

    @Override
    public void onMotion() {
        block21: {
            block20: {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP.isOnLadder()) break block20;
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP2.isInWater()) break block20;
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP3.isInLava()) break block20;
                IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP4 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP4.isInWeb() || !MovementUtils.isMoving()) break block20;
                IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP5 == null) {
                    Intrinsics.throwNpe();
                }
                if (!iEntityPlayerSP5.isInWater()) break block21;
            }
            return;
        }
        if (this.jumps >= 4) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.getOnGround()) {
                this.jumps = 0;
            }
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getOnGround()) {
            IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP6 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP6.setMotionY(this.jumps <= 1 ? 0.42 : 0.4);
            IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP7 == null) {
                Intrinsics.throwNpe();
            }
            float f = iEntityPlayerSP7.getRotationYaw() * ((float)Math.PI / 180);
            IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP8 == null) {
                Intrinsics.throwNpe();
            }
            double d = iEntityPlayerSP8.getMotionX();
            IEntityPlayerSP iEntityPlayerSP9 = iEntityPlayerSP8;
            int n = 0;
            float f2 = (float)Math.sin(f);
            iEntityPlayerSP9.setMotionX(d - (double)(f2 * 0.2f));
            IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP10 == null) {
                Intrinsics.throwNpe();
            }
            d = iEntityPlayerSP10.getMotionZ();
            iEntityPlayerSP9 = iEntityPlayerSP10;
            n = 0;
            f2 = (float)Math.cos(f);
            iEntityPlayerSP9.setMotionZ(d + (double)(f2 * 0.2f));
            n = this.jumps;
            this.jumps = n + 1;
        } else if (this.jumps <= 1) {
            IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP11 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP11.setMotionY(-5.0);
        }
        MovementUtils.strafe$default(0.0f, 1, null);
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
    }

    public NCPYPort() {
        super("NCPYPort");
    }
}

