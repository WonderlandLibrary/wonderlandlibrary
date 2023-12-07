package me.robbanrobbin.jigsaw.client.modules;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.CheckBtnSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.module.Module;

public class Animations extends Module {

	public Animations() {
		super("Animations", 0, Category.FUN, "Changes some animations.");
	}
	
	@Override
	public ModSetting[] getModSettings() {
		CheckBtnSetting box1 = new CheckBtnSetting("Swing Animation", "swingAnimation");
		CheckBtnSetting box2 = new CheckBtnSetting("Better Bobbing", "betterBobbing");
		return new ModSetting[]{box1, box2};
	}

	public void onToggle() {

		super.onToggle();
	}
}
