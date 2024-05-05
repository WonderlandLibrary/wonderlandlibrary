package xyz.cucumber.base.interf.clickgui.content.ex.impl;

import xyz.cucumber.base.module.settings.ModuleSettings;
import xyz.cucumber.base.utils.position.PositionUtils;

public class SettingsButton {
	PositionUtils position;
	
	ModuleSettings settingMain;
	
	public void draw(int mouseX, int mouseY) {
		
	}
	
	public void click(int mouseX, int mouseY, int button) {
		
	}
	
	public void release(int mouseX, int mouseY, int button) {
		
	}
	public void key(char character, int key) {
		
	}
	
	boolean isVisible = true;

	public PositionUtils getPosition() {
		return position;
	}

	public void setPosition(PositionUtils position) {
		this.position = position;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public ModuleSettings getSettingMain() {
		return settingMain;
	}

	public void setSettingMain(ModuleSettings settingMain) {
		this.settingMain = settingMain;
	}
	
	
}
