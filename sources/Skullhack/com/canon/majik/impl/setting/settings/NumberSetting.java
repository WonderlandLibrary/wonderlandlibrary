package com.canon.majik.impl.setting.settings;

import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.setting.Setting;

public class NumberSetting extends Setting<Number> {

    private Number min,max;

    public NumberSetting(String name, Number value, Number min, Number max, Module parent) {
        super(name, value, parent);
        this.min = min;
        this.max = max;
    }

    public Number getMin() {
        return min;
    }

    public void setMin(Number min) {
        this.min = min;
    }

    public Number getMax() {
        return max;
    }

    public void setMax(Number max) {
        this.max = max;
    }
}
