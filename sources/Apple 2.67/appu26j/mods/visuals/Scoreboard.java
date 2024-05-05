package appu26j.mods.visuals;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;

@ModInterface(name = "Scoreboard", description = "Allows you to turn on and change the looks of the scoreboard.", category = Category.VISUALS)
public class Scoreboard extends Mod
{
	public Scoreboard()
	{
        this.addSetting(new Setting("Background", this, true));
        this.addSetting(new Setting("Background Rounded Corners", this, false));
		this.addSetting(new Setting("Text Shadow", this, false));
		this.addSetting(new Setting("Hide Numbers", this, false));
        this.addSetting(new Setting("Footer Text", this, "Default"));
        this.addSetting(new Setting("Rainbow Footer Text", this, false));
	}
}
