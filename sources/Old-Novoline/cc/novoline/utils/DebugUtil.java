package cc.novoline.utils;

import cc.novoline.utils.messages.MessageFactory;
import cc.novoline.utils.messages.TextMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.util.Arrays;

import static net.minecraft.util.EnumChatFormatting.GRAY;
import static net.minecraft.util.EnumChatFormatting.LIGHT_PURPLE;

public class DebugUtil {

    private static Minecraft mc = Minecraft.getInstance();

    public static void print(Object... debug) {
        if (isDev()) {
            String message = Arrays.toString(debug);
            mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(message));
        }
    }

    public static void log(Object message) {
        String text = String.valueOf(message);
        mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(text));
    }

    public static void log(boolean prefix, Object message) {
        String text = prefix().getFormattedText() + " " + message;
        mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(text));
    }

    public static void log(String prefix, Object message) {
        String text = prefix(prefix).getFormattedText() + " " + message;
        mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(text));
    }


    public static TextMessage prefix(String text) {
        return MessageFactory.text(text, LIGHT_PURPLE).append(" \u00bb ", GRAY);
    }

    public static TextMessage prefix() {
        return MessageFactory.text("Novoline", LIGHT_PURPLE).append(" \u00bb ", GRAY);
    }

    private static boolean isDev() {
        return true;
    }
}
