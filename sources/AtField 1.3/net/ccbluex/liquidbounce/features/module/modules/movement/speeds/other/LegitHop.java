/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class LegitHop
extends SpeedMode {
    @Override
    public void onMove(MoveEvent moveEvent) {
        if (LegitHop.minecraft.field_71439_g.field_70122_E) {
            LegitHop.minecraft.field_71439_g.func_70664_aZ();
        }
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMotion() {
    }

    public LegitHop() {
        super("LegitHop");
    }
}

