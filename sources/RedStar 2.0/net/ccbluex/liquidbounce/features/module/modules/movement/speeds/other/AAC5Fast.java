package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\b\u000020B¢J\b0HJ\b0H¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/speeds/other/AAC5Fast;", "Lnet/ccbluex/liquidbounce/features/module/modules/movement/speeds/SpeedMode;", "()V", "onDisable", "", "onUpdate", "Pride"})
public final class AAC5Fast
extends SpeedMode {
    @Override
    public void onUpdate() {
        if (!MovementUtils.isMoving()) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP = SpeedMode.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getOnGround()) {
            IEntityPlayerSP iEntityPlayerSP2 = SpeedMode.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP2.jump();
            IEntityPlayerSP iEntityPlayerSP3 = SpeedMode.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP3.setSpeedInAir(0.0201f);
            SpeedMode.mc.getTimer().setTimerSpeed(0.94f);
        }
        IEntityPlayerSP iEntityPlayerSP4 = SpeedMode.mc.getThePlayer();
        if (iEntityPlayerSP4 == null) {
            Intrinsics.throwNpe();
        }
        if ((double)iEntityPlayerSP4.getFallDistance() > 0.7) {
            IEntityPlayerSP iEntityPlayerSP5 = SpeedMode.mc.getThePlayer();
            if (iEntityPlayerSP5 == null) {
                Intrinsics.throwNpe();
            }
            if ((double)iEntityPlayerSP5.getFallDistance() < 1.3) {
                IEntityPlayerSP iEntityPlayerSP6 = SpeedMode.mc.getThePlayer();
                if (iEntityPlayerSP6 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP6.setSpeedInAir(0.02f);
                SpeedMode.mc.getTimer().setTimerSpeed(1.8f);
            }
        }
    }

    @Override
    public void onDisable() {
        IEntityPlayerSP iEntityPlayerSP = SpeedMode.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        iEntityPlayerSP.setSpeedInAir(0.02f);
        SpeedMode.mc.getTimer().setTimerSpeed(1.0f);
    }

    public AAC5Fast() {
        super("AAC5Fast");
    }
}
