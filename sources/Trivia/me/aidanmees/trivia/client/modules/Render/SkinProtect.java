package me.aidanmees.trivia.client.modules.Render;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.module.Module;

public class SkinProtect extends Module {

	public SkinProtect() {
		super("SkinProtect", Keyboard.KEY_NONE, Category.WORLD, "Hides skins");
	}

}
