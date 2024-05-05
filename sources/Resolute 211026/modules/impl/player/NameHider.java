// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.player;

import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.util.ChatComponentText;
import net.minecraft.network.play.server.S02PacketChat;
import vip.Resolute.events.impl.EventPacket;
import vip.Resolute.events.Event;
import vip.Resolute.modules.Module;

public class NameHider extends Module
{
    public NameHider() {
        super("NameHider", 0, "Hides you ingame name", Category.PLAYER);
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventPacket && e.isPre()) {
            final EventPacket packetEvent = (EventPacket)e;
            if (EventPacket.packet instanceof S02PacketChat) {
                final S02PacketChat packet = (S02PacketChat)EventPacket.packet;
                if (packet.getChatComponent().getUnformattedText().replaceAll("", "").contains(NameHider.mc.getSession().getUsername())) {
                    packet.chatComponent = new ChatComponentText(packet.getChatComponent().getFormattedText().replaceAll("", "").replaceAll(NameHider.mc.getSession().getUsername(), "You"));
                }
            }
            else if (EventPacket.packet instanceof S3CPacketUpdateScore) {
                final S3CPacketUpdateScore packet2 = (S3CPacketUpdateScore)EventPacket.packet;
                if (packet2.getObjectiveName().replaceAll("", "").contains(NameHider.mc.getSession().getUsername())) {
                    packet2.setObjective(packet2.getObjectiveName().replaceAll("", "").replaceAll(NameHider.mc.getSession().getUsername(), "You"));
                }
                if (packet2.getPlayerName().replaceAll("", "").contains(NameHider.mc.getSession().getUsername())) {
                    packet2.setName(packet2.getPlayerName().replaceAll("", "").replaceAll(NameHider.mc.getSession().getUsername(), "You"));
                }
            }
        }
    }
}
