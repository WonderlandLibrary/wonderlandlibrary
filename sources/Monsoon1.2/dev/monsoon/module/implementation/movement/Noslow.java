package dev.monsoon.module.implementation.movement;

import org.lwjgl.input.Keyboard;

import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventUpdate;
import dev.monsoon.module.base.Module;
import dev.monsoon.module.enums.Category;

public class Noslow extends Module {
	public Noslow() {
		super("Noslow", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	public static boolean isnoslow = false;
	
	public void onEnable() {
		isnoslow = true;
	}
	
	@Override
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			isnoslow = true;
		}
	}
	
	public void onDisable() {
		isnoslow = false;
	}
	
}
