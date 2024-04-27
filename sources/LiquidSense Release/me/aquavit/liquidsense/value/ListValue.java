package me.aquavit.liquidsense.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;

@SideOnly(Side.CLIENT)
public class ListValue extends Value<String> {
    public final String[] values;
    public boolean openList;

    public ListValue(String name, String[] values, String value) {
        super(name, value);
        this.values = values;
        this.value = value;
    }

    public String[] getValues() {
        return values;
    }

    public boolean contains(String string) {
        return Arrays.stream(values).anyMatch(s -> s.equalsIgnoreCase(string));
    }

    @Override
    public void changeValue(String value) {
        for (String element : values) {
            if (element.equalsIgnoreCase(value)) {
                this.value = element;
                break;
            }
        }
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(value);
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element.isJsonPrimitive()) {
            changeValue(element.getAsString());
        }
    }

    public String getModeAt(String modeName) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(modeName)) {
                return values[i];
            }
        }
        return "null";
    }

    public int getModeListNumber(String modeName) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(modeName)) {
                return i;
            }
        }
        return 0;
    }
}
