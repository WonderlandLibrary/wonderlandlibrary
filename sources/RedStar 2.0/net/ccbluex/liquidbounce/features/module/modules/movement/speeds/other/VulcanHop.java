package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\u0000\u000020B¢J\b0H¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/speeds/other/VulcanHop;", "Lnet/ccbluex/liquidbounce/features/module/modules/movement/speeds/SpeedMode;", "()V", "onUpdate", "", "Pride"})
public final class VulcanHop
extends SpeedMode {
    @Override
    public void onUpdate() {
        SpeedMode.mc.getTimer().setTimerSpeed(1.0f);
        IEntityPlayerSP iEntityPlayerSP = SpeedMode.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        float f = iEntityPlayerSP.getMovementInput().getMoveStrafe();
        boolean bl = false;
        if (Math.abs(f) < 0.1f) {
            IEntityPlayerSP iEntityPlayerSP2 = SpeedMode.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP2.setJumpMovementFactor(0.0265f);
        } else {
            IEntityPlayerSP iEntityPlayerSP3 = SpeedMode.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP3.setJumpMovementFactor(0.0244f);
        }
        IEntityPlayerSP iEntityPlayerSP4 = SpeedMode.mc.getThePlayer();
        if (iEntityPlayerSP4 == null) {
            Intrinsics.throwNpe();
        }
        if (!iEntityPlayerSP4.getOnGround()) {
            SpeedMode.mc.getGameSettings().getKeyBindJump().setPressed(SpeedMode.mc.getGameSettings().getKeyBindJump().isKeyDown());
        }
        if (MovementUtils.INSTANCE.getSpeed() < 0.215f) {
            MovementUtils.strafe(0.215f);
        }
        IEntityPlayerSP iEntityPlayerSP5 = SpeedMode.mc.getThePlayer();
        if (iEntityPlayerSP5 == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP5.getOnGround() && MovementUtils.isMoving()) {
            SpeedMode.mc.getTimer().setTimerSpeed(1.25f);
            SpeedMode.mc.getGameSettings().getKeyBindJump().setPressed(false);
            IEntityPlayerSP iEntityPlayerSP6 = SpeedMode.mc.getThePlayer();
            if (iEntityPlayerSP6 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP6.jump();
            MovementUtils.strafe$default(0.0f, 1, null);
            if (MovementUtils.INSTANCE.getSpeed() < 0.5f) {
                MovementUtils.strafe(0.4849f);
            }
        } else if (!MovementUtils.isMoving()) {
            SpeedMode.mc.getTimer().setTimerSpeed(1.0f);
            IEntityPlayerSP iEntityPlayerSP7 = SpeedMode.mc.getThePlayer();
            if (iEntityPlayerSP7 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP7.setMotionX(0.0);
            IEntityPlayerSP iEntityPlayerSP8 = SpeedMode.mc.getThePlayer();
            if (iEntityPlayerSP8 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP8.setMotionZ(0.0);
        }
    }

    public VulcanHop() {
        super("VulcanHop");
    }
}
