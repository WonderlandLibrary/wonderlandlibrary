package appu26j.mods.visuals;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;

@ModInterface(name = "Block Overlay", description = "Allows you to customize the block outline and apply an overlay.", category = Category.VISUALS)
public class BlockOverlay extends Mod
{
    public BlockOverlay()
    {
        this.addSetting(new Setting("Alpha", this, 0, 102, 255, 51));
        this.addSetting(new Setting("Color (RGB)", this, new int[] {0, 0, 0}));
        this.addSetting(new Setting("Rainbow Color", this, false));
        this.addSetting(new Setting("Line Thickness", this, 1, 2, 15, 0.5F));
        this.addSetting(new Setting("Show Focused Side Only", this, false));
    }
}
