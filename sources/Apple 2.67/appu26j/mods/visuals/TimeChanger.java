package appu26j.mods.visuals;

import com.google.common.eventbus.Subscribe;

import appu26j.events.entity.EventTick;
import appu26j.events.network.EventPacketReceive;
import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

@ModInterface(name = "Time Changer", description = "Allows you to change the time.", category = Category.VISUALS)
public class TimeChanger extends Mod
{
	public TimeChanger()
	{
		this.addSetting(new Setting("Time", this, 1000, 1000, 24000, 500));
	}
	
	@Subscribe
	public void onTick(EventTick e)
	{
	    if (this.mc.theWorld != null)
	    {
            long worldTime = (long) this.getSetting("Time").getSliderValue();
            this.mc.theWorld.setWorldTime(worldTime);
	    }
	}
	
	@Subscribe
	public void onPacketReceive(EventPacketReceive e)
	{
		if (e.getPacket() instanceof S03PacketTimeUpdate)
		{
			e.setCancelled(true);
		}
	}
}
