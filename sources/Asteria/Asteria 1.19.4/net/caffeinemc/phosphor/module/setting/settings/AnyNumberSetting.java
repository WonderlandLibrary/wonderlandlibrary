package net.caffeinemc.phosphor.module.setting.settings;

import imgui.ImGui;
import imgui.type.ImDouble;
import imgui.type.ImInt;
import net.caffeinemc.phosphor.module.Module;

public class AnyNumberSetting extends NumberSetting {
	public AnyNumberSetting(String name, Module parent, double value, boolean decimal) {
		super(name, parent, value, 0, 0, 0);
		this.decimal = decimal;
	}

	@Override
	public void render() {
		ImGui.pushID(parent.getName()+"/"+this.getName());

		ImGui.text(this.name);
		boolean changed;

		if (decimal) {
			ImDouble val = new ImDouble(this.value);

			ImGui.pushItemWidth(170f);
			changed = ImGui.inputDouble("", val);
			ImGui.popItemWidth();

			if (changed)
				this.value = val.doubleValue();
		} else {
			ImInt val = new ImInt((int) this.value);

			ImGui.pushItemWidth(170f);
			changed = ImGui.inputInt("", val);
			ImGui.popItemWidth();

			if (changed)
				this.value = val.doubleValue();
		}

		ImGui.popID();
	}
}