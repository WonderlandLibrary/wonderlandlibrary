package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;

public class Spider extends Module {

	WaitTimer timer = new WaitTimer();
	boolean jumped = false;

	public Spider() {
		super("Spider", Keyboard.KEY_NONE, Category.MOVEMENT,
				"Automatically climbs walls for you, works 2.5 blocks on NCP mode");
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
		if (!mc.player.isCollidedHorizontally || mc.player.isOnLadder()) {
			timer.reset();
		}
		if (mode("NCP")) {
			if (timer.hasTimeElapsed(200, false) && mc.player.onGround) {
				Jigsaw.sendChatMessage(".damage");
				mc.player.jump();
				mc.player.jump();
				mc.player.jump();
			}
			if (timer.hasTimeElapsed(250, false) && !jumped) {

				jumped = true;
			}
			if (timer.hasTimeElapsed(400, true) && !mc.player.onGround) {
				mc.player.motionY = 0.46;
				jumped = false;
			}
		}
		if (mode("Vanilla")) {
			if (mc.player.isCollidedHorizontally) {
				mc.player.motionY = 0.33;
			}
		}

		super.onUpdate();
	}

	@Override
	public String[] getModes() {
		return new String[] { "Vanilla" };
	}

}
