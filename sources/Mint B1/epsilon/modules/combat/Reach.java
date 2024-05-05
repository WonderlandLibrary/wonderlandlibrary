package epsilon.modules.combat;

import org.lwjgl.input.Keyboard;

import epsilon.events.Event;
import epsilon.events.listeners.EventUpdate;
import epsilon.modules.Module;
import epsilon.settings.setting.NumberSetting;

public class Reach extends Module{

	public NumberSetting reach = new NumberSetting ("Reach", 3, 0, 6, 0.1);
	
	public Reach() {
		super("Reach", Keyboard.KEY_NONE, Category.COMBAT, "Reach");
		this.addSettings(reach);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
		
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
		}
	}

}
