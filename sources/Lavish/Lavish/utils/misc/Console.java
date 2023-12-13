// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.utils.misc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.client.Minecraft;

public class Console
{
    public static Minecraft mc;
    
    static {
        Console.mc = Minecraft.getMinecraft();
    }
    
    public static void print(final String message) {
        System.out.println(message);
    }
    
    public static void printWithPrefix(final String message) {
        System.out.println("[Lavish] " + message);
    }
    
    public static void sendChatToPlayer(final String message) {
        Console.mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(message));
    }
    
    public static void sendChatToPlayerWithPrefix(final String message) {
        Console.mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "[" + EnumChatFormatting.AQUA + "Lavish" + EnumChatFormatting.WHITE + "] " + message));
    }
    
    public static void sendChat(final String message) {
        Console.mc.thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage(message));
    }
}
