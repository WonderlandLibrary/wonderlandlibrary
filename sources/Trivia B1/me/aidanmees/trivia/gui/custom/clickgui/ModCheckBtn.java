package me.aidanmees.trivia.gui.custom.clickgui;

import me.aidanmees.trivia.module.Module;

public class ModCheckBtn extends CheckBtn {
	
	private Module mod;
	
	@Override
	public void update() {
		this.setToggled(mod.isToggled());
		super.update();
	}
	
	public Module getMod() {
		return mod;
	}
	
	public void setMod(Module mod) {
		this.mod = mod;
	}
	
	@Override
	public void onClicked(double x, double y, int button) {
		super.onClicked(x, y, button);
		if(button == 0) {
			mod.toggle();
		}
	}
	
}