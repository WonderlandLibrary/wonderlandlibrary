package me.aquavit.liquidsense.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MultiBoolValue extends Value<boolean[]> {
    public boolean openList = false;
    public String[] values;

    public MultiBoolValue(String name, String[] values, boolean[] value) {
        super(name, value);
        this.values = values;
    }

    public boolean getMultiBool(String name) {
        for (int i = 0; i < values.length; i++) {
            if (!values[i].equals(name)) {
                continue;
            }

            return value[i];
        }
        return false;
    }

    @Override
    public JsonElement toJson() {
        JsonObject valueObject = new JsonObject();
        for (int i = 0; i < values.length; i++) {
            valueObject.addProperty(values[i], value[i]);
        }
        return valueObject;
    }

    @Override
    public void fromJson(JsonElement element) {
        if (!element.isJsonObject()) {
            return;
        }
        JsonObject valueObject = element.getAsJsonObject();
        for (int i = 0; i < values.length; i++) {
            value[i] = valueObject.get(values[i]).getAsBoolean();
        }
    }
}
