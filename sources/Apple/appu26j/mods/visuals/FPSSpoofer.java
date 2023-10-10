package appu26j.mods.visuals;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;

@ModInterface(name = "FPS Spoofer", description = "Spoofs your FPS!", category = Category.VISUALS)
public class FPSSpoofer extends Mod
{
    public FPSSpoofer()
    {
        this.addSetting(new Setting("Multipler", this, 1, 1, 5, 0.5F));
    }
}
