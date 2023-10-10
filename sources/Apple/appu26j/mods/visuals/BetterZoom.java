package appu26j.mods.visuals;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;

@ModInterface(name = "Better Zoom", description = "Allows you to change how OptiFine zoom looks.", category = Category.VISUALS)
public class BetterZoom extends Mod
{
	public BetterZoom()
	{
		this.addSetting(new Setting("Smooth Zoom", this, true));
		this.addSetting(new Setting("Zoom Factor (in %)", this, 0, 100, 100, 5));
        this.addSetting(new Setting("Scroll Zoom", this, true));
	}
}
