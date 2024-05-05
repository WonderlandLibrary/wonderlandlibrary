package tech.drainwalk.utility;

import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class ChatUtils implements Helper {

    public static String chatPrefix = TextFormatting.RED + "\2477(" + TextFormatting.RED + "(HUNNER) > " + TextFormatting.RED +"\2477) " + TextFormatting.RESET;

    public static void addChatMessage(String message) {
        mc.player.addChatMessage(new TextComponentString(chatPrefix + message));
    }
}
