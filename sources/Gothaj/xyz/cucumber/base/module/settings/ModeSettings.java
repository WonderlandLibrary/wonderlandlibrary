package xyz.cucumber.base.module.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ModeSettings extends ModuleSettings {
	
	private String mode;
	private List<String> modes = new ArrayList<String>();

	public ModeSettings(String category,String name, String[] modes) {
		this.name = name;
		this.category = category;
		this.mode = modes[0];
		this.modes =Arrays.asList(modes);
	}
	public ModeSettings(String name,Supplier<Boolean> visibility, String[] modes) {
		this.name = name;
		this.mode = modes[0];
		this.modes =Arrays.asList(modes);
		this.visibility = visibility;
	}
	public ModeSettings(String name, String[] modes) {
		this.name = name;
		this.category = null;
		this.mode = modes[0];
		this.modes =Arrays.asList(modes);
	}
	
	public void cycleModes() {
		if(modes.indexOf(mode) == modes.size()-1) {
			mode = modes.get(0);
			return;
		}
		mode = modes.get(modes.indexOf(mode)+1);
	}
	
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public List<String> getModes() {
		return modes;
	}
	public void setModes(List<String> modes) {
		this.modes = modes;
	}
}
