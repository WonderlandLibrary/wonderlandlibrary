// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.player;

import net.minecraft.util.EnumChatFormatting;
import java.io.File;
import dev.tenacity.scripting.api.ScriptModule;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import dev.tenacity.utils.Utils;

public class ChatUtil implements Utils
{
    public static void print(final boolean prefix, String message) {
        if (ChatUtil.mc.thePlayer != null) {
            if (prefix) {
                message = "§7[§d§lTENACITY§r§7] " + message;
            }
            ChatUtil.mc.thePlayer.addChatMessage(new ChatComponentText(message));
        }
    }
    
    public static void error(final String message) {
        if (ChatUtil.mc.thePlayer != null) {
            ChatUtil.mc.thePlayer.addChatMessage(new ChatComponentText("§7[§c§lERROR§r§7] " + message));
        }
    }
    
    public static void scriptError(final ScriptModule scriptFile, final String message) {
        if (ChatUtil.mc.thePlayer != null) {
            if (scriptFile.getFile() == null) {
                ChatUtil.mc.thePlayer.addChatMessage(new ChatComponentText("§d§lSCRIPT ERROR (Cloud Script): §r§f" + scriptFile.getName() + " §c" + message));
            }
            else {
                ChatUtil.mc.thePlayer.addChatMessage(new ChatComponentText("§d§lSCRIPT ERROR: §r§f" + scriptFile.getFile().getName() + " §c" + message));
            }
        }
    }
    
    public static void scriptError(final File scriptFile, final String message) {
        if (ChatUtil.mc.thePlayer != null) {
            ChatUtil.mc.thePlayer.addChatMessage(new ChatComponentText("§d§lSCRIPT ERROR: §r§f" + scriptFile.getName() + " §c" + message));
        }
    }
    
    public static void print(final String prefix, final EnumChatFormatting color, String message) {
        if (ChatUtil.mc.thePlayer != null) {
            message = "§7[§" + color.formattingCode + "§l" + prefix.toUpperCase() + "§r§7]§r §" + color.formattingCode + message;
            ChatUtil.mc.thePlayer.addChatMessage(new ChatComponentText(message));
        }
    }
    
    public static void print(final Object o) {
        print(true, String.valueOf(o));
    }
    
    public static void send(final String message) {
        if (ChatUtil.mc.thePlayer != null) {
            ChatUtil.mc.thePlayer.sendChatMessage(message);
        }
    }
}
