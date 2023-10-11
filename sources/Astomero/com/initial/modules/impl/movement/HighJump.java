package com.initial.modules.impl.movement;

import com.initial.settings.impl.*;
import com.initial.modules.*;
import com.initial.settings.*;
import com.initial.*;
import net.minecraft.network.play.client.*;
import com.initial.utils.network.*;
import net.minecraft.network.*;
import com.initial.events.*;
import com.initial.events.impl.*;
import com.initial.utils.movement.*;

public class HighJump extends Module
{
    public ModeSet mode;
    boolean launched;
    boolean wasLaunched;
    
    public HighJump() {
        super("HighJump", 0, Category.MOVEMENT);
        this.mode = new ModeSet("Mode", "Vanilla", new String[] { "Vanilla", "Redesky", "BlocksMC" });
        this.launched = false;
        this.wasLaunched = false;
        this.addSettings(this.mode);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        if (!this.mc.thePlayer.onGround) {
            Astomero.addDebugMessage("You must be on ground to use this module");
            this.toggle();
        }
        final String mode = this.mode.getMode();
        switch (mode) {
            case "BlocksMC": {
                this.mc.timer.timerSpeed = 0.3f;
                PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 3.035, this.mc.thePlayer.posZ, false));
                PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
                PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
                this.mc.timer.timerSpeed = 1.0f;
                break;
            }
        }
    }
    
    @EventTarget
    public void onSend(final EventSendPacket event) {
        if (event.getPacket() instanceof C03PacketPlayer) {
            final C03PacketPlayer c03 = (C03PacketPlayer)event.getPacket();
            c03.onGround = true;
        }
    }
    
    @EventTarget
    public void onMotion(final EventMotionUpdate e) {
        this.setDisplayName("High Jump §7" + this.mode.getMode());
        if (e.isPre()) {
            final String mode = this.mode.getMode();
            switch (mode) {
                case "BlocksMC": {
                    if (this.mc.thePlayer.hurtTime > 1 && !this.launched) {
                        this.launched = true;
                    }
                    if (this.launched) {
                        this.mc.thePlayer.motionY = 1.623;
                        MovementUtils.setMotion(2.0333);
                        e.setOnGround(true);
                        this.launched = false;
                        this.wasLaunched = true;
                        this.toggle();
                        break;
                    }
                    break;
                }
                case "Redesky": {
                    this.mc.thePlayer.motionY = 0.93999623450013;
                    MovementUtils.setMotion(0.2433);
                    e.setOnGround(true);
                    this.toggle();
                    break;
                }
            }
        }
    }
}
