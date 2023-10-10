package appu26j.mods.visuals;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;

@ModInterface(name = "Crosshair", description = "Allows you to modify the crosshair.", category = Category.VISUALS)
public class Crosshair extends Mod
{
	public Crosshair()
	{
		this.addSetting(new Setting("Transparency", this, true));
		this.addSetting(new Setting("Crosshair Color (RGB)", this, new int[]{255, 255, 255}));
		this.addSetting(new Setting("Size", this, 0.5F, 1, 2, 0.25F));
	}
}
