package appu26j.mods.visuals;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;

@ModInterface(name = "Tab List", description = "Allows you to change how the tab works.", category = Category.VISUALS)
public class TabList extends Mod
{
    public TabList()
    {
        this.addSetting(new Setting("Text Shadow", this, true));
    }
}
