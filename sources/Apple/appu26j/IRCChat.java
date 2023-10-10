package appu26j;

import java.util.ArrayList;

import appu26j.interfaces.MinecraftInterface;
import net.minecraft.util.ChatComponentText;

public class IRCChat implements MinecraftInterface
{
    private static final ArrayList<String> messages = new ArrayList<>();
    public static boolean switchedToAppleClientChat = false;
    
    public static ArrayList<String> getMessages()
    {
        return messages;
    }
    
    public static void add(String string)
    {
        if (switchedToAppleClientChat)
        {
            ChatComponentText chatComponentText = new ChatComponentText(UUIDs.getName(string.split(":")[0]) + ":" + string.split(":")[1]);
            mc.ingameGUI.getChatGUI().printChatMessage(chatComponentText);
        }
        
        messages.add(string);
    }
    
    public static void sendMessage(String message)
    {
        try
        {
            Apple.CLIENT.getWebsockets().getWebsocketClientEndpoint().sendMessage(message);
        }
        
        catch (Exception e)
        {
            ;
        }
    }
}
