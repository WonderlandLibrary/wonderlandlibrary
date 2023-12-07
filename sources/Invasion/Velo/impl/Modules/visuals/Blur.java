package Velo.impl.Modules.visuals;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.impl.Event.EventUpdate;

public class Blur extends Module {
	
	public static boolean isEnabled = false;
	
	public Blur() {
		super("Blur", "Blur", Keyboard.KEY_NONE, Category.VISUALS);
	}
	
	public void onDisable() {
		isEnabled = false;
	}
	
	public void onUpdate(EventUpdate event) {
		isEnabled = true;
	}
}
