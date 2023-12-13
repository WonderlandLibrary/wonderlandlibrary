package me.aidanmees.trivia.client.modules.Player;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.module.Module;

public class FastPlace extends Module {

	public FastPlace() {
		super("FastPlace", Keyboard.KEY_NONE, Category.PLAYER, "Disables the right click delay. Enables you to place blocks a lot faster.");
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
	public void onUpdate() {

		super.onUpdate();
	}

}
