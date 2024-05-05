package client.module.impl.other.autoregister;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.PacketReceiveEvent;
import client.module.impl.other.AutoRegister;
import client.value.Mode;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;

public class BlocksMCAutoRegister extends Mode<AutoRegister> {

    public BlocksMCAutoRegister(final String name, final AutoRegister parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = event -> {
        final Packet<?> packet = event.getPacket();
        if (packet instanceof S02PacketChat) {
            final S02PacketChat wrapper = (S02PacketChat) packet;
            final String message = wrapper.getChatComponent().getUnformattedText(), password = "baller123";
            if (message.equals("/register <password> <password>")) mc.thePlayer.sendChatMessage("/register " + password + " " + password);
        }
    };
}
