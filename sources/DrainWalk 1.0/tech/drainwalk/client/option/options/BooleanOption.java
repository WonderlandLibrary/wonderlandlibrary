package tech.drainwalk.client.option.options;

import lombok.Getter;
import lombok.Setter;
import tech.drainwalk.client.option.Option;

import java.util.function.BooleanSupplier;

public class BooleanOption extends Option<Boolean> {
    @Setter
    @Getter
    private boolean keyBindVisible;
    @Setter
    @Getter
    private boolean key;
    public BooleanOption(String settingName, Boolean value) {
        super(settingName, value);
    }

    @Override
    public BooleanOption addVisibleCondition(BooleanSupplier visible) {
        setVisible(visible);
        return this;
    }

    @Override
    public BooleanOption addSettingDescription(String settingDescription) {
        this.settingDescription =settingDescription;
        return this;
    }
}
