package Scov.module.impl.visuals;

import org.lwjgl.input.Keyboard;

import Scov.api.annotations.Handler;
import Scov.events.render.EventRender2D;
import Scov.gui.click.ClickGui;
import Scov.gui.click.Panel;
import Scov.module.Module;

public class ClickGUI extends Module {
	
	public ClickGUI() {
		super("ClickGUI", Keyboard.KEY_RSHIFT, ModuleCategory.VISUALS);
	}
	
	public void onEnable() {
		toggle();
		mc.displayGuiScreen(new Panel());
		super.onEnable();
	}
	
	public void onDisable() {
		super.onDisable();
	}
}
