package client.util;

import client.Client;
import lombok.experimental.UtilityClass;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

@UtilityClass
public class ChatUtil implements MinecraftInstance {

    public void display(final Object object, final Object... objects) {
        if (mc.thePlayer == null) return;
        mc.thePlayer.addChatMessage(new ChatComponentText(getPrefix() + String.format(object.toString(), objects)));
    }

    public void displayNoPrefix(final Object object, final Object... objects) {
        if (mc.thePlayer == null) return;
        mc.thePlayer.addChatMessage(new ChatComponentText(String.format(object.toString(), objects)));
    }

    public String getPrefix() {
        return EnumChatFormatting.AQUA + Client.NAME + " » " + EnumChatFormatting.RESET;
    }

}
