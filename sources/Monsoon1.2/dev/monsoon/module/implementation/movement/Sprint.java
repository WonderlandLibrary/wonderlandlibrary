package dev.monsoon.module.implementation.movement;

import dev.monsoon.util.entity.MovementUtil;
import org.lwjgl.input.Keyboard;

import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventUpdate;
import dev.monsoon.module.base.Module;
import dev.monsoon.module.enums.Category;

public class Sprint extends Module {
	public Sprint() {
		super("Sprint", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		mc.thePlayer.setSprinting(mc.gameSettings.keyBindSprint.getIsKeyPressed());
	}
	
	public boolean isSprintToggled = false;
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				if(MovementUtil.isMoving() && !mc.thePlayer.isSneaking() && !mc.thePlayer.isCollidedHorizontally) {
					mc.thePlayer.setSprinting(true);
					isSprintToggled = true;
				}
			}
		}
	}
	
}
