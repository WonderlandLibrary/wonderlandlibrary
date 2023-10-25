package wtf.evolution.settings.options;

import wtf.evolution.helpers.animation.Animation;
import wtf.evolution.helpers.animation.impl.EaseInOutQuad;
import wtf.evolution.module.Module;
import wtf.evolution.settings.Setting;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ModeSetting extends Setting {
public final List<String> modes;
public String currentMode;

public Animation animation = new EaseInOutQuad(200, 1);

public int index;
public boolean opened;

public ModeSetting(String name, String currentMode, String... options) {
    this.name = name;
    this.modes = Arrays.asList(options);
    this.index = modes.indexOf(currentMode);
    this.currentMode = modes.get(index);
}

public String get() {
    return currentMode;
}
public boolean is(String mode) {
    return currentMode.equalsIgnoreCase(mode);
}

    public ModeSetting setHidden(Supplier<Boolean> hidden) {
        this.hidden = hidden;
        return this;
    }
    public ModeSetting call(Module module) {
        module.addSettings(this);
        return this;
    }


}
