package com.canon.majik.impl.setting.settings;

import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.setting.Setting;

import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting<String> {

    List<String> modes;

    public ModeSetting(String name, String value, Module parent, String... modes) {
        super(name, value, parent);
        this.modes = Arrays.asList(modes);
    }

    public List<String> getModes() {
        return modes;
    }
}
