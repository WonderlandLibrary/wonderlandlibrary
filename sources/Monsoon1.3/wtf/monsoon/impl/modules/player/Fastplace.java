package wtf.monsoon.impl.modules.player;

import org.lwjgl.input.Keyboard;

import wtf.monsoon.api.event.EventTarget;
import wtf.monsoon.api.event.impl.EventPreMotion;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;

public class Fastplace extends Module {
	
	public Fastplace() {
		super("Fastplace", "Place blocks faster", Keyboard.KEY_NONE, Category.PLAYER);
	}
	
	@EventTarget
	public void eventMotionHook(EventPreMotion e) {
		mc.rightClickDelayTimer = 0;
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		mc.rightClickDelayTimer = 6;
	}

}
