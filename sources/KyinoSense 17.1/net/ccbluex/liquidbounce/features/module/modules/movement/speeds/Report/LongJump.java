/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report;

import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import obfuscator.NativeMethod;

public class LongJump
extends SpeedMode {
    public LongJump() {
        super("LongJump");
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onMotion() {
        if (LongJump.mc.field_71439_g.func_70090_H()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            if (LongJump.mc.field_71439_g.field_70122_E) {
                LongJump.mc.field_71439_g.func_70664_aZ();
            } else {
                MovementUtils.strafe(MovementUtils.getSpeed() * 1.311f);
            }
        } else {
            LongJump.mc.field_71439_g.field_70159_w = 0.0;
            LongJump.mc.field_71439_g.field_70179_y = 0.0;
        }
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onUpdate() {
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onMove(MoveEvent event) {
    }

    @Override
    public void setMotion(MoveEvent event, double baseMoveSpeed, double d, boolean b) {
    }
}

