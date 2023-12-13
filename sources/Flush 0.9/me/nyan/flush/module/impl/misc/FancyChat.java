package me.nyan.flush.module.impl.misc;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventPacket;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.ModeSetting;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class FancyChat extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", this, "Fancy", "Fancy", "Mock", "Spaced");

    public FancyChat() {
        super("FancyChat", Category.MISC);
    }

    @SubscribeEvent
    public void onPacket(EventPacket e) {
        if (e.getPacket() instanceof C01PacketChatMessage) {
            if (((C01PacketChatMessage) e.getPacket()).getMessage().startsWith("/")) {
                return;
            }

            StringBuilder stringBuilder = new StringBuilder();
            int index = 0;
            for (char character : ((C01PacketChatMessage) e.getPacket()).getMessage().toCharArray()) {
                switch (mode.getValue().toUpperCase()) {
                    case "FANCY":
                        if (character >= '!' && character <= 128) {
                            stringBuilder.append(Character.toChars(character + 'ﻠ'));
                        } else {
                            stringBuilder.append(character);
                        }
                        break;

                    case "MOCK":
                        stringBuilder.append(index % 2 != 0 ? String.valueOf(Character.toUpperCase(character)) :
                                String.valueOf(Character.toLowerCase(character)));
                        break;

                    case "SPACED":
                        stringBuilder.append(character).append(' ');
                        break;
                }

                index++;
            }

            e.setPacket(new C01PacketChatMessage(stringBuilder.toString()));
        }
    }
}