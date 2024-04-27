package net.ccbluex.liquidbounce.features.module.modules.movement;

import me.utils.ValueUtil;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="CustomBhop", description="custom !", category=ModuleCategory.MOVEMENT)
public class CustomBhop
extends Module {
    public final ListValue jumpModeSet = new ListValue("Mode", new String[]{"Custom", "Legit"}, "Custom");
    public final ListValue speedModeSet = new ListValue("Mode", new String[]{"Custom", "Legit", "Ground", "None"}, "Custom");
    public final ListValue airFrictionMode = new ListValue("Friction", new String[]{"None", "Low", "Normal", "High"}, "Normal");
    public final FloatValue timerSpeedSet = new FloatValue("Timer Speed", 1.0f, 0.0f, 3.0f);
    public final BoolValue autoJumpSet = new BoolValue("Auto", true);
    public final FloatValue customJumpHeightSet = new FloatValue("Height", 0.42f, 0.06f, 1.3f);
    public final FloatValue groundSpeedSet = new FloatValue("Ground Multiplier", 1.5f, 0.0f, 3.0f);
    public final FloatValue speedSet = new FloatValue("Speed", 0.31f, 0.1f, 2.0f);
    public final FloatValue speedPotionMultSet = new FloatValue("Potion Multiplier", 0.02f, 0.0f, 0.2f);
    public final FloatValue strafeTicksSet = new FloatValue("Ticks", 1.0f, 1.0f, 20.0f);
    public final BoolValue stopMotionSet = new BoolValue("Stop Motion", true);
    double dec = 0.0;
    double speed = 0.0;

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.getTimer().setTimerSpeed(1.0f);
    }

    @EventTarget
    public void onMove(MoveEvent e) {
        String mode;
        mc.getTimer().setTimerSpeed(((Float)this.timerSpeedSet.getValue()).floatValue());
        switch (mode = (String)this.airFrictionMode.get()) {
            case "Low": {
                this.dec += 0.0021;
                break;
            }
            case "Normal": {
                this.dec += 0.0101;
                break;
            }
            case "High": {
                this.dec += 0.02314;
                break;
            }
            case "None": {
                this.dec = 0.0;
            }
        }
        if (mc.getThePlayer().getOnGround()) {
            this.dec = 0.0;
            this.speed = ((Float)this.speedSet.getValue()).floatValue() * ((Float)this.groundSpeedSet.getValue()).floatValue();
            if (((Boolean)this.autoJumpSet.get()).booleanValue()) {
                String mode2;
                switch (mode2 = (String)this.jumpModeSet.get()) {
                    case "Custom": {
                        e.setY(ValueUtil.getModifiedMotionY(((Float)this.customJumpHeightSet.getValue()).floatValue()));
                        break;
                    }
                    case "Legit": {
                        e.setY(ValueUtil.getBaseMotionY());
                    }
                }
            }
        } else {
            this.speed = ((Float)this.speedSet.getValue()).floatValue();
        }
        this.speed -= this.dec;
        if ((double)((float)mc.getThePlayer().getTicksExisted() % ((Float)this.strafeTicksSet.getValue()).floatValue()) == 0.0 || mc.getThePlayer().getOnGround()) {
            if (MovementUtils.isMoving()) {
                String mode3;
                switch (mode3 = (String)this.speedModeSet.get()) {
                    case "Custom": {
                        MovementUtils.setSpeed1((float)ValueUtil.getMotion(this.speed, ((Float)this.speedPotionMultSet.getValue()).floatValue()));
                        break;
                    }
                    case "Legit": {
                        MovementUtils.setSpeed1(MovementUtils.getSpeed1());
                        break;
                    }
                    case "Ground": {
                        if (!mc.getThePlayer().getOnGround()) break;
                        MovementUtils.setSpeed1((float)ValueUtil.getMotion(this.speed, ((Float)this.speedPotionMultSet.getValue()).floatValue()));
                    }
                }
            } else if (((Boolean)this.stopMotionSet.get()).booleanValue()) {
                mc.getThePlayer().setMotionX(0.0);
                mc.getThePlayer().setMotionZ(0.0);
            }
        }
    }
}
