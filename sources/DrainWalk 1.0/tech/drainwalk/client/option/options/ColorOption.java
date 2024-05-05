package tech.drainwalk.client.option.options;

import tech.drainwalk.client.option.Option;

import java.util.function.BooleanSupplier;

public class ColorOption extends Option<Integer> {
    public ColorOption(String settingName, Integer value) {
        super(settingName, value);
    }

    public ColorOption addVisibleCondition(BooleanSupplier visible) {
        setVisible(visible);
        return this;
    }

    public ColorOption addSettingDescription(String settingDescription) {
        this.settingDescription =settingDescription;
        return this;
    }
}
