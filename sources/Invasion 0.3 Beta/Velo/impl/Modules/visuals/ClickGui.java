package Velo.impl.Modules.visuals;

import org.lwjgl.input.Keyboard;

import Velo.api.ClickGui.Display.DisplayClickGui;
import Velo.api.ClickGui.Display.DropdownClickgui;
import Velo.api.ClickGui.Display.VapeGui.VapeClickGui;
import Velo.api.Module.Module;
import Velo.impl.Event.EventRender;
import Velo.impl.Settings.ModeSetting;
import Velo.impl.Settings.NumberSetting;

public class ClickGui extends Module {
	
public static ModeSetting mode = new ModeSetting("Mode", "Discord", "Discord", "Vape", "Dropdown");
	
	
	public ClickGui() {
		super("ClickGui", "ClickGui", Keyboard.KEY_RSHIFT, Category.VISUALS);
		this.loadSettings(mode);
	}
	
	
	public void onEnable() {
		if(mode.equalsIgnorecase("Discord")) {
		mc.displayGuiScreen(new DisplayClickGui());
		}

		if(mode.equalsIgnorecase("Vape")) {
			mc.displayGuiScreen(new VapeClickGui());
		}
		if(mode.equalsIgnorecase("Dropdown")) {
			mc.displayGuiScreen(new DropdownClickgui());
		}
	}
	
	public void onRenderUpdate(EventRender event) {
		if(mc.currentScreen == null) {
			this.toggle();
		}
	}
}
