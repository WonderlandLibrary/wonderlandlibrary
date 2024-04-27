package me.aquavit.liquidsense.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BoolValue extends Value<Boolean> {

    public BoolValue(String name, Boolean value) {
        super(name, value);
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(value);
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element.isJsonPrimitive())
            value = element.getAsBoolean() || element.getAsString().equalsIgnoreCase("true");
    }
}
