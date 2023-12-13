package dev.rise.setting.impl;

import dev.rise.module.Module;
import dev.rise.setting.Setting;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class NoteSetting extends Setting {

    public NoteSetting(final String note, final Module parent) {
        this.name = note;
        parent.settings.add(this);
    }

}
