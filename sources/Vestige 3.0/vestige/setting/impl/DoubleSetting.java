package vestige.setting.impl;

import lombok.Getter;
import vestige.setting.AbstractSetting;

import java.util.function.Supplier;

@Getter
public class DoubleSetting extends AbstractSetting {

    private double value;
    private double min, max;
    private double increment;

    public DoubleSetting(String name, double value, double min, double max, double increment) {
        super(name);
        this.value = value;
        this.min = min;
        this.max = max;
        this.increment = increment;
    }

    public DoubleSetting(String name, Supplier<Boolean> visibility, double value, double min, double max, double increment) {
        super(name, visibility);
        this.value = value;
        this.min = min;
        this.max = max;
        this.increment = increment;
    }

    public void setValue(double value) {
        double precision = 1 / increment;

        this.value = Math.max(min, Math.min(max, Math.round(value * precision) / precision));
    }

    public String getStringValue() {
        if (value % 1.0 == 0.0) {
            return "" + (int) value;
        }

        return "" + value;
    }

}
