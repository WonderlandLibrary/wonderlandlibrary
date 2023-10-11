package com.initial.modules.impl.player;

import com.initial.settings.impl.*;
import com.initial.modules.*;
import com.initial.settings.*;
import com.initial.events.impl.*;
import com.initial.utils.movement.*;
import net.minecraft.network.play.client.*;
import com.initial.utils.network.*;
import net.minecraft.network.*;
import com.initial.events.*;

public class NoFall extends Module
{
    String[] modes;
    public ModeSet mode;
    int watchdogSafeState;
    int verusState;
    
    public NoFall() {
        super("NoFall", 0, Category.PLAYER);
        this.modes = new String[] { "Packet", "Watchdog", "Safe Packet", "Verus", "Verus Damage", "Verus Float" };
        this.mode = new ModeSet("Mode", "Packet", this.modes);
        this.watchdogSafeState = 0;
        this.verusState = 0;
        this.addSettings(this.mode);
    }
    
    @EventTarget
    public void onPre(final EventMotion event) {
        this.setDisplayName("No Fall §7" + this.mode.getMode());
        if (this.mc.thePlayer.onGround) {
            this.watchdogSafeState = 0;
            this.verusState = 0;
        }
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Verus Float": {
                if (NoFall.localPlayer.fallDistance > 2.9 && !MovementUtils.isOverVoid()) {
                    event.setOnGround(true);
                    NoFall.localPlayer.motionY = 0.0;
                    NoFall.localPlayer.fallDistance = 0.0f;
                    break;
                }
                break;
            }
            case "Verus": {
                if (NoFall.localPlayer.fallDistance > 2.9 && !MovementUtils.isOverVoid()) {
                    event.setOnGround(true);
                    NoFall.localPlayer.fallDistance = 0.0f;
                    break;
                }
                break;
            }
            case "Verus Damage": {
                if (NoFall.localPlayer.fallDistance > 3.95 && !MovementUtils.isOverVoid()) {
                    event.setOnGround(true);
                    NoFall.localPlayer.fallDistance = 0.0f;
                    break;
                }
                break;
            }
            case "Watchdog": {
                if (MovementUtils.isOverVoid()) {
                    return;
                }
                if (this.mc.thePlayer.fallDistance > 3.95) {
                    PacketUtil.sendPacket(new C03PacketPlayer(true));
                    this.mc.thePlayer.fallDistance = 0.0f;
                    break;
                }
                break;
            }
            case "Safe Packet": {
                if (MovementUtils.isOverVoid()) {
                    return;
                }
                if (this.watchdogSafeState < 1) {
                    if (this.mc.thePlayer.fallDistance > 2.7) {
                        PacketUtil.sendPacket(new C03PacketPlayer(true));
                        this.mc.thePlayer.fallDistance = 0.0f;
                        ++this.watchdogSafeState;
                        break;
                    }
                    break;
                }
                else {
                    if (this.mc.thePlayer.fallDistance > 3.9) {
                        PacketUtil.sendPacket(new C03PacketPlayer(true));
                        this.mc.thePlayer.fallDistance = 0.0f;
                        break;
                    }
                    break;
                }
                break;
            }
            case "Packet": {
                if (this.mc.thePlayer.fallDistance > 2.9) {
                    PacketUtil.sendPacket(new C03PacketPlayer(true));
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
}
