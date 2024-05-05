package com.initial.modules.impl.combat;

import com.initial.settings.impl.*;
import com.initial.modules.*;
import com.initial.settings.*;
import com.initial.events.impl.*;
import net.minecraft.network.play.server.*;
import com.initial.events.*;

public class Velocity extends Module
{
    public ModeSet mode;
    
    public Velocity() {
        super("Velocity", 0, Category.COMBAT);
        this.mode = new ModeSet("Mode", "Packet", new String[] { "Packet", "Cancel" });
        this.addSettings(this.mode);
    }
    
    @EventTarget
    public void onGet(final EventReceivePacket e) {
        this.setDisplayName("Velocity §7" + this.mode.getMode());
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Packet": {
                if (e.getPacket() instanceof S12PacketEntityVelocity) {
                    final S12PacketEntityVelocity s12 = (S12PacketEntityVelocity)e.getPacket();
                    e.setCancelled(s12.getEntityID() == this.mc.thePlayer.getEntityId());
                }
                if (e.getPacket() instanceof S27PacketExplosion) {
                    e.setCancelled(true);
                    break;
                }
                break;
            }
        }
    }
}
