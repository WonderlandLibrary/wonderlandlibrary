package me.aidanmees.trivia.client.modules.Player;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.module.Module;
import net.minecraft.client.gui.GuiScreen;

public class AutoRespawn extends Module {

	public AutoRespawn() {
		super("AutoRespawn", Keyboard.KEY_NONE, Category.MISC, "Respawns automatically when you die.");
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

		if (mc.thePlayer.isDead || mc.thePlayer.getHealth() == 0) {
			mc.thePlayer.respawnPlayer();
			
		}

		super.onUpdate();
	}

}
