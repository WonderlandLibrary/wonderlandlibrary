package dev.rise.setting.impl;

import dev.rise.module.Module;
import dev.rise.setting.Setting;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class BooleanSetting extends Setting {

    public boolean enabled;

    public BooleanSetting(final String name, final Module parent, final boolean enabled) {
        this.name = name;
        parent.settings.add(this);
        this.enabled = enabled;
    }

    public void toggle() {
        enabled = !enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
