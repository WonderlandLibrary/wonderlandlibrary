package me.aidanmees.trivia.client.modules.Misc;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.module.Module;

public class AntiDrown extends Module {

	public AntiDrown() {
		super("AntiDrown", Keyboard.KEY_NONE, Category.COMBAT, "Prevents you from drowning in SinglePlayer only!");
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
