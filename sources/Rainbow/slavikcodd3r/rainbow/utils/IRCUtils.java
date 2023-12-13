package slavikcodd3r.rainbow.utils;

import net.minecraft.client.Minecraft;
import org.jibble.pircbot.PircBot;

public class IRCUtils extends PircBot
{
    public IRCUtils() {
    }
    
    public void onMessage(final String channel, final String sender, final String login, final String hostname, final String message) {
        ChatUtils.sendClientMessage("[Chat]§d" + sender + "§8§l>> §f" + message);
        super.onMessage(channel, sender, login, hostname, message);
    }
}
