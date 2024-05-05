/*package me.aidanmees.trivia.client.modules.Hidden;

import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.gui.GuitriviaBypassList;
import me.aidanmees.trivia.module.Module;

public class OpenBypasses extends Module {

	public OpenBypasses() {
		super("Open Bypasses List", 0, Category.BYPASSES, "Opens a list of all bypass settings.");
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		mc.displayGuiScreen(new GuitriviaBypassList(mc.currentScreen));
		
		this.setToggled(false, true);
	}
	
}
*/