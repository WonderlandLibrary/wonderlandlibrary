package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;

public class FastFall extends Module {

	public FastFall() {
		super("FastFall", Keyboard.KEY_NONE, Category.MOVEMENT, "This makes you fall faster.");
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

		if (!mc.player.onGround && !mc.gameSettings.keyBindJump.pressed && !mc.player.isInWater()
				&& !mc.player.isInLava() && !mc.player.isOnLadder() && !mc.player.capabilities.isFlying) {
			for (Module m : Jigsaw.getModuleGroupMananger().getModuleGroupByName("AirGroup").getModules()) {
				if (m.isToggled()) {
					return;
				}
			}
			mc.player.motionY -= 0.1;
		}

		super.onUpdate();
	}

}
