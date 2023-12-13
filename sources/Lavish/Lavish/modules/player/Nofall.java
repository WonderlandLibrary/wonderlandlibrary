// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.player;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import Lavish.event.events.EventPacket;
import Lavish.Client;
import Lavish.event.Event;
import Lavish.modules.Category;
import Lavish.modules.Module;

public class Nofall extends Module
{
    public Nofall() {
        super("Nofall", 0, true, Category.Player, "Makes you take no fall damage");
    }
    
    @Override
    public void onEvent(final Event e) {
        if (Client.instance.moduleManager.getModuleByName("Fly").isEnabled()) {
            return;
        }
        if (Nofall.mc.thePlayer != null && e instanceof EventPacket && ((EventPacket)e).isSending() && Nofall.mc.thePlayer.fallDistance > 2.5) {
            final EventPacket event = (EventPacket)e;
            final Packet<?> packet = (Packet<?>)event.getPacket();
            if (packet instanceof C03PacketPlayer) {
                final C03PacketPlayer C03 = (C03PacketPlayer)packet;
                if (Nofall.mc.thePlayer.fallDistance > 2.5) {
                    Nofall.mc.thePlayer.fallDistance = 0.5f;
                    C03.setOnGround(true);
                }
            }
        }
    }
}
