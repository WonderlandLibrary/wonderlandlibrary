package com.initial.modules.impl.movement;

import com.initial.settings.impl.*;
import com.initial.modules.*;
import com.initial.settings.*;
import com.initial.*;
import com.initial.utils.network.*;
import net.minecraft.network.*;
import net.minecraft.network.play.server.*;
import com.initial.events.*;
import com.initial.utils.movement.*;
import com.initial.utils.player.*;
import net.minecraft.entity.player.*;
import net.minecraft.network.play.client.*;
import java.util.concurrent.*;
import com.initial.events.impl.*;

public class CustomFly extends Module
{
    final ModuleCategory motionCategory;
    private BooleanSet strafeSet;
    private BooleanSet motionSet;
    private ModeSet speedModeSet;
    private DoubleSet speedSet;
    private DoubleSet strafeTicksSet;
    private DoubleSet yAxisSpeedSet;
    private DoubleSet speedPotionMultSet;
    final ModuleCategory bypassCategory;
    private ModeSet damageModeSet;
    private ModeSet bypassModeSet;
    private DoubleSet timerSpeedSet;
    private ModeSet spoofModeSet;
    final ModuleCategory otherCategory;
    private BooleanSet jumpSet;
    private BooleanSet stopMotionSet;
    private BooleanSet timerOnMoveSet;
    private BooleanSet lagbackCheckSet;
    private BooleanSet groundCheckSet;
    int hopsCount;
    boolean done;
    boolean stopMotionTotally;
    
