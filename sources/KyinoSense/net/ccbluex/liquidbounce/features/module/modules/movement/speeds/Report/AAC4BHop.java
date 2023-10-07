/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report;

import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import obfuscator.NativeMethod;

public class AAC4BHop
extends SpeedMode {
    public AAC4BHop() {
        super("AAC4.4.0Bhop");
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onMotion() {
        if (AAC4BHop.mc.field_71439_g.func_70090_H()) {
            return;
        }
        if (AAC4BHop.mc.field_71439_g.field_70701_bs > 0.0f) {
            if (AAC4BHop.mc.field_71439_g.field_70122_E) {
                AAC4BHop.mc.field_71439_g.func_70664_aZ();
                AAC4BHop.mc.field_71428_T.field_74278_d = 1.6105f;
                AAC4BHop.mc.field_71439_g.field_70159_w *= 1.0708;
                AAC4BHop.mc.field_71439_g.field_70179_y *= 1.0708;
            } else if (AAC4BHop.mc.field_71439_g.field_70143_R > 0.0f) {
                AAC4BHop.mc.field_71428_T.field_74278_d = 0.6f;
            }
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

