package wtf.monsoon.impl.modules.visual;

import org.lwjgl.input.Keyboard;

import wtf.monsoon.api.event.EventTarget;
import wtf.monsoon.api.event.impl.EventUpdate;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.Wrapper;


public class Fullbright extends Module {
	public Fullbright() {
		super("Fullbright", "Makes the game Brighter", Keyboard.KEY_NONE, Category.RENDER);
	}

	
	public void onDisable() {
		super.onDisable();
		Wrapper.mc.gameSettings.gammaSetting = 1;
	}

	@EventTarget
	public void onEvent(EventUpdate e) {
		Wrapper.mc.gameSettings.gammaSetting = 100;
	}
}
