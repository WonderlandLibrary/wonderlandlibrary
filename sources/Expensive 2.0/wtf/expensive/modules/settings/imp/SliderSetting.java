package wtf.expensive.modules.settings.imp;


import lombok.Getter;
import net.minecraft.util.math.MathHelper;
import wtf.expensive.modules.settings.Setting;

import java.awt.*;
import java.util.function.Supplier;

public class SliderSetting extends Setting {
    private float value;
    @Getter
    private final float min;
    @Getter
    private final float max;
    @Getter
    private final float increment;


    public SliderSetting(String name, float value, float min, float max, float increment) {
        super(name);
        this.value = value;
        this.min = min;
        this.max = max;
        this.increment = increment;

    }

    public SliderSetting setVisible(Supplier<Boolean> bool) {
        visible = bool;
        return this;
    }

    public Number getValue() {
        return MathHelper.clamp(value, getMin(), getMax());
    }

    public void setValue(float value) {
        this.value = MathHelper.clamp(value, getMin(), getMax());
    }

    @Override
    public SettingType getType() {
        return SettingType.SLIDER_SETTING;
    }
}
