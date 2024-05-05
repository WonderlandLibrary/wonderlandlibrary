package me.aidanmees.trivia.client.modules.Movement;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.events.UpdateEvent;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.module.Module;

public class AirJump extends Module {

	public AirJump() {
		super("AirJump", Keyboard.KEY_H, Category.MOVEMENT, "Allows you to jump in mid air!");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate(UpdateEvent e) {

			mc.thePlayer.onGround = true;
			mc.thePlayer.isAirBorne = false;

		super.onUpdate();
	}

	
}
