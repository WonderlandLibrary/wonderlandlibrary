// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.combat;

import net.augustus.events.EventSendPacket;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.augustus.events.EventAttackEntity;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.StringValue;
import net.augustus.modules.Module;

public class Criticals extends Module
{
    public StringValue mode;
    
    public Criticals() {
        super("Criticals", Color.RED, Categorys.COMBAT);
        this.mode = new StringValue(1, "Mode", this, "NoGround", new String[] { "NoGround", "Packet" });
    }
    
    @EventTarget
    public void onEventAttackEntity(final EventAttackEntity eventAttackEntity) {
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "Packet": {
                Criticals.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + 6.0E-15, Criticals.mc.thePlayer.posZ, false));
                Criticals.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
                break;
            }
        }
    }
    
    @EventTarget
    public void onEventSendPacket(final EventSendPacket eventSendPacket) {
        final Packet packet = eventSendPacket.getPacket();
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "NoGround": {
                if (packet instanceof C03PacketPlayer) {
                    final C03PacketPlayer c03PacketPlayer = (C03PacketPlayer)packet;
                    c03PacketPlayer.setOnGround(false);
                    break;
                }
                break;
            }
        }
    }
}
