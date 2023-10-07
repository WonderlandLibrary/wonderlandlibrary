// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.util.misc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import exhibition.util.MinecraftUtil;

public class ChatUtil implements MinecraftUtil
{
    public static void printChat(final String text) {
        ChatUtil.mc.thePlayer.addChatComponentMessage(new ChatComponentText(text));
    }
    
    public static void sendChat_NoFilter(final String text) {
        ChatUtil.mc.thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage(text));
    }
    
    public static void sendChat(final String text) {
        ChatUtil.mc.thePlayer.sendChatMessage(text);
    }
}
