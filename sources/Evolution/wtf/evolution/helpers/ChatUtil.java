package wtf.evolution.helpers;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class ChatUtil {

    public static void print(String text) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(ChatFormatting.GRAY + "[" + ChatFormatting.AQUA + "Evolution" + ChatFormatting.GRAY + "] " + ChatFormatting.RESET + text));
    }

}
