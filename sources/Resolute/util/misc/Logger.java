// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.misc;

import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import vip.Resolute.Resolute;
import net.minecraft.client.Minecraft;

public class Logger
{
    public static String prefixText;
    public static String prefixColor;
    public static String textColor;
    
    public static void ingameInfo(final String msg) {
        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
            Logger.prefixColor = "9";
            Logger.textColor = "6";
            Logger.prefixText = Resolute.name + " - §" + Logger.textColor + "Info§9";
            final StringBuilder tempMsg = new StringBuilder();
            for (final String line : msg.split("\n")) {
                tempMsg.append(line).append("§7");
            }
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§" + Logger.prefixColor + "[" + Logger.prefixText + "]§" + Logger.textColor + " " + tempMsg.toString()));
        }
    }
    
    public static void ingameError(final String msg) {
        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
            Logger.textColor = "c";
            Logger.prefixText = Resolute.name + " - §" + Logger.textColor + "Error§9";
            final StringBuilder tempMsg = new StringBuilder();
            for (final String line : msg.split("\n")) {
                tempMsg.append(line).append("§7");
            }
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§" + Logger.prefixColor + "[" + Logger.prefixText + "]§" + Logger.textColor + " " + tempMsg.toString()));
        }
    }
    
    public static void ingameWarn(final String msg) {
        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
            Logger.textColor = "e";
            Logger.prefixText = Resolute.name + " - §" + Logger.textColor + "Warning§9";
            final StringBuilder tempMsg = new StringBuilder();
            for (final String line : msg.split("\n")) {
                tempMsg.append(line).append("§7");
            }
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§" + Logger.prefixColor + "[" + Logger.prefixText + "]§" + Logger.textColor + " " + tempMsg.toString()));
        }
    }
    
    static {
        Logger.prefixText = Resolute.fullname;
        Logger.prefixColor = "9";
        Logger.textColor = "f";
    }
}
