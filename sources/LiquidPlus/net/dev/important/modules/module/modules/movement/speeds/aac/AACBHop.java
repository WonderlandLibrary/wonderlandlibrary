/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.MathHelper
 */
package net.dev.important.modules.module.modules.movement.speeds.aac;

import net.dev.important.event.MoveEvent;
import net.dev.important.modules.module.modules.movement.speeds.SpeedMode;
import net.dev.important.utils.MovementUtils;
import net.minecraft.util.MathHelper;

public class AACBHop
extends SpeedMode {
    public AACBHop() {
        super("AACBHop");
    }

    @Override
    public void onMotion() {
        if (AACBHop.mc.field_71439_g.func_70090_H()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            AACBHop.mc.field_71428_T.field_74278_d = 1.08f;
            if (AACBHop.mc.field_71439_g.field_70122_E) {
                AACBHop.mc.field_71439_g.field_70181_x = 0.399;
                float f = AACBHop.mc.field_71439_g.field_70177_z * ((float)Math.PI / 180);
                AACBHop.mc.field_71439_g.field_70159_w -= (double)(MathHelper.func_76126_a((float)f) * 0.2f);
                AACBHop.mc.field_71439_g.field_70179_y += (double)(MathHelper.func_76134_b((float)f) * 0.2f);
                AACBHop.mc.field_71428_T.field_74278_d = 2.0f;
            } else {
                AACBHop.mc.field_71439_g.field_70181_x *= 0.97;
                AACBHop.mc.field_71439_g.field_70159_w *= 1.008;
                AACBHop.mc.field_71439_g.field_70179_y *= 1.008;
            }
        } else {
            AACBHop.mc.field_71439_g.field_70159_w = 0.0;
            AACBHop.mc.field_71439_g.field_70179_y = 0.0;
            AACBHop.mc.field_71428_T.field_74278_d = 1.0f;
        }
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
    }

    @Override
    public void onDisable() {
        AACBHop.mc.field_71428_T.field_74278_d = 1.0f;
    }
}

