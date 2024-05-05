package me.aidanmees.trivia.client.modules.Hidden;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.tools.Bypass;
import me.aidanmees.trivia.module.Module;

public class NCP extends Module {

		public NCP() {
			super("NCP", Keyboard.KEY_NONE, Category.BYPASSES, "Bypass NCP!");
		}

		@Override
		public void onUpdate() {
			Bypass.NCP();
			super.onUpdate();
		}
		@Override
		public boolean isCheckbox() {
			return true;
		}

	}
