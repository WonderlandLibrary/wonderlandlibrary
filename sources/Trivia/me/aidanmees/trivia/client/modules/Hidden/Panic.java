package me.aidanmees.trivia.client.modules.Hidden;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.module.Module;

public class Panic extends Module {

	public Panic() {
		super("Panic", Keyboard.KEY_NONE, Category.PLAYER,
				"Disables all mods instantly.");
	}

	@Override
	public void onEnable() {
		for(Module m : trivia.getToggledModules()) {
			m.setToggled(false, true);
		}
		this.setToggled(false, false);
	}

}
