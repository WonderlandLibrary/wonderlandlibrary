package wtf.evolution.settings.options;


import wtf.evolution.helpers.animation.Animation;
import wtf.evolution.helpers.animation.impl.EaseInOutQuad;
import wtf.evolution.module.Module;
import wtf.evolution.settings.Setting;

import java.util.function.Supplier;

public class BooleanSetting extends Setting {

private boolean state;
public Animation animation = new EaseInOutQuad(150, 1);


public BooleanSetting(String name, boolean state) {
    this.name = name;
    this.state = state;
}


public boolean get() {
    return state;
}

public void set(boolean state) {
    this.state = state;
}

    public BooleanSetting setHidden(Supplier<Boolean> hidden) {
        this.hidden = hidden;
        return this;
    }
    public BooleanSetting call(Module module) {
        module.addSettings(this);
        return this;
    }

}
