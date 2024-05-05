package appu26j.mods.multiplayer;

import com.google.common.eventbus.Subscribe;

import appu26j.events.entity.EventTick;
import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.utils.ServerUtil;
import appu26j.utils.TimeUtil;

@ModInterface(name = "Auto Tip", description = "Automatically tips people on Hypixel.", category = Category.MULTIPLAYER)
public class AutoTip extends Mod
{
	private TimeUtil timeUtil = new TimeUtil();
	
	@Subscribe
	public void onTick(EventTick e)
	{
		if (this.timeUtil.hasTimePassed(600000) && ServerUtil.isPlayerOnHypixel())
		{
			this.mc.thePlayer.sendChatMessage("/tip all");
			this.timeUtil.reset();
		}
	}
}
