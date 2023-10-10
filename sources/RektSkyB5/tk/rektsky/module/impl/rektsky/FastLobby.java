/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.rektsky;

import net.minecraft.network.play.server.S02PacketChat;
import tk.rektsky.event.Event;
import tk.rektsky.event.impl.ChatEvent;
import tk.rektsky.event.impl.PacketReceiveEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;

public class FastLobby
extends Module {
    public static String[] hubCommands = new String[]{"/hub", "/lobby"};
    private boolean keepGoing = false;

    public FastLobby() {
        super("FastLobby", "Use /lobby, /l, /hub once to go back to lobby", 0, Category.REKTSKY);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof PacketReceiveEvent && ((PacketReceiveEvent)event).getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = (S02PacketChat)((PacketReceiveEvent)event).getPacket();
            try {
                if (packet.isChat() && packet.getChatComponent().getUnformattedText().equalsIgnoreCase("Voc\u00ea tem certeza? Utilize /lobby novamente para voltar ao lobby.")) {
                    ((PacketReceiveEvent)event).setCanceled(true);
                    this.mc.thePlayer.sendChatMessage("/hub");
                    this.keepGoing = true;
                }
            }
            catch (Exception e2) {
                return;
            }
        }
        if (event instanceof ChatEvent) {
            for (String cmd : hubCommands) {
                String msg = ((ChatEvent)event).getMessage();
                if (!msg.equalsIgnoreCase(cmd)) continue;
                ((ChatEvent)event).setCanceled(true);
                this.mc.thePlayer.sendChatMessage("/hub");
            }
        }
    }
}

