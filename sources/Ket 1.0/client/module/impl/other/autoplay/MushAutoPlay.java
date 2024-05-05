package client.module.impl.other.autoplay;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.PacketReceiveEvent;
import client.module.impl.other.AutoPlay;
import client.value.Mode;
import client.value.impl.StringValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.IChatComponent;

public class MushAutoPlay extends Mode<AutoPlay> {

    private final StringValue mode = new StringValue("Mode", this, "swsolo");

    public MushAutoPlay(final String name, final AutoPlay parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = event -> {
        final Packet<?> packet = event.getPacket();
        if (packet instanceof S02PacketChat) {
            final S02PacketChat wrapper = (S02PacketChat) packet;
            if (wrapper.getChatComponent().getFormattedText().contains("Deseja jogar novamente?")) {
                for (IChatComponent iChatComponent : wrapper.getChatComponent().getSiblings()) {
                    for (String value : iChatComponent.toString().split("'")) {
                        if (value.startsWith("/play") && !value.contains(".")) {
                            mc.thePlayer.sendChatMessage(value);
                            break;
                        }
                    }
                }

            }
            final String message = wrapper.getChatComponent().getUnformattedText();
            if (message.equals("Enviando ao lobby..."))
                mc.thePlayer.sendChatMessage("/play " + mode.getValue());
        }


    };
}
