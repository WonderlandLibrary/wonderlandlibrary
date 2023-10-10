package appu26j.mods.visuals;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;

@ModInterface(name = "Hit Color", description = "Allows you to change the color of when somebody is attacked.", category = Category.VISUALS)
public class DamageTint extends Mod
{
	public DamageTint()
	{
		this.addSetting(new Setting("Hit Color (RGB)", this, new int[]{255, 0, 0}));
	}
}
