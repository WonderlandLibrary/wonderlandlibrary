package net.caffeinemc.phosphor.module.setting;

import net.caffeinemc.phosphor.module.Module;

public abstract class Setting {
	public String name;
	public Module parent;

	public String getName() {
		return name;
	}
}