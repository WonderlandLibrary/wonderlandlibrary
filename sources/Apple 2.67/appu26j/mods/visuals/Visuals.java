package appu26j.mods.visuals;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;

@ModInterface(name = "1.7 Visuals", description = "This mod changes 1.8 animations to 1.7.", category = Category.VISUALS)
public class Visuals extends Mod
{
	public Visuals()
	{
		this.addSetting(new Setting("1.7 Block Hit", this, true));
		this.addSetting(new Setting("1.7 Item Position", this, true));
		this.addSetting(new Setting("1.7 Armor Hit Color", this, true));
		this.addSetting(new Setting("1.7 Sword Block", this, true));
		this.addSetting(new Setting("1.7 Sneak", this, true));
        this.addSetting(new Setting("1.7 Debug Hitbox", this, true));
	}
}
