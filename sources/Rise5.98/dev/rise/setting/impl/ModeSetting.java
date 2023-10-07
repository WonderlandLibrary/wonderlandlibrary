package dev.rise.setting.impl;

import dev.rise.module.Module;
import dev.rise.setting.Setting;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public final class ModeSetting extends Setting {

    public int index;
    public List<String> modes;

    public ModeSetting(final String name, final Module parent, final String defaultMode, final String... modes) {
        this.name = name;
        parent.settings.add(this);
        this.modes = Arrays.asList(modes);
        index = this.modes.indexOf(defaultMode);
    }

    public String getMode() {
        return modes.get(index);
    }

    public void set(final String mode) {
        if (modes.contains(mode))
            index = this.modes.indexOf(mode);
    }

    public boolean is(final String mode) {
        return index == modes.indexOf(mode);
    }

    public void cycle(final boolean forwards) {
        if (forwards) {
            if (index < modes.size() - 1) {
                index++;
            } else {
                index = 0;
            }
        }
        if (!forwards) {
            if (index > 0) {
                index--;
            } else {
                index = modes.size() - 1;
            }
        }
    }
}
