package tech.drainwalk.client.option.options;

import lombok.Getter;
import net.minecraft.util.math.MathHelper;
import tech.drainwalk.client.option.Option;

import java.util.function.BooleanSupplier;

public class FloatOption extends Option<Float> {
    @Getter
    private final float min;
    @Getter
    private final float max;
    @Getter
    private float inc = 1;

    public FloatOption(String settingName, float value, float min, float max) {
        super(settingName, value);
        this.min = min;
        this.max = max;
    }
    public FloatOption(String settingName, float value, float max) {
        super(settingName, value);
        this.min = 0;
        this.max = max;
    }
    public FloatOption addIncrementValue(float inc) {
        this.inc = inc;
        return this;
    }

    @Override
    public void setValue(final Float value) {
        super.setValue(MathHelper.clamp(value, min, max));
    }


    @Override
    public FloatOption addVisibleCondition(BooleanSupplier visible) {
        setVisible(visible);
        return this;
    }

    @Override
    public FloatOption addSettingDescription(String settingDescription) {
        this.settingDescription =settingDescription;
        return this;
    }
}
