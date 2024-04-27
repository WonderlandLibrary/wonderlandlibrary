/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import org.jetbrains.annotations.NotNull;

public class LegitHop
extends SpeedMode {
    public LegitHop() {
        super("LegitHop");
    }

    @Override
    public void onMotion() {
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(@NotNull MoveEvent event) {
        if (LegitHop.minecraft.field_71439_g.field_70122_E) {
            LegitHop.minecraft.field_71439_g.func_70664_aZ();
        }
    }
}

