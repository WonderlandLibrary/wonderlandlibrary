package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;

public class AirJump extends Module {

	public AirJump() {
		super("AirJump", Keyboard.KEY_H, Category.MOVEMENT, "Enables you to jump in the air.");
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

		if (this.currentMode.equals("Vanilla")) {
			if (mc.gameSettings.keyBindJump.isPressed()) {
				mc.player.motionY = 0.7;
			}
		}
		if (this.currentMode.equals("NCP")) {
			mc.player.onGround = true;
			mc.player.isAirBorne = false;
		}

		super.onUpdate();
	}

	@Override
	public String[] getModes() {
		return new String[] { "Vanilla", "NCP" };
	}
}
