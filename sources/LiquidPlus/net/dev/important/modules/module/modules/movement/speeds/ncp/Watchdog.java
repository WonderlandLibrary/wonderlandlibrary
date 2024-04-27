/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.modules.module.modules.movement.speeds.ncp;

import net.dev.important.event.MoveEvent;
import net.dev.important.modules.module.modules.movement.speeds.SpeedMode;
import net.dev.important.utils.MovementUtils;

public class Watchdog
extends SpeedMode {
    public Watchdog() {
        super("Watchdog");
    }

    @Override
    public void onEnable() {
        Watchdog.mc.field_71428_T.field_74278_d = 1.0865f;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        Watchdog.mc.field_71439_g.field_71102_ce = 0.02f;
        Watchdog.mc.field_71428_T.field_74278_d = 1.0f;
        super.onDisable();
    }

    @Override
    public void onMotion() {
    }

    @Override
    public void onUpdate() {
        if (MovementUtils.isMoving()) {
            if (Watchdog.mc.field_71439_g.field_70122_E) {
                Watchdog.mc.field_71439_g.func_70664_aZ();
                Watchdog.mc.field_71439_g.field_71102_ce = 0.0223f;
            }
            MovementUtils.strafe();
        } else {
            Watchdog.mc.field_71439_g.field_70159_w = 0.0;
            Watchdog.mc.field_71439_g.field_70179_y = 0.0;
        }
    }

    @Override
    public void onMove(MoveEvent event) {
    }
}

