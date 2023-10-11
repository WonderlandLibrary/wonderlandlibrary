package com.initial.modules.impl.movement;

import com.initial.settings.impl.*;
import com.initial.modules.*;
import com.initial.settings.*;
import net.minecraft.network.play.server.*;
import com.initial.events.*;
import com.initial.events.impl.*;
import com.initial.utils.movement.*;
import net.minecraft.client.*;

public class Speed extends Module
{
    public ModeSet mode;
    public DoubleSet speed1;
    public BooleanSet lagback;
    int groundTicks;
    double decay;
    
    public Speed() {
        super("Speed", 0, Category.MOVEMENT);
        this.mode = new ModeSet("Mode", "Vanilla Hop", new String[] { "Vanilla Hop", "Vanilla Ground", "Watchdog", "Strafe", "Verus Hop" });
        this.speed1 = new DoubleSet("Speed", 2.0, 1.0, 10.0, 0.1);
        this.lagback = new BooleanSet("Lagback Check", false);
        this.addSettings(this.mode, this.speed1, this.lagback);
        this.groundTicks = 0;
        this.decay = 0.0;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.mc.timer.timerSpeed = 1.0f;
        this.mc.thePlayer.stepHeight = 0.625f;
    }
    
    @EventTarget
    public void onReceive(final EventReceivePacket e) {
        if (this.lagback.isEnabled() && e.getPacket() instanceof S08PacketPlayerPosLook) {
            this.toggle();
        }
    }
    
    @EventTarget
    public void onMove(final EventMove e) {
        final String mode = this.mode.getMode();
        switch (mode) {
            default: {}
        }
    }
    
    @EventTarget
    @Override
    public void onEvent(final EventNigger e) {
        if (e instanceof UpdateEvent && e.isPre()) {
            this.setDisplayName("Speed §7" + this.mode.getMode());
            final String mode = this.mode.getMode();
            switch (mode) {
                case "Vanilla Hop": {
                    if (MovementUtils.isMoving() && this.mc.thePlayer.onGround) {
                        this.mc.thePlayer.jump();
                    }
                    else if (!MovementUtils.isMoving()) {
                        MovementUtils.setMotion(0.0);
                    }
                    MovementUtils.setSpeed1(MovementUtils.getBaseMoveSpeed() * this.speed1.getValue());
                    break;
                }
                case "Vanilla Ground": {
                    MovementUtils.setSpeed1(MovementUtils.getBaseMoveSpeed() * this.speed1.getValue());
                    if (!MovementUtils.isMoving()) {
                        MovementUtils.setMotion(0.0);
                        break;
                    }
                    break;
                }
                case "Verus Hop": {
                    if (this.mc.thePlayer.onGround && MovementUtils.isMoving()) {
                        this.mc.thePlayer.jump();
                    }
                    else if (!MovementUtils.isMoving()) {
                        MovementUtils.setMotion(0.0);
                    }
                    MovementUtils.setSpeed1(0.32);
                    break;
                }
                case "Strafe": {
                    if (this.mc.thePlayer.isMoving() && this.mc.thePlayer.onGround) {
                        this.mc.thePlayer.jump();
                    }
                    else if (!MovementUtils.isMoving()) {
                        MovementUtils.setMotion(0.0);
                    }
                    MovementUtils.setSpeed1(0.32);
                    break;
                }
            }
        }
    }
    
    public static double getMotion() {
        final Minecraft localMinecraft = Minecraft.getMinecraft();
        final double d1 = localMinecraft.thePlayer.motionX;
        final double d2 = localMinecraft.thePlayer.motionZ;
        return Math.sqrt(d1 * d1 + d2 * d2);
    }
}
