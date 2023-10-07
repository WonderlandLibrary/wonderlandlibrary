package dev.monsoon.module.implementation.player;

import org.lwjgl.input.Keyboard;

import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventMotion;
import dev.monsoon.module.base.Module;
import dev.monsoon.module.enums.Category;

public class Fastplace extends Module {
	public Fastplace() {
		super("Fastplace", Keyboard.KEY_NONE, Category.PLAYER);
	}
	
	
	public void onEvent(Event e) {
		if(e instanceof EventMotion) {
			if(e.isPre()) {
				mc.rightClickDelayTimer = 0;
			}
		}
	}
	
	public void onDisable() {
			mc.rightClickDelayTimer = 6;
		}
	}
