package best.azura.client.impl.module.impl.other;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import net.minecraft.network.play.client.C01PacketChatMessage;

@ModuleInfo(name = "Chat Bypass", category = Category.OTHER, description = "Bypass servers' chat filters")
public class ChatBypass extends Module {

    @EventHandler
    public Listener<EventSentPacket> eventSentPacketListener = e -> {
        if (e.getPacket() instanceof C01PacketChatMessage) {
            C01PacketChatMessage c01 = e.getPacket();
            StringBuilder builder = new StringBuilder();
            boolean b = (c01.getMessage().startsWith("/") && !(c01.getMessage().startsWith("/pc") ||
                    c01.getMessage().startsWith("/shout") ||
                    c01.getMessage().startsWith("/achat") ||
                    c01.getMessage().startsWith("/r"))) || c01.getMessage().startsWith(".");
            int index = 0;
            char lastChar = c01.getMessage().charAt(0);
            for (char c : c01.getMessage().toCharArray()) {
                builder.append(c);
                if (index == 0 && !b && lastChar != '/') builder.append('\u0627');
                index++;
                if (c == ' ') index = 0;
                lastChar = c;
            }
            e.setPacket(new C01PacketChatMessage(builder.toString()));
        }
    };

}