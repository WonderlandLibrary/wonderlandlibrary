package dev.monsoon.module.implementation.movement;

import org.lwjgl.input.Keyboard;

import dev.monsoon.module.base.Module;
import dev.monsoon.module.enums.Category;

public class Safewalk extends Module {
	
	public static boolean isEnabled = false;
	
	public Safewalk() {
		super("Safewalk", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	public void onEnable() {
		isEnabled = true;
	}
	
	public void onDisable() {
		isEnabled = false;
	}
	
}
