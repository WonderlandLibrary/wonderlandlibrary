package cc.novoline.modules.misc;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.PacketEvent;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import net.minecraft.network.play.client.C01PacketChatMessage;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.stream.Stream;

public final class
ChatFilter extends AbstractModule {


    /* constructors @on */
    public ChatFilter(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "ChatBypass", "Chat Bypass", Keyboard.KEY_NONE, EnumModuleType.MISC, "Bypasses N, F words");
    }

    @EventTarget
    public void onPacketSending(PacketEvent event) {
        if(event.getState().equals(PacketEvent.State.OUTGOING)) {
            if (event.getPacket() instanceof C01PacketChatMessage) {
                final C01PacketChatMessage packet = (C01PacketChatMessage) event.getPacket();
                final String message = packet.getMessage();
                final String[] split = message.split(" ");
                final String command = split[0];

                if (message.startsWith("/") && Stream.of("/r", "/shout", "/msg", "/m", "/tell", "/whisper", "/w", "/ac", "/pc", "/gc").anyMatch(command::equalsIgnoreCase)) {
                    if (Stream.of("/r", "/shout", "/ac", "/pc", "/gc").anyMatch(command::equalsIgnoreCase)) {
                        packet.setMessage(split[0] + " " + encrypt(String.join(" ", Arrays.copyOfRange(split, 1, split.length))));
                    } else if (split.length > 1) {
                        packet.setMessage(split[0] + " " + split[1] + " " + encrypt(String.join(" ", Arrays.copyOfRange(split, 2, split.length))));
                    }
                }

                if (!packet.getMessage().startsWith("/") && !packet.getMessage().startsWith(".")
                        && !packet.getMessage().startsWith("http") && !message.equalsIgnoreCase("gg")) {
                    packet.setMessage(encrypt(message));
                }
            }
        }
    }

    private String encrypt(String string) {
        StringBuilder msg = new StringBuilder();
        char[] chars = string.toCharArray();

        for (char aChar : chars) {
            msg.append(aChar).append('\u05fc');
        }

        return msg.toString();
    }
}
