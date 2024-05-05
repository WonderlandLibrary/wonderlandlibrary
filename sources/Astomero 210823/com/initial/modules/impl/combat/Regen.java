package com.initial.modules.impl.combat;

import com.initial.settings.impl.*;
import com.initial.modules.*;
import com.initial.settings.*;
import com.initial.events.impl.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import com.initial.events.*;

public class Regen extends Module
{
    public DoubleSet packets;
    
    public Regen() {
        super("Regen", 0, Category.EXPLOIT);
        this.packets = new DoubleSet("Packets", 2.0, 1.0, 300.0, 1.0);
        this.addSettings(this.packets);
    }
    
    @EventTarget
    public void onUpdate(final EventTick e) {
        this.setDisplayName(String.valueOf(this.packets.getValue()));
        for (int a = 0; a < this.packets.getValue(); ++a) {
            if (this.mc.thePlayer.getHealth() != 20.0f) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(this.mc.thePlayer.onGround));
            }
        }
    }
}
