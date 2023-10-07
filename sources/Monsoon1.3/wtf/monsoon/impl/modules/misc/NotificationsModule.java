package wtf.monsoon.impl.modules.misc;

import org.lwjgl.input.Keyboard;

import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.impl.BooleanSetting;
import wtf.monsoon.api.setting.impl.ModeSetting;
import wtf.monsoon.api.setting.impl.NumberSetting;
import wtf.monsoon.api.util.misc.Timer;

public class NotificationsModule extends Module {

	public NumberSetting time = new NumberSetting("Time", 2.5, 1, 10, 0.5, this);
	public BooleanSetting modToggle = new BooleanSetting("Module Toggle", true, this);
	public ModeSetting font = new ModeSetting("Font", this, "Monsoon", "Monsoon", "Moon");

	public NotificationsModule() {
		super("Notifications", "Customize your Notifications", Keyboard.KEY_NONE, Category.MISC);
		addSettings(time,modToggle,font);
	}
	
	Timer timer = new Timer();
	
	
}