package me.aidanmees.trivia.client.modules.Hidden;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.modules.Combat.KillauraBETA;
import me.aidanmees.trivia.client.tools.Bypass;
import me.aidanmees.trivia.module.Module;

public class Gwen extends Module {

		public Gwen() {
			super("Gwen", Keyboard.KEY_NONE, Category.BYPASSES, "Bypass Mineplex!");
		}
		@Override
		public void onUpdate() {
			Bypass.Gwen();
			
			super.onUpdate();
		}

		@Override
		public boolean isCheckbox() {
			return true;
		}

	}
