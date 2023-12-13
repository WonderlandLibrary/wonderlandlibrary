package me.aidanmees.trivia.client.modules.Hidden;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.tools.Bypass;
import me.aidanmees.trivia.module.Module;

public class AAC extends Module {

		public AAC() {
			super("AAC", Keyboard.KEY_NONE, Category.BYPASSES, "Bypass AAC!");
		}
		@Override
		public void onUpdate() {
			
			Bypass.AAC();
			super.onUpdate();
		}

		@Override
		public boolean isCheckbox() {
			return true;
		}

	}
