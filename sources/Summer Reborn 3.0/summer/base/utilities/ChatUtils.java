package summer.base.utilities;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import summer.Summer;

public class ChatUtils
{
    protected static Minecraft mc;
    
    static {
        ChatUtils.mc = Minecraft.getMinecraft();
    }
    
    public static void sendMessage(final String msg) {
        final EntityPlayerSP thePlayer = ChatUtils.mc.thePlayer;
        final String format = "%s%s";
        final Object[] args = new Object[2];
        final int n = 0;
        final StringBuilder append = new StringBuilder().append(EnumChatFormatting.YELLOW);
        args[0] = append.append(Summer.NAME).append(EnumChatFormatting.GRAY).append(": ").toString();
        args[1] = msg;
        thePlayer.addChatMessage(new ChatComponentText(String.format("%s%s", args)));
    }

    public static void sendMessage(float timerSpeed) {
        final EntityPlayerSP thePlayer = ChatUtils.mc.thePlayer;
        final String format = "%s%s";
        final Object[] args = new Object[2];
        final int n = 0;
        final StringBuilder append = new StringBuilder().append(EnumChatFormatting.YELLOW);
        args[0] = append.append(Summer.NAME).append(EnumChatFormatting.GRAY).append(": ").toString();
        args[1] = timerSpeed;
        thePlayer.addChatMessage(new ChatComponentText(String.format("%s%s", args)));
    }

	public static void sendMessage(double motionY) {
		final EntityPlayerSP thePlayer = ChatUtils.mc.thePlayer;
        final String format = "%s%s";
        final Object[] args = new Object[2];
        final int n = 0;
        final StringBuilder append = new StringBuilder().append(EnumChatFormatting.YELLOW);
        args[0] = append.append(Summer.NAME).append(EnumChatFormatting.GRAY).append(": ").toString();
        args[1] = motionY;
        thePlayer.addChatMessage(new ChatComponentText(String.format("%s%s", args)));
    }

	public static void sendMessage(List<EntityPlayer> bots) {
		final EntityPlayerSP thePlayer = ChatUtils.mc.thePlayer;
        final String format = "%s%s";
        final Object[] args = new Object[2];
        final int n = 0;
        final StringBuilder append = new StringBuilder().append(EnumChatFormatting.YELLOW);
        args[0] = append.append(Summer.NAME).append(EnumChatFormatting.GRAY).append(": ").toString();
        args[1] = bots;
        thePlayer.addChatMessage(new ChatComponentText(String.format("%s%s", args)));
    }
}
