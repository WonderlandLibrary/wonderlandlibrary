package sudo.module.client;

import java.awt.Color;

import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.ColorSetting;
import sudo.module.settings.NumberSetting;

public class ClickGuiMod extends Mod {
	
    public static ClickGuiMod INSTANCE;
    public BooleanSetting description = new BooleanSetting("Module desc", true);
    public BooleanSetting pause = new BooleanSetting("Pause", false);
    public BooleanSetting background = new BooleanSetting("Background", false);
    public BooleanSetting blur = new BooleanSetting("Blur", true);
    public NumberSetting blurIntensity = new NumberSetting("Blur intensity", 1, 50, 2, 1);
    public ColorSetting primaryColor = new ColorSetting("Color", new Color(0xffff0000));
    public ColorSetting secondaryColor = new ColorSetting("Color", new Color(0xff8a0000));
    
    
    public ClickGuiMod() {
        super("ClickGui", "Customize the ClickGUI", Category.CLIENT, 0);
        INSTANCE = this;
        addSettings(description, background, pause, blur, blurIntensity, primaryColor,secondaryColor);
    }
}