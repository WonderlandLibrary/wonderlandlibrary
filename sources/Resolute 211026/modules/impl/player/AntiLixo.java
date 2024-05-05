// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.player;

import net.minecraft.network.play.server.S02PacketChat;
import vip.Resolute.events.impl.EventPacket;
import vip.Resolute.events.Event;
import vip.Resolute.modules.Module;

public class AntiLixo extends Module
{
    public AntiLixo() {
        super("AntiLixo", 0, "Automatically responds to Lixo", Category.PLAYER);
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventPacket && ((EventPacket)e).getPacket() instanceof S02PacketChat) {
            final S02PacketChat packetChat = ((EventPacket)e).getPacket();
            if (packetChat.getChatComponent().getUnformattedText().replaceAll("", "").contains("lixo")) {
                AntiLixo.mc.thePlayer.sendChatMessage("hoes mad just get good");
            }
        }
    }
}
