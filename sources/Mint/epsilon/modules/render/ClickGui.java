package epsilon.modules.render;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.ClickGUI.ClickGUI;
import epsilon.ClickGUI.dropdown.DropdownGUI;
import epsilon.ClickGUI.dropdown.ModeSettingGUI;
import epsilon.events.Event;
import epsilon.events.listeners.EventRenderGUI;
import epsilon.events.listeners.EventUpdate;
import epsilon.modules.Module;

public class ClickGui extends Module{

	public ClickGui() {
		super("ClickGUI", Keyboard.KEY_RSHIFT, Category.RENDER, "e621.net");
	}

	public void onEnable() { 
		
		mc.displayGuiScreen(Epsilon.INSTANCE.dropdown);
		
	}
	
	
	public void onEvent(Event e) {
		if(e instanceof EventRenderGUI) {
			if(!(mc.currentScreen instanceof DropdownGUI || mc.currentScreen instanceof ModeSettingGUI) && this.toggled) {
				this.toggle();
			}
		}

	}

}
