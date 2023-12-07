package me.robbanrobbin.jigsaw.client.modules;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.gui.GuiJigsawBypassList;
import me.robbanrobbin.jigsaw.module.Module;

public class OpenBypasses extends Module {

	public OpenBypasses() {
		super("Open Presets List", 0, Category.PRESETS, "Opens a list of all preset settings for different servers.");
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		mc.displayGuiScreen(new GuiJigsawBypassList(mc.currentScreen));
		
		this.setToggled(false, true);
	}
	
}
