package fun.rich.client.utils.other;

import com.mojang.realmsclient.gui.ChatFormatting;
import fun.rich.client.utils.Helper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class ChatUtils implements Helper {

    public static String chatPrefix = "\2477[" + TextFormatting.WHITE + "Capybara Premium" + "\2477] " + ChatFormatting.RESET;

    public static void addChatMessage(String message) {
        mc.player.addChatMessage(new TextComponentString(chatPrefix + message));
    }
}
