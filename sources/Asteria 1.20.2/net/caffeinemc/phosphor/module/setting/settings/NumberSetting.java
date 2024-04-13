package net.caffeinemc.phosphor.module.setting.settings;

import imgui.ImGui;
import imgui.flag.ImGuiDataType;
import imgui.type.ImDouble;
import imgui.type.ImInt;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.RenderableSetting;
import net.caffeinemc.phosphor.module.setting.Setting;

public class NumberSetting extends Setting implements RenderableSetting {
	public double value;
	public double minimum;
	public double maximum;
	public double increment;
	public boolean decimal;
	  
	public NumberSetting(String name, Module parent, double value, double minimum, double maximum, double increment) {
		this.name = name;
	    this.parent = parent;
	    this.value = value;
	    this.minimum = minimum;
	    this.maximum = maximum;
	    this.increment = increment;
		this.decimal = !(Math.floor(increment) == increment);

		if (parent != null) parent.addSettings(this);
	}
	  
	public double getValue() {
	    return this.value;
	}

	public float getFValue() {
		return (float) this.value;
	}

	public int getIValue() {
		return (int) this.value;
	}
	  
	public void setValue(double value) {
	    double precision = 1.0D / this.increment;
	    this.value = Math.round(Math.max(this.minimum, Math.min(this.maximum, value)) * precision) / precision;
	}
	 
	public void increment(boolean positive) {
	    setValue(getValue() + (positive ? 1 : -1) * increment);
	}
	  
	public double getMinimum() {
	    return this.minimum;
	}

	public void setMinimum(double minimum) {
	    this.minimum = minimum;
	}
	  
	public double getMaximum() {
	    return this.maximum;
	}
	
	public void setMaximum(double maximum) {
	    this.maximum = maximum;
	}
	  
	public double getIncrement() {
	    return this.increment;
	}
	  
	public void setIncrement(double increment) {
	    this.increment = increment;
	}

	@Override
	public void render() {
		ImGui.pushID(parent.getName()+"/"+this.getName());

		ImGui.text(this.name);
		boolean changed;

		if (decimal) {
			ImDouble val = new ImDouble(this.value);

			ImGui.pushItemWidth(170f);
			changed = ImGui.sliderScalar("", ImGuiDataType.Double, val, minimum, maximum, "%.3f");
			ImGui.popItemWidth();

			if (changed)
				this.value = val.doubleValue();
		} else {
			ImInt val = new ImInt((int) this.value);

			ImGui.pushItemWidth(170f);
			changed = ImGui.sliderScalar("", ImGuiDataType.S32, val, (int) minimum, (int) maximum);
			ImGui.popItemWidth();

			if (changed)
				this.value = val.doubleValue();
		}

		ImGui.popID();
	}
}