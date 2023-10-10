package vestige.setting.impl;

import lombok.Getter;
import vestige.setting.AbstractSetting;

import java.util.function.Supplier;

@Getter
public class IntegerSetting extends AbstractSetting {

    private int value;
    private int min, max;
    private int increment;

    public IntegerSetting(String name, int value, int min, int max, int increment) {
        super(name);
        this.value = value;
        this.min = min;
        this.max = max;
        this.increment = increment;
    }

    public IntegerSetting(String name, Supplier<Boolean> visibility, int value, int min, int max, int increment) {
        super(name, visibility);
        this.value = value;
        this.min = min;
        this.max = max;
        this.increment = increment;
    }

    public void setValue(int value) {
        double doubleMin = (double) min;
        double doubleMax = (double) max;

        double doubleIncrement = (double) increment;

        double doubleValue = (double) value;

        double precision = 1.0 / doubleIncrement;

        this.value = (int) Math.max(doubleMin, Math.min(doubleMax, Math.round(doubleValue * precision) / precision));
    }

}
