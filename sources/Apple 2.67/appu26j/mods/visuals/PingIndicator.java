package appu26j.mods.visuals;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;

@ModInterface(name = "Ping Indicator", description = "Shows the ping, in numbers.", category = Category.VISUALS)
public class PingIndicator extends Mod
{
	public PingIndicator()
	{
		this.addSetting(new Setting("Text Shadow", this, true));
		this.addSetting(new Setting("Text Color (RGB)", this, new int[]{0, 255, 75}));
	}
}
