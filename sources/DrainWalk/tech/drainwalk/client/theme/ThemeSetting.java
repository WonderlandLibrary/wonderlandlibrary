package tech.drainwalk.client.theme;

import tech.drainwalk.client.option.Option;

import java.util.Arrays;
import java.util.function.BooleanSupplier;

public class ThemeSetting<T extends Enum<T>> extends Option<T> {

    private final T[] values;

    public ThemeSetting(final String settingName,
                       final T value) {
        super(settingName, value);

        values = getType().getEnumConstants();
    }

    @Override
    public ThemeSetting<T> addVisibleCondition(BooleanSupplier visible) {
        setVisible(visible);
        return this;
    }

    @Override
    public ThemeSetting<T> addSettingDescription(String settingDescription) {
        addSettingDescription(settingDescription);
        return this;
    }

    public void setValue(final int index) {
        setValue(values[Math.max(0, Math.min(values.length - 1, index))]);
    }

    public Class<T> getType() {
        return (Class<T>) getValue().getClass();
    }

    public String[] getValueNames() {
        return Arrays.stream(this.values)
                .map(Enum::toString)
                .toArray(String[]::new);
    }

    public T[] getValues() {
        return values;
    }
}