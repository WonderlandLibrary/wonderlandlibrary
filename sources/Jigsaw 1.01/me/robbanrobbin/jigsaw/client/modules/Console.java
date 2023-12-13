package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.console.GuiJigsawConsole;
import me.robbanrobbin.jigsaw.module.Module;

public class Console extends Module {

	public Console() {
		super("Console", Keyboard.KEY_U, Category.GLOBAL, "Opens the Console");
	}
	
	@Override
	public void onEnable() {
		if(!(mc.currentScreen instanceof GuiJigsawConsole) && !Jigsaw.ghostMode) {
			mc.displayGuiScreen(new GuiJigsawConsole());
		}
		setToggled(false, true);
		super.onEnable();
	}

}
