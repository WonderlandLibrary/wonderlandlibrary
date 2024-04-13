package com.canon.majik.api.utils.client;

import com.canon.majik.api.utils.Globals;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class ChatUtils implements Globals {

    private static final String chatPrefix = ChatFormatting.DARK_GRAY + "[" + ChatFormatting.LIGHT_PURPLE + "Majik" + ChatFormatting.DARK_GRAY + "] " + ChatFormatting.GRAY;
    static ITextComponent itc = null;

    public static void tempMessage(String message, int code) {
        if (mc.player == null) return;
        /*
        1 -> Red
        2 -> Green
        3 -> yellow
        */
        switch (code){
            case 1:
                itc = new TextComponentString(ChatFormatting.RED + message + ChatFormatting.RESET);
                break;
            case 2:
                itc = new TextComponentString(ChatFormatting.GREEN + message + ChatFormatting.RESET);
                break;
            case 3:
                itc = new TextComponentString(ChatFormatting.YELLOW + message + ChatFormatting.RESET);
                break;
        }
        mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(itc, 9999);
    }
}
