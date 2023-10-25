package org.dreamcore.client.helpers.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import javafx.scene.effect.Glow;
import net.minecraft.util.text.TextComponentString;
import org.dreamcore.client.helpers.Helper;

public class ChatHelper implements Helper {

    public static String chatPrefix = "\2477[" + ChatFormatting.RED + "N" + ChatFormatting.WHITE + "ever" + ChatFormatting.RED + "H" + ChatFormatting.WHITE + "ook" + ChatFormatting.RESET + "\2477] " + ChatFormatting.RESET;

    public static void addChatMessage(String message) {
        Glow glow = new Glow();
        mc.player.addChatMessage(new TextComponentString(chatPrefix + message));
    }
}
