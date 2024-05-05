package xyz.cucumber.base.interf.DropdownClickGui.ext.Settings;

import xyz.cucumber.base.interf.DropdownClickGui.ext.DropdownButton;
import xyz.cucumber.base.module.settings.ModuleSettings;
import xyz.cucumber.base.utils.position.PositionUtils;

public class DropdownClickGuiSettings implements DropdownButton {

	PositionUtils position;
	ModuleSettings mainSetting;
	
	@Override
	public void draw(int mouseX, int mouseY) {
		
	}

	@Override
	public void onClick(int mouseX, int mouseY, int button) {

	}

	@Override
	public void onRelease(int mouseX, int mouseY, int button) {
		
	}

	@Override
	public void onKey(char chr, int key) {
		
	}

	public PositionUtils getPosition() {
		return position;
	}

	public void setPosition(PositionUtils position) {
		this.position = position;
	}

	public ModuleSettings getMainSetting() {
		return mainSetting;
	}

	public void setMainSetting(ModuleSettings mainSetting) {
		this.mainSetting = mainSetting;
	}


	
}
