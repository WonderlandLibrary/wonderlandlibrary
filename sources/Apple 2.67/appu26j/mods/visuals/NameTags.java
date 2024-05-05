package appu26j.mods.visuals;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;

@ModInterface(name = "Nametags", description = "Allows you to change the way how nametags work.", category = Category.VISUALS)
public class NameTags extends Mod
{
	public NameTags()
	{
		this.addSetting(new Setting("Show self nametag", this, false));
		this.addSetting(new Setting("Text Shadow", this, false));
	}
}
