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

public final class SpectreLowHop
extends SpeedMode {
    @Override
    public void onMotion() {
        block8: {
            block7: {
                if (!MovementUtils.isMoving()) break block7;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (!iEntityPlayerSP.getMovementInput().getJump()) break block8;
            }
            return;
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getOnGround()) {
            MovementUtils.strafe(1.1f);
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP2.setMotionY(0.15);
            return;
        }
        MovementUtils.strafe$default(0.0f, 1, null);
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
    }

    public SpectreLowHop() {
        super("SpectreLowHop");
    }
}

