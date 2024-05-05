package net.caffeinemc.phosphor.module.setting.settings;

import imgui.ImGui;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.RenderableSetting;
import net.caffeinemc.phosphor.module.setting.Setting;

public class BooleanSetting extends Setting implements RenderableSetting {
	public boolean enabled;
	  
	public BooleanSetting(String name, Module parent, boolean enabled) {
	    this.name = name;
	    this.parent = parent;
	    this.enabled = enabled;
		if (parent != null) parent.addSettings(this);
	}
	  
	public boolean isEnabled() {
	    return this.enabled;
	}
	  
	public void setEnabled(boolean enabled) {
	    this.enabled = enabled;
	}
	
	public void toggle() {
	    this.enabled = !this.enabled;
	}

	@Override
	public void render() {
		ImGui.pushID(parent.getName()+"/"+this.getName());

		ImGui.text(this.name);
		if (ImGui.checkbox("", this.enabled)) {
			toggle();
		}

		ImGui.popID();
	}
}
