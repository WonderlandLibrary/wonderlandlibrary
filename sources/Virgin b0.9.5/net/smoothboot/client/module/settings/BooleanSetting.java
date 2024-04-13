package net.smoothboot.client.module.settings;

import net.minecraft.util.math.Box;

import java.util.ArrayList;

public class BooleanSetting extends Setting {

    public boolean enabled;

    public BooleanSetting(String name, boolean defaultValue) {
        super(name);
        this.enabled = defaultValue;
    }
    protected final ArrayList<Box> boxes = new ArrayList<>();

    public void toggle() {
        this.enabled = !this.enabled;
    }

    public boolean isEnabled() {
        this.enabled = enabled;
        return enabled;
    }

    public void clear()
    {
        boxes.clear();
    }

    public void setEnabled(boolean asBoolean) {
        this.enabled = enabled;
    }
}