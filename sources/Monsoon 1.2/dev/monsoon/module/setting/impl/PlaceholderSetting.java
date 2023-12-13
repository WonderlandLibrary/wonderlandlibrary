package dev.monsoon.module.setting.impl;

import dev.monsoon.module.base.Module;
import dev.monsoon.module.setting.Setting;

public class PlaceholderSetting extends Setting {

	public Module parent;

	public PlaceholderSetting(String name, Module parent) {
		this.name = name;
		this.parent = parent;

		shouldRender = true;
	}

	public PlaceholderSetting(String name, Module parent, boolean shouldRender) {
		this.name = name;
		this.parent = parent;

		this.shouldRender = false;
	}

	
}
