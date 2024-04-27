package me.aquavit.liquidsense.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class IntegerValue extends Value<Integer> {

    public final int minimum;
    public final int maximum;

    public IntegerValue(String name, Integer value, int minimum, int maximum) {
        super(name, value);
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public int getMinimum() {
        return minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(value);
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element.isJsonPrimitive())
            value = element.getAsInt();
    }
}
