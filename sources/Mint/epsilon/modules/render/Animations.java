package epsilon.modules.render;

import org.lwjgl.input.Keyboard;

import epsilon.events.Event;
import epsilon.events.listeners.EventUpdate;
import epsilon.modules.Module;
import epsilon.settings.setting.BooleanSetting;
import epsilon.settings.setting.ModeSetting;
import epsilon.settings.setting.NumberSetting;

public class Animations extends Module{

	public static ModeSetting anim = new ModeSetting("Animation", "Smooth", "Smooth", "Mint","Stab", "Exhibition", "Gyroscope");
	
	public Animations() {
		super("Animations", Keyboard.KEY_NONE, Category.RENDER, "Changes the way blocking and swinging is rendered");
		this.addSettings(anim);
	}
		

}
