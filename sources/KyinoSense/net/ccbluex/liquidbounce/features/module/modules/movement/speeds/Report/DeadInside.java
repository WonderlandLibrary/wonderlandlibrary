/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report;

import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import obfuscator.NativeMethod;

public class DeadInside
extends SpeedMode {
    public DeadInside() {
        super("inside");
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onMotion() {
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onUpdate() {
        if ((DeadInside.mc.field_71439_g.field_71158_b.field_78900_b != 0.0f || DeadInside.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f) && DeadInside.mc.field_71439_g.field_70122_E) {
            MovementUtils.strafe(MovementUtils.getSpeed() * 1.011f);
            DeadInside.mc.field_71439_g.func_70664_aZ();
        }
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onMove(MoveEvent event) {
    }

    @Override
    public void setMotion(MoveEvent event, double baseMoveSpeed, double d, boolean b) {
    }
}

