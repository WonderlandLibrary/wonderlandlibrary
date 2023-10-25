package space.clowdy.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;

public class ChatUtils {
     public static void sendMessage(String string1, String string2) {
          Minecraft.getInstance().player.sendMessage(new StringTextComponent("[" + string1 + "] " + string2), Minecraft.getInstance().player.getUniqueID());
     }
}
