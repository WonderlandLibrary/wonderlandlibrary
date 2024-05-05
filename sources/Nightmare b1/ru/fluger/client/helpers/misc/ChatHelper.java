package wtf.rich.api.utils.other;

import com.mojang.realmsclient.gui.ChatFormatting;
import wtf.rich.api.utils.Helper;

public class ChatHelper implements Helper
{
    public static String chatPrefix;
    
    public static void addChatMessage(final Object message) {
        ChatHelper.mc.h.a(new ho(ChatHelper.chatPrefix + message));
    }
    
    static {
        ChatHelper.chatPrefix = "§7[" + ChatFormatting.DARK_PURPLE + "N" + ChatFormatting.LIGHT_PURPLE + "ight" + ChatFormatting.DARK_PURPLE + "M" + ChatFormatting.LIGHT_PURPLE + "are" + ChatFormatting.RESET + "§7] §8>> §f";
    }
}
