/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.modules.module.modules.movement.speeds.other;

import net.dev.important.Client;
import net.dev.important.event.MoveEvent;
import net.dev.important.modules.module.modules.movement.Speed;
import net.dev.important.modules.module.modules.movement.speeds.SpeedMode;
import net.dev.important.utils.MovementUtils;

public class CustomSpeed
extends SpeedMode {
    public CustomSpeed() {
        super("Custom");
    }

    @Override
    public void onMotion() {
        if (MovementUtils.isMoving()) {
            Speed speed = (Speed)Client.moduleManager.getModule(Speed.class);
            if (speed == null) {
                return;
            }
            CustomSpeed.mc.field_71428_T.field_74278_d = ((Float)speed.customTimerValue.get()).floatValue();
            if (CustomSpeed.mc.field_71439_g.field_70122_E) {
                MovementUtils.strafe(((Float)speed.customSpeedValue.get()).floatValue());
                CustomSpeed.mc.field_71439_g.field_70181_x = ((Float)speed.customYValue.get()).floatValue();
            } else if (((Boolean)speed.customStrafeValue.get()).booleanValue()) {
                MovementUtils.strafe(((Float)speed.customSpeedValue.get()).floatValue());
            } else {
                MovementUtils.strafe();
            }
        } else {
            CustomSpeed.mc.field_71439_g.field_70179_y = 0.0;
            CustomSpeed.mc.field_71439_g.field_70159_w = 0.0;
        }
    }

    @Override
    public void onEnable() {
        Speed speed = (Speed)Client.moduleManager.getModule(Speed.class);
        if (speed == null) {
            return;
        }
        if (((Boolean)speed.resetXZValue.get()).booleanValue()) {
            CustomSpeed.mc.field_71439_g.field_70179_y = 0.0;
            CustomSpeed.mc.field_71439_g.field_70159_w = 0.0;
        }
        if (((Boolean)speed.resetYValue.get()).booleanValue()) {
            CustomSpeed.mc.field_71439_g.field_70181_x = 0.0;
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        CustomSpeed.mc.field_71428_T.field_74278_d = 1.0f;
        super.onDisable();
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
    }
}

