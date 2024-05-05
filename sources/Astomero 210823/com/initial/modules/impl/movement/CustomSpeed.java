package com.initial.modules.impl.movement;

import com.initial.settings.impl.*;
import com.initial.modules.*;
import com.initial.settings.*;
import com.initial.events.impl.*;
import com.initial.utils.player.*;
import com.initial.utils.movement.*;
import com.initial.events.*;

public class CustomSpeed extends Module
{
    public ModuleCategory jumpCategory;
    public ModuleCategory speedCategory;
    public ModuleCategory otherCategory;
    private DoubleSet timerSpeedSet;
    private ModeSet jumpModeSet;
    private BooleanSet autoJumpSet;
    private DoubleSet customJumpHeightSet;
    private ModeSet speedModeSet;
    private DoubleSet groundSpeedSet;
    private DoubleSet speedSet;
    private DoubleSet speedPotionMultSet;
    private DoubleSet strafeTicksSet;
    private ModeSet airFrictionMode;
    private BooleanSet stopMotionSet;
    double dec;
    double speed;
    
    public CustomSpeed() {
        super("CustomSpeed", 0, Category.MOVEMENT);
        this.jumpCategory = new ModuleCategory("Jump...");
        this.speedCategory = new ModuleCategory("Speed...");
        this.otherCategory = new ModuleCategory("Other...");
        this.timerSpeedSet = new DoubleSet("Timer Speed", 1.0, 0.3, 3.0, 0.05);
        this.jumpModeSet = new ModeSet("Mode", "Custom", new String[] { "Custom", "Legit" });
        this.autoJumpSet = new BooleanSet("Auto", true);
        this.customJumpHeightSet = new DoubleSet("Height", 0.42, 0.06, 1.3, 0.01);
        this.speedModeSet = new ModeSet("Mode", "Custom", new String[] { "Custom", "Legit", "Ground", "None" });
        this.groundSpeedSet = new DoubleSet("Ground Multiplier", 1.5, 0.0, 3.0, 0.01);
        this.speedSet = new DoubleSet("Speed", 0.31, 0.1, 2.0, 0.001);
        this.speedPotionMultSet = new DoubleSet("Potion Multiplier", 0.02, 0.0, 0.2, 0.001);
        this.strafeTicksSet = new DoubleSet("Ticks", 1.0, 1.0, 20.0, 1.0);
        this.airFrictionMode = new ModeSet("Friction", "Normal", new String[] { "None", "Low", "Normal", "High" });
        this.stopMotionSet = new BooleanSet("Stop Motion", true);
        this.dec = 0.0;
        this.speed = 0.0;
        this.addSettings(this.timerSpeedSet, this.jumpCategory, this.speedCategory, this.otherCategory);
        this.jumpCategory.addCatSettings(this.jumpModeSet, this.autoJumpSet, this.customJumpHeightSet);
        this.speedCategory.addCatSettings(this.speedModeSet, this.groundSpeedSet, this.speedSet, this.speedPotionMultSet, this.strafeTicksSet);
        this.otherCategory.addCatSettings(this.airFrictionMode, this.stopMotionSet);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.mc.timer.setTimerSpeed(1.0f);
    }
    
    @EventTarget
    public void onMove(final EventMove e) {
        this.mc.timer.setTimerSpeed((float)this.timerSpeedSet.getValue());
        final String mode = this.airFrictionMode.getMode();
        switch (mode) {
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
                break;
            }
        }
        if (this.mc.thePlayer.onGround) {
            this.dec = 0.0;
            this.speed = this.speedSet.getValue() * this.groundSpeedSet.getValue();
            if (this.autoJumpSet.isEnabled()) {
                final String mode2 = this.jumpModeSet.getMode();
                switch (mode2) {
                    case "Custom": {
                        e.setY(ValueUtil.getModifiedMotionY((float)this.customJumpHeightSet.getValue()));
                        break;
                    }
                    case "Legit": {
                        e.setY(ValueUtil.getBaseMotionY());
                        break;
                    }
                }
            }
        }
        else {
            this.speed = this.speedSet.getValue();
        }
        this.speed -= this.dec;
        if (this.mc.thePlayer.ticksExisted % this.strafeTicksSet.getValue() == 0.0 || this.mc.thePlayer.onGround) {
            if (MovementUtils.isMoving()) {
                final String mode3 = this.speedModeSet.getMode();
                switch (mode3) {
                    case "Custom": {
                        MovementUtils.setSpeed1((float)ValueUtil.getMotion(this.speed, this.speedPotionMultSet.getValue()));
                        break;
                    }
                    case "Legit": {
                        MovementUtils.setSpeed1(MovementUtils.getSpeed());
                        break;
                    }
                    case "Ground": {
                        if (this.mc.thePlayer.onGround) {
                            MovementUtils.setSpeed1((float)ValueUtil.getMotion(this.speed, this.speedPotionMultSet.getValue()));
                            break;
                        }
                        break;
                    }
                }
            }
            else if (this.stopMotionSet.isEnabled()) {
                this.mc.thePlayer.motionX = 0.0;
                this.mc.thePlayer.motionZ = 0.0;
            }
        }
    }
}
