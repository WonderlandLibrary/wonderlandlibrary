package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.CheckBtnSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.module.Module;

public class Notifications extends Module {

	@Override
	public ModSetting[] getModSettings() {
		CheckBtnSetting box1 = new CheckBtnSetting("On Hack Enabled", "notificationModulesEnable");
		CheckBtnSetting box2 = new CheckBtnSetting("On Hack Disable", "notificationModulesDisable");
		return new ModSetting[]{box1, box2};
	}
	
	public Notifications() {
		super("Notifications", Keyboard.KEY_NONE, Category.GLOBAL);
	}

}
