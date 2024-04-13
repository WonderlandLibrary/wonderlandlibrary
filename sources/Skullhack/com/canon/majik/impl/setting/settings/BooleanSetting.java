package com.canon.majik.impl.setting.settings;

import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.setting.Setting;

public class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(String name, Boolean value, Module parent) {
        super(name, value, parent);
    }
}
