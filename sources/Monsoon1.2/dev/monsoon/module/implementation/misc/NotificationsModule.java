package dev.monsoon.module.implementation.misc;

import dev.monsoon.event.Event;
import dev.monsoon.module.base.Module;
import dev.monsoon.module.enums.Category;
import dev.monsoon.module.setting.impl.BooleanSetting;
import dev.monsoon.module.setting.impl.ModeSetting;
import dev.monsoon.module.setting.impl.NumberSetting;
import dev.monsoon.util.misc.Timer;
import org.lwjgl.input.Keyboard;

public class NotificationsModule extends Module {

	public NumberSetting time = new NumberSetting("Time", 2.5, 1, 10, 0.5, this);
	public BooleanSetting modToggle = new BooleanSetting("Module Toggle", false, this);

	public NotificationsModule() {
		super("Notifications", Keyboard.KEY_NONE, Category.MISC);
		addSettings(time,modToggle);
	}
	
	Timer timer = new Timer();
	
	boolean PlayerEat = false;
	
	public void onEvent(Event e) {
		
	}
	
	
}