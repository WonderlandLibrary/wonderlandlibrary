package today.flux.module.value;

import com.darkmagician6.eventapi.EventManager;
import lombok.Getter;
import today.flux.gui.clickgui.skeet.EventChangeValue;
import today.flux.utility.SmoothAnimationTimer;

public class FloatValue extends Value {
	@Getter
	SmoothAnimationTimer animationTimer = new SmoothAnimationTimer(1);
	
    @Getter
    private float min, max, increment;

    @Getter
    private String unit;

    public boolean anotherShit;

    public FloatValue(String group, String key, float value, float min, float max, float increment, boolean fromAPI) {
        this.group = group;
        this.key = key;
        this.value = value;

        this.min = min;
        this.max = max;
        this.increment = increment;
        this.anotherShit = false;
        if (!fromAPI) ValueManager.addValue(this);
    }

    public FloatValue(String group, String key, float value, float min, float max, float increment) {
        this(group, key, value, min, max, increment, false);
    }

    public FloatValue(String group, String key, float value, float min, float max, float increment, String unit) {
        this(group, key, value, min, max, increment);
        this.unit = unit;
    }

    public Float getValue() {
        return (Float) this.value;
    }

    public void setValue(float value) {
        if (value < min) {
            value = min;
        }

        if (value > max) {
            value = max;
        }

        EventManager.call(new EventChangeValue(this.group, this.key, this.value, value));
        this.value = value;
    }

	public float getValueState() {return this.getValue();}
	public void setValueState(float val) {this.setValue(val);}
    public float getDMin() {return min;}
    public float getDMax() {return max;}
    public float getDIncrement() {return increment;}
}
