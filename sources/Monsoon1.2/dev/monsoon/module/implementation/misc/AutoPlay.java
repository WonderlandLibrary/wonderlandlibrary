package dev.monsoon.module.implementation.misc;


import dev.monsoon.Monsoon;
import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventPacket;
import dev.monsoon.module.base.Module;
import dev.monsoon.util.misc.Timer;
import net.minecraft.network.play.server.S02PacketChat;
import org.lwjgl.input.Keyboard;
import dev.monsoon.module.enums.Category;

public class AutoPlay extends Module {

    public String knownMode, knownType;
    public Timer delay = new Timer();

    private final String[] strings = new String[]{"1st Killer - ", "1st Place - ", "You died! Want to play again? Click here!", " - Damage Dealt - ", "1st - ", "Winning Team - ", "Winners: ", "Winner: ", "Winning Team: ", " win the game!", "1st Place: ", "Last team standing!", "Winner #1 (", "Top Survivors", "Winners - "};

    public AutoPlay() {
        super("AutoPlay", Keyboard.KEY_NONE, Category.MISC);
    }



    public void onEvent(Event e) {
        if (e instanceof EventPacket && e.isPre() && e.isIncoming()) {
            EventPacket event = (EventPacket) e;
            if (((EventPacket) e).getPacket() instanceof S02PacketChat) {
                Monsoon.sendMessage(((S02PacketChat) ((EventPacket) e).getPacket()).func_148915_c().getUnformattedText());
                //for (String string : strings) {
                    if (((S02PacketChat) ((EventPacket) e).getPacket()).func_148915_c().getUnformattedText().contains("Click here!")) {
                        mc.thePlayer.sendChatMessage("/play " + "solo_insane");
                        Monsoon.sendMessage("New game, bitch!");
                    }
                //}
            }
            if (event.getPacket() instanceof S02PacketChat) {
                String message = ((S02PacketChat) event.getPacket()).func_148915_c().getUnformattedText();
                if (message.equals("Teaming is not allowed on Solo mode!")) {
                    knownType = "solo";
                }
            }
        }
    }
}
