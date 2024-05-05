package tech.drainwalk.client.option.options;

import lombok.Getter;
import tech.drainwalk.client.option.Option;
import tech.drainwalk.utility.math.MathUtility;

import java.util.function.BooleanSupplier;

public class SelectOption extends Option<SelectOptionValue> {
    @Getter
    private final SelectOptionValue[] values;

    public SelectOption(String settingName, int selected, SelectOptionValue... values) {
        super(settingName, values[(int) MathUtility.clamp(selected,0, values.length -1)]);
        this.values = values;
    }
    public boolean getValueByIndex(final int index) {
        return getValue() == getValues()[index];
    }
    @Override
    public SelectOption addVisibleCondition(BooleanSupplier visible) {
        setVisible(visible);
        return this;
    }

    @Override
    public SelectOption addSettingDescription(String settingDescription) {
        this.settingDescription =settingDescription;
        return this;
    }

}
