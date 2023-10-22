package epsilon.modules.render;

import org.lwjgl.input.Keyboard;

import epsilon.modules.Module;
import epsilon.settings.setting.ModeSetting;
import epsilon.settings.setting.NumberSetting;

public class Theme extends Module{

	public static ModeSetting theme = new ModeSetting("Color Theme", "Xeno", "Xeno", "Custom","Stitch", "Tena", "CottonCandy", "Superhero", "Rainbow", "Spongebob", "Grass", "PissWater", "YellowSnow");
	
	public static ModeSetting color1 = new ModeSetting ("CustomColor1", "Red", "Red", "Orange", "Yellow", "Green", "LightBlue","Blue", "Purple", "Pink", "White");
	public static ModeSetting color2 = new ModeSetting ("CustomColor2", "Red", "Red", "Orange", "Yellow", "Green", "LightBlue","Blue", "Purple", "Pink", "White");
	
	public static NumberSetting speed = new NumberSetting("ColorSpeed", 1, 0.1, 5, 0.1);
	
	public Theme() {
		super("Theme", Keyboard.KEY_NONE, Category.RENDER, "Theme");
		this.addSettings(theme, color1, color2, speed);
	}
	
	
	public static String getTheme() {
		return theme.getMode();
	}
	
}
