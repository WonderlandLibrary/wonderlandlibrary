package dev.tenacity.utils.player;

import dev.tenacity.scripting.api.ScriptModule;
import dev.tenacity.utils.Utils;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.io.File;

public class ChatUtil implements Utils {

    public static void print(boolean prefix, String message) {
        if (mc.thePlayer != null) {
            if (prefix) message = "§7[§d§lTENACITY§r§7] " + message;
            mc.thePlayer.addChatMessage(new ChatComponentText(message));
        }
    }

    public static void error(String message) {
        if (mc.thePlayer != null) {
            mc.thePlayer.addChatMessage(new ChatComponentText("§7[§c§lERROR§r§7] " + message));
        }
    }

    public static void scriptError(ScriptModule scriptFile, String message) {
        if (mc.thePlayer != null) {
            if (scriptFile.getFile() == null) {
                mc.thePlayer.addChatMessage(new ChatComponentText("§d§lSCRIPT ERROR (Cloud Script): §r§f" + scriptFile.getName() + " §c" + message));
            } else {
                mc.thePlayer.addChatMessage(new ChatComponentText("§d§lSCRIPT ERROR: §r§f" + scriptFile.getFile().getName() + " §c" + message));
            }
        }
    }

    public static void scriptError(File scriptFile, String message) {
        if (mc.thePlayer != null) {
            mc.thePlayer.addChatMessage(new ChatComponentText("§d§lSCRIPT ERROR: §r§f" + scriptFile.getName() + " §c" + message));
        }
    }

    public static void print(String prefix, EnumChatFormatting color, String message) {
        if (mc.thePlayer != null) {
            message = "§7[§" + color.formattingCode + "§l" + prefix.toUpperCase() + "§r§7]§r §" + color.formattingCode + message;
            mc.thePlayer.addChatMessage(new ChatComponentText(message));
        }
    }

    public static void print(Object o) {
        print(true, String.valueOf(o));
    }

    public static void send(String message) {
        if (mc.thePlayer != null) {
            mc.thePlayer.sendChatMessage(message);
        }
    }

}
