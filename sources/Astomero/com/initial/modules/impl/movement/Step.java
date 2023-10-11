package com.initial.modules.impl.movement;

import com.initial.modules.*;
import com.initial.events.impl.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import com.initial.events.*;

public class Step extends Module
{
    public Step() {
        super("Step", 0, Category.MOVEMENT);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate e) {
        if (this.mc.thePlayer.isCollidedHorizontally && this.mc.thePlayer.onGround) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.42, this.mc.thePlayer.posZ, true));
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.72, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));
            this.mc.thePlayer.stepHeight = 2.0f;
        }
        else {
            this.mc.thePlayer.stepHeight = 0.5f;
        }
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.mc.timer.timerSpeed = 1.0f;
        this.mc.thePlayer.stepHeight = 0.5f;
    }
}
