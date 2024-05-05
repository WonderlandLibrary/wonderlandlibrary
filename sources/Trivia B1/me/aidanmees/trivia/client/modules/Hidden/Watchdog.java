package me.aidanmees.trivia.client.modules.Hidden;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.tools.Bypass;
import me.aidanmees.trivia.module.Module;

public class Watchdog extends Module {

		public Watchdog() {
			super("Watchdog", Keyboard.KEY_NONE, Category.BYPASSES, "Bypass watchdogo!");
		}
		@Override
		public void onUpdate() {
			Bypass.Watchdog();
			super.onUpdate();
		}

		@Override
		public boolean isCheckbox() {
			return true;
		}

	}
