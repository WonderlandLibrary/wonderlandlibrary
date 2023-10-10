package vestige.setting.impl;

import lombok.Getter;
import lombok.Setter;
import vestige.setting.AbstractSetting;

import java.util.function.Supplier;

@Getter
public class BooleanSetting extends AbstractSetting {

    @Setter
    private boolean enabled;

    public BooleanSetting(String name, boolean defaultState) {
        super(name);
        this.enabled = defaultState;
    }

    public BooleanSetting(String name, Supplier<Boolean> visibility, boolean defaultState) {
        super(name, visibility);
        this.enabled = defaultState;
    }
}
