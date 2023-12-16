// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.settings;

import java.util.function.Consumer;
import moonsense.utils.ColorObject;
import java.util.Iterator;
import java.util.ArrayList;
import com.google.gson.JsonObject;
import moonsense.utils.KeyBinding;
import com.google.gson.JsonElement;

public class SettingWrapper
{
    public static void setValue(final Setting setting, final JsonElement element) {
        if (setting.getType() == null) {
            return;
        }
        switch (setting.getType()) {
            case COLOR: {
                final JsonObject colorProperties = element.getAsJsonObject();
                int chromaSpeed = 0;
                if (colorProperties.get("chromaSpeed") != null) {
                    chromaSpeed = colorProperties.get("chromaSpeed").getAsInt();
                }
                setting.setDefault(colorProperties.get("value").getAsInt(), chromaSpeed);
                break;
            }
            case INT_SLIDER:
            case MODE: {
                setting.setDefault(element.getAsInt());
                break;
            }
            case FLOAT_SLIDER: {
                setting.setDefault(element.getAsFloat());
                break;
            }
            case CHECKBOX: {
                setting.setDefault(element.getAsBoolean());
                break;
            }
            case KEYBIND: {
                setting.setDefault(new KeyBinding(element.getAsInt()));
                break;
            }
            case STRING: {
                setting.setDefault(element.getAsString());
                break;
            }
            case COMPOUND: {
                final JsonObject obj = element.getAsJsonObject();
                final ArrayList<Setting> settings = ((Setting.CompoundSettingGroup)setting.getObject()).getSettings();
                for (final Setting s : settings) {
                    getNonNull(obj, s.getKey(), elem -> setValue(s, elem));
                }
                break;
            }
            default: {
                throw new IllegalStateException("Unexpected value: " + setting.getType() + "\nSetting key: " + setting.getKey());
            }
        }
    }
    
    public static void addKey(final JsonObject jsonObject, final String key, final Object value) {
        addProperty(jsonObject, key, value);
    }
    
    public static void addSettingKey(final JsonObject jsonObject, final Setting setting, final Object value) {
        if (setting.getType() == Setting.Type.COLOR) {
            final JsonObject colorProperties = new JsonObject();
            final ColorObject color = setting.getColorObject();
            colorProperties.addProperty("value", color.getColor());
            if (color.isChroma()) {
                colorProperties.addProperty("chromaSpeed", color.getChromaSpeed());
            }
            addProperty(jsonObject, setting.getKey(), colorProperties);
        }
        else if (setting.getType() == Setting.Type.COMPOUND) {
            final JsonObject compoundSettingProperties = new JsonObject();
            final ArrayList<Setting> settings = ((Setting.CompoundSettingGroup)setting.getObject()).getSettings();
            for (final Setting s : settings) {
                if (s.getValue().size() == 0) {
                    continue;
                }
                addSettingKey(compoundSettingProperties, s, s.getObject());
            }
            addProperty(jsonObject, setting.getKey(), compoundSettingProperties);
        }
        else {
            addProperty(jsonObject, setting.getKey(), value);
        }
    }
    
    private static void addProperty(final JsonObject object, final String key, final Object value) {
        if (value instanceof String) {
            object.addProperty(key, (String)value);
        }
        else if (value instanceof Number) {
            object.addProperty(key, (Number)value);
        }
        else if (value instanceof Boolean) {
            object.addProperty(key, (Boolean)value);
        }
        else if (value instanceof KeyBinding) {
            object.addProperty(key, ((KeyBinding)value).getKeyCode());
        }
        else if (value instanceof JsonObject) {
            object.add(key, (JsonElement)value);
        }
    }
    
    private static void getNonNull(final JsonObject jsonObject, final String key, final Consumer<JsonElement> consumer) {
        if (jsonObject != null && jsonObject.get(key) != null) {
            consumer.accept(jsonObject.get(key));
        }
    }
}
