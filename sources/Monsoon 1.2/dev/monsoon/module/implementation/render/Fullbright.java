package dev.monsoon.module.implementation.render;

import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventUpdate;
import org.lwjgl.input.Keyboard;

import dev.monsoon.module.base.Module;
import dev.monsoon.module.enums.Category;

public class Fullbright extends Module {
	public Fullbright() {
		super("Fullbright", Keyboard.KEY_NONE, Category.RENDER);
	}

	
	public void onDisable() {
		mc.gameSettings.gammaSetting = 1;
	}

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			mc.gameSettings.gammaSetting = 100;
		}
	}
}
