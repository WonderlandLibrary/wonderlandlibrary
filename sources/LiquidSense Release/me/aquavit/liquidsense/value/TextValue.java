package me.aquavit.liquidsense.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TextValue extends Value<String> {
    public TextValue(String name, String value) {
        super(name, value);
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(value);
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element.isJsonPrimitive()) {
            value = element.getAsString();
        }
    }
}