    public CustomFly() {
        super("CustomFly", 0, Category.MOVEMENT);
        this.motionCategory = new ModuleCategory("Motion...");
        this.strafeSet = new BooleanSet("Strafe", true);
        this.motionSet = new BooleanSet("Enabled", true);
        this.speedModeSet = new ModeSet("Speed Mode", "Custom", new String[] { "Custom", "Legit", "OnHurt" });
        this.speedSet = new DoubleSet("Custom Speed", 1.0, 0.1, 5.0, 0.05);
        this.strafeTicksSet = new DoubleSet("Strafe Ticks", 1.0, 1.0, 20.0, 1.0);
        this.yAxisSpeedSet = new DoubleSet("Vertical Speed", 0.5, 0.0, 2.0, 0.01);
        this.speedPotionMultSet = new DoubleSet("Potion Multiplier", 0.02, 0.0, 0.2, 0.001);
        this.bypassCategory = new ModuleCategory("Bypass...");
        this.damageModeSet = new ModeSet("Damage", "None", new String[] { "None", "Simple", "Incremental", "NoGround" });
        this.bypassModeSet = new ModeSet("Bypass", "None", new String[] { "None", "Creative", "LagBack" });
        this.timerSpeedSet = new DoubleSet("Timer Speed", 1.0, 0.3, 3.0, 0.05);
        this.spoofModeSet = new ModeSet("Ground Spoof", "Edit", new String[] { "Edit", "None", "Packet" });
        this.otherCategory = new ModuleCategory("Other...");
        this.jumpSet = new BooleanSet("Jump", false);
        this.stopMotionSet = new BooleanSet("Stop Motion", true);
        this.timerOnMoveSet = new BooleanSet("Timer Moving Check", false);
        this.lagbackCheckSet = new BooleanSet("LagBack Check", false);
        this.groundCheckSet = new BooleanSet("Ground Check", false);
        this.hopsCount = 0;
        this.done = false;
        this.stopMotionTotally = false;
        this.addSettings(this.motionCategory, this.bypassCategory, this.otherCategory);
        this.motionCategory.addCatSettings(this.strafeSet, this.motionSet, this.speedModeSet, this.speedSet, this.strafeTicksSet, this.yAxisSpeedSet, this.speedPotionMultSet);
        this.bypassCategory.addCatSettings(this.damageModeSet, this.bypassModeSet, this.timerSpeedSet, this.spoofModeSet);
        this.otherCategory.addCatSettings(this.jumpSet, this.stopMotionSet, this.timerOnMoveSet, this.lagbackCheckSet, this.groundCheckSet);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        if (this.groundCheckSet.isEnabled() && this.mc.thePlayer.onGround) {
            Astomero.addChatMessage("Please toggle this on ground");
            this.toggle();
            return;
        }
        this.done = false;
        this.hopsCount = 0;
        final String mode = this.damageModeSet.getMode();
        switch (mode) {
            case "Simple": {
                PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 3.035, this.mc.thePlayer.posZ, false));
                PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
                PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
                break;
            }
            case "Incremental": {
                for (int a = 0; a < 65; ++a) {
                    PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + a / 20.0f, this.mc.thePlayer.posZ, false));
                }
                PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
                PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
                break;
            }
        }
        if (this.jumpSet.isEnabled() && !this.damageModeSet.getMode().equalsIgnoreCase("NoGround")) {
            this.mc.thePlayer.jump();
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.mc.timer.setTimerSpeed(1.0f);
        this.mc.thePlayer.capabilities.isFlying = false;
        if (this.stopMotionSet.isEnabled()) {
            this.mc.thePlayer.motionX = 0.0;
            this.mc.thePlayer.motionZ = 0.0;
        }
    }
    
    @EventTarget
    public void onGet(final EventReceivePacket e) {
        if (e.getPacket() instanceof S08PacketPlayerPosLook && this.lagbackCheckSet.isEnabled()) {
            Astomero.addChatMessage("You got lagged back, disabling fly to prevent flags..");
            this.toggle();
        }
    }
    
    @EventTarget
    public void onPre(final EventMotion e) {
        this.setDisplayName("Custom Fly");
        this.stopMotionTotally = false;
        if (this.damageModeSet.getMode().equalsIgnoreCase("NoGround") && this.hopsCount < 4) {
            this.stopMotionTotally = true;
            if (this.mc.thePlayer.onGround) {
                ++this.hopsCount;
                this.mc.thePlayer.jump();
            }
            e.setOnGround(false);
            return;
        }
        if (this.timerOnMoveSet.isEnabled()) {
            if (MovementUtils.isMoving()) {
                this.mc.timer.setTimerSpeed((float)this.timerSpeedSet.getValue());
            }
            else {
                this.mc.timer.setTimerSpeed(1.0f);
            }
        }
        else {
            this.mc.timer.setTimerSpeed((float)this.timerSpeedSet.getValue());
        }
        if (this.motionSet.isEnabled()) {
            if (this.mc.gameSettings.keyBindJump.isKeyDown()) {
                this.mc.thePlayer.motionY = this.yAxisSpeedSet.getValue();
            }
            else if (this.mc.gameSettings.keyBindSneak.isKeyDown()) {
                this.mc.thePlayer.motionY = -this.yAxisSpeedSet.getValue();
            }
            else {
                this.mc.thePlayer.motionY = 0.0;
            }
        }
        else {
            this.mc.thePlayer.capabilities.isFlying = true;
        }
        if (this.strafeSet.isEnabled()) {
            if (MovementUtils.isMoving()) {
                if (this.mc.thePlayer.ticksExisted % this.strafeTicksSet.getValue() == 0.0) {
                    final String mode = this.speedModeSet.getMode();
                    switch (mode) {
                        case "Custom": {
                            MovementUtils.setSpeed1((float)ValueUtil.getMotion(this.speedSet.getValue(), this.speedPotionMultSet.getValue()));
                            break;
                        }
                        case "Legit": {
                            MovementUtils.setSpeed1(MovementUtils.getSpeed());
                            break;
                        }
                        case "OnHurt": {
                            this.mc.thePlayer.motionX = 0.0;
                            this.mc.thePlayer.motionZ = 0.0;
                            if (this.mc.thePlayer.hurtTime > 0) {
                                MovementUtils.setSpeed1((float)ValueUtil.getMotion(this.speedSet.getValue(), this.speedPotionMultSet.getValue()));
                                break;
                            }
                            MovementUtils.setSpeed1(0.2630000114440918);
                            break;
                        }
                    }
                }
            }
            else if (this.stopMotionSet.isEnabled()) {
                this.mc.thePlayer.motionX = 0.0;
                this.mc.thePlayer.motionZ = 0.0;
            }
        }
        final String mode2 = this.spoofModeSet.getMode();
        switch (mode2) {
            case "Edit": {
                e.setOnGround(true);
                break;
            }
            case "Packet": {
                PacketUtil.sendPacketSilent(new C03PacketPlayer(true));
                break;
            }
        }
        final String mode3 = this.bypassModeSet.getMode();
        switch (mode3) {
            case "Creative": {
                final PlayerCapabilities pc = new PlayerCapabilities();
                pc.isFlying = true;
                pc.isCreativeMode = true;
                PacketUtil.sendPacketSilent(new C13PacketPlayerAbilities(pc));
                break;
            }
            case "LagBack": {
                if (this.mc.thePlayer.ticksExisted % 25 == 0) {
                    PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(0.0, ThreadLocalRandom.current().nextDouble(-3.2141313E7, -3.6171231E7), 0.0, false));
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onMove(final EventMove e) {
        if (this.stopMotionTotally) {
            e.setX(0.0);
            e.setZ(0.0);
        }
    }
}
