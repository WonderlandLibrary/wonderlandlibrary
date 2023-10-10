package appu26j.mods.general;

import com.google.common.eventbus.Subscribe;

import appu26j.Apple;
import appu26j.events.entity.EventTick;
import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;
import appu26j.utils.SoundUtil;
import appu26j.utils.TimeUtil;
import net.minecraft.util.ChatComponentText;

@ModInterface(name = "Timer Countdown", description = "Allows you to make the timer ring after a specific time.", category = Category.GENERAL)
public class ClockTimer extends Mod
{
	private TimeUtil timeUtil = new TimeUtil();
	
	public ClockTimer()
	{
		this.addSetting(new Setting("Time to ring after (seconds)", this, "0"));
		this.addSetting(new Setting("Time to ring after (minutes)", this, "1"));
		this.addSetting(new Setting("Time to ring after (hours)", this, "0"));
		this.addSetting(new Setting("Ring sound (bell)", this, true));
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		this.timeUtil.reset();
	}
	
	@Subscribe
	public void onTick(EventTick e)
	{
		long seconds = Long.parseLong(this.getSetting("Time to ring after (seconds)").getTextBoxValue());
		long minutesToSeconds = Long.parseLong(this.getSetting("Time to ring after (minutes)").getTextBoxValue()) * 60;
		long hoursToSeconds = Long.parseLong(this.getSetting("Time to ring after (hours)").getTextBoxValue()) * 3600;
		long time = (hoursToSeconds + minutesToSeconds + seconds) * 1000;
		
		if (this.timeUtil.hasTimePassed(time))
		{
			this.ring();
			Apple.CLIENT.getModsManager().getMod("Timer Countdown").setEnabled(false);
			this.timeUtil.reset();
		}
	}
	
	public void ring()
	{
		if (this.getSetting("Ring sound (bell)").getCheckBoxValue())
		{
			SoundUtil.playBellSound();
		}
		
		this.mc.thePlayer.addChatMessage(new ChatComponentText("§b§lApple Client > §r§bBeep Boop! The timer has rang!"));
	}
}
