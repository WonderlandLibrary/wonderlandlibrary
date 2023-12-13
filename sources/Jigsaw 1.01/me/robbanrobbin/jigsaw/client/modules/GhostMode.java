package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;

public class GhostMode extends Module {

	public GhostMode() {
		super("GhostMode", Keyboard.KEY_P, Category.GLOBAL, "Hides all Jigsaw-related things in your Minecraft. Press p to turn it off!");
	}

	@Override
	public void onClientLoad() {
		Display.setTitle("[" + Jigsaw.getClientName() + " " + Jigsaw.getClientVersion() + "]1.12.2");
		super.onClientLoad();
	}

	@Override
	public void onDisable() {
		Display.setTitle("[" + Jigsaw.getClientName() + " " + Jigsaw.getClientVersion() + "]1.12.2");
		Jigsaw.ghostMode(false);
		super.onDisable();
	}

	@Override
	public void onEnable() {
		Display.setTitle("Minecraft 1.12.2");
		Jigsaw.ghostMode(true);
		mc.entityRenderer.stopUseShader();
		super.onEnable();
	}

	@Override
	public void onUpdate() {

		super.onUpdate();
	}

}
