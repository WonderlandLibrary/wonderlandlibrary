package me.aidanmees.trivia.client.modules.World;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.module.Module;

public class SafeWalk extends Module {

	public SafeWalk() {
		super("SafeWalk", Keyboard.KEY_NONE, Category.MOVEMENT, "Prevents you from falling from blocks, just like if you were sneaking.");
	}

}
