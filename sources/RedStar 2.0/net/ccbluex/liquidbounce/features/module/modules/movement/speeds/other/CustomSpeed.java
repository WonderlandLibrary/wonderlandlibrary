package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public class CustomSpeed
extends SpeedMode {
    public CustomSpeed() {
        super("Custom");
    }

    @Override
    public void onMotion() {
        if (MovementUtils.isMoving()) {
            Speed speed = (Speed)LiquidBounce.moduleManager.getModule(Speed.class);
            if (speed == null) {
                return;
            }
            mc.getTimer().setTimerSpeed(((Float)speed.customTimerValue.get()).floatValue());
            if (CustomSpeed.mc2.player.field_70122_E) {
                MovementUtils.strafe(((Float)speed.customSpeedValue.get()).floatValue());
                CustomSpeed.mc2.player.field_70181_x = ((Float)speed.customYValue.get()).floatValue();
            } else if (((Boolean)speed.customStrafeValue.get()).booleanValue()) {
                MovementUtils.strafe(((Float)speed.customSpeedValue.get()).floatValue());
            } else {
                MovementUtils.strafe();
            }
        } else {
            CustomSpeed.mc2.player.field_70179_y = 0.0;
            CustomSpeed.mc2.player.field_70159_w = 0.0;
        }
    }

    @Override
    public void onEnable() {
        Speed speed = (Speed)LiquidBounce.moduleManager.getModule(Speed.class);
        if (speed == null) {
            return;
        }
        if (((Boolean)speed.resetXZValue.get()).booleanValue()) {
            CustomSpeed.mc2.player.field_70179_y = 0.0;
            CustomSpeed.mc2.player.field_70159_w = 0.0;
        }
        if (((Boolean)speed.resetYValue.get()).booleanValue()) {
            CustomSpeed.mc2.player.field_70181_x = 0.0;
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.getTimer().setTimerSpeed(1.0f);
        super.onDisable();
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
    }
}
