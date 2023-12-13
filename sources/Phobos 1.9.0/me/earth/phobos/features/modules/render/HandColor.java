package me.earth.phobos.features.modules.render;

import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;

public class HandColor
        extends Module {
    public static HandColor INSTANCE;
    public Setting<Boolean> colorSync = this.register(new Setting<Boolean>("Sync", false));
    public Setting<Boolean> rainbow = this.register(new Setting<Boolean>("Rainbow", false));
    public Setting<Integer> saturation = this.register(new Setting<Object>("Saturation", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(100), v -> this.rainbow.getValue()));
    public Setting<Integer> brightness = this.register(new Setting<Object>("Brightness", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(100), v -> this.rainbow.getValue()));
    public Setting<Integer> speed = this.register(new Setting<Object>("Speed", Integer.valueOf(40), Integer.valueOf(1), Integer.valueOf(100), v -> this.rainbow.getValue()));
    public Setting<Integer> red = this.register(new Setting<Object>("Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    public Setting<Integer> green = this.register(new Setting<Object>("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    public Setting<Integer> blue = this.register(new Setting<Object>("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    public Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));

    public HandColor() {
        super("HandColor", "Changes the color of your hands", Module.Category.RENDER, false, false, false);
        INSTANCE = this;
    }
}

