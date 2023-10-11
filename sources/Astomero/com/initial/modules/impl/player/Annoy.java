package com.initial.modules.impl.player;

import com.initial.settings.impl.*;
import com.initial.modules.*;
import com.initial.settings.*;
import com.initial.events.impl.*;
import com.initial.*;
import com.initial.modules.impl.combat.*;
import java.util.concurrent.*;
import com.initial.events.*;

public class Annoy extends Module
{
    public ModeSet mode;
    public BooleanSet moduleCheck;
    public DoubleSet spinnerSpeed;
    public ModuleCategory customCategory;
    public ModeSet customYawMode;
    public BooleanSet customJump;
    public DoubleSet minPitch;
    public DoubleSet maxPitch;
    public BooleanSet customSneak;
    public BooleanSet customSwing;
    double spinnerYaw;
    
    public Annoy() {
        super("Annoy", 0, Category.PLAYER);
        this.mode = new ModeSet("Mode", "Spinner", new String[] { "Spinner", "Custom" });
        this.moduleCheck = new BooleanSet("Module Check", true);
        this.spinnerSpeed = new DoubleSet("Spinner Speed", 3.0, 1.0, 90.0, 0.1);
        this.customCategory = new ModuleCategory("Custom...");
        this.customYawMode = new ModeSet("Yaw", "None", new String[] { "None", "Random", "Backwards" });
        this.customJump = new BooleanSet("Jump", false);
        this.minPitch = new DoubleSet("Min Pitch", 90.0, -180.0, 180.0, 1.0);
        this.maxPitch = new DoubleSet("Max Pitch", 90.0, -180.0, 180.0, 1.0);
        this.customSneak = new BooleanSet("Sneak", false);
        this.customSwing = new BooleanSet("Swing", false);
        this.spinnerYaw = 0.0;
        this.addSettings(this.mode, this.moduleCheck, this.spinnerSpeed, this.customCategory);
        this.customCategory.addCatSettings(this.customYawMode, this.customJump, this.minPitch, this.maxPitch, this.customSneak, this.customSwing);
    }
    
    @EventTarget
    public void onPre(final EventMotion e) {
        final boolean killauraDoinStuff = Astomero.instance.moduleManager.getModuleByName("KillAura").isToggled() && KillAura.target != null;
        final boolean scaffoldToggled = Astomero.instance.moduleManager.getModuleByName("Scaffold").isToggled();
        final boolean isModuleMotifying = scaffoldToggled || killauraDoinStuff;
        if (this.moduleCheck.isEnabled() && isModuleMotifying) {
            return;
        }
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Spinner": {
                this.spinnerYaw += this.spinnerSpeed.getValue();
                e.setYaw((float)this.spinnerYaw);
                break;
            }
            case "Custom": {
                if (this.customJump.isEnabled() && Annoy.localPlayer.onGround) {
                    Annoy.localPlayer.jump();
                }
                if (this.customSneak.isEnabled()) {
                    this.mc.gameSettings.keyBindSneak.pressed = (Annoy.localPlayer.ticksExisted % 4 != 0);
                }
                if (this.customSwing.isEnabled()) {
                    Annoy.localPlayer.swingItem();
                }
                final double min = this.minPitch.getValue();
                double max = this.maxPitch.getValue();
                if (min >= max) {
                    ++max;
                }
                final double finalPitch = ThreadLocalRandom.current().nextDouble(min, max);
                e.setPitch((float)finalPitch);
                double yaw = Annoy.localPlayer.rotationYaw;
                final String mode2 = this.customYawMode.getMode();
                switch (mode2) {
                    case "Random": {
                        yaw = ThreadLocalRandom.current().nextDouble(-180.0, 180.0);
                        break;
                    }
                    case "Backwards": {
                        yaw = Annoy.localPlayer.rotationYaw + 180.0f;
                        break;
                    }
                }
                e.setYaw((float)yaw);
                break;
            }
        }
        this.mc.thePlayer.rotationPitchHead = e.getPitch();
        this.mc.thePlayer.renderYawOffset = e.getYaw();
        this.mc.thePlayer.prevRenderYawOffset = e.getYaw();
    }
}
