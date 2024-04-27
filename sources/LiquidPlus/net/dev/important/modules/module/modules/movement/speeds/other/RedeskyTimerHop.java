/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.modules.module.modules.movement.speeds.other;

import net.dev.important.event.MoveEvent;
import net.dev.important.modules.module.modules.movement.speeds.SpeedMode;

public class RedeskyTimerHop
extends SpeedMode {
    public RedeskyTimerHop() {
        super("RedeskyTimerHop");
    }

    @Override
    public void onMotion() {
        RedeskyTimerHop.mc.field_71428_T.field_74278_d = 1.0f;
        if (RedeskyTimerHop.mc.field_71439_g.func_70090_H()) {
            return;
        }
        if (RedeskyTimerHop.mc.field_71439_g.field_70701_bs > 0.0f) {
            if (RedeskyTimerHop.mc.field_71439_g.field_70122_E) {
                RedeskyTimerHop.mc.field_71428_T.field_74278_d = 6.0f;
                RedeskyTimerHop.mc.field_71439_g.func_70664_aZ();
            } else if (RedeskyTimerHop.mc.field_71439_g.field_70143_R > 0.0f) {
                RedeskyTimerHop.mc.field_71428_T.field_74278_d = 1.095f;
            }
        }
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
    }
}

