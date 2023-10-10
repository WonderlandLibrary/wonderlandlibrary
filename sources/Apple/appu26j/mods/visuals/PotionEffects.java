package appu26j.mods.visuals;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;

@ModInterface(name = "Potion Effects", description = "Allows you to customize potions.", category = Category.VISUALS)
public class PotionEffects extends Mod
{
    public PotionEffects()
    {
        this.addSetting(new Setting("Hide own potion effect particles", this, false));
        this.addSetting(new Setting("Centered inventory", this, false));
    }
}
