package summer.ui.clickuiutils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

/**
 * @author: AmirCC
 * 04:10 pm, 11/10/2020, Wednesday
 **/
public enum ClientUtil {
    INSTANCE;

    private String prefix = String.format("%s%s%s %s", EnumChatFormatting.GRAY + "[", EnumChatFormatting.WHITE + "Client", EnumChatFormatting.GRAY + "]", EnumChatFormatting.WHITE + ": ");
    private Minecraft mc = Minecraft.getMinecraft();

    public void sendMessage(String message, boolean prefix){
        mc.thePlayer.addChatMessage(new ChatComponentText(prefix ? this.prefix + message : message));
    }

    public void sendMessage(String message){
        sendMessage(message, true);
    }



}
