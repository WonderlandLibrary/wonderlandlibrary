package markgg.modules.impl.render;

import markgg.modules.ModuleInfo;
import markgg.ui.click.astolfo.AstolfoGui;

import org.lwjgl.input.Keyboard;

import markgg.modules.Module;

@ModuleInfo(name = "ClickGui", category = Module.Category.RENDER, bind = Keyboard.KEY_RSHIFT)
public class ClickGUI extends Module{

	public void onEnable() {
		mc.displayGuiScreen(new AstolfoGui());
		toggle();
	}
}
