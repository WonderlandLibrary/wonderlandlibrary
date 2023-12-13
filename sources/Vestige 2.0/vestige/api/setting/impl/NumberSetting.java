package vestige.api.setting.impl;

import lombok.Getter;
import vestige.api.module.Module;
import vestige.api.setting.Setting;

public class NumberSetting extends Setting {

	@Getter
	protected double currentValue;

	@Getter protected double min, max;
	@Getter protected double increment;
	private boolean convertToInteger;

    public NumberSetting(String name, Module parent, double value, double min, double max, double increment, boolean convertToInteger) {
        super(name, parent);
        this.currentValue = value;
        this.min = min;
        this.max = max;
        this.increment = increment;
        this.convertToInteger = convertToInteger;
    }

    public void setCurrentValue(double value) {
        double precision = 1 / increment;
        this.currentValue = Math.round(Math.max(min, Math.min(max, value)) * precision) / precision;
    }
    
    public boolean shouldConvertToInteger() {
    	return convertToInteger;
    }
    
}
