package appu26j.mods.multiplayer;

import com.google.common.eventbus.Subscribe;

import appu26j.events.entity.EventTick;
import appu26j.events.mc.EventWorldChange;
import appu26j.events.network.EventPacketReceive;
import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;
import appu26j.utils.ServerUtil;
import appu26j.utils.TimeUtil;
import me.duck.autogg.GGUtil;
import net.minecraft.network.play.server.S02PacketChat;

@ModInterface(name = "Auto GG", description = "Automatically says GG at the end of a game.", category = Category.MULTIPLAYER)
public class AutoGG extends Mod
{
	private TimeUtil timeUtil = new TimeUtil();
	private boolean canSendMessage = true;
	
	public AutoGG()
	{
		this.addSetting(new Setting("GG Message", this, "gg"));
	}
	
	@Subscribe
	public void onPacketReceive(EventPacketReceive e)
	{
		if (e.getPacket() instanceof S02PacketChat && this.mc.getCurrentServerData() != null)
		{
			String message = ((S02PacketChat) e.getPacket()).getChatComponent().getUnformattedText();
			
			if (GGUtil.hasGameEnded(message) && this.canSendMessage)
			{
				if (ServerUtil.tntExplodesEarly())
				{
					this.mc.thePlayer.sendChatMessage("/achat " + this.getSetting("GG Message").getTextBoxValue());
				}
				
				else
				{
					this.mc.thePlayer.sendChatMessage(this.getSetting("GG Message").getTextBoxValue());
				}
				
				this.canSendMessage = false;
			}
		}
	}
	
	@Subscribe
	public void onWorldChange(EventWorldChange e)
	{
		this.canSendMessage = true;
	}
}
