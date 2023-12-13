package wtf.monsoon.api.setting.impl;

import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.Setting;

public class NumberSetting extends Setting {
	public double value, minimum, maximum, increment;
	public Module parent;
	
	public NumberSetting(String name, double value, double minimum, double maximum, double increment,  Module parent) {
		this.name = name;
		this.value = value;
		this.minimum = minimum;
		this.maximum = maximum;
		this.increment = increment;
		this.parent = parent;

		shouldRender = true;
	}

	public NumberSetting(String name, double value, double minimum, double maximum, double increment, Module parent, boolean shouldRender) {
		super();
		this.name = name;
		this.value = value;
		this.minimum = minimum;
		this.maximum = maximum;
		this.increment = increment;
		this.parent = parent;
		shouldRender = false;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		double precision = 1 / increment;
		this.value = Math.round(Math.max(minimum, Math.min(maximum, value)) * precision) / precision;
	}

	public void setValueFromConfig(double value) {
		this.value = value;
	}
	
	public void increment(boolean positive) {
		setValue(getValue() + (positive ? 1 : -1) * increment);
	}

	public double getMinimum() {
		return minimum;
	}

	public void setMinimum(double minimum) {
		this.minimum = minimum;
	}

	public double getMaximum() {
		return maximum;
	}

	public void setMaximum(double maximum) {
		this.maximum = maximum;
	}

	public double getIncrement() {
		return increment;
	}

	public void setIncrement(double increment) {
		this.increment = increment;
	}


	//old panelstudio things
	public double getNumber() {
		return value;
	}

	public void setNumber(double value) {
		this.value = value;

	}
}
