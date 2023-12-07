package Velo.impl.Modules.visuals;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.api.Module.Module.Category;
import Velo.impl.Event.EventRender;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.ModeSetting;
import Velo.impl.Settings.NumberSetting;

public class Chams extends Module {
	

	public static boolean isEnabled = false;
	
	public Chams() {
		super("Chams", "Chams", Keyboard.KEY_NONE, Category.VISUALS);
	
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		isEnabled = false;
	}
	
	public void onUpdate(EventUpdate event) {
		isEnabled = true;
	}
}
