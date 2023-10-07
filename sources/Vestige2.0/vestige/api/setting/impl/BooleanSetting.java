package vestige.api.setting.impl;

import lombok.Getter;
import lombok.Setter;
import vestige.api.module.Module;
import vestige.api.setting.Setting;

@Getter
@Setter
public class BooleanSetting extends Setting {

    private boolean enabled;

    public BooleanSetting(String name, Module parent, boolean initialState) {
        super(name, parent);
        this.enabled = initialState;
    }

}
