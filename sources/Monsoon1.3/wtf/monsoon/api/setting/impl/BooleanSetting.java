package wtf.monsoon.api.setting.impl;

import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.Setting;

public class BooleanSetting extends Setting {

	public boolean enabled;
	public Module parent;

	public BooleanSetting(String name,boolean enabled,Module parent) {
		this.name = name;
		this.enabled = enabled;
		this.parent = parent;

		shouldRender = true;
	}

	public BooleanSetting(String name,boolean enabled,Module parent, boolean shouldRender) {
		this.name = name;
		this.enabled = enabled;
		this.parent = parent;
		shouldRender = false;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean getValue() {
		return enabled;
	}
	
	public void toggle() {
		enabled = !enabled;
	}

	public boolean isOn() {
		return enabled;
	}
	
}
