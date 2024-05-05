package Velo.impl.Settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import Velo.api.setting.Setting;

public class NumberSetting extends Setting {
	
	@Expose
    @SerializedName("setting")
	public double value;
	
	public double minimum, maximum, increment;


	public NumberSetting(String name, double value, double minimum, double maximum, double increment) {
		this.name = name;
		this.value = value;
		this.minimum = minimum;
		this.maximum = maximum;
		this.increment = increment;

	}
	
	public double getValue() {
		return value;
	}
	
	

	
	public float getValueFloat() {
		return (float)value;
	}

	public void setValue(double value) {
		double precision = 1 / increment;
		this.value = Math.round(Math.max(minimum, Math.min(maximum, value)) * precision) / precision;
	}
	
	public void setValue2(double value) {
		this.value = value;
	}
	
	public void increment(boolean positive) {
		setValue(getValue() + (positive ? 1 : -1 ) * increment);
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
	
}
