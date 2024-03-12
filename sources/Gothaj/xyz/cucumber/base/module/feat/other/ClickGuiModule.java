package xyz.cucumber.base.module.feat.other;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiScreen;
import xyz.cucumber.base.interf.DropdownClickGui.DropdownClickGui;
import xyz.cucumber.base.interf.clickgui.ClickGui;
import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.ModeSettings;


@ModuleInfo(category = Category.OTHER, description = "Displays client gui", name = "Click Gui", key = Keyboard.KEY_RSHIFT, priority = ArrayPriority.LOW)
public class ClickGuiModule extends Mod{
	
	GuiScreen clickGui;
	
	public ModeSettings mode = new ModeSettings("Mode", new String[] {
			"Windowed", "Dropdown"
	});
	public ClickGuiModule() {
		this.addSettings(
				mode
				);
	}
	
	public void onEnable() {
		if(clickGui == null) clickGui = new ClickGui();
		
		switch(mode.getMode().toLowerCase()) {
		case "windowed":
			if(!(clickGui instanceof ClickGui)) clickGui = new ClickGui();
			break;
		case "dropdown":
			if(!(clickGui instanceof DropdownClickGui)) clickGui = new DropdownClickGui();
			break;
		}
		
		mc.displayGuiScreen(clickGui);
		this.toggle();
	}
	
}
