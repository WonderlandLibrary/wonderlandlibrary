package net.caffeinemc.phosphor.module.setting.settings;

import java.util.Arrays;

import imgui.ImGui;
import imgui.type.ImInt;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.RenderableSetting;
import net.caffeinemc.phosphor.module.setting.Setting;

public class ModeSetting extends Setting implements RenderableSetting {
	public int index;
	  
	public String[] modes;
	  
	public ModeSetting(String name, Module parent, String defaultMode, String... modes) {
	    this.name = name;
	    this.parent = parent;
	    this.modes = modes;
	    this.index = Arrays.stream(this.modes).toList().indexOf(defaultMode);
		parent.addSettings(this);
	}
	  
	public String getMode() {
	    return this.modes[this.index];
	}
	  
	public void setMode(String mode) {
		this.index = Arrays.stream(this.modes).toList().indexOf(mode);
	}
	  
	public boolean is(String mode) {
	    return (this.index == Arrays.stream(this.modes).toList().indexOf(mode));
	}

	@Override
	public void render() {
		ImGui.pushID(parent.getName()+"/"+this.getName());

		ImGui.text(this.name);

		ImInt currentItem = new ImInt(this.index);

		ImGui.pushItemWidth(170f);
		ImGui.combo("", currentItem, modes);
		ImGui.popItemWidth();

		this.index = currentItem.get();

		ImGui.popID();
	}
}