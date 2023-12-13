package me.aidanmees.trivia.client.modules.Hidden;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.tools.Bypass;
import me.aidanmees.trivia.module.Module;

public class Guardian extends Module {

		public Guardian() {
			super("Guardian", Keyboard.KEY_NONE, Category.BYPASSES, "Bypass Guardian!");
		}

		@Override
		public void onUpdate() {
			Bypass.Guardian();
			super.onUpdate();
		}
		@Override
		public boolean isCheckbox() {
			return true;
		}

	}
