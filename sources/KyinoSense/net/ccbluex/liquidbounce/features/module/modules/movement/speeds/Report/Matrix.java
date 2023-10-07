/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report;

import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public class Matrix
extends SpeedMode {
    public Matrix() {
        super("Matrix");
    }

    @Override
    public void onUpdate() {
        if (Matrix.mc.field_71439_g.func_70090_H()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            if (Matrix.mc.field_71439_g.field_70122_E) {
                Matrix.mc.field_71439_g.func_70664_aZ();
                Matrix.mc.field_71439_g.field_71102_ce = 0.02098f;
                Matrix.mc.field_71428_T.field_74278_d = 1.055f;
            } else {
                MovementUtils.strafe(MovementUtils.getSpeed());
            }
        } else {
            Matrix.mc.field_71428_T.field_74278_d = 1.0f;
        }
    }

    @Override
    public void onMotion() {
    }

    @Override
    public void onMove(MoveEvent event) {
    }

    @Override
    public void onDisable() {
        Matrix.mc.field_71439_g.field_71102_ce = 0.02f;
        Matrix.mc.field_71428_T.field_74278_d = 1.0f;
    }

    @Override
    public void setMotion(MoveEvent event, double baseMoveSpeed, double d, boolean b) {
    }
}

