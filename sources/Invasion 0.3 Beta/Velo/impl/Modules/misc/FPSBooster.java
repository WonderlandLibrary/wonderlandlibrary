package Velo.impl.Modules.misc;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.impl.Event.EventUpdate;

public class FPSBooster extends Module {
	
	public FPSBooster() {
		super("FPSBooster", "FPSBooster", Keyboard.KEY_NONE, Category.OTHER);
	}
	
	public void onUpdate(EventUpdate event) {
		//Empty Shit
	}
}
