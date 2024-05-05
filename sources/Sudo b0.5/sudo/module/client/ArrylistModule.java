package sudo.module.client;

import java.awt.Color;

import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.ColorSetting;
import sudo.module.settings.ModeSetting;
import sudo.module.settings.NumberSetting;

public class ArrylistModule extends Mod{

	public BooleanSetting show = new BooleanSetting("Show", true);
	public NumberSetting offsetX1= new NumberSetting("Offset X", -500, 500, -3, 1);
	public NumberSetting offsetY1= new NumberSetting("Offset Y", -500, 500, 6, 1);
	public ModeSetting mode = new ModeSetting("Color", "Pulse", "Pulse", "Simple", "Cute");
	public ModeSetting SortY = new ModeSetting("Sorting", "Normal", "Normal", "Reversed");
	public ModeSetting SortX = new ModeSetting("Alignment", "Right", "Right", "Left");
	public BooleanSetting background = new BooleanSetting("Background", true);
	public BooleanSetting outline = new BooleanSetting("Outline", true);
	public BooleanSetting glow = new BooleanSetting("Glow", false);
    public ColorSetting glowcolor = new ColorSetting("Glow color", new Color(100,0,0));
	
	public ArrylistModule() {
		super("Arraylist", "Customize the Arraylist", Category.CLIENT, 0);
		addSettings(show, mode, offsetX1, offsetY1, SortX, SortY, background, outline, glow, glowcolor);
	}
	
	@Override
	public void onTick() {
		super.onTick();
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
	}
	@Override
	public void onDisable() {
		super.onDisable();
	}
}
