package me.aquavit.liquidsense.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FloatValue extends Value<Float> {
    public float minimum;
    public float maximum;

    public FloatValue(String name, float value, float minimum, float maximum) {
        super(name, value);
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public float getMaximum() {
        return maximum;
    }

    public float getMinimum() {
        return minimum;
    }

    public void setMaximum(float maximum) {
        this.maximum = maximum;
    }

    public void setMinimum(float minimum) {
        this.minimum = minimum;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(value);
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element.isJsonPrimitive()) {
            value = element.getAsFloat();
        }
    }
}
