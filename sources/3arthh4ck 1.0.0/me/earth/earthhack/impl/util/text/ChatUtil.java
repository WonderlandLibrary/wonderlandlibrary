package me.earth.earthhack.impl.util.text;

import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.api.util.TextColor;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class ChatUtil implements Globals
{
    public static final String PREFIX = TextColor.DARK_RED + "<" + TextColor.RED +  "3arthh4ck" + TextColor.DARK_RED + "> " + TextColor.RESET;

    public static void sendMessage(String message)
    {
        if (mc.ingameGUI != null)
        {
            mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(PREFIX + message));
        }
    }

    public static void sendMessage(String message, int id)
    {
        if (mc.ingameGUI != null)
        {
            mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(PREFIX + message), id);
        }
    }

    public static void deleteMessage(int id)
    {
        if (mc.ingameGUI != null)
        {
            mc.ingameGUI.getChatGUI().deleteChatLine(id);
        }
    }

    public static void sendComponent(ITextComponent component, int id)
    {
        if (mc.ingameGUI != null)
        {
            mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(component, id);
        }
    }

}
