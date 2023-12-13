package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.gui.ScreenPos;
import me.robbanrobbin.jigsaw.module.Module;

public class Coords extends Module {

	public Coords() {
		super("Coords", Keyboard.KEY_NONE, Category.PLAYER, "Displays your coordinates.");
	}

	@Override
	public void onGui() {

		Jigsaw.getUIManager().addToQueue(String.valueOf("§jX: §r" + (int) mc.player.posX), ScreenPos.LEFTUP);
		Jigsaw.getUIManager().addToQueue(String.valueOf("§jY: §r" + (int) mc.player.posY), ScreenPos.LEFTUP);
		Jigsaw.getUIManager().addToQueue(String.valueOf("§jZ: §r" + (int) mc.player.posZ), ScreenPos.LEFTUP);

		super.onGui();
	}

}
