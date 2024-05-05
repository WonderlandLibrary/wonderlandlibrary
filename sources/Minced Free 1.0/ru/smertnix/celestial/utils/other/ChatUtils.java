package ru.smertnix.celestial.utils.other;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import ru.smertnix.celestial.utils.Helper;

public class ChatUtils implements Helper {

    public static String chatPrefix = TextFormatting.GRAY + "[" + TextFormatting.LIGHT_PURPLE + "Celestial" + TextFormatting.GRAY + "] -" + ChatFormatting.RESET;

    public static void addChatMessage(String message) {
        mc.player.addChatMessage(new TextComponentString(TextFormatting.GRAY + "(" + TextFormatting.LIGHT_PURPLE + "Celestial" + TextFormatting.GRAY + ") " + TextFormatting.DARK_GRAY +  "- " + ChatFormatting.RESET + message));
    }
}
