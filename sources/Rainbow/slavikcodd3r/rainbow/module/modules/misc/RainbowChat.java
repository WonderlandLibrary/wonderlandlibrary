package slavikcodd3r.rainbow.module.modules.misc;

import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.world.WorldSettings;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.IRCUtils;

import java.io.IOException;

import org.jibble.pircbot.IrcException;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C00PacketKeepAlive;

@Module.Mod(displayName = "RainbowChat")
public class RainbowChat extends Module
{
    public static boolean isEnabled;
    public static IRCUtils bot;
    public static boolean parties;
    
    static {
        bot = new IRCUtils();
        parties = false;
    }
	
    public void enable() {
    	ClientUtils.sendMessage("Welcome to Rainbow 2.0 IRC");
    	ClientUtils.sendMessage("Use # to write in the chat");
    	bot = null;
        (bot = new IRCUtils()).setVerbose(false);
        bot.setAutoNickChange(true);
        try {
            bot.connect("irc.freenode.net");
        }
        catch (IOException ex) {}
        catch (IrcException ex2) {}
        bot.joinChannel("#Rainbow846173");
        isEnabled = true;
    	super.enable();
        }
    public void disable() {
        bot.disconnect();
    	ClientUtils.sendMessage("You leave from chat");
    	super.disable();
    }
}