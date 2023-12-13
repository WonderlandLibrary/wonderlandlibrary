package dev.monsoon.module.implementation.misc;

import dev.monsoon.module.setting.impl.ModeSetting;
import org.lwjgl.input.Keyboard;

import dev.monsoon.event.Event;
import dev.monsoon.module.base.Module;
import dev.monsoon.module.enums.Category;

public class TabGUI extends Module {
	
	public ModeSetting ColorOption = new ModeSetting("Color", this, "Blue", "Colorful", "Red", "Blue", "Orange", "Green", "White");
	public int currentTab;
	public boolean expanded;
	
	public TabGUI() {
		super("TabGUI", Keyboard.KEY_NONE, Category.MISC);
		//toggled = true;
		this.addSettings(ColorOption);
	}
	
	public void onEvent(Event e) {

	}
}