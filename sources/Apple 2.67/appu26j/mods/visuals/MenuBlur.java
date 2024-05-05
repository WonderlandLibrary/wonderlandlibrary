package appu26j.mods.visuals;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;

@ModInterface(name = "Menu Blur", description = "Blurs the background of the inventory.", category = Category.VISUALS)
public class MenuBlur extends Mod
{
    public MenuBlur()
    {
        this.addSetting(new Setting("Intensity", this, 1, 15, 40, 1));
        this.addSetting(new Setting("Reduce background darkening", this, true));
    }
}
