/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report;

import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import obfuscator.NativeMethod;

public class BlocksmcLowHop
extends SpeedMode {
    public BlocksmcLowHop() {
        super("OldBlocksmcLowHop");
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onMotion() {
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onUpdate() {
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onMove(MoveEvent event) {
        if (!(BlocksmcLowHop.mc.field_71439_g.field_70134_J || BlocksmcLowHop.mc.field_71439_g.func_180799_ab() || BlocksmcLowHop.mc.field_71439_g.func_70090_H() || BlocksmcLowHop.mc.field_71439_g.func_70617_f_() || BlocksmcLowHop.mc.field_71439_g.field_70154_o != null || !MovementUtils.isMoving())) {
            BlocksmcLowHop.mc.field_71474_y.field_74314_A.field_74513_e = false;
            if (BlocksmcLowHop.mc.field_71439_g.field_70122_E) {
                BlocksmcLowHop.mc.field_71439_g.func_70664_aZ();
                BlocksmcLowHop.mc.field_71439_g.field_70181_x = 0.0;
                MovementUtils.strafe(0.61f);
                event.setY(0.41999998688698);
            }
            MovementUtils.strafe();
        }
    }

    @Override
    public void setMotion(MoveEvent event, double baseMoveSpeed, double d, boolean b) {
    }
}

