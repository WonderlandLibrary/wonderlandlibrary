package tech.drainwalk.utility.minecraft;

import net.minecraft.util.text.TextComponentString;
import tech.drainwalk.utility.Utility;

public class ChatUtility extends Utility {
    public static void addChatMessage(String text) {
        mc.player.sendMessage(new TextComponentString(text));
    }
}
